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

import edu.regis.dptu.model.MatrixChainProblem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author corey
 */
public class MatrixChainProblemTest {
    
    public MatrixChainProblemTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @Test
    public void testAll() {
        int[][] sizes =  {
            {10, 5}, {5, 2}, {2, 20}, {20, 12}, {12, 4}, {4, 60}
        };

        MatrixChainProblem problem = new MatrixChainProblem(sizes);

        // Start in PRE
        assertEquals(MatrixChainProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        // Step once to enter R_LOOP
        problem.step();
        assertEquals(MatrixChainProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());

        // Complete R_LOOP
        for (int i = 0; i < problem.getN() + 1; i++) {
            problem.step();
        }
        
        problem.step();
        
        assertEquals(MatrixChainProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());

        // Complete full DP table build
        while (problem.getExecutionState() != MatrixChainProblem.EXECUTION_STATE.POST) {
            problem.step();
        }

        // Final state should be POST
        assertEquals(MatrixChainProblem.EXECUTION_STATE.POST, problem.getExecutionState());

        // Check final cost value is correct
        int expectedCost = 2356; // Verified optimal cost for this matrix chain
        assertEquals(expectedCost, problem.getValueAt(0, problem.getN()));

        // Optional printout
        problem.prettyPrint();
    }
}
