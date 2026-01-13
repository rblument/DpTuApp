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
    private Stack<int[]> mHistory = new Stack<>();

    // For storing split table changes
    private Stack<int[]> sHistory = new Stack<>();

    // Parenthesization result
    private String currentParens = "";

    // Backtracking highlight table
    private int[][] bTable;

    // Stack for backtracking
    private Stack<Object> btStack = new Stack<>();

    private static final String CLOSE_PAREN = ")";

    public MatrixChainProblem(int[][] sizes) {
        this(Model.DEFAULT_ID, sizes);
    }

    public MatrixChainProblem(int id, int[][] sizes) {
        super(id);

        int n = sizes.length;
        ArrayList<Integer> d = new ArrayList<>();

        d.add(sizes[0][0]);
        for (int[] size : sizes) d.add(size[1]);

        variables.put("n", n);
        variables.put("c", 1);
        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("d", d);
        variables.put("m", new int[n][n]);
        variables.put("s", new int[n][n]);
        variables.put("b", new int[n][n]);
        bTable = (int[][]) variables.get("b");

        int[][] m = (int[][]) variables.get("m");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                m[i][j] = -1;
                s[i][j] = -1;
                b[i][j] = UNVISITED;
            }

        tableVariable = "m";
        executionState = EXECUTION_STATE.PRE;

        loadCodeStatements();
    }

    @Override
    public ProblemKind getType() {
        return ProblemKind.MATRIX_CHAIN;
    }

    @Override
    public boolean backtrackReady() {
        return executionState == EXECUTION_STATE.POST
                || executionState != EXECUTION_STATE.BACKTRACK_DONE;
    }

    @Override
    public void backtrackingOn() {
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
        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;

        executionHistory.clear();
        mHistory.clear();
        sHistory.clear();
        btStack.clear();
        currentParens = "";

        int[][] m = (int[][]) variables.get("m");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");
        int n = (int) variables.get("n");

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                m[i][j] = -1;
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
        codeStatements.add("<html><pre>for i = 0 to n-1</pre></html>");
        codeStatements.add("<html><pre>    m[i][i] = 0</pre></html>");
        codeStatements.add("<html><pre>for c = 1 to n-1</pre></html>");
        codeStatements.add("<html><pre>    for i = 0 to n - c</pre></html>");
        codeStatements.add("<html><pre>        j = i + c</pre></html>");
        codeStatements.add("<html><pre>        m[i][j] = ∞</pre></html>");
        codeStatements.add("<html><pre>        for k = i to j-1</pre></html>");
        codeStatements.add(
                "<html><pre>            cost = m[i][k] + m[k+1][j] + d[i]d[k+1]d[j+1]</pre></html>");
        codeStatements.add("<html><pre>            if cost < m[i][j]: m[i][j] = cost</pre></html>");
        codeStatements.add("<html><pre>return m</pre></html>");
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
    }

    public void executeLine1() {
        int i = (int) variables.get("i");
        int n = (int) variables.get("n");
        int[][] m = (int[][]) variables.get("m");

        mHistory.push(new int[] {i, i, m[i][i]});
        m[i][i] = 0;

        if (i + 1 == n) {
            variables.put("c", 1);
            executionState = EXECUTION_STATE.C_LOOP;
            nextLineNumber = 2;
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
        } else {
            variables.put("i", 0);
            nextLineNumber = 3;
        }
    }

    public void executeLine3() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");

        if (i > n - c - 1) {
            variables.put("c", c + 1);
            nextLineNumber = 2;
        } else {
            nextLineNumber = 4;
        }
    }

    public void executeLine4() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");

        variables.put("j", i + c);
        nextLineNumber = 5;
    }

    public void executeLine5() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");

        mHistory.push(new int[] {i, j, m[i][j]});
        m[i][j] = Integer.MAX_VALUE;
        variables.put("k", i);

        nextLineNumber = 6;
    }

    public void executeLine6() {
        int k = (int) variables.get("k");
        int j = (int) variables.get("j");

        if (k >= j) {
            int i = (int) variables.get("i");
            variables.put("i", i + 1);
            nextLineNumber = 3;
        } else nextLineNumber = 7;
    }

    public void executeLine7() {
        int[][] m = (int[][]) variables.get("m");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int k = (int) variables.get("k");
        ArrayList<Integer> d = (ArrayList<Integer>) variables.get("d");

        int cost = m[i][k] + m[k + 1][j] + d.get(i) * d.get(k + 1) * d.get(j + 1);

        variables.put("cost", cost);
        nextLineNumber = 8;
    }

    public void executeLine8() {
        int cost = (int) variables.get("cost");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");

        if (cost < m[i][j]) {
            mHistory.push(new int[] {i, j, m[i][j]});
            m[i][j] = cost;

            int[][] s = (int[][]) variables.get("s");
            int kVal = (int) variables.get("k");

            sHistory.push(new int[] {i, j, s[i][j]});
            s[i][j] = kVal;
        }

        int k = (int) variables.get("k");
        variables.put("k", k + 1);
        nextLineNumber = 6;
    }

    public void executeLine9() {
        executionState = EXECUTION_STATE.POST;
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
    }

    public void executeLine101() {
        if (btStack.isEmpty()) {
            executionState = EXECUTION_STATE.BACKTRACK_DONE;
            nextLineNumber = 118;
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
            return;
        }

        int[] pair = (int[]) obj;
        variables.put("i", pair[0]);
        variables.put("j", pair[1]);

        executionState = EXECUTION_STATE.BACKTRACK_POP;
        nextLineNumber = 103;
    }

    public void executeLine103() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");

        if (i == j) {
            currentParens += "A" + i;
            bTable[i][j] = ADD_TO_SOLUTION;

            executionState = EXECUTION_STATE.BACKTRACK_LEAF;
            nextLineNumber = 101;
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
    }

    public void executeLine118() {
        executionState = EXECUTION_STATE.BACKTRACK_DONE;
    }

    // =========================================================================
    // — UNDO METHODS (unchanged except removal of bState references) —
    // =========================================================================

    public void undoLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;
    }

    public void undoLine1() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }

        int i = (int) variables.get("i") - 1;
        if (i >= 0) variables.put("i", i);
    }

    public void undoLine2() {
        int c = (int) variables.get("c") - 1;
        variables.put("c", c);
        variables.put("i", 0);
    }

    public void undoLine3() {
        int i = (int) variables.get("i") - 1;
        variables.put("i", i);
    }

    public void undoLine4() {
        variables.put("j", 0);
    }

    public void undoLine5() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }
    }

    public void undoLine6() {
        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    public void undoLine7() {
        variables.remove("cost");
    }

    public void undoLine8() {
        if (!mHistory.isEmpty()) {
            int[] change = mHistory.pop();
            int[][] m = (int[][]) variables.get("m");
            m[change[0]][change[1]] = change[2];
        }

        if (!sHistory.isEmpty()) {
            int[] prev = sHistory.pop();
            int[][] s = (int[][]) variables.get("s");
            s[prev[0]][prev[1]] = prev[2];
        }

        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    public void undoLine9() {
        variables.put("c", (int) variables.get("n"));
        executionState = EXECUTION_STATE.C_LOOP;
    }

    public void undoLine100() {
        btStack.clear();
        currentParens = "";
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
    }

    public void undoLine104() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        bTable[i][j] = UNVISITED;

        if (currentParens.endsWith("(")) {
            currentParens = currentParens.substring(0, currentParens.length() - 1);
        }
    }

    public void undoLine118() {
        executionState = EXECUTION_STATE.BACKTRACK_LOOP;
    }

    public EXECUTION_STATE getExecutionState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExecutionState'");
    }

    public void prettyPrint() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prettyPrint'");
    }
}
