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

import edu.regis.dptu.model.BloomLevel;
import edu.regis.dptu.model.ExercisingLocation;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.aol.OutcomeGranularity;

/** Unit tests for {@link edu.regis.dptu.model.KnowledgeComponent}. */
class KnowledgeComponentTest {

    @Test
    void testConstructorInitializesIdAndEmptyLocations() {
        KnowledgeComponent kc = new KnowledgeComponent(42);

        assertEquals(42, kc.getId());
        assertNotNull(kc.getExercisingLocations(), "exercisingLocations should be initialized");
        assertTrue(kc.getExercisingLocations().isEmpty(), "exercisingLocations should start empty");
    }

    @Test
    void testBloomLevelGetSet() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        kc.setBloomLevel(BloomLevel.KNOWLEDGE);
        assertEquals(BloomLevel.KNOWLEDGE, kc.getBloomLevel());

        kc.setBloomLevel(BloomLevel.ANALYSIS);
        assertEquals(BloomLevel.ANALYSIS, kc.getBloomLevel());
    }

    @Test
    void testGranularityGetSet() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        kc.setGranularity(OutcomeGranularity.KNOWLEDGE_COMPONENT);
        assertEquals(OutcomeGranularity.KNOWLEDGE_COMPONENT, kc.getGranularity());

        kc.setGranularity(OutcomeGranularity.UNIT);
        assertEquals(OutcomeGranularity.UNIT, kc.getGranularity());
    }

    @Test
    void testPedagogyGetSet() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        kc.setPedagogy(TaskSelectionKind.MASTERY_LEARNING);
        assertEquals(TaskSelectionKind.MASTERY_LEARNING, kc.getPedagogy());
    }

    @Test
    void testDomainFocusGetSetAndAliasGetter() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        // default false
        assertFalse(kc.isDomainFocus());
        assertFalse(kc.getIsDomainFocus());

        kc.setIsDomainFocus(true);
        assertTrue(kc.isDomainFocus());
        assertTrue(kc.getIsDomainFocus());

        kc.setIsDomainFocus(false);
        assertFalse(kc.isDomainFocus());
        assertFalse(kc.getIsDomainFocus());
    }

    @Test
    void testAddExercisingLocationAppendsToList() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        ExercisingLocation loc = new ExercisingLocation(10);
        loc.setCourseId(100);
        loc.setUnitId(200);
        loc.setTaskId(300);
        loc.setStepId(400);

        kc.addExercisingLocation(loc);

        assertEquals(1, kc.getExercisingLocations().size());
        assertSame(loc, kc.getExercisingLocations().get(0));
        assertEquals(10, kc.getExercisingLocations().get(0).getId());
    }

    @Test
    void testSetExercisingLocationsReplacesList() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        ArrayList<ExercisingLocation> list = new ArrayList<>();
        list.add(new ExercisingLocation(1));
        list.add(new ExercisingLocation(2));

        kc.setExercisingLocations(list);

        assertSame(list, kc.getExercisingLocations());
        assertEquals(2, kc.getExercisingLocations().size());
        assertEquals(1, kc.getExercisingLocations().get(0).getId());
        assertEquals(2, kc.getExercisingLocations().get(1).getId());
    }

    @Test
    void testSetExercisingLocationsAllowsNull() {
        KnowledgeComponent kc = new KnowledgeComponent(1);

        kc.setExercisingLocations(null);
        assertNull(kc.getExercisingLocations(), "Current behavior allows null list assignment");
    }
}
