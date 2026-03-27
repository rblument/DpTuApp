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

import edu.regis.dptu.err.InconsistentDBException;

public class InconsistentDBExceptionTest {

    @Test
    public void testConstructorWithCause() {
        Throwable cause = new IllegalStateException("root-cause");
        InconsistentDBException ex = new InconsistentDBException("inconsistent", cause);

        assertEquals("inconsistent", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
