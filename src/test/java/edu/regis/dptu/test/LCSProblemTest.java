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
        
        assertEquals(x.length(), n);
        assertEquals(y.length(), m);
        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        assertEquals(0, problem.getCurrentLineNumber());
        problem.step(); // executeLine0 LCS(x,y);
        assertEquals(LCSProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());
        
        int r = 0;

        // Execute the r loop
        while (r <= n) { // we start with r==0 which corresponds to -1 on the table
            assertEquals(1, problem.getCurrentLineNumber());
            problem.step(); // Line 1 r++
            r++;
            problem.step(); // Line 2 L[r,-1] = 0;
        }

        assertEquals(1, problem.getCurrentLineNumber());
        problem.step(); // Line 1 one last time to increment r past the boundary and break the loop

        assertEquals(LCSProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());
        assertEquals(-1, problem.getVariableValue("r"));

        // c loop
        for (int c = 0; c < m; c++) {
            assertEquals(3, problem.getCurrentLineNumber());
            problem.step(); // Line 3 c++
            problem.step(); // Line 4 L[-1,c] = 0;
        }

        assertEquals(3, problem.getCurrentLineNumber());
        problem.step(); // Line 3 one last time; sets execution state to I_LOOP and goto Line 5
        assertEquals(-1, problem.getVariableValue("c"));
        
        for (int i = 0; i < n; i++) {
            assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
            assertEquals(5, problem.getCurrentLineNumber());
            problem.step(); // Line 5 i loop
            assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
            for (int j = 0; j < m; j++) {
                assertEquals(i + 1, problem.getVariableValue("i"));
                assertEquals(6, problem.getCurrentLineNumber());
                problem.step(); // Line 6: j loop
                assertEquals(j + 1, problem.getVariableValue("j"));
                assertEquals(7, problem.getCurrentLineNumber());
                problem.step(); // Line 7: if statement always executes
                if (x.charAt(i) == y.charAt(j)) {
                    assertEquals(8, problem.getCurrentLineNumber());
                    problem.step(); // Line 8
                } else {
                    assertEquals(9, problem.getCurrentLineNumber());
                    problem.step(); // Line 9
                    problem.step(); // Line 10
                }
            }
            assertEquals(6, problem.getCurrentLineNumber());
            problem.step(); // Line 6 one last time to break the j loop
            //assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        }
        
        assertEquals(5, problem.getCurrentLineNumber());
        problem.step(); // Line 5 one last time to break the i loop and go to line 11

        assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());
        assertEquals(-1, problem.getVariableValue("i"));
        assertEquals(-1, problem.getVariableValue("j"));

        problem.step(); // Line 11

        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());

        //---------------------------Backtracking-------------------------------
        
        int row = n;
        int col = m;
        
        problem.backtrackingOn();
        assertEquals(LCSProblem.EXECUTION_STATE.B_PRE, problem.getExecutionState());
        
        problem.step(); // Line 100 Backtrack(table)
        problem.step(); // Line 101 row = ... col = ...
        assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
        
        while (row >= 1 && col >= 1) {
            assertEquals(102, problem.getNextLineNumber());
            problem.step(); // Line 102 while
            assertEquals(LCSProblem.EXECUTION_STATE.B_IF, problem.getExecutionState());
            // if
            if (x.charAt(row-1) == y.charAt(col-1)) {
                assertEquals(103, problem.getNextLineNumber());
                problem.step(); // Line 103 if x[row] == y[col]
                problem.step(); // Line 104 add to front of LCS
                problem.step(); // Line 105 row--
                row--;
                problem.step(); // Line 106 col--
                col--;
                assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
            }
            else {
                // if evaluated false
                assertEquals(103, problem.getNextLineNumber());
                problem.step(); // Line 103 if (now evaluating false)
                assertEquals(LCSProblem.EXECUTION_STATE.B_ELIF, problem.getExecutionState());
                // else if
                int[][] lTable = (int[][]) problem.getVariableObject("l");
                if (lTable[row-1][col] >= lTable[row][col-1]) {
                    assertEquals(107, problem.getNextLineNumber());
                    problem.step(); // Line 107 else if
                    assertEquals(108, problem.getNextLineNumber());
                    problem.step(); // Line 108 row--
                    row--;
                    assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
                }
                else {
                    //else if evaluated false
                    assertEquals(107, problem.getNextLineNumber());
                    problem.step(); // Line 107 else if
                    assertEquals(LCSProblem.EXECUTION_STATE.B_ELSE, problem.getExecutionState());
                    assertEquals(109, problem.getNextLineNumber());
                    problem.step(); // Line 109 else
                    problem.step(); // Line 110 col--
                    col--;
                    assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
                }
            }
        }
        //break while loop
        problem.step(); // Line 102 while
        assertEquals(LCSProblem.EXECUTION_STATE.B_RETRN, problem.getExecutionState());
        problem.step(); // Line 111 return LCS
        assertEquals(LCSProblem.EXECUTION_STATE.B_POST, problem.getExecutionState());
        
        //-------------------------------UNDO-----------------------------------
        
        //------------------------UNDO BACKTRACKING-----------------------------

        problem.undo(); // Line 111
        assertEquals(LCSProblem.EXECUTION_STATE.B_RETRN, problem.getExecutionState());
        
        problem.undo(); // Line 102 - the last while that breaks the loop
        assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
        
        while (row < n || col < m) {
            // check for nearby highlighted cells
            int[][] bTable = (int[][]) problem.getVariableObject("b");
            if (bTable[row][col+1] != -1) {
                // if we're here, then we wound up in the else case
                problem.undo(); // Line 110 col--
                col++;
                assertEquals(110, problem.getNextLineNumber());
                assertEquals(LCSProblem.EXECUTION_STATE.B_ELSE, problem.getExecutionState());
                problem.undo(); // Line 109 else
                problem.undo(); // Line 107 else if
                assertEquals(LCSProblem.EXECUTION_STATE.B_ELIF, problem.getExecutionState());
                problem.undo(); // Line 103 if
                assertEquals(LCSProblem.EXECUTION_STATE.B_IF, problem.getExecutionState());
                problem.undo(); // Line 102 while
                assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
            }
            else if (bTable[row+1][col] != -1) {
                // if we're here, we wound up in the else if case
                problem.undo(); // Line 108 row--
                row++;
                assertEquals(LCSProblem.EXECUTION_STATE.B_ELIF, problem.getExecutionState());
                problem.undo(); // Line 107 else if
                problem.undo(); // Line 103 if
                assertEquals(103, problem.getNextLineNumber());
                assertEquals(LCSProblem.EXECUTION_STATE.B_IF, problem.getExecutionState());
                problem.undo(); // Line 102 while
                assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
            }
            else {
                // If we're here, the if statement was true
                problem.undo(); // Line 106 col--
                col++;
                assertEquals(LCSProblem.EXECUTION_STATE.B_IF, problem.getExecutionState());
                problem.undo(); // Line 105 row--
                row++;
                problem.undo(); // Line 104 add to LCS
                problem.undo(); // Line 103 if
                problem.undo(); // Line 102 while
                assertEquals(LCSProblem.EXECUTION_STATE.B_WHILE, problem.getExecutionState());
            }
        }
        problem.undo(); // Line 101 row=... col=...
        assertEquals(LCSProblem.EXECUTION_STATE.B_PRE, problem.getExecutionState());
        problem.undo(); // Line 100 Backtrack(table)
        problem.undoingBacktrackButton(); // back to LCS algorithm
        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());
        
        //----------------------------UNDO LCS----------------------------------
        
        problem.undo(); // Line 11 return LCS
        assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());
        
        problem.undo(); // Line 5 last time checking i loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        for (int i = n-1; i >= 0; i--) {
            problem.undo(); // Line 6 last time checking j loop
            assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
            for (int j = m-1; j >= 0; j--) {
                String word1 = problem.getX();
                String word2 = problem.getY();
                if (word1.charAt(i) == word2.charAt(j)) {
                    problem.undo(); // Line 8 L[i,j] = L[i-1,j-1] + 1
                    problem.undo(); // Line 7 if xi == yj
                    problem.undo(); // Line 6 for j=0 to m-1
                }
                else {
                    problem.undo(); // Line 10 L[i,j] = max...
                    problem.undo(); // Line 9 else
                    problem.undo(); // Line 7 if xi == yj
                    problem.undo(); // Line 6 for j=0 to m-1
                }
            }
            problem.undo(); // Line 5 for i=0 to n-1
            assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        }
        
        problem.undo(); // Line 3 last time checking c loop
        assertEquals(LCSProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());
        for (int c = 0; c < m; c++) {
            problem.undo(); // Line 4 L[-1,c] = 0
            problem.undo(); // Line 3 c loop
        }
        problem.undo(); // Line 1 last time checking r loop
        assertEquals(LCSProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());
        for (r = -1; r < n; r++) {
            problem.undo(); // Line 2 L[r,-1] = 0
            problem.undo(); // Line 1 r loop
        }
        
        problem.undo(); // Line 0 LCS(x,y)
        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());

        // assertEquals(LCSProblem.EXECUTION_STATE.RETRN, problem.getExecutionState());

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
