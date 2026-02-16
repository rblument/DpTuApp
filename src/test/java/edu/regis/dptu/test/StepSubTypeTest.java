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

import edu.regis.dptu.model.StepSubType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StepSubTypeTest {

    @Test
    void testGetSubTypeMatchesToStringForEachEnumConstant() {
        for (StepSubType t : StepSubType.values()) {
            assertEquals(t.getSubType(), t.toString(), "toString() should match getSubType()");
        }
    }

    @Test
    void testKnownSubTypeStrings() {
        assertEquals("Information Message", StepSubType.INFO_MESSAGE.getSubType());
        assertEquals("Step Comleted", StepSubType.STEP_COMPLETED.getSubType());
        assertEquals("Review Problem", StepSubType.PROBLEM_REVIEW.getSubType());
        assertEquals("GUI Action", StepSubType.GUI_ACTION.getSubType());
        assertEquals("Complete Cell", StepSubType.COMPLETE_CELL.getSubType());
        assertEquals("Complete First Row", StepSubType.COMPLETE_FIRST_ROW.getSubType());
        assertEquals("Complete First Col", StepSubType.COMPLETE_FIRST_COL.getSubType());
        assertEquals("Cell Step: Default Zero", StepSubType.DEFAULT_ZERO.getSubType());
        assertEquals(
                "Cell Step: Increase Diagonal Value + 1", StepSubType.INCREASE_DIAGONAL.getSubType());
        assertEquals("Cell Step: Use value of cell to the left.", StepSubType.USE_LEFT.getSubType());
        assertEquals("Cell Step: Use value of cell above.", StepSubType.USE_UPPER.getSubType());
        assertEquals("Step Completion Reply", StepSubType.STEP_COMPLETION_REPLY.getSubType());
        assertEquals("Request Hint", StepSubType.REQUEST_HINT.getSubType());
        assertEquals("Unknown", StepSubType.DEFAULT.getSubType());
    }
}
