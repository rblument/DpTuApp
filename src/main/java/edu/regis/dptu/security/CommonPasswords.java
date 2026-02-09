package edu.regis.dptu.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.BitSet;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common password deny-list check aligned with NIST SP 800-63B.
 *
 * <p><b>NIST alignment</b> (verifier-side guidance):
 *
 * <ul>
 *   <li>Reject "commonly used, expected, or compromised" passwords by checking candidates against a
 *       deny-list (e.g., breached/common lists).
 *   <li>Apply appropriate string normalization (e.g., Unicode normalization) to avoid bypass via
 *       visually similar or canonically equivalent forms.
 *   <li>Do not log secrets (passwords are never logged).
 * </ul>
 *
 * <p><b>Implementation notes</b>:
 *
 * <ul>
 *   <li>Uses a Bloom filter rather than a {@code Set<String>} to reduce memory.
 *   <li>Bloom filters can produce false positives (rare, configurable). This may reject some
 *       non-common passwords; choose a low false-positive rate.
 *   <li>Resource file is loaded from the classpath: {@code common-passwords.txt}
 * </ul>
 */
public final class CommonPasswords {

    private static final Logger log = LoggerFactory.getLogger(CommonPasswords.class);

    /**
     * Classpath resource containing the deny-list. Location:
     * src/main/resources/common-passwords.txt
     */
    private static final String RESOURCE = "common-passwords.txt";

    /**
     * Bloom filter sizing parameters.
     *
     * <p>These should be adjusted based on how large the deny list is. If the file grows a lot,
     * increase EXPECTED_INSERTIONS accordingly.
     */
    private static final int EXPECTED_INSERTIONS = 50_000;

    /**
     * Target false positive probability.
     *
     * <p>1e-6 means about 1 false positive per 1,000,000 distinct lookups (statistically). Lower
     * values increase memory usage.
     */
    private static final double FALSE_POSITIVE_PROBABILITY = 1e-6;

    /** The loaded Bloom filter containing normalized deny-list entries. */
    private static final BloomFilter COMMON = loadBloomFilter();

    private CommonPasswords() {
        // utility class
    }

    /**
     * Checks whether the given password appears in a known-bad password list.
     *
     * <p>Security properties:
     *
     * <ul>
     *   <li>Password value is never logged.
     *   <li>Candidate is NFKC-normalized (Normalization Form Compatibility Composition) and
     *       lowercased to prevent bypass using canonically equivalent Unicode or case variants.
     * </ul>
     *
     * @param password password as char[] (caller controls lifetime / clearing)
     * @return true if the password matches the deny-list (Bloom membership)
     */
    public static boolean isCommon(char[] password) {
        if (password == null || password.length == 0) {
            return false;
        }

        // NOTE: Converting char[] -> String creates an immutable object on the heap.
        String candidate = normalizeCandidate(new String(password));

        boolean match = COMMON.mightContain(candidate);

        if (match && log.isWarnEnabled()) {
            // IMPORTANT: never log the password value.
            log.warn("Password rejected: matched common/compromised password deny list");
        } else if (!match && log.isDebugEnabled()) {
            // Debug-level visibility without leaking secrets.
            log.debug("Password accepted by common-password deny-list check (no match)");
        }

        return match;
    }

    /**
     * Normalize input according to typical verifier-side expectations:
     *
     * <ul>
     *   <li>Trim leading/trailing whitespace (resource lines may have accidental spaces)
     *   <li>Unicode NFKC normalization (to collapse compatibility equivalents)
     *   <li>Lowercase using Locale.ROOT (stable, locale-independent)
     * </ul>
     */
    private static String normalizeCandidate(String s) {
        String trimmed = (s == null) ? "" : s.trim();
        // Unicode normalization step (NFKC).
        String nfkc = Normalizer.normalize(trimmed, Normalizer.Form.NFKC);
        // Case-folding
        return nfkc.toLowerCase(Locale.ROOT);
    }

