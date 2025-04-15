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
package edu.regis.dptu.model;

/**
 * Represents a Longest Common Subsequence Dynamic Programming problem with
 * inputs sequences represented as Java Strings x and y of length n and m,
 * respectively.
 *
 * Execution of the step() method executes the next statement in the algorithm.
 * This may take a loop iteration variable from its uninitialized state to its
 * initial value or a loop that has reached its maximum iteration value to
 * exiting the loop. Otherwise, it executes the next iteration of the loop
 * incrementing the loop variable and performing the statements within the
 * associated loop.
 *
 * Note: in the Dynamic Programming cell table, the indexes of rows range from
 * -1 to n and columns from -1 to m. The corresponding Java subproblemL array
 * indexes corresponding rows from 0 to n+1 and columns from 0 to m+1. Hence,
 * cell [-1][-1] in the Dynamic Programming problem is array [0][0] in the Java
 * subproblemL array.
 *
 * @author rickb
 */
public class LCSProblem extends Problem {

    /**
     * Current state of execution capturing which of the loops are current. Note
     * if the corresponding iteration index for a loop is -1, the loop hasn't
     * entered its first iteration.
     */
    public enum EXECUTION_STATE {
        PRE, R_LOOP, C_LOOP, I_LOOP, J_LOOP, RETRN, POST
    };

    /**
     * Input sequence 1.
     */
    private final String x;

    /**
     * Convenience reference to the length of x.
     */
    // private final int n;
    /**
     * Input sequence 2.
     */
    private final String y;
    /**
     * Convenience reference to the length of y.
     */
    //private final int m;
    /**
     * Current value of iteration loop variable r. When this is -1, the loop
     * hasn't been entered, when it has a value of 0, it represents cell value
     * -1 in the Dynamic Programming problem.
     */
    //private int r;
    /**
     * Current value of iteration loop variable c. When this is -1, the loop
     * hasn't been entered, when it has a value of 0, it represents cell value
     * -1 in the Dynamic Programming problem.
     */
    //private int c;
    /**
     * Current value of iteration loop variable i. When this is -1, the loop
     * hasn't been entered, when it has a value of 0, it represents cell value
     * -1 in the Dynamic Programming problem.
     */
    //private int i;
    /**
     * Current value of iteration loop variable j. When this is -1, the loop
     * hasn't been entered, when it has a value of 0, it represents cell value
     * -1 in the Dynamic Programming problem.
     */
    //private int j;
    /**
     * The subproblem dynamic values.
     *
     * Note all indexes are shifted by 1 since subproblem[0][0] in the Java
     * array corresponds to cell[-1][-1] in the Dynamic Programming problem.
     */
    //private final int[][] subproblemL;
    /**
     * The current state of the algorithm, before the loops, in a loop, and
     * after all of the loops have executed.
     */
    private EXECUTION_STATE executionState;

    /**
     * Initialize this problem with the given input sequences.
     *
     * @param x the first input sequence, as a String
     * @param y the second input sequence, as String
     */
    public LCSProblem(String x, String y) {
        super();

        this.x = x;
        this.y = y;

        int n = x.length();
        int m = y.length();

        variables.put("x", x);
        variables.put("y", y);
        variables.put("n", n);
        variables.put("m", m);
        variables.put("r", -1);
        variables.put("c", -1);
        variables.put("i", -1);
        variables.put("j", -1);
        variables.put("l", new int[n + 1][m + 1]);

        tableVariable = "l";

        /*
        this.x = x;
        this.y = y;
        
        n = x.length();
        m = y.length();
        
        r = -1; // Loops have not been entered.
        c = -1;
        i = -1;
        j = -1;
        
        subproblemL = new int[n+1][m+1];
         */
        executionState = EXECUTION_STATE.PRE;

        kind = TaskKind.LCS_PROBLEM;

        loadCodeStatements();
    }

