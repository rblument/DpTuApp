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

import edu.regis.dptu.model.TaskSelectionKind;

public class TaskSelectionKindTest {

    @Test
    public void testTitles() {
        assertEquals("Student Choice", TaskSelectionKind.STUDENT_CHOICE.getTitle());
        assertEquals("Fixed Sequence", TaskSelectionKind.FIXED_SEQUENCE.getTitle());
        assertEquals("Mastery Learning", TaskSelectionKind.MASTERY_LEARNING.getTitle());
        assertEquals("Microadaptation", TaskSelectionKind.MICROADAPTATION.getTitle());
        assertEquals("Other", TaskSelectionKind.OTHER.getTitle());
        assertEquals("Error", TaskSelectionKind.ERROR.getTitle());
    }

    @Test
    public void testFindValueMatchesEveryTitle() {
        for (TaskSelectionKind kind : TaskSelectionKind.values()) {
            assertEquals(kind, TaskSelectionKind.findValue(kind.getTitle()));
        }
    }

    @Test
    public void testFindValueIsCaseInsensitive() {
        assertEquals(
                TaskSelectionKind.MASTERY_LEARNING,
                TaskSelectionKind.findValue("mastery learning"));
        assertEquals(
                TaskSelectionKind.FIXED_SEQUENCE, TaskSelectionKind.findValue("FIXED SEQUENCE"));
    }

    @Test
    public void testFindValueUnknownReturnsError() {
        assertEquals(TaskSelectionKind.ERROR, TaskSelectionKind.findValue("no-such-title"));
    }

    @Test
    public void testValueOfRoundTrip() {
        assertEquals(
                TaskSelectionKind.OTHER, TaskSelectionKind.valueOf(TaskSelectionKind.OTHER.name()));
    }
}
