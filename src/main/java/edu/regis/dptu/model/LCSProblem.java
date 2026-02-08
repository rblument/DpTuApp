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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Longest Common Subsequence Dynamic Programming problem with inputs sequences
 * represented as Java Strings x and y of length n and m, respectively.
 *
 * <p>Execution of the step() method executes the next statement in the algorithm. This may take a
 * loop iteration variable from its uninitialized state to its initial value or a loop that has
 * reached its maximum iteration value to exiting the loop. Otherwise, it executes the next
 * iteration of the loop incrementing the loop variable and performing the statements within the
 * associated loop.
 *
 * <p>Note: in the Dynamic Programming cell table, the indexes of rows range from -1 to n and
 * columns from -1 to m. The corresponding Java subproblemL array indexes corresponding rows from 0
 * to n+1 and columns from 0 to m+1. Hence, cell [-1][-1] in the Dynamic Programming problem is
 * array [0][0] in the Java subproblemL array.
 *
 * @author rickb
 */
public class LCSProblem extends Problem {
    /** The logger for the class */
    private static final Logger log = LoggerFactory.getLogger(LCSProblem.class);

    /**
     * Current state of execution capturing which of the loops are current. Note if the
     * corresponding iteration index for a loop is -1, the loop hasn't entered its first iteration.
     */
    public enum EXECUTION_STATE {
        PRE,
        R_LOOP,
        C_LOOP,
        I_LOOP, // } These represent the nested
        J_LOOP, // } for loop
        RETRN,
        POST,
        B_PRE,
        B_WHILE,
        B_IF,
        B_RETRN,
        B_ELIF,
        B_ELSE,
        B_POST
    };

    /** Input sequence 1. (Stored in variables map) */
    private final String x; // Keep original field if needed, though data is in map

    /** Input sequence 2. (Stored in variables map) */
    private final String y; // Keep original field if needed

    /** This keeps track of the lcs as determined by the backtracking algorithm */
    private String currentLcs = "";

    /**
     * The current state of the algorithm, before the loops, in a loop, and after all of the loops
     * have executed.
     */
    private EXECUTION_STATE executionState;

    public LCSProblem() {
        this(Model.DEFAULT_ID, "skullandbones", "lullabybabies");
    }

    /**
     * Initialize this problem with the given input sequences.
     *
     * @param x the first input sequence, as a String
     * @param y the second input sequence, as String
     */
    public LCSProblem(String x, String y) {
        this(Model.DEFAULT_ID, x, y);
    }

