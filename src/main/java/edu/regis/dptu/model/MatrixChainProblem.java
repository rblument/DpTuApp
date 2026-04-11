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

public class MatrixChainProblem extends Problem {
    private static final Logger log = LoggerFactory.getLogger(MatrixChainProblem.class);

    /**
     * Unified execution states, including both forward execution and backtracking. This matches the
     * design of LCSProblem.
     */
    public enum EXECUTION_STATE {
        PRE,
        R_LOOP,
        C_LOOP,
        I_LOOP,
        J_LOOP,
        POST,

        // Backtracking phases
        BACKTRACK_PRE,
        BACKTRACK_LOOP,
        BACKTRACK_POP,
        BACKTRACK_LEAF,
        BACKTRACK_SPLIT,
        BACKTRACK_CLOSE,
        BACKTRACK_DONE
    }

    // Current execution state
    private EXECUTION_STATE executionState;

    // Stack to record changes to m[i][j]
    private final Stack<int[]> mHistory = new Stack<>();

    // For storing split table changes
    private final Stack<int[]> sHistory = new Stack<>();

    // Parenthesization result
    private String currentParens = "";

    // Backtracking highlight table
    private int[][] bTable;

    // Stack for backtracking
    private final Stack<Object> btStack = new Stack<>();

    private static final String CLOSE_PAREN = ")";

    public MatrixChainProblem(int[][] sizes) {
        this(Model.DEFAULT_ID, sizes);
    }

    public MatrixChainProblem(int id, int[][] sizes) {
        super(id);

        int n = sizes.length; // Number of rows (i.e. number of matrices)
        int m = n; // Number of columns (equal to rows)
        ArrayList<Integer> d = new ArrayList<>();

        d.add(sizes[0][0]);
        for (int[] size : sizes) d.add(size[1]);

        variables.put("n", n);
        variables.put("m", m);
        variables.put("c", 1);
        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("d", d);
        /* used "l" for table variable to be consistent with LCSProblem
        and because this previously using "m" caused issues in
        SubproblemTableView since it expected n=rows m=columns l=table */
        variables.put("l", new int[n + 1][m + 1]);
        variables.put("s", new int[n + 1][m + 1]);
        variables.put("b", new int[n + 1][m + 1]);
        bTable = (int[][]) variables.get("b");

        int[][] l = (int[][]) variables.get("l");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");

        for (int i = 0; i <= n; i++)
            for (int j = 0; j <= m; j++) {
                l[i][j] = -1;
                s[i][j] = -1;
                b[i][j] = UNVISITED;
            }

        tableVariable = "l";
        executionState = EXECUTION_STATE.PRE;

        log.debug(
                "MatrixChainProblem created: id={}, n={}, m={}, d.size={}, sizes[0]={}x{}",
                id,
                n,
                m,
                d.size(),
                sizes[0][0],
                sizes[0][1]);

        loadCodeStatements();
        loadBacktrackingCodeStatements();
    }

    @Override
    public ProblemKind getType() {
        return ProblemKind.MATRIX_CHAIN;
    }

    @Override
    public boolean backtrackReady() {
        // (As written, this is effectively always true unless POST? Keeping behavior unchanged.)
        return executionState == EXECUTION_STATE.POST
                || executionState != EXECUTION_STATE.BACKTRACK_DONE;
    }

    @Override
    public void backtrackingOn() {
        log.debug(
                "Backtracking enabled: priorState={}, nextLineNumber={} -> {}",
                executionState,
                nextLineNumber,
                BACKTRACKING_START_NUM);

        nextLineNumber = BACKTRACKING_START_NUM;
        executionState = EXECUTION_STATE.BACKTRACK_PRE;

        // reset backtracking tables
        for (int[] row : bTable) java.util.Arrays.fill(row, UNVISITED);

        currentParens = "";
        btStack.clear();

        notifyProblemListeners();
    }

    @Override
    public boolean undoingBacktrackButton() {
        return false;
    }

    @Override
    public boolean canStepBack() {
        return !executionHistory.isEmpty();
    }

    @Override
    public boolean hasFinished() {
        return executionState == EXECUTION_STATE.POST
                || executionState == EXECUTION_STATE.BACKTRACK_DONE;
    }

