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
    void testGetSubTypeMatchesToStringForAllValues() {
        for (StepSubType type : StepSubType.values()) {
            assertNotNull(type.getSubType(), "getSubType() should never return null");
            assertEquals(
                    type.getSubType(),
                    type.toString(),
                    "toString() should return same value as getSubType()");
            assertFalse(type.getSubType().isBlank(), "subType text should not be blank");
        }
    }

    @Test
    void testSpecificHumanReadableLabels() {
        assertEquals("Complete Cell", StepSubType.COMPLETE_CELL.getSubType());
        assertEquals("Unknown", StepSubType.DEFAULT.getSubType());
        assertEquals("Information Message", StepSubType.INFO_MESSAGE.getSubType());
    }

    @Test
    void testValueOfLookupWorks() {
        StepSubType t = StepSubType.valueOf("USE_LEFT");
        assertEquals(StepSubType.USE_LEFT, t);
        assertEquals("Cell Step: Use value of cell to the left.", t.getSubType());
    }

    @Test
    void testEnumValuesArrayNotEmpty() {
        StepSubType[] values = StepSubType.values();
        assertTrue(values.length > 0);
        assertNotNull(values[0]);
    }
}
