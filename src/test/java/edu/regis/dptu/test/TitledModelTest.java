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
import edu.regis.dptu.model.TitledModel;

public class TitledModelTest {

    /** Minimal concrete subclass so the abstract base can be exercised directly. */
    private static final class TestModel extends TitledModel {
        TestModel() {
            super();
        }

        TestModel(int id) {
            super(id);
        }

        TestModel(int id, String title) {
            super(id, title);
        }
    }

    @Test
    public void testDefaultConstructor() {
        TestModel model = new TestModel();

        assertEquals(Model.DEFAULT_ID, model.getId());
        assertEquals("", model.getTitle());
        assertEquals("", model.getDescription());
    }

    @Test
    public void testIdConstructorHasEmptyTitleAndDescription() {
        TestModel model = new TestModel(11);

        assertEquals(11, model.getId());
        assertEquals("", model.getTitle());
        assertEquals("", model.getDescription());
    }

    @Test
    public void testIdAndTitleConstructor() {
        TestModel model = new TestModel(3, "Chapter 1");

        assertEquals(3, model.getId());
        assertEquals("Chapter 1", model.getTitle());
        assertEquals("", model.getDescription());
    }

    @Test
    public void testTitleRoundTrip() {
        TestModel model = new TestModel();

        model.setTitle("Updated");
        assertEquals("Updated", model.getTitle());
    }

    @Test
    public void testDescriptionRoundTrip() {
        TestModel model = new TestModel();

        model.setDescription("A longer description");
        assertEquals("A longer description", model.getDescription());
    }
}
