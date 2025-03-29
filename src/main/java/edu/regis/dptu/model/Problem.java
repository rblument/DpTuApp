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
 * The primary task that a student is attempting to solve, as part of a unit 
 * within a course. 
 * 
 * In VanLehn's sense, this is a task for the student to complete, but we treat
 * tasks at a finer granularity i.e. as subproblems within the primary problem.
 * The tasks within a problem are derived from the specific nature of the
 * problem.
 * 
 * @author rickb
 */
public abstract class Problem extends TitledModel {
    
    protected TaskKind kind;
    
    protected ArrayList codeStatements = new ArrayList();
    protected int currentLineNumber = -1;
    
    protected ArrayList<ProblemListener> problemListeners = new ArrayList();
    
    public Problem() {
        this(DEFAULT_ID);
    }
    
    public Problem(int id) {
        super(id);
    }
    
    public String getTitle() {
        return title;
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

    public ArrayList<ProblemListener> getProblemListeners() {
        return problemListeners;
    }

    public void addProblemListener(ProblemListener listener) {
        problemListeners.add(listener);
    }
    
    /**
     * This method adds all the statements for the codeView to the arrayList.
     * 
     * Note: Hardcoded as of now but can be overridden by specific problem types 
     * in the future.
     */
    protected void loadCodeStatements() {
        
        codeStatements.add("<html><pre>"
                + "<b>for</b> r =0 to n-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   L[r,-1] = 0"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "<b>for</b> c =0 to m-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   L[-1,c] = 0"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "<b>for</b> i =0 to n-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "   <b>for</b> j =0 to m-1 <b>do</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "       <b>if</b> x<sub>i</sub> = y<sub>j</sub> <b>then</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "           L[i, j] = L[i-1, j+1] + 1\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "       <b>else</b>\n"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "           L[i, j] = max(L[i-1,j], L[i, j-1])"
                + "</pre></html>");
        codeStatements.add("<html><pre>"
                + "<b>return</b> array L"
                + "</pre></html>");

    } 
    
    /**
     * Basic method that each problem will Override to handle decoding its 
     * specific problem from a SubproblemTableView table cell location to a 
     * line number.
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
     * Notifies each problemListener in the problemListeners array list. 
     * Passes itself to each.
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

