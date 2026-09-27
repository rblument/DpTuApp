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
import edu.regis.dptu.err.NonRecoverableException;

public class NonRecoverableExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        NonRecoverableException ex = new NonRecoverableException("db down");

        assertEquals("db down", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("socket reset");
        NonRecoverableException ex = new NonRecoverableException("db down", cause);

        assertEquals("db down", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testIsDpTuException() {
        assertTrue(new NonRecoverableException("x") instanceof DpTuException);
    }
}