    @Override
    public void reset() {
        log.debug(
                "Reset requested: state {} -> PRE; histories cleared (exec={}, mHist={}, sHist={}, btStack={})",
                executionState,
                executionHistory.size(),
                mHistory.size(),
                sHistory.size(),
                btStack.size());

        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;

        executionHistory.clear();
        mHistory.clear();
        sHistory.clear();
        btStack.clear();
        currentParens = "";

        int[][] l = (int[][]) variables.get("l");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");
        int n = (int) variables.get("n");

        for (int i = 0; i <= n; i++)
            for (int j = 0; j <= n; j++) {
                l[i][j] = -1;
                s[i][j] = -1;
                b[i][j] = UNVISITED;
            }

        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("c", 1);

        notifyProblemListeners();
    }

    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for i = 0 to n-1</pre></html>"); // Line 0
        codeStatements.add("<html><pre>    m[i][i] = 0</pre></html>"); // Line 1
        codeStatements.add("<html><pre>for c = 1 to n-1</pre></html>"); // Line 2
        codeStatements.add("<html><pre>    for i = 0 to n - c</pre></html>"); // Line 3
        codeStatements.add("<html><pre>        j = i + c</pre></html>"); // Line 4
        codeStatements.add("<html><pre>        m[i][j] = ∞</pre></html>"); // Line 5
        codeStatements.add("<html><pre>        for k = i to j-1</pre></html>"); // Line 6
        codeStatements.add(
                "<html><pre>            cost = m[i][k] + m[k+1][j] + d[i]d[k+1]d[j+1]</pre></html>"); // Line 7
        codeStatements.add(
                "<html><pre>            if cost < m[i][j]: m[i][j] = cost</pre></html>"); // Line 8
        codeStatements.add("<html><pre>return m</pre></html>"); // Line 9
    }

    @Override
    protected void loadBacktrackingCodeStatements() {
        backtrackingCodeStatements.clear();
        backtrackingCodeStatements.add("<html><pre>BACKTRACK(0, n-1)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>push(0, n-1)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>while stack not empty</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  (i,j) = pop()</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  if i == j: output Ai</pre></html>");
        backtrackingCodeStatements.add("<html><pre>  else: k = s[i][j]</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    output '('</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    BACKTRACK(i, k)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    BACKTRACK(k+1, j)</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    output ')'</pre></html>");
    }

    // =========================================================================
    // FORWARD EXECUTION LINES 0–9
    // =========================================================================

    public void executeLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.R_LOOP;
        nextLineNumber = 1;

        log.debug(
                "executeLine0: i=0, state -> {}, nextLineNumber={}",
                executionState,
                nextLineNumber);
    }

    public void executeLine1() {
        int i = (int) variables.get("i");
        int n = (int) variables.get("n");
        int[][] l = (int[][]) variables.get("l");

        mHistory.push(new int[] {i, i, l[i][i]});
        l[i][i] = 0;

        log.debug("executeLine1: set m[{}][{}]=0; n={}, i={}", i, i, n, i);

        if (i + 1 == n) {
            variables.put("c", 1);
            executionState = EXECUTION_STATE.C_LOOP;
            nextLineNumber = 2;

            log.debug(
                    "executeLine1: finished diagonal init; c=1, state -> {}, nextLineNumber={}",
                    executionState,
                    nextLineNumber);
        } else {
            variables.put("i", i + 1);
            nextLineNumber = 1;
        }
    }

    public void executeLine2() {
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");

        if (c == n) {
            executionState = EXECUTION_STATE.POST;
            nextLineNumber = 9;

            log.debug(
                    "executeLine2: done (c==n); state -> {}, nextLineNumber={}",
                    executionState,
                    nextLineNumber);
        } else {
            variables.put("i", 0);
            nextLineNumber = 3;

            log.debug(
                    "executeLine2: start chain length c={}; i=0, nextLineNumber={}",
                    c,
                    nextLineNumber);
        }
    }

    public void executeLine3() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");

        if (i > n - c - 1) {
            variables.put("c", c + 1);
            nextLineNumber = 2;

            log.debug("executeLine3: advance c -> {}; reset loop via line2", c + 1);
        } else {
            nextLineNumber = 4;
        }
    }

    public void executeLine4() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");

        variables.put("j", i + c);
        nextLineNumber = 5;

        log.debug("executeLine4: set j=i+c -> j={}, i={}, c={}", i + c, i, c);
    }

    public void executeLine5() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] l = (int[][]) variables.get("l");

        mHistory.push(new int[] {i, j, l[i][j]});
        l[i][j] = Integer.MAX_VALUE;
        variables.put("k", i);

        nextLineNumber = 6;

        log.debug(
                "executeLine5: init m[{}][{}]=INF; k=i -> {}; nextLineNumber={}",
                i,
                j,
                i,
                nextLineNumber);
    }

    public void executeLine6() {
        int k = (int) variables.get("k");
        int j = (int) variables.get("j");

        if (k >= j) {
            int i = (int) variables.get("i");
            variables.put("i", i + 1);
            nextLineNumber = 3;

            log.debug(
                    "executeLine6: k>=j (k={}, j={}); i -> {}; nextLineNumber={}",
                    k,
                    j,
                    i + 1,
                    nextLineNumber);
        } else {
            nextLineNumber = 7;
        }
    }

    @SuppressWarnings("unchecked")
    public void executeLine7() {
        int[][] l = (int[][]) variables.get("l");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int k = (int) variables.get("k");
        ArrayList<Integer> d = (ArrayList<Integer>) variables.get("d");

        int cost = l[i][k] + l[k + 1][j] + d.get(i) * d.get(k + 1) * d.get(j + 1);

        variables.put("cost", cost);
        nextLineNumber = 8;

        log.debug("executeLine7: cost computed for (i={}, j={}, k={}): {}", i, j, k, cost);
    }

    public void executeLine8() {
        int cost = (int) variables.get("cost");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] l = (int[][]) variables.get("l");

        if (cost < l[i][j]) {
            int prev = l[i][j];

            mHistory.push(new int[] {i, j, prev});
            l[i][j] = cost;

            int[][] s = (int[][]) variables.get("s");
            int kVal = (int) variables.get("k");

            sHistory.push(new int[] {i, j, s[i][j]});
            s[i][j] = kVal;

            log.debug(
                    "executeLine8: improved m[{}][{}]: {} -> {}; set s[{}][{}]={}",
                    i,
                    j,
                    prev,
                    cost,
                    i,
                    j,
                    kVal);
        }

        int k = (int) variables.get("k");
        variables.put("k", k + 1);
        nextLineNumber = 6;
    }

    public void executeLine9() {
        executionState = EXECUTION_STATE.POST;
        log.debug("executeLine9: state -> POST");
    }

    // =========================================================================
    // BACKTRACKING LINES 100–118
    // =========================================================================

    public void executeLine100() {
        currentParens = "";
        btStack.clear();

        int n = (int) variables.get("n");
        btStack.push(new int[] {0, n - 1});

        executionState = EXECUTION_STATE.BACKTRACK_PRE;
        nextLineNumber = 101;

        log.debug(
                "executeLine100: init backtrack stack with (0, {}); nextLineNumber={}",
                n - 1,
                nextLineNumber);
    }

    public void executeLine101() {
        if (btStack.isEmpty()) {
            executionState = EXECUTION_STATE.BACKTRACK_DONE;
            nextLineNumber = 118;

            log.debug(
                    "executeLine101: btStack empty; state -> {}, nextLineNumber={}",
                    executionState,
                    nextLineNumber);
        } else {
            executionState = EXECUTION_STATE.BACKTRACK_LOOP;
            nextLineNumber = 102;
        }
    }

    public void executeLine102() {
        Object obj = btStack.pop();

        if (obj instanceof String) {
            currentParens += ")";
            executionState = EXECUTION_STATE.BACKTRACK_CLOSE;
            nextLineNumber = 101;

            log.debug("executeLine102: pop CLOSE_PAREN; currentParens='{}'", currentParens);
            return;
        }

        int[] pair = (int[]) obj;
        variables.put("i", pair[0]);
        variables.put("j", pair[1]);

        executionState = EXECUTION_STATE.BACKTRACK_POP;
        nextLineNumber = 103;

        log.debug(
                "executeLine102: pop pair (i={}, j={}); nextLineNumber={}",
                pair[0],
                pair[1],
                nextLineNumber);
    }

    public void executeLine103() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        if (i == j) {
            currentParens += "A" + i;
            bTable[i][j] = ADD_TO_SOLUTION;

            executionState = EXECUTION_STATE.BACKTRACK_LEAF;
            nextLineNumber = 101;

            log.debug("executeLine103: leaf (i==j=={}); currentParens='{}'", i, currentParens);
            return;
        }

        nextLineNumber = 104;
    }

    public void executeLine104() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        int[][] s = (int[][]) variables.get("s");
        int k = s[i][j];

        variables.put("k", k);

        currentParens += "(";
        bTable[i][j] = HIT;

        btStack.push(CLOSE_PAREN);
        btStack.push(new int[] {k + 1, j});
        btStack.push(new int[] {i, k});

        executionState = EXECUTION_STATE.BACKTRACK_SPLIT;
        nextLineNumber = 101;

        log.debug(
                "executeLine104: split (i={}, j={}) at k={}; push (i,k)=({},{}), (k+1,j)=({},{}); currentParens='{}'",
                i,
                j,
                k,
                i,
                k,
                k + 1,
                j,
                currentParens);
    }

    public void executeLine118() {
        executionState = EXECUTION_STATE.BACKTRACK_DONE;
        log.debug("executeLine118: state -> BACKTRACK_DONE; currentParens='{}'", currentParens);
    }

    // =========================================================================
    // — UNDO METHODS —
    // =========================================================================

    public void undoLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;

        log.debug("undoLine0: state -> PRE; nextLineNumber=0");
    }

    public void undoLine1() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] l = (int[][]) variables.get("l");
            l[change[0]][change[1]] = change[2];

            log.debug("undoLine1: restore m[{}][{}] -> {}", change[0], change[1], change[2]);
        }

        int i = (int) variables.get("i") - 1;
        if (i >= 0) variables.put("i", i);
    }

    public void undoLine2() {
        int c = (int) variables.get("c") - 1;
        variables.put("c", c);
        variables.put("i", 0);

        log.debug("undoLine2: c -> {}; i -> 0", c);
    }

    public void undoLine3() {
        int i = (int) variables.get("i") - 1;
        variables.put("i", i);

        log.debug("undoLine3: i -> {}", i);
    }

    public void undoLine4() {
        variables.put("j", 0);
        log.debug("undoLine4: j -> 0");
    }

    public void undoLine5() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] l = (int[][]) variables.get("l");
            l[change[0]][change[1]] = change[2];

            log.debug("undoLine5: restore m[{}][{}] -> {}", change[0], change[1], change[2]);
        }
    }

    public void undoLine6() {
        int k = (int) variables.get("k") - 1;
        variables.put("k", k);

        log.debug("undoLine6: k -> {}", k);
    }

    public void undoLine7() {
        variables.remove("cost");
        log.debug("undoLine7: removed cost");
    }

    public void undoLine8() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] l = (int[][]) variables.get("l");
            l[change[0]][change[1]] = change[2];

            log.debug("undoLine8: restore m[{}][{}] -> {}", change[0], change[1], change[2]);
        }

        if (!sHistory.isEmpty()) {
            int[] prev = sHistory.pop();
            int[][] s = (int[][]) variables.get("s");
            s[prev[0]][prev[1]] = prev[2];

            log.debug("undoLine8: restore s[{}][{}] -> {}", prev[0], prev[1], prev[2]);
        }

        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    public void undoLine9() {
        variables.put("c", (int) variables.get("n"));
        executionState = EXECUTION_STATE.C_LOOP;

        log.debug("undoLine9: state -> C_LOOP; c -> n");
    }

    public void undoLine100() {
        btStack.clear();
        currentParens = "";

        log.debug("undoLine100: cleared btStack and currentParens");
    }

    public void undoLine101() {}

    public void undoLine102() {}

    public void undoLine103() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        bTable[i][j] = UNVISITED;

        if (currentParens.endsWith("A" + i)) {
            currentParens = currentParens.substring(0, currentParens.length() - ("A" + i).length());
        }

        log.debug("undoLine103: unvisit b[{}][{}]; currentParens='{}'", i, j, currentParens);
    }

    public void undoLine104() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        bTable[i][j] = UNVISITED;

        if (currentParens.endsWith("(")) {
            currentParens = currentParens.substring(0, currentParens.length() - 1);
        }

        log.debug("undoLine104: unvisit b[{}][{}]; currentParens='{}'", i, j, currentParens);
    }

    public void undoLine118() {
        executionState = EXECUTION_STATE.BACKTRACK_LOOP;
        log.debug("undoLine118: state -> BACKTRACK_LOOP");
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    public void prettyPrint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prettyPrint'");
    }
}
