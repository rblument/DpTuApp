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

import edu.regis.dptu.model.aol.OutcomeGranularity;

/** Unit tests for {@link edu.regis.dptu.model.aol.OutcomeGranularity}. */
class OutcomeGranularityTest {

    @Test
    void testTitleAccessor() {
        assertEquals("Course", OutcomeGranularity.COURSE.title());
        assertEquals("Knowledge Component", OutcomeGranularity.KNOWLEDGE_COMPONENT.title());
        assertEquals("Unit", OutcomeGranularity.UNIT.title());
        assertEquals("Error", OutcomeGranularity.ERROR.title());
    }

    @Test
    void testFindValueExactMatch() {
        assertEquals(OutcomeGranularity.COURSE, OutcomeGranularity.findValue("Course"));
        assertEquals(OutcomeGranularity.UNIT, OutcomeGranularity.findValue("Unit"));
    }

    @Test
    void testFindValueCaseInsensitive() {
        assertEquals(OutcomeGranularity.COURSE, OutcomeGranularity.findValue("course"));
        assertEquals(
                OutcomeGranularity.KNOWLEDGE_COMPONENT,
                OutcomeGranularity.findValue("KNOWLEDGE COMPONENT"));
        assertEquals(OutcomeGranularity.UNIT, OutcomeGranularity.findValue("uNiT"));
    }

    @Test
    void testFindValueReturnsErrorWhenNoMatch() {
        assertEquals(OutcomeGranularity.ERROR, OutcomeGranularity.findValue("Not real"));
        assertEquals(OutcomeGranularity.ERROR, OutcomeGranularity.findValue(""));
    }

    @Test
    void testAllEnumValuesHaveTitles() {
        for (OutcomeGranularity g : OutcomeGranularity.values()) {
            assertNotNull(g.title());
            assertFalse(g.title().isBlank());
        }
    }

    @Test
    void testFindValueNullReturnsError() {
        // Implementation calls: kind.title().equalsIgnoreCase(aTitle)
        // If aTitle is null, equalsIgnoreCase returns false (no NPE), and ERROR is returned.
        assertEquals(OutcomeGranularity.ERROR, OutcomeGranularity.findValue(null));
    }
}
