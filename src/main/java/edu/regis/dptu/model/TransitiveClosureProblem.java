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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Transitive Closure Dynamic Programming problem
 *
 * <p>The class calculates the transitive closure matrix of a directed graph of n vertices using the
 * Warshall algorithm
 *
 * @author curtis
 */
public class TransitiveClosureProblem extends Problem {

    private static final Logger log = LoggerFactory.getLogger(TransitiveClosureProblem.class);

    /** Define triple loop execution state */
    public enum EXECUTION_STATE {
        PRE,
        K_LOOP,
        I_LOOP,
        J_LOOP,
        POST
    }

    private final int[][] adjacency;
    private final String[] labels;
    private EXECUTION_STATE executionState;

    public TransitiveClosureProblem(int[][] adjacency) {
        this(Model.DEFAULT_ID, adjacency, null);
    }

    public TransitiveClosureProblem(int[][] adjacency, String[] labels) {
        this(Model.DEFAULT_ID, adjacency, labels);
    }

    public TransitiveClosureProblem(int id, int[][] adjacency, String[] labels) {
        super(id);

        if (adjacency == null)
            throw new IllegalArgumentException(
                    "TransitiveClosureProblem: adjacency matrix must not be null");

        int n = adjacency.length;
        if (n == 0)
            throw new IllegalArgumentException(
                    "TransitiveClosureProblem: adjacency matrix must not be empty");

        int[][] adjCopy = new int[n][n];
        for (int i = 0; i < n; i++) {
            if (adjacency[i] == null || adjacency[i].length != n)
                throw new IllegalArgumentException(
                        "TransitiveClosureProblem: adjacency matrix must be square (n x n)");
            for (int j = 0; j < n; j++) {
                int v = adjacency[i][j];
                if (v != 0 && v != 1)
                    throw new IllegalArgumentException(
                            "TransitiveClosureProblem: adjacency cells must be 0 or 1");
                adjCopy[i][j] = v;
            }
        }

        this.adjacency = adjCopy;

        if (labels == null) {
            this.labels = new String[n];
            for (int i = 0; i < n; i++) this.labels[i] = String.valueOf(i);
        } else {
            if (labels.length != n)
                throw new IllegalArgumentException(
                        "TransitiveClosureProblem: labels length must equal matrix size");
            this.labels = labels.clone();
        }

        variables.put("n", n);
        variables.put("labels", this.labels);
        variables.put("k", 0);
        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("reach", new int[n][n]);

        tableVariable = "reach";
        backtrackingTableVariable = "reach";
        executionState = EXECUTION_STATE.PRE;

        loadCodeStatements();
        loadBacktrackingCodeStatements();
        reset();

        log.debug("TransitiveClosureProblem created: id={}, n={}", id, n);
    }

    @Override
    public ProblemKind getType() {
        return ProblemKind.TRANSITIVE_CLOSURE;
    }

    @Override
    public boolean hasFinished() {
        return executionState == EXECUTION_STATE.POST;
    }

    @Override
    public boolean canStepBack() {
        return !executionHistory.isEmpty();
    }

    /** No separate backtracking phase required */
    @Override
    public boolean backtrackReady() {
        return false;
    }

    @Override
    public boolean undoingBacktrackButton() {
        return false;
    }

    @Override
    public void backtrackingOn() {
        log.debug("backtrackingOn: no-op (Transitive Closure has no backtracking phase)");
    }

    @Override
    public void reset() {
        log.debug(
                "Reset requested: state {} -> PRE; history cleared (exec={})",
                executionState,
                executionHistory.size());

        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;
        executionHistory.clear();

        int n = (int) variables.get("n");
        int[][] reach = (int[][]) variables.get("reach");

        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) reach[i][j] = adjacency[i][j];

        variables.put("k", 0);
        variables.put("i", 0);
        variables.put("j", 0);

