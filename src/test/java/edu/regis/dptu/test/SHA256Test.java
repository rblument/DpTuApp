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

import org.junit.jupiter.api.Test;

import edu.regis.dptu.util.SHA_256;

import static org.junit.jupiter.api.Assertions.*;

/** Unit tests for {@link SHA_256}. */
@SuppressWarnings("Logging")
public class SHA256Test {

    @Test
    public void testSha256KnownVector_abc() {
        // Standard test vector for SHA-256("abc")
        String expected = "ba7816bf8f01cfea414140de5dae2223" + "b00361a396177a9cb410ff61f20015ad";

        String actual = SHA_256.instance().sha256("abc");
        assertEquals(expected, actual);
    }

    @Test
    public void testSha256EmptyString() {
        String expected = "e3b0c44298fc1c149afbf4c8996fb924" + "27ae41e4649b934ca495991b7852b855";

        String actual = SHA_256.instance().sha256("");
        assertEquals(expected, actual);
    }

    @Test
    public void testPadLeftZeros() {
        SHA_256 sha = SHA_256.instance();
        assertEquals("000f", sha.padLeftZeros("f", 4));
        assertEquals("0010", sha.padLeftZeros("10", 4));
        assertEquals("1234", sha.padLeftZeros("1234", 4));
    }

    @Test
    public void testHashDeterminism() {
        SHA_256 sha = SHA_256.instance();
        String h1 = sha.sha256("CorrectHorseBatteryStaple");
        String h2 = sha.sha256("CorrectHorseBatteryStaple");
        String h3 = sha.sha256("CorrectHorseBatteryStaple!");

        assertEquals(h1, h2, "Same input should hash to same output");
        assertNotEquals(h1, h3, "Different input should hash differently");
        assertEquals(64, h1.length(), "SHA-256 hex should be 64 chars");
    }
}
