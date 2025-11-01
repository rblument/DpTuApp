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

import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(LCSProblem.class.getName());

    /**
     * Current state of execution capturing which of the loops are current. Note if the
     * corresponding iteration index for a loop is -1, the loop hasn't entered its first iteration.
     */
    public enum EXECUTION_STATE {
        PRE,
        R_LOOP,
        C_LOOP,
        I_LOOP,
        J_LOOP,
        RETRN,
        POST
    };

    /** Input sequence 1. (Stored in variables map) */
    private final String x; // Keep original field if needed, though data is in map

    /** Input sequence 2. (Stored in variables map) */
    private final String y; // Keep original field if needed

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
        variables.put("i", -1);
        variables.put("j", -1);
        variables.put("l", new int[n + 1][m + 1]); // Create the DP table

        tableVariable = "l"; // Set the name of the table variable

        // Initialize the DP table with -1 (or another indicator of uncomputed)
        int[][] initialTable = (int[][]) variables.get(tableVariable);
        for (int row = 0; row <= n; row++) {
            for (int col = 0; col <= m; col++) {
                initialTable[row][col] = -1; // Indicate not yet computed
            }
        }

        executionState = EXECUTION_STATE.PRE;

        loadCodeStatements(); // Load the pseudocode lines

        reset(); // Call reset to ensure consistent initial state including table values
    }

    /**
     * Return the type of this Dynamic Programming problem.
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

    /** {@inheritDoc} */
    @Override
    public boolean hasFinished() {
        return executionState == EXECUTION_STATE.POST;
    }

    /** LCS(x,y) - Line 0 (Implicit start) */
    public void executeLine0() {
        currentLineNumber = 1; // Move to first actual line of code
    }

    /** for r = 0 to n-1 do (DP indices) -> for r = 1 to n (Array indices) Line 1 */
    public void executeLine1() {
        int r = (int) variables.get("r");

        if (r == -1) { // First entry into this loop
            r = 0; // Start array index r at 0 (corresponds to DP index -1)
            variables.put("r", r);
            executionState = EXECUTION_STATE.R_LOOP;
            currentLineNumber = 2; // Go to loop body
        } else { // Subsequent iterations
            r++; // Increment loop variable r (array index)
            variables.put("r", r);
            int n = (int) variables.get("n");

            if (r == n + 1) { // Check loop boundary (array index goes up to n)
                // Finished r loop, move to next section
                currentLineNumber = 3; // Move to start of c loop
                executionState = EXECUTION_STATE.C_LOOP;
                variables.put("r", -1); // Reset r
            } else {
                // Continue r loop
                currentLineNumber = 2; // Go back to loop body
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
            System.err.println("ERROR: LCSProblem executeLine2 accessing out of bounds: r=" + r);
        }
        currentLineNumber = 1; // Go back to check r loop condition
    }

    /** for c = 0 to m-1 do (DP indices) -> for c = 1 to m (Array indices) Line 3 */
    public void executeLine3() {
        int c = (int) variables.get("c");

        if (c == -1) { // First entry into c loop
            c = 1; // Start array index c at 1 (corresponds to DP index 0)
            variables.put("c", c);
            // executionState should already be C_LOOP
            currentLineNumber = 4; // Go to loop body
        } else { // Subsequent iterations
            c++; // Increment loop variable c (array index)
            variables.put("c", c);
            int m = (int) variables.get("m");

            if (c == m + 1) { // Check loop boundary (array index goes up to m)
                // Finished c loop
                currentLineNumber = 5; // Move to start of i loop
                executionState = EXECUTION_STATE.I_LOOP;
                variables.put("c", -1); // Reset c for clarity
            } else {
                // Continue c loop
                currentLineNumber = 4; // Go back to loop body
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
            System.err.println("ERROR: LCSProblem executeLine4 accessing out of bounds: c=" + c);
        }
        currentLineNumber = 3; // Go back to check c loop condition
    }

    /** for i = 0 to n-1 do (DP indices) -> for i = 1 to n (Array indices) Line 5 */
    public void executeLine5() {
        int i = (int) variables.get("i");

        if (i == -1) { // First entry
            i = 1; // Start array index i at 1 (DP index 0)
            variables.put("i", i);
            variables.put("j", -1); // Reset j loop for this i
            executionState = EXECUTION_STATE.I_LOOP; // Already should be this state
            currentLineNumber = 6; // Enter j loop
        } else { // Subsequent iterations
            i++; // Increment i (array index)
            variables.put("i", i);
            int n = (int) variables.get("n");

            if (i == n + 1) { // Check boundary (array index up to n)
                // Finished i loop
                currentLineNumber = 11; // Go to return statement
                executionState = EXECUTION_STATE.RETRN;
                variables.put("i", -1); // Reset i
            } else {
                // Continue i loop, reset j loop for the new i
                variables.put("j", -1);
                currentLineNumber = 6; // Re-enter j loop
            }
        }
    }

    /** for j = 0 to m-1 do (DP indices) -> for j = 1 to m (Array indices) Line 6 */
    public void executeLine6() {
        int j = (int) variables.get("j");

        if (j == -1) { // First entry for current i
            j = 1; // Start array index j at 1 (DP index 0)
            variables.put("j", j);
            executionState = EXECUTION_STATE.J_LOOP; // Set state for inner loop
            currentLineNumber = 7; // Go to 'if' statement
        } else { // Subsequent iterations for current i
            j++; // Increment j (array index)
            variables.put("j", j);
            int m = (int) variables.get("m");

            if (j == m + 1) { // Check boundary (array index up to m)
                // Finished j loop for current i
                currentLineNumber = 5; // Go back to outer i loop
                executionState = EXECUTION_STATE.I_LOOP; // Back to outer loop state
                variables.put("j", -1); // Reset j
            } else {
                // Continue j loop
                currentLineNumber = 7; // Go back to 'if' statement
            }
        }
    }

    /** Line 7: if x[i] == y[j] (DP indices) -> if x[i-1] == y[j-1] (String/Array indices) */
    public void executeLine7() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        String xStr = (String) variables.get("x");
        String yStr = (String) variables.get("y");

        // Adjust indices for 0-based String access
        if (i > 0 && j > 0 && i <= xStr.length() && j <= yStr.length()) {
            if (xStr.charAt(i - 1) == yStr.charAt(j - 1)) {
                currentLineNumber = 8; // Match case
            } else {
                currentLineNumber = 10; // No match case (line 9 is 'else')
            }
        } else {
            System.err.println(
                    "ERROR: LCSProblem executeLine7 accessing String out of bounds: i="
                            + i
                            + ", j="
                            + j);
            currentLineNumber = 6; // Tentatively go back to j loop check
        }
    }

    /**
     * Line 8: L[i, j] = L[i-1, j-1] + 1 (DP indices) Maps to subproblem[i][j] =
     * subproblem[i-1][j-1] + 1 (Array indices)
     */
    public void executeLine8() {
        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        // Bounds check for array access
        if (subproblemL != null
                && i > 0
                && j > 0
                && i < subproblemL.length
                && j < subproblemL[i].length
                && (i - 1) < subproblemL.length
                && (j - 1) < subproblemL[i - 1].length) {
            int newValue = subproblemL[i - 1][j - 1] + 1;
            subproblemL[i][j] = newValue;
        } else {
            System.err.println(
                    "ERROR: LCSProblem executeLine8 accessing out of bounds: i=" + i + ", j=" + j);
        }
        currentLineNumber = 6; // Go back to check j loop condition
    }

    /** Line 9: else (No operation, just determines control flow) */
    // No executeLine9 needed

    /**
     * Line 10: L[i, j] = max(L[i-1, j], L[i, j-1]) (DP indices) Maps to subproblem[i][j] =
     * max(subproblem[i-1][j], subproblem[i][j-1]) (Array indices)
     */
    public void executeLine10() {
        int[][] subproblemL = (int[][]) variables.get(tableVariable);
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        // Bounds check for array access
        if (subproblemL != null
                && i > 0
                && j > 0
                && i < subproblemL.length
                && j < subproblemL[i].length
                && (i - 1) < subproblemL.length
                && j < subproblemL[i - 1].length
                && i < subproblemL.length
                && (j - 1) < subproblemL[i].length) {
            int valAbove = subproblemL[i - 1][j];
            int valLeft = subproblemL[i][j - 1];
            int newValue = Integer.max(valAbove, valLeft);
            subproblemL[i][j] = newValue;
        } else {
            System.err.println(
                    "ERROR: LCSProblem executeLine10 accessing out of bounds: i=" + i + ", j=" + j);
        }
        currentLineNumber = 6; // Go back to check j loop condition
    }

    /** Line 11: return L */
    public void executeLine11() {
        if (executionState == EXECUTION_STATE.RETRN) {
            executionState = EXECUTION_STATE.POST; // Mark as finished
        } else {
            System.err.println("ERROR: Reached line 11 unexpectedly. State: " + executionState);
        }
    }

    // --- Undo Methods (Simplified Stubs - Requires Proper Implementation) ---
    public void undoLine0() {
        reset();
    }

    public void undoLine1() {
        /* Restore 'r', state */
    }

    public void undoLine2() {
        /* Restore subproblem[r][0], line=1 */
    }

    public void undoLine3() {
        /* Restore 'c', state */
    }

    public void undoLine4() {
        /* Restore subproblem[0][c], line=3 */
    }

    public void undoLine5() {
        /* Restore 'i', state */
    }

    public void undoLine6() {
        /* Restore 'j', state */
    }

    public void undoLine7() {
        currentLineNumber = 6;
    }

    public void undoLine8() {
        /* Restore subproblem[i][j], line=7 */
    }

    public void undoLine10() {
        /* Restore subproblem[i][j], line=7 */
    }

    public void undoLine11() {
        /* Restore state, line=5 or 6 */
    }

    /** Loads the pseudo-code statements for display. */
    @Override
    protected void loadCodeStatements() {
        codeStatements.clear();
        codeStatements.add("<html><pre><b>LCS(x,y)</b></pre></html>"); // Line 0
        codeStatements.add(
                "<html><pre>  <b>for</b> r = -1 to n-1 <b>do</b></pre></html>"); // Line 1
        codeStatements.add("<html><pre>    L[r,-1] = 0</pre></html>"); // Line 2
        codeStatements.add("<html><pre>  <b>for</b> c = 0 to m-1 <b>do</b></pre></html>"); // Line 3
        codeStatements.add("<html><pre>    L[-1,c] = 0</pre></html>"); // Line 4
        codeStatements.add("<html><pre>  <b>for</b> i = 0 to n-1 <b>do</b></pre></html>"); // Line 5
        codeStatements.add(
                "<html><pre>    <b>for</b> j = 0 to m-1 <b>do</b></pre></html>"); // Line 6
        codeStatements.add(
                "<html><pre>      <b>if</b> x<sub>i</sub> == y<sub>j</sub> <b>then</b></pre></html>"); // Line 7
        codeStatements.add("<html><pre>        L[i, j] = L[i-1, j-1] + 1</pre></html>"); // Line 8
        codeStatements.add("<html><pre>      <b>else</b></pre></html>"); // Line 9
        codeStatements.add(
                "<html><pre>        L[i, j] = max(L[i-1, j], L[i, j-1])</pre></html>"); // Line 10
        codeStatements.add("<html><pre>  <b>return</b> L</pre></html>"); // Line 11
    }

    /**
     * Resets this problem (algorithm) back to its initial state before execution of the first
     * statement. Includes re-initializing the DP table.
     */
    @Override
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
                subproblemL[p][q] = -1; // Reset to uncomputed state
            }
        }
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

    /**
     * Outputs to System.out the current state (of the algorithm variables). (Kept for potential
     * manual debugging)
     */
    public void prettyPrint() {
        // Original prettyPrint code retained
        LCSProblem.LOGGER.log(Level.INFO, "--- LCSProblem State ---");
        LCSProblem.LOGGER.log(Level.INFO, "ExecutionState: " + executionState);
        LCSProblem.LOGGER.log(Level.INFO, "Current Line #: " + currentLineNumber);
        LCSProblem.LOGGER.log(Level.INFO, "r (array idx): " + variables.get("r"));
        LCSProblem.LOGGER.log(Level.INFO, "c (array idx): " + variables.get("c"));
        LCSProblem.LOGGER.log(Level.INFO, "i (array idx): " + variables.get("i"));
        LCSProblem.LOGGER.log(Level.INFO, "j (array idx): " + variables.get("j"));

        int n = (int) variables.get("n");
        int m = (int) variables.get("m");
        int[][] subproblemL = (int[][]) variables.get("l");

        LCSProblem.LOGGER.log(Level.INFO, "DP Table (l):");
        System.out.print("       "); // Align header
        for (int q = 0; q <= m; q++) {
            System.out.printf("%4d ", q - 1); // Print DP Col Index (-1 to m-1)
        }
        LCSProblem.LOGGER.log(Level.INFO, "");

        for (int p = 0; p <= n; p++) {
            System.out.printf("%4d | ", p - 1); // Print DP Row Index (-1 to n-1)
            for (int q = 0; q <= m; q++) {
                int val = subproblemL[p][q];
                System.out.printf(
                        "%4s ", (val == -1 ? "." : String.valueOf(val))); // Use '.' for uncomputed
            }
            LCSProblem.LOGGER.log(Level.INFO, "|");
        }
        LCSProblem.LOGGER.log(Level.INFO, "------------------------");
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
