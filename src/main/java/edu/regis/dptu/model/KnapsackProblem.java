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
 * Represents a 0/1 Knapsack Dynamic Programming problem.
 *
 * <p>Given a knapsack with a weight capacity W and a list of n items, each with a weight and value,
 * the algorithm fills a DP table dp[i][w] = maximum value achievable using the first i items with
 * capacity w. Backtracking then identifies which items are included in the optimal solution.
 *
 * @author Cormac Moss
 */
public class KnapsackProblem extends Problem {

    private static final Logger log = LoggerFactory.getLogger(KnapsackProblem.class);

    public enum EXECUTION_STATE {
        PRE,
        I_LOOP,
        W_LOOP,
        POST,
        BACKTRACK_PRE,
        BACKTRACK_LOOP,
        BACKTRACK_DONE
    }

    private final int[] weights;
    private final int[] values;
    private final String[] names;
    private final int capacity;
    private EXECUTION_STATE executionState;

    public KnapsackProblem(String[] names, int[] weights, int[] values, int capacity) {
        this(Model.DEFAULT_ID, names, weights, values, capacity);
    }

    public KnapsackProblem(int id, String[] names, int[] weights, int[] values, int capacity) {
        super(id);

        if (names == null || weights == null || values == null)
            throw new IllegalArgumentException(
                    "KnapsackProblem: names, weights, and values must not be null");
        if (names.length != weights.length || names.length != values.length)
            throw new IllegalArgumentException(
                    "KnapsackProblem: names, weights, and values arrays must have equal length");
        if (capacity <= 0)
            throw new IllegalArgumentException("KnapsackProblem: capacity must be positive");

        int n = names.length;
        this.names = names.clone();
        this.weights = weights.clone();
        this.values = values.clone();
        this.capacity = capacity;

        variables.put("n", n);
        variables.put("W", capacity);
        variables.put("weights", this.weights);
        variables.put("values", this.values);
        variables.put("names", this.names);
        variables.put("i", 0);
        variables.put("w", 0);
        variables.put("dp", new int[n + 1][capacity + 1]);
        variables.put("bt", new int[n + 1][capacity + 1]);

        tableVariable = "dp";
        backtrackingTableVariable = "bt";
        executionState = EXECUTION_STATE.PRE;

        loadCodeStatements();
        loadBacktrackingCodeStatements();
        reset();

        log.debug("KnapsackProblem created: id={}, n={}, W={}", id, n, capacity);
    }

    @Override
    public ProblemKind getType() {
        return ProblemKind.KNAPSACK_0_1;
    }

    @Override
    public boolean hasFinished() {
        return executionState == EXECUTION_STATE.POST
                || executionState == EXECUTION_STATE.BACKTRACK_DONE;
    }

    @Override
    public boolean backtrackReady() {
        return executionState == EXECUTION_STATE.POST
                || executionState == EXECUTION_STATE.BACKTRACK_PRE
                || executionState == EXECUTION_STATE.BACKTRACK_LOOP
                || executionState == EXECUTION_STATE.BACKTRACK_DONE;
    }

    @Override
    public boolean canStepBack() {
        return !executionHistory.isEmpty();
    }

    @Override
    public boolean undoingBacktrackButton() {
        return false;
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

        int n = (int) variables.get("n");
        int W = (int) variables.get("W");
        int[][] bt = (int[][]) variables.get("bt");
        for (int i = 0; i <= n; i++) for (int w = 0; w <= W; w++) bt[i][w] = UNVISITED;

        notifyProblemListeners();
    }

