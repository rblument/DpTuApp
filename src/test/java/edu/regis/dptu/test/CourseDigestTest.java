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

import edu.regis.dptu.model.CourseDigest;
import edu.regis.dptu.model.Model;
import edu.regis.dptu.model.TaskSelectionKind;

/**
 * Verifies the constructors and accessors of {@link CourseDigest}, a lightweight course summary
 * returned to the GUI during a tutoring session.
 */
public class CourseDigestTest {

    @Test
    public void testDefaultConstructorUsesDefaultId() {
        CourseDigest digest = new CourseDigest();

        assertEquals(Model.DEFAULT_ID, digest.getId());
        assertEquals("", digest.getTitle());
        assertEquals("", digest.getDescription());
        assertNull(digest.getPrimaryPedagogy());
    }

    @Test
    public void testIdConstructor() {
        CourseDigest digest = new CourseDigest(7);

        assertEquals(7, digest.getId());
        assertEquals("", digest.getTitle());
    }

    @Test
    public void testIdAndTitleConstructor() {
        CourseDigest digest = new CourseDigest(9, "Dynamic Programming");

        assertEquals(9, digest.getId());
        assertEquals("Dynamic Programming", digest.getTitle());
    }

    @Test
    public void testPrimaryPedagogyRoundTrip() {
        CourseDigest digest = new CourseDigest(1, "DP");

        digest.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        assertEquals(TaskSelectionKind.FIXED_SEQUENCE, digest.getPrimaryPedagogy());

        digest.setPrimaryPedagogy(TaskSelectionKind.MASTERY_LEARNING);
        assertEquals(TaskSelectionKind.MASTERY_LEARNING, digest.getPrimaryPedagogy());
    }
}
