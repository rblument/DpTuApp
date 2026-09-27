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

import edu.regis.dptu.model.TaskKind;

public class TaskKindTest {

    @Test
    public void testTitles() {
        assertEquals("Dynamic PRogramming Problem", TaskKind.PROBLEM.title());
        assertEquals("Initiailize First Row", TaskKind.INITIALIZE_FIRST_ROW.title());
        assertEquals("Initialize First Column", TaskKind.INITIALIZE_FIRST_COL.title());
        assertEquals("Assign a Cell Value", TaskKind.ASSIGN_CELL.title());
        assertEquals("Create Table", TaskKind.CREATE_TABLE.title());
        assertEquals("Solution Path", TaskKind.SOLUTION_PATH.title());
        assertEquals("Solve", TaskKind.SOLVE.title());
    }

    @Test
    public void testEveryValueHasNonBlankTitle() {
        for (TaskKind kind : TaskKind.values()) {
            assertNotNull(kind.title());
            assertFalse(kind.title().isBlank());
        }
    }

    @Test
    public void testValueCount() {
        assertEquals(7, TaskKind.values().length);
    }

    @Test
    public void testValueOfRoundTrip() {
        assertEquals(TaskKind.SOLVE, TaskKind.valueOf(TaskKind.SOLVE.name()));
    }
}
