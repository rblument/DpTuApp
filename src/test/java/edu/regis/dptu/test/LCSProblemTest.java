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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.LCSProblem;

/**
 * @author rickb
 */
public class LCSProblemTest {

    public LCSProblemTest() {}

    @BeforeAll
    public static void setUpClass() {}

    @AfterAll
    public static void tearDownClass() {}

    @BeforeEach
    public void setUp() {}

    @AfterEach
    public void tearDown() {}

    /** Test the step() and undo() methods of Problem */
    @Test
    public void testAll() {
        // This test should pass with any strings except empty strings
        String x = "skullandbones"; // n == 13
        String y = "lullabybabies";

        LCSProblem problem = new LCSProblem(x, y);
        problem.reset();

        int n = problem.getVariableValue("n");
        int m = problem.getVariableValue("m");

        // assertEquals(x.length(), problem.getN());
        // assertEquals(y.length(), problem.getM());
        assertEquals(x.length(), n);
        assertEquals(y.length(), m);
        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        problem.step(); // executeLine0 LCS(x,y);
        problem.step(); // executeLine1 r_loop starting at -1 (really 0 in Java);
        int r = 0;

        assertEquals(LCSProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());

        problem.step(); // executeLine2 L[-1,-1]  really L[0,0] in Java

        // Finish the r loop
        while (r <= n) {//we start with r==0 which corresponds to -1 on the table
            problem.step(); // Line 1 r++
            r++;
            problem.step(); // Line 2 L[r,-1] = 0;
        }
        
        problem.step(); // Line 1 one last time to increment r past the boundary and break the loop
        
        assertEquals(LCSProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());
        assertEquals(-1, problem.getVariableValue("r"));

        // c loop - we already did the column for the first letter
        for (int c = 0; c < m-1; c++) {
            problem.step(); // Line 3 c++
            problem.step(); // Line 4 L[-1,c] = 0;
        }
        
        problem.step(); // Line 3 one last time; sets execution state to I_LOOP and goto Line 5

        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        assertEquals(-1, problem.getVariableValue("c"));
        
        problem.step(); // Line 5 i loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        for (int i = 0; i < n; i++) {
            problem.step(); // Line 6 j loop
            assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
            for (int j = 0; j < m; j++) {
                assertEquals(i+1, problem.getVariableValue("i"));
                assertEquals(j+1, problem.getVariableValue("j"));
                problem.step(); // Line 7: if statement always executes
                if (x.charAt(i) == y.charAt(j)) {
                    problem.step(); // Line 8
                }
                else {
                    problem.step(); // Line 10
                }
                problem.step(); // Line 6 j loop
            }
            assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
            problem.step(); // Line 5 i loop
        }
        
        assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());
        assertEquals(-1, problem.getVariableValue("i"));
        assertEquals(-1, problem.getVariableValue("j"));
        
        problem.step(); // Line 11
        
        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());
        
        problem.prettyPrint();

        //TODO implement undo() methods in LCSProblem
        problem.undo();
        
        //assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());
        

//
//        // Start undoing
//        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());
//
//        problem.undo(); // Line 11
//
//        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
//
//        for (int xx = 0; xx < 591; xx++) problem.undo();
//
//        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());
//
//        problem.prettyPrint();
    }

    /**
     * Test method for stepRLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testRLoop() {
        /*
                System.out.println("\nPerforming testRLoop() method:\n\n");

                String x = "skullandbones";
                String y = "lullabybabies";

                LCSProblem problem = new LCSProblem(x, y);
                problem.reset();

                Random rand = new Random();

                int stepCount = rand.nextInt(14);

                System.out.println("stepCount = " + stepCount);

                problem.stepRLoop(stepCount);

                problem.prettyPrint();
        */
    }

    /**
     * Test method for stepRLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testCLoop() {
        /*
        System.out.println("\nPerforming testCLoop() method:\n\n");

        String x = "skullandbones";
        String y = "lullabybabies";

        LCSProblem problem = new LCSProblem(x, y);
        problem.reset();

        Random rand = new Random();

        int stepCount = rand.nextInt(14);

        System.out.println("stepCount = " + stepCount);

        problem.stepCLoop(stepCount);

        problem.prettyPrint();
        */

    }

    /**
     * Test method for stepIJLoop() method.
     *
     * @author EverettCV
     */
    @Test
    public void testIJLoop() {
        /*
                System.out.println("\nPerforming testIJLoop() method:\n\n");

                String x = "skullandbones";
                String y = "lullabybabies";

                LCSProblem problem = new LCSProblem(x, y);
                problem.reset();

                Random rand = new Random();

                int stepCountI = rand.nextInt(y.length());
                int stepCountJ = rand.nextInt(x.length());

                System.out.println("stepCountI = " + stepCountI);
                System.out.println("stepCountJ = " + stepCountJ);

                problem.stepIJLoop(stepCountI, stepCountJ);

                problem.prettyPrint();
        */
    }
}