    /**
     * Return the first input sequence.
     *
     * @return String
     */
    // public String getX() {
    //    return x;
    //}
    /**
     * Return the second input sequence.
     *
     * @return String
     */
    //public String getY() {
    //    return y;
    // }
    /**
     * Return the length of the first input sequence
     *
     * @return int
     */
    //public int getN() {
    //    return n;
    // }
    /**
     * Return the length of the second input sequence
     *
     * @return
     */
    //public int getM() {
    //    return m;
    // }
    //public int getR() {
    //    return r - 1;
    //}
    // public int getC() {
    //    return c;
    // }
    // public int getI() {
    //    return i;
    //}
    //  public int getJ() {
    //    return j;
    //}
    // public int getCurrentValue() {
    //     return subproblemL[i][j];
    // }
    //  public int getValueAt(int row, int column) {
    //     return subproblemL[row][column];
    // }
    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    /**
     *
     * LCS(x,y)
     */
    private void executeLine0() {
        //System.out.println("executeLine0");

        variables.put("x", x);
        variables.put("y", y);
        currentLineNumber = 1;

    }

    /**
     *
     * for r = 1 to n-1 do
     */
    private void executeLine1() {
        //System.out.println("executeLine1");
        int r = (int) variables.get("r");


        if (r == -1) {
            variables.put("r", 0);
            executionState = EXECUTION_STATE.R_LOOP;
            currentLineNumber = 2;

        } else {
            int n = (int) variables.get("n");

            r++; // increment loop variable r
            variables.put("r", r);

            if (r == n + 1) {
                currentLineNumber = 3;
                executionState = EXECUTION_STATE.C_LOOP;

            } else {
                currentLineNumber = 2;
            }
        }

    }

    /**
     *
     * L[i,-1] = 0
     */
    private void executeLine2() {
        //System.out.println("executeLine2");

        int r = (int) variables.get("r");
        int[][] subproblem = (int[][]) variables.get(tableVariable);
        subproblem[r][0] = 0;
        currentLineNumber = 1;

    }

    /**
     *
     * for j = 0 to m-1 do
     */
    private void executeLine3() {
        //System.out.println("executeLine3");
        int c = (int) variables.get("c");

        if (c == -1) { // 
            variables.put("c", 1); // L[0,0] is already set
            currentLineNumber = 4;

        } else {
            int m = (int) variables.get("m");

            c++; // increment loop variable c
            variables.put("c", c);

            if (c == m + 1) { // Check loop boundary
                // fall out of c-loop
                currentLineNumber = 5;
                executionState = EXECUTION_STATE.I_LOOP;

            } else {
                currentLineNumber = 4;
            }
        }

    }

    public void executeLine4() {
       // System.out.println("executeLine4");

        int c = (int) variables.get("c");

        int[][] subproblem = (int[][]) variables.get(tableVariable);
        subproblem[0][c] = 0;
        currentLineNumber = 3;

    }

    /**
     *
     * Line 5 for i 0 to n-1 do
     */
    public void executeLine5() {
        //System.out.println("executeLine5: for i");

        int i = (int) variables.get("i");

        if (i == -1) { // 
            variables.put("i", 1); // remember 0 is -1 index
            currentLineNumber = 6;
            executionState = EXECUTION_STATE.J_LOOP;

        } else {
            int n = (int) variables.get("m");

            i++; // increment loop variable c
            variables.put("i", i);

            if (i == n + 1) { // Check loop boundary
                // fall out of i-loop
                currentLineNumber = 11;
                executionState = EXECUTION_STATE.RETRN;

            } else {
                // As we're re-entered the i-loop, we need to reset j-loop
                int j = (int) variables.get("j");
                int m = (int) variables.get("m");

                if (j == m + 1) {
                    variables.put("j", -1);
                    currentLineNumber = 6; // j-loop
                    executionState = EXECUTION_STATE.J_LOOP;
                } else {
                    currentLineNumber = 7; // if stmt
                }
            }
        }
    }

