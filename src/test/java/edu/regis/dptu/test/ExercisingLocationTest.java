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

import edu.regis.dptu.model.ExercisingLocation;

/** Unit tests for {@link edu.regis.dptu.model.ExercisingLocation}. */
class ExercisingLocationTest {

    @Test
    void testDefaultConstructor() {
        ExercisingLocation loc = new ExercisingLocation();

        assertNotNull(loc);

        // default numeric fields should be zero
        assertEquals(0, loc.getCourseId());
        assertEquals(0, loc.getUnitId());
        assertEquals(0, loc.getTaskId());
        assertEquals(0, loc.getStepId());
    }

    @Test
    void testIdConstructorSetsId() {
        ExercisingLocation loc = new ExercisingLocation(99);

        assertEquals(99, loc.getId());
    }

    @Test
    void testCourseIdGetSet() {
        ExercisingLocation loc = new ExercisingLocation(1);

        loc.setCourseId(10);
        assertEquals(10, loc.getCourseId());

        loc.setCourseId(0);
        assertEquals(0, loc.getCourseId());
    }

    @Test
    void testUnitIdGetSet() {
        ExercisingLocation loc = new ExercisingLocation(1);

        loc.setUnitId(20);
        assertEquals(20, loc.getUnitId());
    }

    @Test
    void testTaskIdGetSet() {
        ExercisingLocation loc = new ExercisingLocation(1);

        loc.setTaskId(30);
        assertEquals(30, loc.getTaskId());
    }

    @Test
    void testStepIdGetSet() {
        ExercisingLocation loc = new ExercisingLocation(1);

        loc.setStepId(40);
        assertEquals(40, loc.getStepId());
    }

    @Test
    void testAllFieldsIndependent() {
        ExercisingLocation loc = new ExercisingLocation(5);

        loc.setCourseId(1);
        loc.setUnitId(2);
        loc.setTaskId(3);
        loc.setStepId(4);

        assertEquals(1, loc.getCourseId());
        assertEquals(2, loc.getUnitId());
        assertEquals(3, loc.getTaskId());
        assertEquals(4, loc.getStepId());
    }
}
