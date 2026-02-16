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

import edu.regis.dptu.model.Hint;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {

    @Test
    void testDefaultConstructor() {
        Hint hint = new Hint();

        assertNotNull(hint);
        assertEquals("", hint.getText(), "Default text should be empty string");
        assertEquals(1, hint.getSequenceIndex(), "Default sequenceIndex should be 1");
    }

    @Test
    void testConstructorWithId() {
        int id = 42;
        Hint hint = new Hint(id);

        assertNotNull(hint);
        assertEquals(id, hint.getId(), "Constructor should set model id");
    }

    @Test
    void testSetAndGetText() {
        Hint hint = new Hint();

        hint.setText("Try filling the table row by row.");

        assertEquals("Try filling the table row by row.", hint.getText());
    }

    @Test
    void testSetAndGetSequenceIndex() {
        Hint hint = new Hint();

        hint.setSequenceIndex(5);

        assertEquals(5, hint.getSequenceIndex());
    }

    @Test
    void testMultipleUpdates() {
        Hint hint = new Hint();

        hint.setText("First hint");
        hint.setSequenceIndex(2);

        hint.setText("Second hint");
        hint.setSequenceIndex(3);

        assertEquals("Second hint", hint.getText());
        assertEquals(3, hint.getSequenceIndex());
    }
}
