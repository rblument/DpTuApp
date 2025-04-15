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

/**
 *
 * @author corey
 */

public class MatrixChainProblem extends Problem {

    public enum EXECUTION_STATE { PRE, R_LOOP, C_LOOP, I_LOOP, J_LOOP, POST }

    private EXECUTION_STATE executionState;
    
    // Stack to store m[i][j] values for undo tracking
    private Stack<int[]> mHistory = new Stack<>();

    public MatrixChainProblem(int[][] sizes) {
        super();

        int n = sizes.length;
        ArrayList<Integer> d = new ArrayList<>();
        
        d.add(sizes[0][0]);
        for (int[] size : sizes) {
            d.add(size[1]);
        }

        variables.put("n", n);
        variables.put("c", 1);
        variables.put("i", 0);
        variables.put("j", 0);
        variables.put("k", 0);
        variables.put("d", d);
        variables.put("m", new int[n][n]);

        tableVariable = "m";

        int[][] m = (int[][]) variables.get("m");
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                m[x][y] = -1;
            }
        }

        executionState = EXECUTION_STATE.PRE;
        kind = TaskKind.MATRIX_CHAIN;

        loadCodeStatements();
    }

    @Override
    protected void loadCodeStatements() {
        codeStatements.add("<html><pre>for i = 0 to n-1</pre></html>"); // 0
        codeStatements.add("<html><pre>    m[i][i] = 0</pre></html>"); // 1
        codeStatements.add("<html><pre>for c = 1 to n-1</pre></html>"); // 2
        codeStatements.add("<html><pre>    for i = 0 to n - c</pre></html>"); // 3
        codeStatements.add("<html><pre>        j = i + c</pre></html>"); // 4
        codeStatements.add("<html><pre>        m[i][j] = ∞</pre></html>"); // 5
        codeStatements.add("<html><pre>        for k = i to j-1</pre></html>"); // 6
        codeStatements.add("<html><pre>            cost = m[i][k] + m[k+1][j] + d[i]*d[k+1]*d[j+1]</pre></html>"); // 7
        codeStatements.add("<html><pre>            if cost < m[i][j]: m[i][j] = cost</pre></html>"); // 8
        codeStatements.add("<html><pre>return m</pre></html>"); // 9
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }

    public void prettyPrint() {
        int[][] m = (int[][]) variables.get("m");
        int n = (int) variables.get("n");

        System.out.println("ExecutionState: " + executionState);
        System.out.println("Current line: " + currentLineNumber);
        System.out.println("DP Table (m):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%6s", m[i][j] == -1 ? "-" : m[i][j]);
            }
            System.out.println();
        }
    }

    public void executeLine0() {
        variables.put("i", 0);
        executionState = EXECUTION_STATE.R_LOOP;
        currentLineNumber = 1;
    }

    public void executeLine1() {
        int i = (int) variables.get("i");
        int n = (int) variables.get("n");
        int[][] m = (int[][]) variables.get("m");
        mHistory.push(new int[]{i, i, m[i][i]});
        m[i][i] = 0;
        if (i + 1 == n) {
            variables.put("c", 1);
            executionState = EXECUTION_STATE.C_LOOP;
            currentLineNumber = 2;
        } else {
            variables.put("i", i + 1);
            currentLineNumber = 1;
        }
    }

    public void executeLine2() {
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");
        if (c == n) {
            executionState = EXECUTION_STATE.POST;
            currentLineNumber = 9;
        } else {
            variables.put("i", 0);
            currentLineNumber = 3;
        }
    }

    public void executeLine3() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int n = (int) variables.get("n");
        if (i > n - c - 1) {
            variables.put("c", c + 1);
            currentLineNumber = 2;
        } else {
            currentLineNumber = 4;
        }
    }

    public void executeLine4() {
        int i = (int) variables.get("i");
        int c = (int) variables.get("c");
        int j = i + c;
        variables.put("j", j);
        currentLineNumber = 5;
    }

    public void executeLine5() {
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");
        mHistory.push(new int[]{i, j, m[i][j]});
        m[i][j] = Integer.MAX_VALUE;
        variables.put("k", i);
        currentLineNumber = 6;
    }

    public void executeLine6() {
        int k = (int) variables.get("k");
        int j = (int) variables.get("j");
        if (k >= j) {
            int i = (int) variables.get("i");
            variables.put("i", i + 1);
            currentLineNumber = 3;
        } else {
            currentLineNumber = 7;
        }
    }

    public void executeLine7() {
        int[][] m = (int[][]) variables.get("m");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int k = (int) variables.get("k");
        ArrayList<Integer> d = (ArrayList<Integer>) variables.get("d");
        int cost = m[i][k] + m[k + 1][j] + d.get(i) * d.get(k + 1) * d.get(j + 1);
        variables.put("cost", cost);
        currentLineNumber = 8;
    }

    public void executeLine8() {
        int cost = (int) variables.get("cost");
        int i = (int) variables.get("i");
        int j = (int) variables.get("j");
        int[][] m = (int[][]) variables.get("m");
        if (cost < m[i][j]) {
            mHistory.push(new int[]{i, j, m[i][j]});
            m[i][j] = cost;
        }
        int k = (int) variables.get("k");
        variables.put("k", k + 1);
        currentLineNumber = 6;
    }

    public void executeLine9() {
        executionState = EXECUTION_STATE.POST;
    }

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

    public void undoLine2() {
        int c = (int) variables.get("c") - 1;
        variables.put("c", c);
        variables.put("i", 0);
        if (c <= 0) executionState = EXECUTION_STATE.R_LOOP;
    }

    public void undoLine3() {
        int i = (int) variables.get("i") - 1;
        variables.put("i", i);
        if (i < 0) executionState = EXECUTION_STATE.C_LOOP;
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
        int k = (int) variables.get("k") - 1;
        variables.put("k", k);
    }

    public void undoLine9() {
        executionState = EXECUTION_STATE.C_LOOP;
        variables.put("c", (int) variables.get("n"));
    }
}
