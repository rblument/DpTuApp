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

import edu.regis.dptu.model.Model;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.UnitDigest;

public class UnitDigestTest {

    @Test
    public void testDefaultConstructorDefaults() {
        UnitDigest digest = new UnitDigest();

        assertEquals(Model.DEFAULT_ID, digest.getId());
        assertEquals("", digest.getTitle());
        assertEquals("", digest.getDescription());
        assertEquals(0, digest.getCourseId());
        assertEquals(0, digest.getSequenceIndex());
        assertNull(digest.getPedagogy());
    }

    @Test
    public void testIdConstructor() {
        UnitDigest digest = new UnitDigest(5);

        assertEquals(5, digest.getId());
        assertEquals("", digest.getTitle());
    }

    @Test
    public void testCourseIdRoundTrip() {
        UnitDigest digest = new UnitDigest(1);

        digest.setCourseId(42);
        assertEquals(42, digest.getCourseId());
    }

    @Test
    public void testSequenceIndexRoundTrip() {
        UnitDigest digest = new UnitDigest(1);

        digest.setSequenceIndex(3);
        assertEquals(3, digest.getSequenceIndex());
    }

    @Test
    public void testPedagogyRoundTrip() {
        UnitDigest digest = new UnitDigest(1);

        digest.setPedagogy(TaskSelectionKind.MICROADAPTATION);
        assertEquals(TaskSelectionKind.MICROADAPTATION, digest.getPedagogy());
    }
}
