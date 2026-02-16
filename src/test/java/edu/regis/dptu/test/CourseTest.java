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

import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.ExercisingLocation;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.Unit;

class CourseTest {

    @Test
    void testConstructorsInitializeLists() {
        Course c1 = new Course();
        assertNotNull(c1.getUnits());
        assertNotNull(c1.getOutcomes());
        assertTrue(c1.getUnits().isEmpty());
        assertTrue(c1.getOutcomes().isEmpty());

        Course c2 = new Course(42);
        assertEquals(42, c2.getId());
        assertNotNull(c2.getUnits());
        assertNotNull(c2.getOutcomes());
        assertTrue(c2.getUnits().isEmpty());
        assertTrue(c2.getOutcomes().isEmpty());
    }

    @Test
    void testPrimaryPedagogyGetSet() {
        Course course = new Course(1);

        assertNull(course.getPrimaryPedagogy());

        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, course.getPrimaryPedagogy());

        course.setPrimaryPedagogy(TaskSelectionKind.MASTERY_LEARNING);
        assertEquals(TaskSelectionKind.MASTERY_LEARNING, course.getPrimaryPedagogy());
    }

    @Test
    void testGetDigestCopiesFields() {
        Course course = new Course(7);
        course.setTitle("DP 101");
        course.setDescription("Intro to DP");
        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);

        CourseDigest digest = course.getDigest();
        assertNotNull(digest);

        assertEquals(7, digest.getId());
        assertEquals("DP 101", digest.getTitle());
        assertEquals("Intro to DP", digest.getDescription());
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, digest.getPrimaryPedagogy());
    }

    @Test
    void testCurrentUnitWhenEmptyReturnsNull() {
        Course course = new Course(1);
        assertNull(course.currentUnit());
    }

    @Test
    void testAddUnitAndCurrentUnitReturnsFirstUnit() throws Exception {
        Course course = new Course(1);

        // Unit must expose getId() and getSequenceId()
        Unit unit1 = new Unit(10);
        unit1.setSequenceId(1);
        Unit unit2 = new Unit(20);
        unit2.setSequenceId(2);

        course.addUnit(unit1);
        course.addUnit(unit2);

        assertEquals(2, course.getUnits().size());
        assertSame(unit1, course.currentUnit());

        assertSame(unit1, course.findUnit(10));
        assertSame(unit2, course.findUnit(20));
    }

    @Test
    void testFindUnitThrowsWhenNotFound() {
        Course course = new Course(1);

        Unit unit = new Unit(10);
        unit.setSequenceId(1);
        course.addUnit(unit);

        assertThrows(ObjNotFoundException.class, () -> course.findUnit(999));
    }

    @Test
    void testFindUnitBySequenceIdFoundAndNotFound() {
        Course course = new Course(1);

        Unit unit1 = new Unit(10);
        unit1.setSequenceId(1);
        Unit unit2 = new Unit(20);
        unit2.setSequenceId(2);
        course.addUnit(unit1);
        course.addUnit(unit2);

        assertSame(unit2, course.findUnitBySequenceId(2));
        assertNull(course.findUnitBySequenceId(999));
    }

    @Test
    void testUnitsSetterGetter() {
        Course course = new Course(1);

        ArrayList<Unit> units = new ArrayList<>();
        units.add(new Unit(10));
        units.get(0).setSequenceId(1);
        units.add(new Unit(20));
        units.get(1).setSequenceId(2);

        course.setUnits(units);

        assertSame(units, course.getUnits());
        assertEquals(2, course.getUnits().size());
    }

    @Test
    void testOutcomesAddGetSetAndFindKnowledgeComponent() throws Exception {
        Course course = new Course(1);

        KnowledgeComponent kc1 = new KnowledgeComponent(5);
        KnowledgeComponent kc2 = new KnowledgeComponent(6);

        course.addOutcome(kc1);
        course.addOutcome(kc2);

        assertEquals(2, course.getOutcomes().size());
        assertSame(kc1, course.findKnowledgeComponent(5));
        assertSame(kc2, course.findKnowledgeComponent(6));

        assertThrows(ObjNotFoundException.class, () -> course.findKnowledgeComponent(999));

        ArrayList<KnowledgeComponent> replacement = new ArrayList<>();
        replacement.add(new KnowledgeComponent(99));
        course.setOutcomes(replacement);

        assertSame(replacement, course.getOutcomes());
        assertEquals(1, course.getOutcomes().size());
        assertEquals(99, course.getOutcomes().get(0).getId());
    }

    @Test
    void testExercisingLocationsSetGetAndFindLocation() {
        Course course = new Course(1);

        ExercisingLocation loc1 = new ExercisingLocation(11);
        ExercisingLocation loc2 = new ExercisingLocation(22);

        ArrayList<ExercisingLocation> locations = new ArrayList<>();
        locations.add(loc1);
        locations.add(loc2);

        course.setExercisingLocations(locations);

        assertSame(locations, course.getExercisingLocations());
        assertSame(loc1, course.findLocation(11));
        assertSame(loc2, course.findLocation(22));
        assertNull(course.findLocation(999));
    }
}