        notifyProblemListeners();
    }

    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for k = 0 to n-1</pre></html>");
        codeStatements.add("<html><pre>    for i = 0 to n-1</pre></html>");
        codeStatements.add("<html><pre>        for j = 0 to n-1</pre></html>");
        codeStatements.add("<html><pre>            if reach[i][k] and reach[k][j]</pre></html>");
        codeStatements.add("<html><pre>                reach[i][j] = 1</pre></html>");
        codeStatements.add("<html><pre>return reach</pre></html>");
    }

    @Override
    protected void loadBacktrackingCodeStatements() {
        backtrackingCodeStatements.clear();
    }

    public int getN() {
        return (int) variables.get("n");
    }

    public String[] getLabels() {
        return labels.clone();
    }

    public int[][] getAdjacencyMatrix() {
        int n = adjacency.length;
        int[][] copy = new int[n][n];

        for (int i = 0; i < n; i++) copy[i] = adjacency[i].clone();

        return copy;
    }

    public int[][] getReachMatrix() {
        return (int[][]) variables.get("reach");
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    public void executeLine0() {
        variables.put("k", 0);
        executionState = EXECUTION_STATE.K_LOOP;

        nextLineNumber = 1;

        log.debug("executeLine0: k=0, state -> K_LOOP");
    }

    public void executeLine1() {
        int k = (int) variables.get("k");
        int n = (int) variables.get("n");
        if (k >= n) {
            executionState = EXECUTION_STATE.POST;

            nextLineNumber = 5;

            log.debug("executeLine1: k>=n ({}); state -> POST", k);
        } else {
            variables.put("i", 0);
            executionState = EXECUTION_STATE.I_LOOP;
            nextLineNumber = 2;

            log.debug("executeLine1: k={}, i=0, enter i-loop", k);
        }
    }

    public void executeLine2() {
        int i = (int) variables.get("i");
        int k = (int) variables.get("k");
        int n = (int) variables.get("n");
        if (i >= n) {
            variables.put("k", k + 1);

            nextLineNumber = 1;

            log.debug("executeLine2: i>=n; advance k -> {}", k + 1);
        } else {
            variables.put("j", 0);
            executionState = EXECUTION_STATE.J_LOOP;

            nextLineNumber = 3;

            log.debug("executeLine2: i={}, j=0, enter j-loop", i);
        }
    }

    public void executeLine3() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int k = (int) variables.get("k");
        int n = (int) variables.get("n");
        if (j >= n) {
            variables.put("i", i + 1);
            nextLineNumber = 2;

            log.debug("executeLine3: j>=n; advance i -> {}", i + 1);

            return;
        }

        int[][] reach = (int[][]) variables.get("reach");

        // Only set the cell if path exists
        if (reach[i][k] == HIT && reach[k][j] == HIT) {
            nextLineNumber = 4;

            log.debug("executeLine3: reach[{}][{}] && reach[{}][{}] -> set cell", i, k, k, j);
        } else {
            variables.put("j", j + 1);
            nextLineNumber = 3;

            log.debug("executeLine3: no path through k={} for ({},{}); j -> {}", k, i, j, j + 1);
        }
    }

    public void executeLine4() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] reach = (int[][]) variables.get("reach");

        reach[i][j] = HIT;
        variables.put("j", j + 1);

        nextLineNumber = 3;

        log.debug("executeLine4: reach[{}][{}] = 1; j -> {}", i, j, j + 1);
    }

    public void executeLine5() {
        executionState = EXECUTION_STATE.POST;
        log.debug("executeLine5: state -> POST");
    }

    public void undoLine0() {
        variables.put("k", 0);
        executionState = EXECUTION_STATE.PRE;

        nextLineNumber = 0;
    }

    public void undoLine1() {
        executionState = EXECUTION_STATE.K_LOOP;

        nextLineNumber = 1;
    }

    public void undoLine2() {
        executionState = EXECUTION_STATE.I_LOOP;

        nextLineNumber = 2;
    }

    public void undoLine3() {
        executionState = EXECUTION_STATE.J_LOOP;

        nextLineNumber = 3;
    }

    public void undoLine4() {
        int j = (int) variables.get("j");
        if (j > 0) variables.put("j", j - 1);

        nextLineNumber = 3;
    }

    public void undoLine5() {
        executionState = EXECUTION_STATE.K_LOOP;

        nextLineNumber = 5;
    }
}
