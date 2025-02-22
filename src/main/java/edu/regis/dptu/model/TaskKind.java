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

/**
 * The task types that a student may be required to complete in the tutor.
 * 
 * @author rickb
 */
public enum TaskKind {
    /**
     * A task requiring a Student to complete a Problem.
     */
    LCS_PROBLEM("LCS Problem"),
    
    MATRIX_CHAIN("Matrix Chaining"),
    
    KNAPSACK_0_1("0/1 Knapsack"),
    
    INITIALIZE_FIRST_ROW("Initiailize First Row"),
    
    INITIALIZE_FIRST_COL("Initialize First Column"),
    
    /**
     * This is a specific cell value identified by an i,j value in during the
     * i_j_loop of the algorithm.
     */
    ASSIGN_CELL("Assign a Cell Value"),
    
    /**
     * The task of generating the table for a dynamic programming problem
     */
    CREATE_TABLE("Create Table"),
    
    /**
     * The task of tracing a path through the dynamic programming table to 
     * identify the solution
     */
    SOLUTION_PATH("Solution Path"), 
    
    /**
     * The task of producing a solution using a generated solution path
     */
    SOLVE("Solve");
    
    /**
     * A GUI displayable string identifying this task kind.
     */
    private final String title;
    /**
     * Initialize this task kind with its title.
     * 
     * @param title 
     */
    TaskKind(String title) {
        this.title = title;
    }
    
    /**
     * Return the title for this task.
     * 
     * @return a String
     */
    public String title() {
        return title;
    }
}
    