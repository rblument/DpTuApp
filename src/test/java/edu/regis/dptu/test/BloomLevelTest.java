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

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.BloomLevel;

/** Unit tests for {@link edu.regis.dptu.model.BloomLevel}. */
class BloomLevelTest {

    @Test
    void testTitleAccessorMatchesExpectedValues() {
        assertEquals("Knowledge", BloomLevel.KNOWLEDGE.title());
        assertEquals("Comprehension", BloomLevel.COMPREHENSION.title());
        assertEquals("Application", BloomLevel.APPLICATION.title());
        assertEquals("Analysis", BloomLevel.ANALYSIS.title());
        assertEquals("Synthesis", BloomLevel.SYNTHESIS.title());
        assertEquals("Evaluation", BloomLevel.EVALUATION.title());
        assertEquals("Error", BloomLevel.ERROR.title());
    }

    @Test
    void testFindValueExactMatch() {
        assertEquals(BloomLevel.KNOWLEDGE, BloomLevel.findValue("Knowledge"));
        assertEquals(BloomLevel.APPLICATION, BloomLevel.findValue("Application"));
    }

    @Test
    void testFindValueCaseInsensitiveMatch() {
        assertEquals(BloomLevel.KNOWLEDGE, BloomLevel.findValue("knowledge"));
        assertEquals(BloomLevel.ANALYSIS, BloomLevel.findValue("ANALYSIS"));
        assertEquals(BloomLevel.SYNTHESIS, BloomLevel.findValue("sYnThEsIs"));
    }

    @Test
    void testFindValueReturnsErrorWhenNoMatch() {
        assertEquals(BloomLevel.ERROR, BloomLevel.findValue("Not a real level"));
        assertEquals(BloomLevel.ERROR, BloomLevel.findValue(""));
    }

    @Test
    void testAllEnumConstantsHaveNonNullTitles() {
        for (BloomLevel level : BloomLevel.values()) {
            assertNotNull(level.title());
            assertFalse(level.title().isEmpty());
        }
    }

    @Test
    void testFindValueNullInputThrowsNullPointerException() {
        // current implementation calls equalsIgnoreCase on title,
        // so passing null will throw NPE. This documents current behavior.
        assertThrows(NullPointerException.class, () -> BloomLevel.findValue(null));
    }
}