    /**
     * Line 6 for j = 0 to m-1 do
     */
    public void executeLine6() {
        //System.out.println("executeLine6");

        int j = (int) variables.get("j");

        if (j == -1) { // 
            variables.put("j", 1); // remember 0 is -1 index
            currentLineNumber = 7;

        } else {
            int m = (int) variables.get("m");

            j++; // increment loop variable c
            variables.put("j", j);

            if (j == m + 1) { // Check loop boundary
                // fall out of i-loop
                currentLineNumber = 5;
                executionState = EXECUTION_STATE.I_LOOP;

            } else {
                currentLineNumber = 7;  // if stmt
            }
        }
    }

    /**
     *
     * Line 7 if x[i] == y[i]
     */
    public void executeLine7() {
       // System.out.println("executeLine7: if");
        int[][] subproblem = (int[][]) variables.get(tableVariable);
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        String x = (String) variables.get("x");
        String y = (String) variables.get("y");

        if (x.charAt(i - 1) == y.charAt(j - 1)) {
            currentLineNumber = 8;
        } else { // the else is Line 9
            currentLineNumber = 10;
        }
    }

    /**
     *
     * Line 8 subproblemL[i][j] = subproblemL[i-1][j-1] + 1;
     */
    public void executeLine8() {
       // System.out.println("executeLine8");

        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        subproblemL[i][j] = subproblemL[i - 1][j - 1] + 1;

        currentLineNumber = 6; // j loop
    }

    /**
     *
     * Line 10 subproblemL[i][j] = Integer.max(subproblemL[i-1][j],
     * subproblemL[i][j-1]);
     */
    public void executeLine10() {
       // System.out.println("executeLine10");

        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        subproblemL[i][j] = Integer.max(subproblemL[i - 1][j],
                subproblemL[i][j - 1]);

        currentLineNumber = 6; // j loop
    }

    /**
     *
     * Line 11 return L
     */
    public void executeLine11() {
       // System.out.println("executeLine11");
        if (executionState == EXECUTION_STATE.RETRN) {
            executionState = EXECUTION_STATE.POST;
        } else {
            System.out.println("ERROR: Executing past return statement");
        }
    }

    public void undoLine0() {
        //System.out.println("undoLine0");

        reset();
    }

    public void undoLine1() {
       // System.out.println("undoLine1");
        
        int r = (int) variables.get("r");
        r--;
        
        if (r == -1) {
           executionState = EXECUTION_STATE.PRE;
           variables.put("r", -1);

        } else {
            variables.put("r", r);
        }
    }

    public void undoLine2() {
       // System.out.println("undoLine2");
        int r = (int) variables.get("r");
        int[][] subproblemL = (int[][]) variables.get("l");
        subproblemL[r][0] = -1;
    }

    public void undoLine3() {
        //System.out.println("undoLine3 for c");
        
        int c = (int) variables.get("c");

        c--;
        
        if (c == 0) { // C started looping from 1, so zero isn't handled here
           executionState = EXECUTION_STATE.R_LOOP;
           variables.put("c", -1);

        } else {
            variables.put("c", c);
        }
    }

    public void undoLine4() {
      //  System.out.println("undoLine4");
        int c = (int) variables.get("c");
        int[][] subproblemL = (int[][]) variables.get("l");
        subproblemL[0][c] = -1;
    }

    public void undoLine5() {
       // System.out.println("undoLine5: for i");
        
        int i = (int) variables.get("i");
        i--;
        
        if (i == 0) {
           executionState = EXECUTION_STATE.C_LOOP;
           variables.put("i", -1);

        } else {
            variables.put("i", i);
            int m = (int) variables.get("m");
            variables.put("j", m + 1);
            
        }
    }

    public void undoLine6() {
       // System.out.println("undoLine6: for j");
        
        int j = (int) variables.get("j");
        j--;
        
        if (j == 0) {
           executionState = EXECUTION_STATE.I_LOOP;
           variables.put("j", -1);
        } else {
            variables.put("j", j);
        }
    }

    public void undoLine7() {
       // System.out.println("undoLine7");
        executionState = EXECUTION_STATE.J_LOOP;
    }

