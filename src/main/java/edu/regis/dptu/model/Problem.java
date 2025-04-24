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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The primary Dynamic Programming task that a student is attempting to solve,
 * which is associated with a unit within a course.
 *
 * Subclasses need to implement the abstract loadCodeStatements
 *
 * In VanLehn's sense, this is a task for the student to complete, but we treat
 * tasks at a finer granularity i.e. as subproblems within the primary problem.
 * The tasks within a problem are derived from the specific nature of the
 * problem.
 *
 * @author rickb
 */
public abstract class Problem extends TitledModel {

    /**
     * The task associated with this problem. (ToDo: )
     */
    protected TaskKind kind;

    /**
     * The variables used in the algorithmic solution to this dynamic
     * programming problem, which is determined by the child subclass.
     *
     * In general, all variables except the tableVariable will have a data type
     * of int, while the tableVariable with have a type int[][].
     */
    protected HashMap<String, Object> variables;

    /**
     * The variable name containing the matrix cell table for this problem.
     */
    protected String tableVariable;

    /**
     * The algorithmic solution to this dynamic programming problem as textual
     * lines of code.
     */
    protected ArrayList codeStatements = new ArrayList();

    /**
     * The currently line number to execute
     */
    protected int currentLineNumber = 0;

    /**
     * A history of the line numbers that were executed prior to the current
     * line number.
     *
     * step() adds to this history, undo() removes items from it.
     */
    protected ArrayList<Integer> executionHistory;

    protected ArrayList<ProblemListener> problemListeners;

    protected abstract void loadCodeStatements();

    public Problem() {
        this(DEFAULT_ID);
    }

    public Problem(int id) {
        super(id);

        variables = new HashMap<>();
        codeStatements = new ArrayList<>();
        executionHistory = new ArrayList<>();
        problemListeners = new ArrayList<>();
    }

    public TaskKind getKind() {
        return kind;
    }

    public void setKind(TaskKind kind) {
        this.kind = kind;
    }

    public ArrayList<String> getCodeStatements() {
        return codeStatements;
    }

    public void setCodeStatements(ArrayList codeStatements) {
        this.codeStatements = codeStatements;
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public void setCurrentLineNumber(int currentLineNumber) {
        this.currentLineNumber = currentLineNumber;
    }

    public String getTableVariable() {
        return tableVariable;
    }

    public void setTableVariable(String tableVariable) {
        this.tableVariable = tableVariable;
    }

    public ArrayList<String> getVariableNames() {
        return new ArrayList(variables.keySet());
    }

    public int getVariableValue(String variableName) {
        return (int) variables.get(variableName);
    }

    public int getValueAt(int row, int column) {
        int[][] table = (int[][]) variables.get(tableVariable);

        return table[row][column];
    }

    public ArrayList<ProblemListener> getProblemListeners() {
        return problemListeners;
    }
    
    /**
     * Returns the raw Object value for a variable.
     * Use this for non-int variables like the DP table.
     * @param variableName The name of the variable.
     * @return The variable's value as an Object, or null if not found.
     */
    public Object getVariableObject(String variableName) {
        // Added null check for safety
        if (variables != null) {
            return variables.get(variableName);
        }
        return null;
    }

    public void addProblemListener(ProblemListener listener) {
        problemListeners.add(listener);
    }

    /**
     * Execute the current line of code and then update to the "next" line of
     * code to execute.
     */
    public void step() {
        executionHistory.add(currentLineNumber);
        String methodName = "executeLine" + currentLineNumber;
        executeMethod(methodName);
        
        // Notify listeners that the problem has been updated
        notifyProblemListeners();
    }

    /**
     * Execute the next n statements (forward).
     *
     * @param n number of steps to execute
     */
    public void step(int n) {
        for (int i = 0; i < n; i++) {
            step();
        }
    }

    /**
     * Take one step backward in the algorithm by undoing the
     */
    public void undo() {
        int size = executionHistory.size();

        if (size == 0) {
            System.out.println("Cannot undo past Line 0");
            
        } else {
            int lastItemPos = size - 1;

            int previousLineNumber = executionHistory.remove(lastItemPos);

            String methodName = "undoLine" + previousLineNumber;
            executeMethod(methodName);

            if (previousLineNumber != 0) {
                lastItemPos--;

                currentLineNumber = executionHistory.get(lastItemPos);
            }
            
            // Notify listeners that the problem has been updated
            notifyProblemListeners();
        }
    }
    
    /**
     * Reset the problem to its initial state.
     */
    public void reset() {
        currentLineNumber = 0;
        executionHistory.clear();
        
        // Additional reset logic implemented by subclasses
        
        // Notify listeners that the problem has been updated
        notifyProblemListeners();
    }

    /**
     * Undo the previous n statements (backwards)
     *
     * @param n number of steps to execute
     */
    public void undo(int n) {
        for (int i = 0; i < n; i++) {
            undo();
        }
    }

    /**
     * Use reflection to execute the given method name.
     *
     * @param methodName
     */
    public void executeMethod(String methodName) {
        Class clazz = this.getClass();

        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(this);

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Basic method that each problem will Override to handle decoding its
     * specific problem from a SubproblemTableView table cell location to a line
     * number.
     *
     * Note: Similar to notifyProblemListener, this could be effective for both
     * highlighting problems. If the step() methods has information about what
     * cell is currently being computed, this decoder would have the correct
     * line number if the user clicked it, or have the correct line number based
     * on what step we are on.
     *
     * @param row
     * @param column
     * @return correspondingLineNumber
     */
    protected int TableToLineNumberdecoder(int row, int column) {
        int correspondingLineNumber = -1;
        // Next steps are creating the LCS problem decoder, letting codeView 
        // use that to decode the line number from the row and column.
        return correspondingLineNumber;
    }

    /**
     * Notifies each problemListener in the problemListeners array list. Passes
     * itself to each.
     *
     * Note: When considering how to handle step interaction AND click
     * interaction, potentially this could work for both. Since it is simply
     * passing itself, we could leave it to the views to handle changes for
     * either. So in LCSProblem, we would call this at the end of the step
     * methods or the clickListener in SubproblemTableView.
     */
    protected void notifyProblemListeners() {
        for (int i = 0; i < problemListeners.size(); i++) {
            problemListeners.get(i).problemUpdated(this);
        }
    }

}