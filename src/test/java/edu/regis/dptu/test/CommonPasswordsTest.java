/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.BitSet;

import org.junit.jupiter.api.Test;

import edu.regis.dptu.security.CommonPasswords;

/**
 * Unit tests for {@link CommonPasswords}.
 *
 * <p>These tests verify:
 * <ul>
 *   <li>Resource-backed deny list loads successfully</li>
 *   <li>Known common passwords are rejected</li>
 *   <li>Unicode NFKC normalization prevents bypass</li>
 *   <li>Case normalization is consistent</li>
 *   <li>Null/empty inputs are handled safely</li>
 * </ul>
 */
@SuppressWarnings("Logging")
public class CommonPasswordsTest {

    /** Ensure null passwords are not treated as common (no rejection). */
    @Test
    public void testNullPassword() {
        assertFalse(CommonPasswords.isCommon(null));
    }

    /** Ensure empty passwords are not treated as common (no rejection). */
    @Test
    public void testEmptyPassword() {
        assertFalse(CommonPasswords.isCommon(new char[0]));
    }

    /** Verify a few canonical common passwords are rejected. */
    @Test
    public void testKnownCommonPasswordsRejected() {
        assertTrue(CommonPasswords.isCommon("password".toCharArray()));
        assertTrue(CommonPasswords.isCommon("123456".toCharArray()));
        assertTrue(CommonPasswords.isCommon("qwerty".toCharArray()));
        assertTrue(CommonPasswords.isCommon("admin".toCharArray()));
        assertTrue(CommonPasswords.isCommon("p@ssw0rd".toCharArray()));
    }

    /**
     * Verify case-insensitive behavior.
     *
     * <p>The implementation lowercases both deny-list entries at load time and
     * candidates at check time, so case variants should match.
     */
    @Test
    public void testCaseNormalization() {
        assertTrue(CommonPasswords.isCommon("Password".toCharArray()));
        assertTrue(CommonPasswords.isCommon("PASSWORD".toCharArray()));
        assertTrue(CommonPasswords.isCommon("Admin".toCharArray()));
        assertTrue(CommonPasswords.isCommon("P@SSW0RD".toCharArray()));
    }

    /**
     * Verify Unicode NFKC normalization prevents bypass.
     *
     * <p>These inputs use compatibility characters that normalize under NFKC to
     * ASCII equivalents:
     * <ul>
     *   <li>Fullwidth digits: １２３４５６ -> 123456</li>
     *   <li>Fullwidth letters: ｐａｓｓｗｏｒｄ -> password</li>
     * </ul>
     *
     * <p>If your deny list contains "123456" and "password", these should be rejected.
     */
    @Test
    public void testUnicodeNfkcNormalizationBlocksBypass() {
        // U+FF11..FF16 fullwidth digits
        assertTrue(CommonPasswords.isCommon("１２３４５６".toCharArray()));

        // Fullwidth "password" characters (U+FF50.. etc depending)
        assertTrue(CommonPasswords.isCommon("ｐａｓｓｗｏｒｄ".toCharArray()));
    }

    /** Verify a password not in the deny list is not rejected (probabilistic due to Bloom filter). */
    @Test
    public void testUncommonPasswordNotRejected() {
        // Choose a long, high-entropy string to minimize chance of Bloom false positive.
        String strong = "CorrectHorseBatteryStaple!2026#A9f3zQ1";
        assertFalse(CommonPasswords.isCommon(strong.toCharArray()));
    }

    /**
     * Sanity-check that the Bloom filter was actually loaded and populated.
     *
     * <p>This uses reflection to avoid adding test-only APIs to production code.
     * It validates that the internal BitSet exists and has at least one bit set,
     * which implies the resource file loaded at least one entry.
     */
    @Test
    public void testBloomFilterIsPopulated() throws Exception {
        // CommonPasswords has: private static final BloomFilter COMMON
        Field commonField = CommonPasswords.class.getDeclaredField("COMMON");
        commonField.setAccessible(true);
        Object bloom = commonField.get(null);
        assertNotNull(bloom, "Bloom filter COMMON should not be null");

        // BloomFilter has: private final BitSet bits
        Field bitsField = bloom.getClass().getDeclaredField("bits");
        bitsField.setAccessible(true);
        BitSet bits = (BitSet) bitsField.get(bloom);

        assertNotNull(bits, "Bloom filter BitSet should not be null");
        assertTrue(bits.length() > 0, "Bloom filter should have at least one bit set (list loaded)");
    }
}