    public void undoLine8() {
       // System.out.println("undoLine8");
        
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] subproblemL = (int[][]) variables.get("l");
        subproblemL[i][j] = -1;
    }

    public void undoLine10() {
       // System.out.println("undoLine10");
        
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] subproblemL = (int[][]) variables.get("l");
        subproblemL[i][j] = -1;
    }

    public void undoLine11() {
      //  System.out.println("undoLine11");

        executionState = EXECUTION_STATE.I_LOOP;

    }

    /**
     * This method adds all the statements for the codeView to the arrayList.
     *
     */
    @Override
    protected void loadCodeStatements() {
        // Line 0
        codeStatements.add("<html><pre>"
                + "<b>LCS(x,y)\n"
                + "</pre></html>");
        // Line 1
        codeStatements.add("<html><pre>"
                + "<b>for</b> r =0 to n-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   L[r,-1] = 0"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "<b>for</b> c =0 to m-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   L[-1,c] = 0"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "<b>for</b> i =0 to n-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   <b>for</b> j =0 to m-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "       <b>if</b> x<sub>i</sub> = y<sub>j</sub> <b>then</b>\n"
                + "</pre></html>");
        // Line 8
        codeStatements.add("<html><pre>"
                + "           L[i, j] = L[i-1, j+1] + 1\n"
                + "</pre></html>");
        // Line 9
        codeStatements.add("<html><pre>"
                + "       <b>else</b>\n"
                + "</pre></html>");
        // Line 10
        codeStatements.add("<html><pre>"
                + "           L[i, j] = max(L[i-1,j], L[i, j-1])"
                + "</pre></html>");
        // Line 11
        codeStatements.add("<html><pre>"
                + "<b>return</b> array L"
                + "</pre></html>");

    }

   
    /**
     * Undo the execution of the previous statement in the algorithm taking the
     * problem back to the previous state.
     *
     * Order of the states first -> last: (PRE, R_LOOP, C_LOOP, I_LOOP, J_LOOP,
     * POST) Order of the states last -> first: (POST, J_LOOP, I_LOOP, C_LOOP,
     * R_LOOP, PRE)
     *
     * @author EverettCV
     */
    /*
    public void stepBack() {
    
        switch (executionState) {
            // POST case, stepBack sets executionState to J_LOOP and sets i and j = to their respective max.
            case POST:
                executionState = EXECUTION_STATE.J_LOOP;
                i = n;
                j = m;
                break;
            
            // J_LOOP case, stepBack goes backwards one cell inside the current I row, and resets value to -1 (value when unvisited). If it is at the beginning of the row, set I_LOOP state.
            case J_LOOP:
                if (j > 0) {
                    subproblemL[i][j] = -1; // Resetting cell value to -1 (Starting value when unvisited)
                    j--;
                } else {
                    executionState = EXECUTION_STATE.I_LOOP;
                }
                break;

            // I_LOOP case, stepBack goes backwards (upwards technically) one cell. Decrements the i value and then sets the state to J_LOOP if there are more i rows left or C_LOOP if no more i rows left.
            case I_LOOP:
                if (i > 1) {
                    i--;
                    j = m;
                    executionState = EXECUTION_STATE.J_LOOP;
                } else {
                    i = -1;
                    j = -1;
                    executionState = EXECUTION_STATE.C_LOOP;
                }
                break;

            // C_LOOP case, stepBack goes backwards one cell at the top row (0 index). Resets value in specified cell to -1 (value when unvisited), sets state to R_LOOP once at the beginning of the c row.
            case C_LOOP:
                if (c == 1) {
                    executionState = EXECUTION_STATE.R_LOOP;
                    c = -1;
                } else {
                    c--;
                    subproblemL[0][c] = -1;

                }
                break;

            // R_LOOP case, stepBack goes backwards (technically upwards) one cell. Resets value in specified cell to -1 (value when unvisited), sets state to PRE once all cells have been reset.
            case R_LOOP:
                if (r == 0) {
                    executionState = EXECUTION_STATE.PRE;
                    r = -1;
                } else {
                    r--;
                    subproblemL[r][0] = -1;
                }
                break;

            // PRE case, Cannot steBack from PRE state, outputs a warning and ends stepBack.
            case PRE:
                System.out.println("Shouldn't stepBack() in PRE state");
                break;

            default: // Should never get here
                System.out.println("Unknown execution state: " + executionState);
                break;
        }
    }
     */
    /**
     * Forward the algorithm from an initial state to the indicated iteration
     * step in the r_loop.
     *
     * @param step number of iterations of the r-loop to execute.
     * @author EverettCV
     */
    /*
    public void stepRLoop(int step) {
        reset();
        executionState = EXECUTION_STATE.R_LOOP; // Set state to R_LOOP (First step from PRE)
        // Determines if the step input is valid and worth continuing execution.
        if (step > m + 1) {
            System.out.println("Invalid step count of: " + step + ". Maximum step count is: " + (m + 1) + ".");
        } else { // Sets r to 0 and begins to iterate through the r column until all values up until the specified step is filled with 0.
            r = 0;
            for (int i = 0; i < step; i++) {
                subproblemL[r][0] = 0;
                r++;
            }
        }
    }
     */
    /**
     * Forward the algorithm from an initial state to the indicated iteration
     * step in the c_loop (this will execute all iterations of the r_loop).
     *
     * @param step number of iterations of the c-loop to execute.
     * @author EverettCV
     */
    /*
    public void stepCLoop(int step) {
        reset();
        executionState = EXECUTION_STATE.R_LOOP; // Set state to R_LOOP (First step from PRE)
        r = 0; // Sets r value to starting value for R_LOOP.
        
        // Iterates through the r column, filling all cells with 0.
        while (executionState == EXECUTION_STATE.R_LOOP) {
            subproblemL[r][0] = 0;
            r++;
            
            if (r == m + 1) { // Once at the end of the R_LOOP, sets state to C_LOOP and initializes the starting value of the c row.
                executionState = EXECUTION_STATE.C_LOOP;
                c = 0;
            }
        }
        // Determines if the step input for C_LOOP is valid and worth continuing execution.
        if (step > n + 1) {
            System.out.println("Invalid step count of: " + step + ". Maximum step count is: " + (n + 1) + ".");
        } else { // Iterates through the c row, populating cells with a value of 0 until reaching the specified step input.
            for (int i = 0; i < step; i++) {
                subproblemL[0][c] = 0;
                c++;
            }
        }

    }
     */
    /**
     * Forward the algorithm from an initial state to the indicated iteration
     * step in the i and j loops (this will execute all iterations of the r and
     * c loops).
     *
     * @param stepI number of iterations of the i-loop to execute.
     * @param stepJ number of iterations of the j-loop to execute at the current
     * iteration of the given i-loop.
     * @author EverettCV
     */
    /*
    public void stepIJLoop(int stepI, int stepJ) {
        reset();
        executionState = EXECUTION_STATE.R_LOOP; // Set state to R_LOOP (First step from PRE)
        r = 0; // Sets r value to starting value for R_LOOP.

        // Iterates through the r column, filling all cells with 0.
        while(executionState == EXECUTION_STATE.R_LOOP) {
            subproblemL[r][0] = 0;
            r++;

            if (r == m + 1) { // Once at the end of the R_LOOP, sets state to C_LOOP and initializes the starting value of the c row.
                executionState = EXECUTION_STATE.C_LOOP;
                c = 0;
            }
        }

        // Iterates through the c row, filling all cells with 0.
        while (executionState == EXECUTION_STATE.C_LOOP) {
            subproblemL[0][c] = 0;
            c++;

            if (c == n + 1) { // Once at the end of the R_LOOP, sets state to C_LOOP.
                executionState = EXECUTION_STATE.I_LOOP;
            }
        }

        // Determines if stepI and stepJ inputs are valid and worth continuing execution. Only one needs to be invalid for both to be invalid.
        if (stepI > m + 1 || stepJ > n + 1) {
            if (stepI > m +1) {
                System.out.println("Invalid step count for i of: " + stepI + ". Maximum step count for i is: " + (m + 1) + ".");
            } else {
                System.out.println("Invalid step count for j of: " + stepJ + ". Maximum step count for j is: " + (n + 1) + ".");
            }
        } else { // Begin execution of the I/J LOOPs.
            for (int k = 0; k < stepI; k++) { // Executes until reaching specified i row.
                if (executionState == EXECUTION_STATE.I_LOOP) { // If first i row, initialize correct values and set executionState to J_LOOP.
                    if (i == -1) {
                        i = 1;
                        j = 0;
                        executionState = EXECUTION_STATE.J_LOOP;
                    } else { // Incrememnts i value and checks for if we have completed the entire problem or if we need to reset j value to 0 and set executionState to J_LOOP.
                        i++;
                        if (i == n + 1) {
                            executionState = EXECUTION_STATE.POST;
                        } else {
                            j = 0;
                            executionState = EXECUTION_STATE.J_LOOP;
                        }
                    }
                }

                if (executionState == EXECUTION_STATE.J_LOOP) { // J_LOOP logic
                    if (i == stepI) { // If we reach the specified i row from stepI value, we update cell values in the row until the specified j column from the stepJ value.
                        for (int l = 0; l < stepJ; l++) {
                            j++;
                            if (x.charAt(i-1) == y.charAt(j-1)) { // LCS match, populates cell with the above cell's value + 1.
                                subproblemL[i][j] = subproblemL[i-1][j-1] + 1;
                            } else { // No match, populates cell with the max of the previous row/column value or current row/previous column value.
                                subproblemL[i][j] = Integer.max(subproblemL[i-1][j],
                                                                subproblemL[i][j-1]);
                            }
                        }
                    } else { // If we have not reached specified i row value from stepI value, we iterate through entire row and populate cells or set executionState to I_LOOP and begin the next row.
                        while (executionState == EXECUTION_STATE.J_LOOP) {
                            j++;
                            if (j == m + 1) { // If we reach the end of the row, we set the executionState to I_LOOP to begin the next row.
                                executionState = EXECUTION_STATE.I_LOOP;
                            } else { // Still in current row
                                if (x.charAt(i-1) == y.charAt(j-1)) { // LCS match, populates cell with the above cell's value + 1.
                                    subproblemL[i][j] = subproblemL[i-1][j-1] + 1;
                                } else { // No match, populates cell with the max of the previous row/column value or current row/previous column value.
                                    subproblemL[i][j] = Integer.max(subproblemL[i-1][j],
                                                                    subproblemL[i][j-1]);
                                }
                            }
                        }
                    }
                }
            }
        }

    }
     */
    /**
     * Resets this problem (algorithm) back to its initial state before
     * execution of the first statement.
     */
    public void reset() {
        currentLineNumber = 0;
        variables.put("r", -1);
        variables.put("c", -1);
        variables.put("i", -1);
        variables.put("j", -1);

        int n = (int) variables.get("n");
        int m = (int) variables.get("m");
        int[][] subproblemL = (int[][]) variables.get(tableVariable);

        for (int p = 0; p <= n; p++) {
            for (int q = 0; q <= m; q++) {
                subproblemL[p][q] = -1;
            }
        }

        variables.put("l", subproblemL);
        
        executionState = EXECUTION_STATE.PRE;
    }

    /**
     * Outputs to System.out the current state (of the algorithm).
     */
    public void prettyPrint() {
        System.out.println("\nExecutionState: " + executionState);
        System.out.println("r: " + variables.get("r"));
        System.out.println("c: " + variables.get("c"));
        System.out.println("i: " + variables.get("i"));
        System.out.println("j: " + variables.get("j"));

        int n = (int) variables.get("n");
        int m = (int) variables.get("m");
        int[][] subproblemL = (int[][]) variables.get("l");

        /*
        System.out.println("r: " + r);
        System.out.println("c: " + c);
        System.out.println("i: " + i);
        System.out.println("j: " + j);
         */
        for (int p = 0; p <= n; p++) {
            for (int q = 0; q <= m; q++) {
                System.out.print(subproblemL[p][q] + " ");
            }

            System.out.println("");

        }

    }

}