    /**
     * Loads the deny-list from a classpath resource and inserts entries into a Bloom filter.
     *
     * <p>File format:
     *
     * <ul>
     *   <li>UTF-8 text
     *   <li>One password per line
     *   <li>Blank lines ignored
     *   <li>Lines starting with '#' ignored (comments)
     * </ul>
     *
     * <p>All entries are NFKC-normalized + lowercased at load time to match candidate
     * normalization.
     */
    private static BloomFilter loadBloomFilter() {
        long started = System.currentTimeMillis();

        BloomFilter filter = BloomFilter.create(EXPECTED_INSERTIONS, FALSE_POSITIVE_PROBABILITY);

        int totalLines = 0;
        int loaded = 0;
        int skippedBlank = 0;
        int skippedComment = 0;

        ClassLoader cl = CommonPasswords.class.getClassLoader();

        try (InputStream in = cl.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                // Fail fast: missing deny-list should be treated as a deployment/config error.
                throw new IllegalStateException(
                        "Password deny list not found on classpath: " + RESOURCE);
            }

            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;

                    String raw = line.trim();
                    if (raw.isEmpty()) {
                        skippedBlank++;
                        continue;
                    }
                    if (raw.startsWith("#")) {
                        skippedComment++;
                        continue;
                    }

                    String normalized = normalizeCandidate(raw);

                    // Optionally skip very short entries
                    filter.put(normalized);
                    loaded++;
                }
            }

        } catch (Exception e) {
            // Fail hard: a verifier without deny-list is not operating as intended.
            log.error("Failed to load password deny list resource: {}", RESOURCE);
            throw new IllegalStateException("Failed to load common password deny list", e);
        }

        long elapsedMs = System.currentTimeMillis() - started;

        // Provide useful operational logging (without leaking secrets).
        log.info(
                "Loaded password deny list from '{}' into Bloom filter: loaded={}, totalLines={}, skippedBlank={}, skippedComment={}, mBits={}, kHashes={}, fppTarget={}, elapsedMs={}",
                RESOURCE,
                loaded,
                totalLines,
                skippedBlank,
                skippedComment,
                filter.bitSize(),
                filter.numHashFunctions(),
                FALSE_POSITIVE_PROBABILITY,
                elapsedMs);

        if (loaded == 0) {
            log.warn("Password deny list resource '{}' contained no usable entries", RESOURCE);
        } else if (loaded < 1000) {
            log.warn(
                    "Password deny list resource '{}' is small ({} entries). Consider expanding it for better NIST coverage.",
                    RESOURCE,
                    loaded);
        }

        return filter;
    }

    /**
     * Minimal Bloom filter implementation for Strings with no external dependencies.
     *
     * <p>Hashing strategy:
     *
     * <ul>
     *   <li>Uses SHA-256 over UTF-8 bytes as a base digest.
     *   <li>Derives k indices from the digest using 32-bit chunks.
     * </ul>
     *
     * <p>This is not intended to be cryptographic protection (no secrets are stored); it's a
     * space-efficient membership approximation.
     */
    static final class BloomFilter {
        private final BitSet bits;
        private final int m; // number of bits
        private final int k; // number of hash functions

        private BloomFilter(int mBits, int kHashes) {
            this.m = mBits;
            this.k = kHashes;
            this.bits = new BitSet(mBits);
        }

        static BloomFilter create(int expectedInsertions, double fpp) {
            if (expectedInsertions <= 0) {
                throw new IllegalArgumentException("expectedInsertions must be > 0");
            }
            if (!(fpp > 0.0 && fpp < 1.0)) {
                throw new IllegalArgumentException("fpp must be between 0 and 1");
            }

            // Standard Bloom filter sizing formulas:
            // m = - (n * ln(p)) / (ln 2)^2
            // k = (m/n) * ln 2
            double ln2 = Math.log(2.0);
            int m = (int) Math.ceil(-(expectedInsertions * Math.log(fpp)) / (ln2 * ln2));
            int k = (int) Math.ceil((m / (double) expectedInsertions) * ln2);

            // Ensure sane minimums.
            m = Math.max(m, 1024);
            k = Math.max(k, 2);

            return new BloomFilter(m, k);
        }

        void put(String value) {
            if (value == null) return;
            int[] idx = indices(value);
            for (int i : idx) {
                bits.set(i);
            }
        }

        boolean mightContain(String value) {
            if (value == null) return false;
            int[] idx = indices(value);
            for (int i : idx) {
                if (!bits.get(i)) return false;
            }
            return true;
        }

        int bitSize() {
            return m;
        }

        int numHashFunctions() {
            return k;
        }

        private int[] indices(String value) {
            byte[] digest = sha256(value);

            // Derive indices from digest bytes.
            // SHA-256 gives 32 bytes = 8 x 32-bit integers.
            // If k > 8, we re-mix by hashing the digest again deterministically.
            int[] out = new int[k];
            byte[] current = digest;

            int produced = 0;
            int round = 0;

            while (produced < k) {
                for (int off = 0; off + 4 <= current.length && produced < k; off += 4) {
                    int h =
                            ((current[off] & 0xFF) << 24)
                                    | ((current[off + 1] & 0xFF) << 16)
                                    | ((current[off + 2] & 0xFF) << 8)
                                    | (current[off + 3] & 0xFF);

                    // Convert to a non-negative index in [0, m).
                    int idx = (h & 0x7FFFFFFF) % m;
                    out[produced++] = idx;
                }

                // If we still need more indices, hash again:
                // SHA256( roundByte || currentDigest )
                if (produced < k) {
                    round++;
                    current = sha256(round + ":" + toHex(current));
                }
            }

            return out;
        }

        private static byte[] sha256(String s) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                return md.digest(bytes);
            } catch (Exception e) {
                // Should never happen in a standard JVM.
                throw new IllegalStateException("SHA-256 not available", e);
            }
        }

        private static String toHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        }
    }
}
