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

import edu.regis.dptu.err.DpTuException;
import edu.regis.dptu.err.IllegalArgException;

/**
 * Verifies {@link IllegalArgException} preserves its message. This checked exception guards method
 * arguments across the service layer (e.g. duplicate account creation).
 */
public class IllegalArgExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        IllegalArgException ex = new IllegalArgException("id already exists");

        assertEquals("id already exists", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testIsDpTuException() {
        assertTrue(new IllegalArgException("x") instanceof DpTuException);
    }
}
