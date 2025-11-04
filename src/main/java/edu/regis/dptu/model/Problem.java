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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Dynamic Programming problem that a student is attempting to solve.
 *
 * <p>The specific type of Dynamic Programming problem is specified by the value returned by the
 * getType() method, which is declared abstract in this parent class.
 *
 * <p>In VanLehn's sense, this is a task for the student to complete, but we treat tasks at a finer
 * granularity i.e. as subproblems within the primary problem. The tasks within a problem are
 * derived from the specific nature of the problem.
 *
 * @author rickb
 */
public abstract class Problem extends TitledModel {
    private static final Logger log = LoggerFactory.getLogger(Problem.class);

    /** The time between steps when running all */
    private static final int RUN_STEP_INTERVAL = 500;

    /** The logger for the class */
    private static final java.util.logging.Logger julLogger =
            java.util.logging.Logger.getLogger(Problem.class.getName());

    /**
     * The type of this Dynamic Programming problem, which must be assigned when instantiating a
     * subclass
     */
    protected ProblemKind type;

    /**
     * The id that serves as an index into the DB subtype table.
     *
     * <p>For example, if type is LCS_PROBLEM, then the subtype id is the id of the problem in the
     * LCSProblem table (with the Model.id being the id in the Problem table
     */
    protected int subTypeId;

    /**
     * The variables used in the algorithmic solution to this dynamic programming problem, which is
     * determined by the child subclass.
     *
     * <p>In general, all variables except the tableVariable will have a data type of int, while the
     * tableVariable with have a type int[][].
     */
    protected HashMap<String, Object> variables;

    /** The variable name containing the matrix cell table for this problem. */
    protected String tableVariable;

    /** The name of the internal table that keeps track of highlighting */
    protected String backtrackingTableVariable;

    /** The algorithmic solution to this dynamic programming problem as textual lines of code. */
    protected ArrayList<String> codeStatements;

    /** The algorithmic for backtracking and finding the final solution from the table. */
    protected ArrayList<String> backtrackingCodeStatements;

    /** The number of the line that will execute the next time "step forward" is clicked */
    protected int nextLineNumber = 0;

    protected final int BACKTRACKING_START_NUM = 100;
    protected final int HIT = 1;
    protected final int MISS = 0;
    protected final int ADD_TO_SOLUTION = 2;
    protected final int UNVISITED = -1;

    /**
     * A history of the line numbers that were executed prior to the current line number.
     *
     * <p>step() adds to this history, undo() removes items from it.
     */
    protected ArrayList<Integer> executionHistory;

    /** Observers who are listening for changes to the state of this problem. */
    protected ArrayList<ProblemListener> problemListeners;

    /**
     * Return the type of this Dynamic Programming problem.
     *
     * @return
     */
    public abstract ProblemKind getType();

    public int getNextLineNumber() {
        return nextLineNumber;
    }

    /**
     * Method that determines if the subclass has completed with either the dp algorithm or the
     * backtracking algorithm. Used to disable functionality in the UI
     *
     * @return whether the problem has finished
     */
    public abstract boolean hasFinished();

    /** Method that resets the problem */
    public abstract void reset();

    /** Loads the pseudo-code statements for display. */
    protected abstract void loadCodeStatements();

    protected abstract void loadBacktrackingCodeStatements();

    /**
     * This prevents user from pressing the "step back" button when they shouldn't
     *
     * @return
     */
    public abstract boolean canStepBack();

    /**
     * This is to prevent the user from hitting the backtrack button before the dp table is filled
     * in completely.
     *
     * @return
     */
    public abstract boolean backtrackReady();

    /**
     * Takes care of all housekeeping required to switch from dp algorithm to backtracking
     * algorithm. Also called when restarting backtracking
     */
    public abstract void backtrackingOn();

    /**
     * When the backtracking button has been clicked, but the step forward button has not yet
     * executed any backtracking steps, this method will return true (to help the undo button) and
     * reset the nextLineNumber. This is needed because when going backwards from the backtracking
     * algorithm into the DP algorithm, the code view needs to change from BacktrackingCodeView to
     * CodeView, which cannot be handled from within the Problem object, since it does not know
     * about views.
     *
     * @return
     */
    public abstract boolean undoingBacktrackButton();

    /** Instantiate a Dynamic Programming problem with a DEFAULT_ID. */
    public Problem() {
        this(DEFAULT_ID);
    }

