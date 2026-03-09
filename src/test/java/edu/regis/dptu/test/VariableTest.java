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

import edu.regis.dptu.model.DataType;
import edu.regis.dptu.model.Variable;

public class VariableTest {

    @Test
    public void testVariableMutators() {
        Variable variable = new Variable("n");
        variable.setDataType(DataType.INT);
        variable.setValue(42);
        variable.setIsInput(true);
        variable.setDimensions(1);

        assertEquals("n", variable.getName());
        assertEquals(DataType.INT, variable.getDataType());
        assertEquals(42, variable.getValue());
        assertTrue(variable.isIsInput());
        assertEquals(1, variable.getDimensions());
    }
}