    @Override
    public void reset() {
        log.debug(
                "Reset requested: state {} -> PRE; histories cleared (exec={})",
                executionState,
                executionHistory.size());

        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;
        executionHistory.clear();

        int n = (int) variables.get("n");
        int W = (int) variables.get("W");
        int[][] dp = (int[][]) variables.get("dp");
        int[][] bt = (int[][]) variables.get("bt");

        for (int i = 0; i <= n; i++)
            for (int w = 0; w <= W; w++) {
                dp[i][w] = 0;
                bt[i][w] = UNVISITED;
            }

        variables.put("i", 0);
        variables.put("w", 0);

        notifyProblemListeners();
    }

    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for i = 1 to n</pre></html>");
        codeStatements.add("<html><pre>    for w = 0 to W</pre></html>");
        codeStatements.add("<html><pre>        if weight[i] &lt;= w</pre></html>");
        codeStatements.add(
                "<html><pre>            dp[i][w] = max(dp[i-1][w], value[i] + dp[i-1][w-weight[i]])</pre></html>");
        codeStatements.add("<html><pre>        else</pre></html>");
        codeStatements.add("<html><pre>            dp[i][w] = dp[i-1][w]</pre></html>");
        codeStatements.add("<html><pre>return dp[n][W]</pre></html>");
    }

    @Override
    protected void loadBacktrackingCodeStatements() {
        backtrackingCodeStatements.clear();
        backtrackingCodeStatements.add("<html><pre>i = n, w = W</pre></html>");
        backtrackingCodeStatements.add("<html><pre>while i &gt; 0 and w &gt; 0</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    if dp[i][w] != dp[i-1][w]</pre></html>");
        backtrackingCodeStatements.add("<html><pre>        include item i</pre></html>");
        backtrackingCodeStatements.add(
                "<html><pre>        w = w - weight[i]; i = i - 1</pre></html>");
        backtrackingCodeStatements.add("<html><pre>    else</pre></html>");
        backtrackingCodeStatements.add("<html><pre>        i = i - 1</pre></html>");
    }

    public int getCapacity() {
        return capacity;
    }

    public int getN() {
        return (int) variables.get("n");
    }

    public int[] getWeights() {
        return weights.clone();
    }

    public int[] getValues() {
        return values.clone();
    }

    public String[] getNames() {
        return names.clone();
    }

    public int[][] getDpTable() {
        return (int[][]) variables.get("dp");
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    public void executeLine0() {
        variables.put("i", 1);
        executionState = EXECUTION_STATE.I_LOOP;
        nextLineNumber = 1;
        log.debug("executeLine0: i=1, state -> {}", executionState);
    }

    public void executeLine1() {
        int i = (int) variables.get("i");
        int n = (int) variables.get("n");
        if (i > n) {
            executionState = EXECUTION_STATE.POST;
            nextLineNumber = 6;
            log.debug("executeLine1: i>n ({}); state -> POST", i);
        } else {
            variables.put("w", 0);
            executionState = EXECUTION_STATE.W_LOOP;
            nextLineNumber = 2;
            log.debug("executeLine1: i={}, w=0, start inner loop", i);
        }
    }

    public void executeLine2() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int W = (int) variables.get("W");
        if (w > W) {
            variables.put("i", i + 1);
            nextLineNumber = 1;
            log.debug("executeLine2: w>W; advance i -> {}", i + 1);
            return;
        }
        nextLineNumber = (weights[i - 1] <= w) ? 3 : 5;
        log.debug(
                "executeLine2: i={}, w={}, weight[i-1]={} -> line {}",
                i,
                w,
                weights[i - 1],
                nextLineNumber);
    }

    public void executeLine3() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] dp = (int[][]) variables.get("dp");
        int skip = dp[i - 1][w];
        int take = values[i - 1] + dp[i - 1][w - weights[i - 1]];
        dp[i][w] = Math.max(skip, take);
        log.debug("executeLine3: dp[{}][{}] = max({}, {}) = {}", i, w, skip, take, dp[i][w]);
        variables.put("w", w + 1);
        nextLineNumber = 2;
    }

    public void executeLine5() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] dp = (int[][]) variables.get("dp");
        dp[i][w] = dp[i - 1][w];
        log.debug("executeLine5: dp[{}][{}] = dp[{}][{}] = {}", i, w, i - 1, w, dp[i][w]);
        variables.put("w", w + 1);
        nextLineNumber = 2;
    }

    public void executeLine6() {
        executionState = EXECUTION_STATE.POST;
        log.debug("executeLine6: state -> POST");
    }

    public void executeLine100() {
        int n = (int) variables.get("n");
        int W = (int) variables.get("W");
        variables.put("i", n);
        variables.put("w", W);
        executionState = EXECUTION_STATE.BACKTRACK_PRE;
        nextLineNumber = 101;
        log.debug("executeLine100: init backtrack i={}, w={}", n, W);
    }

    public void executeLine101() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        if (i <= 0 || w <= 0) {
            executionState = EXECUTION_STATE.BACKTRACK_DONE;
            nextLineNumber = 106;
            log.debug("executeLine101: loop done (i={}, w={})", i, w);
        } else {
            executionState = EXECUTION_STATE.BACKTRACK_LOOP;
            nextLineNumber = 102;
        }
    }

    public void executeLine102() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] dp = (int[][]) variables.get("dp");
        int[][] bt = (int[][]) variables.get("bt");
        bt[i][w] = HIT;
        nextLineNumber = (dp[i][w] != dp[i - 1][w]) ? 103 : 105;
        log.debug("executeLine102: i={}, w={} -> line {}", i, w, nextLineNumber);
    }

    public void executeLine103() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] bt = (int[][]) variables.get("bt");
        bt[i][w] = ADD_TO_SOLUTION;
        nextLineNumber = 104;
        log.debug("executeLine103: item {} included (w={})", i, w);
    }

    public void executeLine104() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        variables.put("w", w - weights[i - 1]);
        variables.put("i", i - 1);
        nextLineNumber = 101;
        log.debug("executeLine104: w -> {}, i -> {}", w - weights[i - 1], i - 1);
    }

    public void executeLine105() {
        int i = (int) variables.get("i");
        variables.put("i", i - 1);
        nextLineNumber = 101;
        log.debug("executeLine105: item {} skipped; i -> {}", i, i - 1);
    }

    public void executeLine106() {
        executionState = EXECUTION_STATE.BACKTRACK_DONE;
        log.debug("executeLine106: state -> BACKTRACK_DONE");
    }

    public void undoLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.PRE;
        nextLineNumber = 0;
    }

    public void undoLine1() {
        int i = (int) variables.get("i");
        if (i > 1) variables.put("i", i - 1);
        executionState = EXECUTION_STATE.I_LOOP;
        nextLineNumber = 1;
    }

    public void undoLine2() {
        int w = (int) variables.get("w");
        if (w > 0) variables.put("w", w - 1);
        nextLineNumber = 2;
    }

    public void undoLine3() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w") - 1;
        variables.put("w", w);
        int[][] dp = (int[][]) variables.get("dp");
        dp[i][w] = 0;
        nextLineNumber = 2;
    }

    public void undoLine5() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w") - 1;
        variables.put("w", w);
        int[][] dp = (int[][]) variables.get("dp");
        dp[i][w] = 0;
        nextLineNumber = 2;
    }

    public void undoLine6() {
        executionState = EXECUTION_STATE.I_LOOP;
        nextLineNumber = 6;
    }

    public void undoLine100() {
        variables.put("i", 0);
        variables.put("w", 0);
    }

    public void undoLine101() {}

    public void undoLine102() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] bt = (int[][]) variables.get("bt");
        bt[i][w] = UNVISITED;
    }

    public void undoLine103() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        int[][] bt = (int[][]) variables.get("bt");
        bt[i][w] = HIT;
        nextLineNumber = 102;
    }

    public void undoLine104() {
        int i = (int) variables.get("i");
        int w = (int) variables.get("w");
        variables.put("i", i + 1);
        variables.put(
                "w", w + weights[i]); // i is already decremented, so weights[i] = weights[(i+1)-1]
        nextLineNumber = 103;
    }

    public void undoLine105() {
        int i = (int) variables.get("i");
        variables.put("i", i + 1);
        nextLineNumber = 102;
    }

    public void undoLine106() {
        executionState = EXECUTION_STATE.BACKTRACK_LOOP;
    }
}
