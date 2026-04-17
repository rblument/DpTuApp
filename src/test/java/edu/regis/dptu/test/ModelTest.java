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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.regis.dptu.model.Model;

@SuppressWarnings("Logging")
public class ModelTest {

    @Test
    public void modelSupportsIdentityAndValueSemantics() {
        StubModel defaultModel = new StubModel();
        StubModel modelA = new StubModel(7);
        StubModel modelB = new StubModel(7);
        StubModel modelC = new StubModel(9);

        assertEquals(Model.DEFAULT_ID, defaultModel.getId());
        assertEquals(7, modelA.getId());

        modelA.setId(8);
        assertEquals(8, modelA.getId());

        assertTrue(modelA.equals(modelA));
        assertFalse(modelA.equals(null));
        assertFalse(modelA.equals("not-model"));
        assertTrue(modelB.equals(new StubModel(7)));
        assertFalse(modelB.equals(modelC));

        assertEquals(new StubModel(8).hashCode(), modelA.hashCode());
        assertNotEquals("", modelA.toString());
    }

    private static class StubModel extends Model {
        StubModel() {
            super();
        }

        StubModel(int id) {
            super(id);
        }
    }
}
