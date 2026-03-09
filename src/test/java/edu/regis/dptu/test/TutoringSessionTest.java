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

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.UnitDigest;

public class TutoringSessionTest {

    @Test
    public void testSessionStateAndTaskManagement() {
        Account account = new Account("student@regis.edu", "encrypted");
        TutoringSession session = new TutoringSession(account, null);

        session.setId(77);
        session.setSecurityToken("token");
        session.setUserId("newstudent@regis.edu");
        session.setIsActive(false);

        GregorianCalendar startDate = new GregorianCalendar(2026, 1, 1);
        session.setStartDate(startDate);

        CourseDigest course = new CourseDigest(11, "DP Course");
        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        UnitDigest unit = new UnitDigest(12);
        unit.setCourseId(11);
        unit.setPedagogy(TaskSelectionKind.MASTERY_LEARNING);
        unit.setSequenceIndex(2);

        session.setCourse(course);
        session.setUnit(unit);
        session.setMode(Mode.SEE_ONE);

        Task task = new Task(3);
        task.setTitle("Task 1");
        PendingTask pendingTask = new PendingTask(task);
        session.addTask(pendingTask);

        assertEquals(77, session.getId());
        assertEquals("token", session.getSecurityToken());
        assertEquals("newstudent@regis.edu", session.getUserId());
        assertFalse(session.isIsActive());
        assertEquals(startDate, session.getStartDate());
        assertEquals(course, session.getCourse());
        assertEquals(unit, session.getUnit());
        assertEquals(Mode.SEE_ONE, session.getMode());
        assertEquals(1, session.getTasks().size());
        assertSame(pendingTask, session.getCurrentTask());

        session.removeTask(task.getId());
        assertEquals(0, session.getTasks().size());

        session.addTask(pendingTask);
        session.removeTask(pendingTask);
        assertEquals(0, session.getTasks().size());
    }
}