    /**
     * Initialize this problem with the given input sequences.
     *
     * @param id, the unique id of this problem, as assigned by the DB.
     * @param x the first input sequence, as a String
     * @param y the second input sequence, as String
     */
    public LCSProblem(int id, String x, String y) {
        super(id);

        this.x = x; // Store original if needed by other parts not shown
        this.y = y; // Store original if needed

        int n = x.length();
        int m = y.length();

        // Initialize variables map
        variables.put("x", x);
        variables.put("y", y);
        variables.put("n", n);
        variables.put("m", m);
        variables.put("r", -1);
        variables.put("c", -1);
        // The DP table
        variables.put("l", new int[n + 1][m + 1]);
        // An internal table to keep track of highlighting for backtracking
        variables.put("b", new int[n + 1][m + 1]);

        tableVariable = "l"; // Set the name of the table variable
        backtrackingTableVariable = "b";
        // Tables are initialized in reset()

        executionState = EXECUTION_STATE.PRE;

        loadCodeStatements(); // Load the pseudocode lines
        loadBacktrackingCodeStatements();

        reset(); // Call reset to ensure consistent initial state including table values
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getCurrentLcs() {
        return currentLcs;
    }

    /**
     * {@inheritDoc}
     *
     * @return ProblemKind.LCS_PROBLEM
     */
    @Override
    public ProblemKind getType() {
        return ProblemKind.LCS_PROBLEM;
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    /** Initialize the DP table with -1 (or another indicator of un-computed) */
    private void initializeTable() {
        int[][] initialTable = (int[][]) variables.get(tableVariable);
        for (int row = 0; row <= x.length(); row++) {
            for (int col = 0; col <= y.length(); col++) {
                initialTable[row][col] = -1; // Indicate not yet computed
            }
        }
    }

    /** Resets the bTable that controls highlighting back to initial values */
    private void initializeBacktrackingTable() {
        int n = (int) variables.get("n");
        int m = (int) variables.get("m");
        int[][] bTable = (int[][]) variables.get(backtrackingTableVariable);
        // bTable has one more row and column than the strings' length
        for (int row = 0; row < n + 1; row++) {
            for (int col = 0; col < m + 1; col++) {
                bTable[row][col] = -1;
            }
        }
        // Since we're resetting the backtracking table, we also reset the lcs
        currentLcs = "";
    }

    /**
     * {@inheritDoc} These are the states from which it is okay to click the backtrack button
     *
     * @return
     */
    @Override
    public boolean backtrackReady() {
        switch (executionState) {
            case POST:
            case B_PRE:
            case B_WHILE:
            case B_IF:
            case B_ELIF:
            case B_ELSE:
            case B_RETRN:
            case B_POST:
                return true;
            default:
                return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void backtrackingOn() {
        nextLineNumber = BACKTRACKING_START_NUM;
        executionState = EXECUTION_STATE.B_PRE;
        initializeBacktrackingTable();
        // remove backtracking numbers from execution history so we can start fresh
        Iterator<Integer> iterator = executionHistory.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if (num >= BACKTRACKING_START_NUM) {
                iterator.remove();
            }
        }
        notifyProblemListeners();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean undoingBacktrackButton() {
        boolean satisfied = nextLineNumber == BACKTRACKING_START_NUM;
        if (satisfied) {
            executionState = EXECUTION_STATE.POST;
            nextLineNumber = 12;
        }
        return satisfied;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean canStepBack() {
        if (executionState == EXECUTION_STATE.PRE) {
            return false;
        } else {
            return true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasFinished() {
        switch (executionState) {
            case POST:
            case B_POST:
                return true;
            default:
                return false;
        }
    }

    /**
     * Resets this problem (algorithm) back to its initial state before execution of the first
     * statement. Includes re-initializing the DP table.
     */
    @Override
    public void reset() {
        nextLineNumber = 0;
        variables.put("r", -1);
        variables.put("c", -1);

        initializeTable();
        initializeBacktrackingTable();

        // Initialize boundary conditions according to algorithm logic (Lines 2 & 4 do this)
        // We don't need to explicitly set subproblem[r][0]=0 or subproblem[0][c]=0 here,
        // as the step execution will handle that starting from the PRE state.
        // However, if direct access requires base cases pre-filled, do it here:
        // for (int p = 0; p <= n; p++) subproblemL[p][0] = 0;
        // for (int q = 0; q <= m; q++) subproblemL[0][q] = 0;

        executionState = EXECUTION_STATE.PRE;
        executionHistory.clear();

        notifyProblemListeners();
    }

    /** Loads the pseudo-code statements for display. */
    @Override
    protected void loadCodeStatements() {
        codeStatements.clear();
        codeStatements.add("<html><pre><b>LCS(x,y)</b></pre></html>"); // Line 0
        codeStatements.add(
                "<html><pre>  <b>for</b> row = -1 to n-1 <b>do</b></pre></html>"); // Line 1
        codeStatements.add("<html><pre>    L[row,-1] = 0</pre></html>"); // Line 2
        codeStatements.add(
                "<html><pre>  <b>for</b> col = 0 to m-1 <b>do</b></pre></html>"); // Line 3
        codeStatements.add("<html><pre>    L[-1,col] = 0</pre></html>"); // Line 4
        codeStatements.add(
                "<html><pre>  <b>for</b> row = 0 to n-1 <b>do</b></pre></html>"); // Line 5
        codeStatements.add(
                "<html><pre>    <b>for</b> col = 0 to m-1 <b>do</b></pre></html>"); // Line 6
        codeStatements.add(
                "<html><pre>      <b>if</b> x[row] == y[col] <b>then</b></pre></html>"); // Line 7
        codeStatements.add(
                "<html><pre>        L[row, col] = L[row-1, col-1] + 1</pre></html>"); // Line 8
        codeStatements.add("<html><pre>      <b>else</b></pre></html>"); // Line 9
        codeStatements.add(
                "<html><pre>        L[row, col] = max(L[row-1, col], L[row, col-1])</pre></html>"); // Line 10
        codeStatements.add("<html><pre>  <b>return</b> L</pre></html>"); // Line 11
    }

    @Override
    protected void loadBacktrackingCodeStatements() {
        backtrackingCodeStatements.clear();
        backtrackingCodeStatements.add(
                "<html><pre><b>BACKTRACK(table L)</b></pre></html>"); // line 0
        backtrackingCodeStatements.add(
                "<html><pre>row = n - 1, col = m - 1</pre></html>"); // line 1
        backtrackingCodeStatements.add(
                "<html><pre>while(row >= 0 && col >= 0)</pre></html>"); // line 2
        backtrackingCodeStatements.add("<html><pre>  if (x[row] == y[col])</pre></html>"); // line 3
        backtrackingCodeStatements.add(
                "<html><pre>    add y[col] to front of LCS</pre></html>"); // line 4
        backtrackingCodeStatements.add("<html><pre>    row--</pre></html>"); // line 5
        backtrackingCodeStatements.add("<html><pre>    col--</pre></html>"); // line 6
        backtrackingCodeStatements.add(
                "<html><pre>  else if (L[row - 1, col] >= L[row, col - 1])</pre></html>"); // line 7
        backtrackingCodeStatements.add("<html><pre>    row--</pre></html>"); // line 8
        backtrackingCodeStatements.add("<html><pre>  else</pre></html>"); // line 9
        backtrackingCodeStatements.add("<html><pre>    col--</pre></html>"); // line 10
        backtrackingCodeStatements.add("<html><pre>return LCS</pre></html>"); // line 11
    }

    /**
     * Logs the current state (of the algorithm variables). Intended for debugging and diagnostics.
     */
    public void prettyPrint() {
        log.info("--- LCSProblem State ---");
        log.info("ExecutionState: {}", executionState);
        log.info("Current Line #: {}", nextLineNumber);
        log.info("r (array idx): {}", variables.get("r"));
        log.info("c (array idx): {}", variables.get("c"));

        int n = (int) variables.get("n");
        int m = (int) variables.get("m");
        int[][] subproblemL = (int[][]) variables.get("l");

        log.info("DP Table (l):");

        // Header row
        StringBuilder header = new StringBuilder("       ");
        for (int q = 0; q <= m; q++) {
            header.append(String.format("%4d ", q - 1));
        }
        log.info(header.toString());

        // Table rows
        for (int p = 0; p <= n; p++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%4d | ", p - 1));

            for (int q = 0; q <= m; q++) {
                int val = subproblemL[p][q];
                row.append(String.format("%4s ", val == -1 ? "." : val));
            }

            row.append("|");
            log.info(row.toString());
        }

        log.info("------------------------");
    }

    // -----------------------LCS Algorithm-------------------------------------
    // -------------------------------------------------------------------------

    /** LCS(x,y) - Line 0 (Implicit start) */
    public void executeLine0() {
        nextLineNumber = 1;
        // We always move to the next execution state at the end of the previous method
        executionState = EXECUTION_STATE.R_LOOP;
    }

    /** for r = 0 to n-1 do (DP indices) -> for r = 1 to n (Array indices) Line 1 */
    public void executeLine1() {
        int r = (int) variables.get("r");

        if (r == -1) { // First entry into this loop
            r = 0; // Start array index r at 0 (corresponds to DP index -1)
            variables.put("r", r);
            nextLineNumber = 2; // Go to loop body
        } else { // Subsequent iterations
            r++; // Increment loop variable r (array index)
            variables.put("r", r);
            int n = (int) variables.get("n");

            if (r == n + 1) { // Check loop boundary (array index goes up to n)
                // Finished r loop, move to next section
                nextLineNumber = 3; // Move to start of c loop
                executionState = EXECUTION_STATE.C_LOOP;
                variables.put("r", -1); // Reset r
            } else {
                // Continue r loop
                nextLineNumber = 2; // Go back to loop body
            }
        }
    }

    /**
     * L[r,-1] = 0 (DP Table indices) Maps to subproblem[r][0] (Array indices, where array 'r'
     * corresponds to DP 'r') Line 2
     */
    public void executeLine2() {
        int r = (int) variables.get("r");
        int[][] subproblem = (int[][]) variables.get(tableVariable);
        // Safe bounds check for array access
        if (subproblem != null && r >= 0 && r < subproblem.length) {
            subproblem[r][0] = 0; // Assign new value
        } else {
            log.error("ERROR: LCSProblem executeLine2 accessing out of bounds: r=" + r);
        }
        nextLineNumber = 1; // Go back to check r loop condition
    }

    /** for c = 0 to m-1 do (DP indices) -> for c = 1 to m (Array indices) Line 3 */
    public void executeLine3() {
        int c = (int) variables.get("c");

        if (c == -1) { // First entry into c loop
            c = 1; // Start array index c at 1 (corresponds to DP index 0)
            variables.put("c", c);
            // executionState should already be C_LOOP
            nextLineNumber = 4; // Go to loop body
        } else { // Subsequent iterations
            c++; // Increment loop variable c (array index)
            variables.put("c", c);
            int m = (int) variables.get("m");

            if (c == m + 1) { // Check loop boundary (array index goes up to m)
                // Finished c loop
                nextLineNumber = 5; // Move to start of i loop
                executionState = EXECUTION_STATE.I_LOOP;
                variables.put("c", -1); // Reset c for clarity
            } else {
                // Continue c loop
                nextLineNumber = 4; // Go back to loop body
            }
        }
    }

    /**
     * L[-1,c] = 0 (DP Table indices) Maps to subproblem[0][c] (Array indices, where array 'c'
     * corresponds to DP 'c') Line 4
     */
    public void executeLine4() {
        int c = (int) variables.get("c");
        int[][] subproblem = (int[][]) variables.get(tableVariable);
        // Safe bounds check
        if (subproblem != null && subproblem.length > 0 && c >= 0 && c < subproblem[0].length) {
            subproblem[0][c] = 0; // Assign new value
        } else {
            log.error("ERROR: LCSProblem executeLine4 accessing out of bounds: c=" + c);
        }
        nextLineNumber = 3; // Go back to check c loop condition
    }

    /** for row = 0 to n-1 do (DP indices) -> for row = 1 to n (Array indices) Line 5 */
    public void executeLine5() {
        int r = (int) variables.get("r");

        if (r == -1) { // First entry
            r = 1; // Start array index row at 1 (DP index 0)
            variables.put("r", r);
            executionState = EXECUTION_STATE.J_LOOP;
            nextLineNumber = 6; // Enter col loop (j loop)
        } else { // Subsequent iterations
            r++; // Increment row (array index)
            variables.put("r", r);
            int n = (int) variables.get("n");

            if (r == n + 1) { // Check boundary (array index up to n)
                // Finished row loop
                nextLineNumber = 11; // Go to return statement
                executionState = EXECUTION_STATE.RETRN;
                variables.put("r", -1); // Reset row
            } else {
                // Continue row loop, reset col loop for the new row
                nextLineNumber = 6; // Re-enter col loop
                executionState = EXECUTION_STATE.J_LOOP;
            }
        }
    }

    /** for col = 0 to m-1 do (DP indices) -> for col = 1 to m (Array indices) Line 6 */
    public void executeLine6() {
        int c = (int) variables.get("c");

        if (c == -1) { // First entry for current row
            c = 1; // Start array index col at 1 (DP index 0)
            variables.put("c", c);
            nextLineNumber = 7; // Go to 'if' statement
        } else { // Subsequent iterations for current row
            c++; // Increment col (array index)
            variables.put("c", c);
            int m = (int) variables.get("m");

            if (c == m + 1) { // Check boundary (array index up to m)
                // Finished col loop for current row
                nextLineNumber = 5; // Go back to outer row loop
                executionState = EXECUTION_STATE.I_LOOP; // Back to outer loop state
                variables.put("c", -1); // Reset col
            } else {
                // Continue col loop
                nextLineNumber = 7; // Go back to 'if' statement
            }
        }
    }

    /**
     * Line 7: if x[row] == y[col] (DP indices) -> if x[row-1] == y[col-1] (String/Array indices)
     */
    public void executeLine7() {
        int r = (int) variables.get("r");
        int c = (int) variables.get("c");
        String xStr = (String) variables.get("x");
        String yStr = (String) variables.get("y");

        // Adjust indices for 0-based String access
        if (r > 0 && c > 0 && r <= xStr.length() && c <= yStr.length()) {
            if (xStr.charAt(r - 1) == yStr.charAt(c - 1)) {
                nextLineNumber = 8; // Match case
            } else {
                nextLineNumber = 9; // No match case (line 9 is 'else')
            }
        } else {
            log.error(
                    "ERROR: LCSProblem executeLine7 accessing String out of bounds: r="
                            + r
                            + ", c="
                            + c);
            nextLineNumber = 6; // Tentatively go back to c loop check
        }
    }

    /**
     * Line 8: L[row, col] = L[row-1, col-1] + 1 (DP indices) Maps to subproblem[r][c] =
     * subproblem[r-1][c-1] + 1 (Array indices)
     */
    public void executeLine8() {
        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int r = (int) variables.get("r");
        int c = (int) variables.get("c");
        // Bounds check for array access
        if (subproblemL != null
                && r > 0
                && c > 0
                && r < subproblemL.length
                && c < subproblemL[r].length) {
            int newValue = subproblemL[r - 1][c - 1] + 1;
            subproblemL[r][c] = newValue;
        } else {
            log.error(
                    "ERROR: LCSProblem executeLine8 accessing out of bounds: r=" + r + ", c=" + c);
        }
        nextLineNumber = 6; // Go back to check c loop condition
    }

    /** Line 9: else (No operation, just determines control flow) */
    public void executeLine9() {
        nextLineNumber = 10;
    }

    /**
     * Line 10: L[row, col] = max(L[row-1, col], L[row, col-1]) (DP indices) Maps to
     * subproblem[r][c] = max(subproblem[r-1][c], subproblem[r][c-1]) (Array indices)
     */
    public void executeLine10() {
        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int r = (int) variables.get("r");
        int c = (int) variables.get("c");
        // Bounds check for array access
        if (subproblemL != null
                && r > 0
                && c > 0
                && r < subproblemL.length
                && c < subproblemL[r].length
                && (r - 1) < subproblemL.length
                && c < subproblemL[r - 1].length
                && r < subproblemL.length
                && (c - 1) < subproblemL[r].length) {
            int valAbove = subproblemL[r - 1][c];
            int valLeft = subproblemL[r][c - 1];
            int newValue = Integer.max(valAbove, valLeft);
            subproblemL[r][c] = newValue;
        } else {
            log.error(
                    "ERROR: LCSProblem executeLine10 accessing out of bounds: r=" + r + ", c=" + c);
        }
        nextLineNumber = 6; // Go back to check c loop condition
    }

    /** Line 11: return L */
    public void executeLine11() {
        if (executionState == EXECUTION_STATE.RETRN) {
            // The step button will become disabled when we reach state POST
            executionState = EXECUTION_STATE.POST; // Mark as finished
            /* There is no line 12. This ensures the table will lose highlight
            when the algorithm finishes */
            nextLineNumber = 12;
        } else {
            log.error("ERROR: Reached line 11 unexpectedly. State: " + executionState);
        }
    }

    /**
     * This is basically a NOOP If we try to step past the end of the algorithm, this is the method
     * that will execute. This can only happen if the user clicks the "run steps" button several
     * times, launching multiple threads which interfere with the normal method of checking if the
     * algorithm is finished.
     */
    public void executeLine12() {
        int lastLineIndex = executionHistory.size() - 1;
        // Remove any record of this line executing
        executionHistory.remove(lastLineIndex);
    }

    // ---------------------------LCS Algorithm Finished-------------------------

    // -----------------------Backtracking Algorithm Begins---------------------

    /**
     * Line: Backtrack(table) executionState has already been changed to B_PRE courtesy of the
     * backtrackingOn() method
     */
    public void executeLine100() {
        nextLineNumber = 101;
    }

    /** Line: row = x.length - 1, col = y.length - 1 */
    public void executeLine101() {
        // There is a "-1" row, so the rows go from 1 to x.length()
        variables.put("r", x.length());
        // There is a "-1" column, so the columns go from 1 to y.length()
        variables.put("c", y.length());
        nextLineNumber = 102;
        executionState = EXECUTION_STATE.B_WHILE;
    }

    /** Line: while (row >= 0 && col >= 0) */
    public void executeLine102() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        if (row >= 1 && col >= 1) {
            nextLineNumber = 103;
            executionState = EXECUTION_STATE.B_IF;

        } else {
            nextLineNumber = 111;
            executionState = EXECUTION_STATE.B_RETRN;
        }
    }

    /** Line: if (x[row] == y[col]) */
    public void executeLine103() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int[][] bTable = (int[][]) variables.get(backtrackingTableVariable);
        char charInX = x.charAt(row - 1);
        char charInY = y.charAt(col - 1);
        if (charInX == charInY) {
            // Change highlight in table to indicate status
            bTable[row][col] = HIT;
            nextLineNumber = 104;
        } else {
            // Change highlight in table to indicate status
            bTable[row][col] = MISS;
            nextLineNumber = 107;
            executionState = EXECUTION_STATE.B_ELIF;
        }
    }

    /** Line: add y[col] to front of LCS */
    public void executeLine104() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int[][] bTable = (int[][]) variables.get(backtrackingTableVariable);
        bTable[row][col] = ADD_TO_SOLUTION; // highlight in green
        currentLcs = y.charAt(col - 1) + currentLcs;
        nextLineNumber = 105;
    }

    /** Line: row-- */
    public void executeLine105() {
        int row = (int) variables.get("r");
        row--;
        variables.put("r", row);
        nextLineNumber = 106;
    }

    /** Line: col-- */
    public void executeLine106() {
        int col = (int) variables.get("c");
        col--;
        variables.put("c", col);
        nextLineNumber = 102;
        executionState = EXECUTION_STATE.B_WHILE;
    }

    /** Line: else if (table[row-1][col] >= table[row][col-1]) */
    public void executeLine107() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int lTable[][] = (int[][]) variables.get(tableVariable);

        if (lTable[row - 1][col] >= lTable[row][col - 1]) {
            nextLineNumber = 108;
        } else {
            nextLineNumber = 109;
            executionState = EXECUTION_STATE.B_ELSE;
        }
    }

    /** Line: row-- */
    public void executeLine108() {
        int row = (int) variables.get("r");
        row--;
        variables.put("r", row);
        nextLineNumber = 102;
        executionState = EXECUTION_STATE.B_WHILE;
    }

    /** Line: else */
    public void executeLine109() {
        nextLineNumber = 110;
    }

    /** Line: col-- */
    public void executeLine110() {
        int col = (int) variables.get("c");
        col--;
        variables.put("c", col);
        nextLineNumber = 102;
        executionState = EXECUTION_STATE.B_WHILE;
    }

    /** Line: Return LCS */
    public void executeLine111() {
        /* Line 112 is left as a NOOP so that the table will lose highlight
        when finished */
        nextLineNumber = 112;
        // Ths step button will become disabled when we enter B_POST
        executionState = EXECUTION_STATE.B_POST;
    }

    /** This is basically a NOOP. We will remove it from executionHistory */
    public void executeLine112() {
        int lastLineIndex = executionHistory.size() - 1;
        executionHistory.remove(lastLineIndex);
    }

    // ---------------------Backtracking Finished--------------------------------

    // -----------------------------Undo----------------------------------------

    /* method undo() in Problem.java handles resetting nextLineNumber and
    removing the last item from executionHistory
    */
    public void undoLine0() {
        executionState = EXECUTION_STATE.PRE;
        // reset();
    }

    /** Undoes executeLine1: for r = -1 to n-1 do */
    public void undoLine1() {
        int r = (int) variables.get("r");
        if (r == 0) { // First iteration returns to initial flag value
            r = -1;
        } else if (r == -1) { // Last iteration goes back to the middle
            executionState = EXECUTION_STATE.R_LOOP;
            int n = (int) variables.get("n");
            r = n + 1;
            r--;
        } else { // Middle iterations just decrement r
            r--;
        }
        variables.put("r", r);
    }

    /** Undoes executeLine2: L[r,-1] = 0 */
    public void undoLine2() {
        int r = (int) variables.get("r");
        int[][] lTable = (int[][]) variables.get(tableVariable);
        lTable[r][0] = -1; // -1 is the initial flag value
    }

    /** Undoes executeLine3: for c = 0 to m-1 do */
    public void undoLine3() {
        int c = (int) variables.get("c");
        if (c == 1) { // First iteration returns to initial flag value
            c = -1;
        } else if (c == -1) { // Last iteration goes back to the middle
            executionState = EXECUTION_STATE.C_LOOP;
            int m = (int) variables.get("m");
            c = m;
        } else { // Middle iterations just decrement c
            c--;
        }
        variables.put("c", c);
    }

    /** Undoes executeLine4: L[-1,c] = 0 */
    public void undoLine4() {
        int c = (int) variables.get("c");
        int[][] lTable = (int[][]) variables.get(tableVariable);
        lTable[0][c] = -1; // -1 is the initial flag value
    }

    /** Undoes executeLine5: for row = 0 to n-1 do */
    public void undoLine5() {
        int r = (int) variables.get("r");
        if (r == 1) { // First iteration goes back to initial flag value
            r = -1;
            executionState = EXECUTION_STATE.I_LOOP;
        }
        // Last iteration returns to the r loop (i loop)
        else if (executionState == EXECUTION_STATE.RETRN) {
            executionState = EXECUTION_STATE.I_LOOP;
            int n = (int) variables.get("n");
            r = n;
        } else { // Other iterations just decrement row
            r--;
            executionState = EXECUTION_STATE.I_LOOP;
        }
        variables.put("r", r);
    }

    /** Undoes executeLine6: for col = 0 to m-1 do */
    public void undoLine6() {
        int c = (int) variables.get("c");
        if (c == 1) { // First iteration goes back to initial flag value
            c = -1;
        }
        // Last iteration returns to the col (j) loop
        else if (executionState == EXECUTION_STATE.I_LOOP) {
            executionState = EXECUTION_STATE.J_LOOP;
            int m = (int) variables.get("m");
            c = m + 1;
            c--;
        } else { // Other iterations just decrement col
            c--;
        }
        variables.put("c", c);
    }

    /** Undoes executeLine7: if x[row] == y[col] */
    public void undoLine7() {
        // NOOP
    }

    /** Undoes executeLine8: L[row,col] = L[row-1, col-1] + 1 */
    public void undoLine8() {
        int r = (int) variables.get("r");
        int c = (int) variables.get("c");
        int[][] lTable = (int[][]) variables.get(tableVariable);
        lTable[r][c] = -1; // Restore to flag value
    }

    /** Undoes executeLine9: else */
    public void undoLine9() {
        // NOOP
    }

    /** Undoes executeLine10: L[row, col] = max(L[row-1, col], L[row, col-1]) */
    public void undoLine10() {
        int r = (int) variables.get("r");
        int c = (int) variables.get("c");
        int[][] lTable = (int[][]) variables.get(tableVariable);
        lTable[r][c] = -1; // Restore to flag value
    }

    /** Undoes executeLine11: return L */
    public void undoLine11() {
        executionState = EXECUTION_STATE.RETRN;
    }

    public void undoLine12() {
        // NOOP
    }

    // ----------------------------Undo Backtracking-----------------------------

    /** Undoes executeLine100: Backtrack(table) */
    public void undoLine100() {
        // NOOP
    }

    /** Undoes executeLine101: row = x.length-1, col = y.length-1 */
    public void undoLine101() {
        variables.put("r", -1);
        variables.put("c", -1);
        executionState = EXECUTION_STATE.B_PRE;
    }

    /** Undoes executeLine102: while (row >= 0 && col >= 0) */
    public void undoLine102() {
        executionState = EXECUTION_STATE.B_WHILE;
    }

    /** Undoes executeLine103: if (x[row] == y[col]) */
    public void undoLine103() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int[][] bTable = (int[][]) variables.get(backtrackingTableVariable);
        bTable[row][col] = UNVISITED;

        char charInX = x.charAt(row - 1);
        char charInY = y.charAt(col - 1);
        if (charInX != charInY) {
            executionState = EXECUTION_STATE.B_IF;
        }
    }

    /** Undoes executeLine104: add y[col] to front of LCS */
    public void undoLine104() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int[][] bTable = (int[][]) variables.get(backtrackingTableVariable);
        bTable[row][col] = HIT; // highlight in yellow
        currentLcs = currentLcs.substring(1);
    }

    /** Undoes executeLine105: row-- */
    public void undoLine105() {
        int row = (int) variables.get("r");
        row++;
        variables.put("r", row);
    }

    /** Undoes executeLine106: col-- */
    public void undoLine106() {
        int col = (int) variables.get("c");
        col++;
        variables.put("c", col);
        executionState = EXECUTION_STATE.B_IF;
    }

    /** Undoes executeLine107: else if (table[row-1][col] >= table[row][col-1]) */
    public void undoLine107() {
        int row = (int) variables.get("r");
        int col = (int) variables.get("c");
        int[][] lTable = (int[][]) variables.get(tableVariable);
        if (!(lTable[row - 1][col] >= lTable[row][col - 1])) {
            executionState = EXECUTION_STATE.B_ELIF;
        }
    }

    /** Undoes executeLine108: row-- */
    public void undoLine108() {
        int row = (int) variables.get("r");
        row++;
        variables.put("r", row);
        executionState = EXECUTION_STATE.B_ELIF;
    }

    /** Undoes executeLine109: else */
    public void undoLine109() {
        // NOOP
    }

    /** Undoes executeLine110: col-- */
    public void undoLine110() {
        int col = (int) variables.get("c");
        col++;
        variables.put("c", col);
        executionState = EXECUTION_STATE.B_ELSE;
    }

    /** Undoes executeLine111: Return LCS */
    public void undoLine111() {
        executionState = EXECUTION_STATE.B_RETRN;
    }

    public void undoLine112() {
        // NOOP
    }
}
