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



/**
 *
 * @author corey
 */
public class MatrixChainProblem extends Problem {
    
    /**
     * Current state of execution capturing which of the loops are current.
     * Note if the corresponding iteration index for a loop is -1, the loop
     * hasn't entered its first iteration.
     */
    public enum EXECUTION_STATE {PRE, R_LOOP, C_LOOP, I_LOOP, J_LOOP, POST};
    
    /**
     * Convenience reference to the number of matrices.
     */
    private final int n;

    public int getN() {
        return n;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getK() {
        return k;
    }

    public EXECUTION_STATE getExecutionState() {
        return executionState;
    }
        
    /**
     * Convenience reference to the length of y.
     */
//    private final int m;
    
    /**
     * Current value of iteration loop variable r.
     * When this is -1, the loop hasn't been entered, when it has a value of 0,
     * it represents cell value -1 in the Dynamic Programming problem.
     */
    private int r;
    
    /**
     * Current value of iteration loop variable c.
     * When this is -1, the loop hasn't been entered, when it has a value of 0,
     * it represents cell value -1 in the Dynamic Programming problem.
     */
    private int c;
    
    /**
     * Current value of iteration loop variable i.
     * When this is -1, the loop hasn't been entered, when it has a value of 0,
     * it represents cell value -1 in the Dynamic Programming problem.
     */
    private int i;
    
    /**
     * Current value of iteration loop variable j.
     * When this is -1, the loop hasn't been entered, when it has a value of 0,
     * it represents cell value -1 in the Dynamic Programming problem.
     */
    private int j;
    
    private int k;
    
    private ArrayList<int[][]> matrices;
    
    private ArrayList<int[]> matricesSizes;
    
    private ArrayList<Integer> dArray;
    
    /**
     * The subproblem dynamic values.
     * 
     * Note all indexes are shifted by 1 since 
     * subproblem[0][0] in the Java array corresponds to cell[-1][-1] in
     * the Dynamic Programming problem.
     */
    private final int[][] subproblemL;
    
       
    /**
     * The current state of the algorithm, before the loops, in a loop, and
     * after all of the loops have executed.
     */
    private MatrixChainProblem.EXECUTION_STATE executionState;
    
    public MatrixChainProblem(int[][] matriceSizes) {
//        this.matrices = matrices;
        
//        this.n = this.matrices.size();
        
//        for(int[][] matrix : matrices) {
//            int [] matrixSize = {matrix.length, matrix[0].length};
//            this.matricesSizes.add(matrixSize);
//        }
//      
        matricesSizes = new ArrayList<>();
        
        for (int[] mSize : matriceSizes) {
            matricesSizes.add(mSize);
        }
        
        n = matricesSizes.size() - 1;
        
        executionState = EXECUTION_STATE.PRE;
        
        r = -1; // Loops have not been entered.
        c = -1;
        i = -1;
        j = -1;
        k = -1;  
        
        subproblemL = new int[n+1][n+1];
        
        buildDArray();
    }
       
    /**
     *
     * @param matrices
     */
//    public MatrixChainProblem(ArrayList<int[][]> matrices) {
//        for(int[][] matrix : matrices) {
//            int [] matrixSize = {matrix.length, matrix[0].length};
//            this.matricesSizes.add(matrixSize);
//        }
//        
//        this();
//    }
    
//    public MatrixChainProblem(int[][] matriceSizes) {
//        
//        for (int[] mSize : matriceSizes) {
//            this.matricesSizes.add(mSize);
//        }
//        
//        this();
//    }
        
    private void buildDArray() {
        dArray = new ArrayList<>();
        for (int i = 0; i < matricesSizes.size(); i++) {
            if (i == 0) {
                dArray.add(matricesSizes.get(i)[0]);
            }
            dArray.add(matricesSizes.get(i)[1]);
        }
    }
    
    public void step() {
        switch (executionState) {
            case PRE:
                reset();
                executionState = EXECUTION_STATE.R_LOOP;
                break;

            case R_LOOP:
                if (++r == n + 1) {
                    executionState = EXECUTION_STATE.C_LOOP;
                    c = 1;
                } else {
                    subproblemL[r][r] = 0;
                }
                break;

            case C_LOOP:
                if (c == n + 1) {
                    executionState = EXECUTION_STATE.I_LOOP;
                    i = 0;
                } else {
                    i = 0;
                    j = c;
                    k = i;
                    subproblemL[i][j] = Integer.MAX_VALUE;
                    executionState = EXECUTION_STATE.I_LOOP;
                }
                break;

            case I_LOOP:
                j = i + c;
                k = i;
                subproblemL[i][j] = Integer.MAX_VALUE;
                executionState = EXECUTION_STATE.J_LOOP;
                break;

            case J_LOOP:
                if (k < j) {
                    int cost = subproblemL[i][k] + subproblemL[k + 1][j]
                            + dArray.get(i) * dArray.get(k + 1) * dArray.get(j + 1);
                    if (cost < subproblemL[i][j]) {
                        subproblemL[i][j] = cost;
                    }
                    k++;
                } else {
                    i++;
                    if (i + c >= n + 1) {
                        c++;
                        if (c == n + 1) {
                            executionState = EXECUTION_STATE.POST;
                        } else {
                            executionState = EXECUTION_STATE.C_LOOP;
                        }
                    } else {
                        j = i + c;
                        k = i;
                        subproblemL[i][j] = Integer.MAX_VALUE;
                        executionState = EXECUTION_STATE.J_LOOP;
                    }
                }
                break;

            case POST:
                System.out.println("Shouldn't call step() in POST state.");
                break;

            default:
                System.out.println("Unknown execution state.");
        }
    }
    
    public int getCurrentValue() {
    return subproblemL[i][j];
}

    public int getValueAt(int row, int column) {
        return subproblemL[row][column];
    }

    
        /**
     * Resets this problem (algorithm) back to its initial state before
     * execution of the first statement.
     */
    public void reset() {
        r = -1;
        c = -1;
        i = -1;
        j = -1;
        k = -1;
        
        for (int p = 0; p <= n; p++)
            for (int q = 0; q <= n; q++)
                subproblemL[p][q] = -1;
    }
    
    /**
     * Outputs to System.out the current state (of the algorithm).
     */
    public void prettyPrint() {
        System.out.println("\nExecutionState: " + executionState);
        System.out.println("r: " + r);
        System.out.println("c: " + c);
        System.out.println("i: " + i);
        System.out.println("j: " + j);
            
        for (int p = 0; p <= n; p++) {
            for (int q = 0; q <= n; q++)
                System.out.print(subproblemL[p][q] + " ");
           
            System.out.println("");           
        }       
    }
}

    