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

import edu.regis.dptu.model.StepSubType;

class StepSubTypeTest {

    @Test
    void testGetSubTypeMatchesToString() {
        for (StepSubType type : StepSubType.values()) {
            assertNotNull(type.getSubType());
            assertEquals(
                    type.getSubType(),
                    type.toString(),
                    "toString() should return same value as getSubType()");
        }
    }

    @Test
    void testSpecificEnumValues() {
        assertEquals("Complete Cell", StepSubType.COMPLETE_CELL.getSubType());
        assertEquals("Unknown", StepSubType.DEFAULT.getSubType());
        assertEquals("Information Message", StepSubType.INFO_MESSAGE.getSubType());
    }

    @Test
    void testValueOfLookup() {
        StepSubType type = StepSubType.valueOf("COMPLETE_CELL");
        assertEquals(StepSubType.COMPLETE_CELL, type);
        assertEquals("Complete Cell", type.getSubType());
    }

    @Test
    void testAllEnumConstantsAccessible() {
        StepSubType[] values = StepSubType.values();

        assertTrue(values.length > 0, "Enum should contain values");

        for (StepSubType type : values) {
            assertNotNull(type.name());
            assertNotNull(type.getSubType());
        }
    }

    @Test
    void testToStringHumanReadable() {
        StepSubType type = StepSubType.USE_LEFT;

        assertEquals(type.getSubType(), type.toString());
        assertFalse(type.toString().isEmpty());
    }
}
