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

/**
 * Verifies the abstract root {@link DpTuException} preserves the message and cause it is
 * constructed with. A minimal concrete subclass is used because the class is abstract.
 */
public class DpTuExceptionTest {

    /** Minimal concrete subclass so the abstract base can be exercised directly. */
    private static final class TestException extends DpTuException {
        TestException(String msg) {
            super(msg);
        }

        TestException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    @Test
    public void testMessageOnlyConstructor() {
        DpTuException ex = new TestException("something failed");

        assertEquals("something failed", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        Throwable cause = new IllegalStateException("root-cause");
        DpTuException ex = new TestException("wrapped failure", cause);

        assertEquals("wrapped failure", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testIsCheckedException() {
        assertTrue(new TestException("x") instanceof Exception);
    }
}
