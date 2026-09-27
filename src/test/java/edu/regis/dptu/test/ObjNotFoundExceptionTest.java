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
import edu.regis.dptu.err.ObjNotFoundException;

public class ObjNotFoundExceptionTest {

    @Test
    public void testMessageOnlyConstructor() {
        ObjNotFoundException ex = new ObjNotFoundException("no such user: 42");

        assertEquals("no such user: 42", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        Throwable cause = new IllegalStateException("row missing");
        ObjNotFoundException ex = new ObjNotFoundException("no such user: 42", cause);

        assertEquals("no such user: 42", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testIsDpTuException() {
        assertTrue(new ObjNotFoundException("x") instanceof DpTuException);
    }
}
