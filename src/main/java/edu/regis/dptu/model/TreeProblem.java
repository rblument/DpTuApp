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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeProblem extends Problem {
    private static final Logger log = LoggerFactory.getLogger(TreeProblem.class);

    /**
     * TODO: continue implementation Unified execution states, including both forward execution and
     * backtracking. This matches the design of LCSProblem.
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

    // Parenthesization result
    private String currentParens = "";

    // Backtracking highlight table
    private int[][] bTable;

    // Stack for backtracking
    private final Stack<Object> btStack = new Stack<>();

    private static final String CLOSE_PAREN = ")";

    // Scalar variables that a step may change
    private static final String[] SCALAR_VARIABLES = {"i", "j", "k", "c", "cost"};

    /** Everything a single step can change, saved so undo() can restore it exactly. */
    private record Snapshot(
            Map<String, Object> scalars,
            EXECUTION_STATE state,
            String parens,
            List<Object> stack) {}

    // One snapshot per entry in executionHistory
    private final Stack<Snapshot> snapshots = new Stack<>();

    public TreeProblem(int[][] sizes) {
        this(Model.DEFAULT_ID, sizes);
    }

    public TreeProblem(int id, int[][] sizes) {
        super(id);

        loadCodeStatements();
        loadBacktrackingCodeStatements();
    }

    @Override
    public ProblemKind getType() {
        return ProblemKind.MATRIX_CHAIN;
    }

    @Override
    public boolean backtrackReady() {
        // Only after the table is complete (POST), or to restart while backtracking
        return switch (executionState) {
            case PRE, R_LOOP, C_LOOP, I_LOOP, J_LOOP -> false;
            default -> true;
        };
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
                "Reset requested: state {} -> PRE; histories cleared (exec={}, lHist={}, sHist={}, btStack={})",
                executionState,
                executionHistory.size(),
                btStack.size());

        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;

        executionHistory.clear();
        btStack.clear();
        snapshots.clear();
        currentParens = "";

        int[][] l = (int[][]) variables.get("l");
        int[][] s = (int[][]) variables.get("s");
        int[][] b = (int[][]) variables.get("b");
        int n = (int) variables.get("n");
        int m = (int) variables.get("m");

        for (int i = 0; i <= n; i++)
            for (int j = 0; j <= m; j++) {
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

    /** Save the current state, then execute the next line. */
    @Override
    public void step() {
        snapshots.push(takeSnapshot());
        super.step();
    }

    /** Restore the state saved before the most recent step. */
    @Override
    public void undo() {
        if (executionHistory.isEmpty() || snapshots.isEmpty()) {
            super.undo(); // logs "Cannot undo past Line 0"
            return;
        }

        restoreSnapshot(snapshots.pop());
        nextLineNumber = executionHistory.remove(executionHistory.size() - 1);

        log.debug("undo: restored state before line {}", nextLineNumber);
        notifyProblemListeners();
    }

    private Snapshot takeSnapshot() {
        Map<String, Object> scalars = new HashMap<>();
        for (String name : SCALAR_VARIABLES) {
            scalars.put(name, variables.get(name));
        }

        return new Snapshot(scalars, executionState, currentParens, new ArrayList<>(btStack));
    }

    private void restoreSnapshot(Snapshot snapshot) {
        for (Map.Entry<String, Object> entry : snapshot.scalars().entrySet()) {
            if (entry.getValue() == null) {
                variables.remove(entry.getKey());
            } else {
                variables.put(entry.getKey(), entry.getValue());
            }
        }

        executionState = snapshot.state();
        currentParens = snapshot.parens();
        btStack.clear();
        btStack.addAll(snapshot.stack());
    }

    private int[][] copyTable(String name) {
        int[][] table = (int[][]) variables.get(name);
        int[][] copy = new int[table.length][];
        for (int row = 0; row < table.length; row++) {
            copy[row] = table[row].clone();
        }
        return copy;
    }

    // Copy into the existing array, since views and bTable hold references to it
    private void restoreTable(String name, int[][] saved) {
        int[][] table = (int[][]) variables.get(name);
        for (int row = 0; row < saved.length; row++) {
            System.arraycopy(saved[row], 0, table[row], 0, saved[row].length);
        }
    }

    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for i = 0 to n-1</pre></html>"); // Line 0
        codeStatements.add("<html><pre>    l[i][i] = 0</pre></html>"); // Line 1
        codeStatements.add("<html><pre>for c = 1 to n-1</pre></html>"); // Line 2
        codeStatements.add("<html><pre>    for i = 0 to n - c</pre></html>"); // Line 3
        codeStatements.add("<html><pre>        j = i + c</pre></html>"); // Line 4
        codeStatements.add("<html><pre>        l[i][j] = ∞</pre></html>"); // Line 5
        codeStatements.add("<html><pre>        for k = i to j-1</pre></html>"); // Line 6
        codeStatements.add(
                "<html><pre>            cost = l[i][k] + l[k+1][j] + d[i]d[k+1]d[j+1]</pre></html>"); // Line 7
        codeStatements.add(
                "<html><pre>            if cost &lt; l[i][j]: l[i][j] = "
                        + "cost</pre></html>"); // Line 8
        codeStatements.add("<html><pre>return l</pre></html>"); // Line 9
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

    // TODO: Add steps implementation for tree problems.
}
