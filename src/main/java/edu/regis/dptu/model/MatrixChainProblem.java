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

import java.util.ArrayList;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MatrixChainProblem implements the dynamic programming algorithm for the Matrix Chain
 * Multiplication problem. It extends the generic Problem class and provides step-by-step execution
 * and undo functionality.
 *
 * <p>State transitions are tracked via the EXECUTION_STATE enum, and intermediate DP table changes
 * are recorded in a history stack to support undo operations.
 *
 * @author Corey Brantley
 */
public class MatrixChainProblem extends Problem {
    private static final Logger log = LoggerFactory.getLogger(MatrixChainProblem.class);

    /**
     * Enumeration of execution states corresponding to each loop or phase in the matrix chain
     * algorithm.
     */
    public enum EXECUTION_STATE {
        PRE,
        R_LOOP,
        C_LOOP,
        I_LOOP,
        J_LOOP,
        POST
    }

    // Current state of execution within the algorithm
    private EXECUTION_STATE executionState;

    // Stack to record changes to m[i][j] for undo functionality
    private Stack<int[]> mHistory = new Stack<>();

    //For storing split points
    private Stack<int[]> sHistory = new Stack<>();

    // Backtracking parenthesization result
    private String currentParens = "";

    // backtracking table for highlighting
    private int[][] bTable;
    
    // Enum backtracking states
    public enum B_STATE {
        B_PRE,
        B_WHILE,
        B_POP,
        B_LEAF,
        B_SPLIT,
        B_CLOSE,
        B_POST
    }
    
    private B_STATE bState;

    // Stack for backtracking
    private Stack<Object> btStack = new Stack<>();

    // Marker for ")"
    private static final String CLOSE_PAREN = ")";
    
    /**
     * Constructor initializes the DP variables, dimension list "d", and the empty cost table "m"
     * with -1 placeholders. Sets initial execution state and loads code statements.
     *
     * @param sizes 2D array of matrix dimensions (p x q pairs)
     */
    public MatrixChainProblem(int[][] sizes) {
        this(Model.DEFAULT_ID, sizes);
    }