    /**
     * Instantiate a Dynamic Programming problem with the given id.
     *
     * @param id unique int id of this problem, as assigned by the DB.
     */
    public Problem(int id) {
        super(id);

        variables = new HashMap<>();
        codeStatements = new ArrayList<>();
        backtrackingCodeStatements = new ArrayList<>();
        executionHistory = new ArrayList<>();
        problemListeners = new ArrayList<>();
    }

    public int getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(int subTypeId) {
        this.subTypeId = subTypeId;
    }

    public ArrayList<String> getCodeStatements() {
        return codeStatements;
    }

    public void setCodeStatements(ArrayList<String> codeStatements) {
        this.codeStatements = codeStatements;
    }

    public ArrayList<String> getBacktrackingCodeStatements() {
        return backtrackingCodeStatements;
    }

    public void setBacktrackingCodeStatements(ArrayList<String> backtrackingStatements) {
        this.backtrackingCodeStatements = backtrackingStatements;
    }

    public int getCurrentLineNumber() {
        return nextLineNumber;
    }

    public int getBacktrackingStartNum() {
        return BACKTRACKING_START_NUM;
    }

    public void setCurrentLineNumber(int currentLineNumber) {
        this.nextLineNumber = currentLineNumber;
    }

    public String getTableVariable() {
        return tableVariable;
    }

    public void setTableVariable(String tableVariable) {
        this.tableVariable = tableVariable;
    }

    public ArrayList<String> getVariableNames() {
        return new ArrayList<String>(variables.keySet());
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
     * Returns the raw Object value for a variable. Use this for non-int variables like the DP
     * table.
     *
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

    /** Execute the current line of code and then update to the "next" line of code to execute. */
    public void step() {
        executionHistory.add(nextLineNumber);
        String methodName = "executeLine" + nextLineNumber;
        executeMethod(methodName);

        // Notify listeners that the problem has been updated
        notifyProblemListeners();
    }

    /**
     * Execute the next n statements (forward). Will take a brief pause between steps so the user
     * can follow
     *
     * @param n number of steps to execute
     */
    public void step(int n) {
        final int[] count = {0};
        javax.swing.Timer timer =
                new javax.swing.Timer(
                        RUN_STEP_INTERVAL,
                        e -> {
                            step();
                            count[0]++;

                            if (hasFinished() || count[0] >= n) {
                                ((javax.swing.Timer) e.getSource()).stop();
                                // Notify listeners that we've finished running steps
                                notifyProblemListeners();
                            }
                        });
        timer.start();
    }

    /** Take one step backward in the algorithm by undoing the */
    public void undo() {
        int size = executionHistory.size();

        if (size == 0) {
            Problem.julLogger.log(java.util.logging.Level.WARNING, "Cannot undo past Line 0");

        } else {
            int lastItemPos = size - 1;

            int previousLineNumber = executionHistory.remove(lastItemPos);

            String methodName = "undoLine" + previousLineNumber;
            executeMethod(methodName);

            /* Whatever line we removed from the history, that's the line that
            we want to execute again if we click "step forward" */
            nextLineNumber = previousLineNumber;

            // Notify listeners that the problem has been updated
            notifyProblemListeners();
        }
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
        // wildcard the generic to avoid build warnings
        Class<?> clazz = this.getClass();

        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(this);

        } catch (NoSuchMethodException ex) {
            Problem.julLogger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Problem.julLogger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Problem.julLogger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Problem.julLogger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Problem.julLogger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * Basic method that each problem will Override to handle decoding its specific problem from a
     * SubproblemTableView table cell location to a line number.
     *
     * <p>Note: Similar to notifyProblemListener, this could be effective for both highlighting
     * problems. If the step() methods has information about what cell is currently being computed,
     * this decoder would have the correct line number if the user clicked it, or have the correct
     * line number based on what step we are on.
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
     * Notifies each problemListener in the problemListeners array list. Passes itself to each.
     *
     * <p>Note: When considering how to handle step interaction AND click interaction, potentially
     * this could work for both. Since it is simply passing itself, we could leave it to the views
     * to handle changes for either. So in LCSProblem, we would call this at the end of the step
     * methods or the clickListener in SubproblemTableView.
     */
    protected void notifyProblemListeners() {
        for (int i = 0; i < problemListeners.size(); i++) {
            problemListeners.get(i).problemUpdated(this);
        }
    }
}
