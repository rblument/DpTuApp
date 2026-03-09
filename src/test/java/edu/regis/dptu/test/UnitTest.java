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

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.Unit;
import edu.regis.dptu.model.UnitDigest;

/** Unit tests for {@link edu.regis.dptu.model.Unit}. */
class UnitTest {

    @Test
    void testDefaultConstructorSetsDefaultIdAndPedagogy() {
        Unit u = new Unit();

        // DEFAULT_ID is defined up the Model hierarchy; we can't access it here directly.
        // But we can at least assert that the object exists and pedagogy defaults.
        assertNotNull(u);
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, u.getPedagogy());
    }

    @Test
    void testIdConstructorSetsIdAndDefaultPedagogy() {
        Unit u = new Unit(123);

        assertEquals(123, u.getId());
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, u.getPedagogy());
    }

    @Test
    void testSequenceIdGetSet() {
        Unit u = new Unit(1);

        // default is DEFAULT_ID, but we can't reference it; just verify set/get works
        u.setSequenceId(7);
        assertEquals(7, u.getSequenceId());

        u.setSequenceId(0);
        assertEquals(0, u.getSequenceId());
    }

    @Test
    void testPedagogyGetSet() {
        Unit u = new Unit(1);

        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, u.getPedagogy());

        u.setPedagogy(TaskSelectionKind.MASTERY_LEARNING);
        assertEquals(TaskSelectionKind.MASTERY_LEARNING, u.getPedagogy());
    }

    @Test
    void testGetDigestCopiesIdTitleDescription() {
        Unit u = new Unit(55);
        u.setTitle("Unit A");
        u.setDescription("Desc A");

        UnitDigest digest = u.getDigest();
        assertNotNull(digest);

        assertEquals(55, digest.getId());
        assertEquals("Unit A", digest.getTitle());
        assertEquals("Desc A", digest.getDescription());
    }

    @Test
    void testAddTaskInitializesListIfNullAndAddsTask() {
        Unit u = new Unit(1);

        assertNull(u.getTasks(), "tasks should start null until first addTask() or setTasks()");

        Task t1 = mock(Task.class);
        when(t1.getId()).thenReturn(10);
        when(t1.getSequenceIndex()).thenReturn(1);

        u.addTask(t1);

        assertNotNull(u.getTasks(), "tasks list should be initialized by addTask()");
        assertEquals(1, u.getTasks().size());
        assertSame(t1, u.getTasks().get(0));
    }

    @Test
    void testSetTasksAndGetTasks() {
        Unit u = new Unit(1);

        ArrayList<Task> tasks = new ArrayList<>();
        Task t1 = mock(Task.class);
        Task t2 = mock(Task.class);
        tasks.add(t1);
        tasks.add(t2);

        u.setTasks(tasks);

        assertSame(tasks, u.getTasks());
        assertEquals(2, u.getTasks().size());
    }

    @Test
    void testFindTaskBySequenceReturnsMatchingTask() {
        Unit u = new Unit(1);

        Task t1 = mock(Task.class);
        when(t1.getSequenceIndex()).thenReturn(1);

        Task t2 = mock(Task.class);
        when(t2.getSequenceIndex()).thenReturn(2);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        u.setTasks(tasks);

        assertSame(t2, u.findTaskBySequence(2));
        assertSame(t1, u.findTaskBySequence(1));
    }

    @Test
    void testFindTaskBySequenceReturnsNullWhenNotFound() {
        Unit u = new Unit(1);

        Task t1 = mock(Task.class);
        when(t1.getSequenceIndex()).thenReturn(1);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        u.setTasks(tasks);

        assertNull(u.findTaskBySequence(999));
    }

    @Test
    void testFindTaskBySequenceThrowsIfTasksNull() {
        Unit u = new Unit(1);

        // The implementation iterates "for (Task task : tasks)" without a null-check,
        // so this documents current behavior.
        assertThrows(NullPointerException.class, () -> u.findTaskBySequence(1));
    }
}