    /**
     * Constructor initializes the DP variables, dimension list "d", and the empty cost table "m"
     * with -1 placeholders. Sets initial execution state and loads code statements.
     *
     * @param id the unique id of this problem, as assigned by the DB.
     * @param sizes 2D array of matrix dimensions (p x q pairs)
     */
    public MatrixChainProblem(int id, int[][] sizes) {
        super(id);

        int n = sizes.length; // number of matrices
        ArrayList<Integer> d = new ArrayList<>();

        // Build dimension list: first matrix's row count
        d.add(sizes[0][0]);
        // Add each matrix's column count
        for (int[] size : sizes) {
            d.add(size[1]);
        }

        // Initialize DP variables in Problem.variables map
        variables.put("n", n);
        variables.put("c", 1);
        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("d", d);
        variables.put("m", new int[n][n]);
        variables.put("s", new int[n][n]);        // splits
        variables.put("b", new int[n][n]);        // backtracking highlight
        bTable = (int[][]) variables.get("b");
        

        // Specify which variable represents the DP table
        tableVariable = "m";

        // Fill cost table with -1 as placeholder
        int[][] m = (int[][]) variables.get("m");
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                m[x][y] = -1;
            }
        }

        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                s[x][y] = -1;           // no split recorded yet
                b[x][y] = UNVISITED;    // highlight table
            }
        }
                

        // Set initial execution state and task kind
        executionState = EXECUTION_STATE.PRE;

        // Load pseudocode statements for display
        loadCodeStatements();
    }

    /**
     * {@iniheritDoc}
     *
     * @return ProblemKind.MATRIX_CHAIN
     */
    @Override
    public ProblemKind getType() {
        return ProblemKind.MATRIX_CHAIN;
    }

    /**
     * Returns the current execution state of the algorithm.
     *
     * @return The current EXECUTION_STATE
     */
    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    @Override
    public boolean backtrackReady() {
        return executionState == EXECUTION_STATE.POST
            || (bState != null && bState != B_STATE.B_POST);
    }

    @Override
    public void backtrackingOn() {
        nextLineNumber = BACKTRACKING_START_NUM;
        bState = B_STATE.B_PRE;
        
        // Clear b table
        int[][] b = (int[][]) variables.get("b");
        for (int[] row : b) {
            java.util.Arrays.fill(row, UNVISITED);
        }
        
        currentParens = "";
        btStack.clear();

        notifyProblemListeners();
    }

    @Override
    public boolean undoingBacktrackButton() {
        // TODO
        return false;
    }

    @Override
    public boolean canStepBack() {
        return !executionHistory.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasFinished() {
        return executionState == EXECUTION_STATE.POST
            || (bState == B_STATE.B_POST);
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        executionState = EXECUTION_STATE.PRE;
        bState = null;
        nextLineNumber = 0;
        executionHistory.clear();
        mHistory.clear();
        sHistory.clear();
        btStack.clear();
        currentParens = "";

        // Reset m and s tables
        int[][] m = (int[][]) variables.get("m");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");
        int n = (int) variables.get("n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = -1;
                s[i][j] = -1;
                b[i][j] = UNVISITED;
            }
        }

        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("c", 1);

        notifyProblemListeners();
    }

    /**
     * Loads HTML-formatted code statements representing each step of the matrix chain
     * multiplication algorithm. These statements are shown in the view during execution.
     */
    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for i = 0 to n-1</pre></html>"); // initialize diagonals
        codeStatements.add("<html><pre>    m[i][i] = 0</pre></html>");
        codeStatements.add("<html><pre>for c = 1 to n-1</pre></html>"); // chain length loop
        codeStatements.add("<html><pre>    for i = 0 to n - c</pre></html>");
        codeStatements.add("<html><pre>        j = i + c</pre></html>");
        codeStatements.add("<html><pre>        m[i][j] = ∞</pre></html>");
        codeStatements.add("<html><pre>        for k = i to j-1</pre></html>");
        codeStatements.add(
                "<html><pre>            cost = m[i][k] + m[k+1][j] + d[i]*d[k+1]*d[j+1]</pre></html>");
        codeStatements.add("<html><pre>            if cost < m[i][j]: m[i][j] = cost</pre></html>");
        codeStatements.add("<html><pre>return m</pre></html>"); // final result
    }

    @Override
    protected void loadBacktrackingCodeStatements() {
        backtrackingCodeStatements.clear();
        backtrackingCodeStatements.add("<html><pre>BACKTRACK(0, n-1)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>push (0, n-1)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>while stack not empty</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  (i,j) = pop()</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  if i == j: output Ai</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  else:</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    k = s[i][j]</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    output '('</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    BACKTRACK(i, k)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    BACKTRACK(k+1, j)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    output ')'</pre></html>");
    }

    /**
     * Prints a console representation of the DP table and state, useful for debugging and testing.
     */
    public void prettyPrint() {
        int[][] m = (int[][]) variables.get("m");
        int n = (int) variables.get("n");

        System.out.println("ExecutionState: " + executionState);
        System.out.println("Current line: " + nextLineNumber);
        System.out.println("DP Table (m):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Display '-' for uninitialized cells
                System.out.printf("%6s", (m[i][j] == -1 ? "-" : m[i][j]));
            }
            System.out.println();
        }
    }

    /** Executes line 0: initializes i to 0 and transitions to R_LOOP. */
    public void executeLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.R_LOOP;
        nextLineNumber = 1;
    }

    /**
     * Executes line 1: sets m[i][i] to 0 and records the change. Advances i or moves to chain
     * length loop when done.
     */
    public void executeLine1() {
        int i = (int) variables.get("i");
        int n = (int) variables.get("n");
        int[][] m = (int[][]) variables.get("m");
        // Record previous value for undo
        mHistory.push(new int[] {i, i, m[i][i]});
        m[i][i] = 0;
        if (i + 1 == n) {
            // Move to chain length iteration
            variables.put("c", 1);
            executionState = EXECUTION_STATE.C_LOOP;
            nextLineNumber = 2;
        } else {
            // Continue along the diagonal
            variables.put("i", i + 1);
            nextLineNumber = 1;
        }
    }

    /**
     * Executes line 2: checks chain length c; if complete, transitions to POST, otherwise starts
     * inner loops.
     */
    public void executeLine2() {
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");
        if (c == n) {
            executionState = EXECUTION_STATE.POST;
            nextLineNumber = 9;
        } else {
            variables.put("i", 0);
            nextLineNumber = 3;
        }
    }

    /**
     * Executes line 3: determines whether to increment c or proceed to set j based on current i and
     * c.
     */
    public void executeLine3() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");
        if (i > n - c - 1) {
            // End of i-loop for this chain length
            variables.put("c", c + 1);
            nextLineNumber = 2;
        } else {
            nextLineNumber = 4;
        }
    }

    /** Executes line 4: computes j = i + c and stores it. */
    public void executeLine4() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int j = i + c;
        variables.put("j", j);
        nextLineNumber = 5;
    }

    /**
     * Executes line 5: sets m[i][j] to infinity (max int), records change, and initializes k for
     * k-loop.
     */
    public void executeLine5() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");
        mHistory.push(new int[] {i, j, m[i][j]});
        m[i][j] = Integer.MAX_VALUE;
        variables.put("k", i);
        nextLineNumber = 6;
    }

    /**
     * Executes line 6: checks if k-loop is done; if so, increments i to next row; otherwise moves
     * to cost calc.
     */
    public void executeLine6() {
        int k = (int) variables.get("k");
        int j = (int) variables.get("j");
        if (k >= j) {
            int i = (int) variables.get("i");
            variables.put("i", i + 1);
            nextLineNumber = 3;
        } else {
            nextLineNumber = 7;
        }
    }

    /**
     * Executes line 7: computes cost for splitting at k and stores it in variables for comparison.
     */
    @SuppressWarnings("unchecked")
    public void executeLine7() {
        int[][] m = (int[][]) variables.get("m");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int k = (int) variables.get("k");
        ArrayList<Integer> d = (ArrayList<Integer>) variables.get("d");
        // cost = cost_left + cost_right + multiplication cost
        int cost = m[i][k] + m[k + 1][j] + d.get(i) * d.get(k + 1) * d.get(j + 1);
        variables.put("cost", cost);
        nextLineNumber = 8;
    }

    /**
     * Executes line 8: if new cost is lower, updates m[i][j], records change, and increments k for
     * next iteration.
     */
    public void executeLine8() {
        int cost = (int) variables.get("cost");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");
        if (cost < m[i][j]) {
            mHistory.push(new int[] {i, j, m[i][j]});
            m[i][j] = cost;

            // Record split
            int[][] s = (int[][]) variables.get("s");
            sHistory.push(new int[] {i, j, s[i][j]});
            s[i][j] = k; 
        }
        int k = (int) variables.get("k");
        variables.put("k", k + 1);
        nextLineNumber = 6;
    }

    /** Executes line 9: marks algorithm as complete. */
    public void executeLine9() {
        executionState = EXECUTION_STATE.POST;
    }

    // Line 100: Initialize
    public void executeLine100() {
        currentParens = "";
        btStack.clear();
        btStack.push(new int[]{0, (int)variables.get("n") - 1});
        bState = B_STATE.B_PRE;
        nextLineNumber = 101;
    }

    // Line 101: If stack empty then done
    public void executeLine101() {
        if (btStack.isEmpty()) {
            nextLineNumber = 118;
            bState = B_STATE.B_POST;
        } else {
            nextLineNumber = 102;
            bState = B_STATE.B_WHILE;
        }
    }

    // Line 102: Pop
    public void executeLine102() {
        Object obj = btStack.pop();

        if (obj instanceof String) {
            // CLOSE_PAREN handling
            currentParens += ")";
            nextLineNumber = 101;
            bState = B_STATE.B_CLOSE;
            return;
        }

        int[] pair = (int[]) obj;
        variables.put("i", pair[0]);
        variables.put("j", pair[1]);

        nextLineNumber = 103;
        bState = B_STATE.B_POP;
    }

    // Line 103: Leaf
    public void executeLine103() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        if (i == j) {
            currentParens += "A" + i;
            bTable[i][j] = ADD_TO_SOLUTION;
            nextLineNumber = 101;
            bState = B_STATE.B_LEAF;
        } else {
            nextLineNumber = 104;
        }
    }

    // Line 104: Split
    public void executeLine104() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        
        int[][] s = (int[][]) variables.get("s");
        int k = s[i][j];
        variables.put("k", k);

        currentParens += "(";
        bTable[i][j] = HIT;

        // Order: closing paren
        btStack.push(CLOSE_PAREN);
        btStack.push(new int[]{k + 1, j});
        btStack.push(new int[]{i, k});

        nextLineNumber = 101;
        bState = B_STATE.B_SPLIT;
    }

    // Line 118: Done
    public void executeLine118() {
        bState = B_STATE.B_POST;
    }
        
        
    /** Undoes executeLine0: resets i and returns to PRE state. */
    public void undoLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;
    }

    /**
     * Undoes the effects of executeLine1 (m[i][i] assignment). Restores previous value from
     * history.
     */
    public void undoLine1() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }
        int i = (int) variables.get("i") - 1;
        if (i >= 0) {
            variables.put("i", i);
        }
        if (i <= 0) executionState = EXECUTION_STATE.PRE;
    }

    /** Undoes executeLine2: decrements c and resets i. */
    public void undoLine2() {
        int c = (int) variables.get("c") - 1;
        variables.put("c", c);
        variables.put("i", 0);
        if (c <= 0) executionState = EXECUTION_STATE.R_LOOP;
    }

    /** Undoes executeLine3: decrements i and adjusts state if needed. */
    public void undoLine3() {
        int i = (int) variables.get("i") - 1;
        variables.put("i", i);
        if (i < 0) executionState = EXECUTION_STATE.C_LOOP;
    }

    /** Undoes executeLine4: resets j to 0. */
    public void undoLine4() {
        variables.put("j", 0);
    }

    /** Undoes executeLine5: restores previous m[i][j] from history. */
    public void undoLine5() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }
    }

    /** Undoes executeLine6: decrements k for previous iteration. */
    public void undoLine6() {
        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    /** Undoes executeLine7: removes cost variable. */
    public void undoLine7() {
        variables.remove("cost");
    }

    /** Undoes executeLine8: restores m[i][j] and decrements k. */
    public void undoLine8() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }
        // Restore split table
        if (!sHistory.isEmpty()) {
            int[] prev = sHistory.pop();
            int[][] s = (int[][]) variables.get("s");
            s[prev[0]][prev[1]] = prev[2];
        }
        
        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    /** Undoes executeLine9: resets c and state to before POST. */
    public void undoLine9() {
        executionState = EXECUTION_STATE.C_LOOP;
        variables.put("c", (int) variables.get("n"));
    }

    public void undoLine100() {
        btStack.clear();
        currentParens = "";
        bState = B_STATE.B_PRE;
    }

    public void undoLine101() {
        // no state change
    }

    public void undoLine102() {
        //none
    }

    public void undoLine103() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        bTable[i][j] = UNVISITED;

        // remove A + i
        if (currentParens.endsWith("A" + i)) {
            currentParens = currentParens.substring(0,
                currentParens.length() - ("A" + i).length());
        }
    }

    public void undoLine104() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        bTable[i][j] = UNVISITED;

        // undo "("
        if (currentParens.endsWith("(")) {
            currentParens = currentParens.substring(0,
                currentParens.length() - 1);
        }
    }

    public void undoLine118() {
        bState = B_STATE.B_WHILE;
    }

}
