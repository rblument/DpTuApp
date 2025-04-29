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
package edu.regis.dptu.view;

import edu.regis.dptu.model.LCSProblem; // Needed for creating the initial problem
import edu.regis.dptu.model.Problem;   // Needed for type consistency
import edu.regis.dptu.model.TutoringSession;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

/**
 * Displays a tutoring session (the top-level GUI view for the application).
 * Integrates views and ensures the shared Problem model is distributed correctly.
 * Includes Debug Prints.
 *
 * @author rickb (Modified by Assistant for Functional Integration & Debug)
 */
public class TutoringSessionView extends GPanel {

    // ... (fields remain the same) ...
    private TutoringSession model;
    private VariablesView variablesView;
    private JLabel subproblemView;
    private JLabel xView;
    private CodeView codeView;

    private ProblemInputView problemInputView;
    private SubSequenceView subSeqView;

    private SubproblemTableView tableView;
    private StepViewPanel stepViewPanel;
    // ...

    /**
     * Initialize this view including creating and laying out its child
     * components. Uses original layout.
     */
    public TutoringSessionView() {
        System.out.println("DEBUG: TutoringSessionView constructor called.");
        initializeComponents(); // Creates components and sets up model sharing
        layoutComponents();     // Uses original layout constraints
    }

    // ... (getModel, getTableView, stubs remain the same) ...
    public TutoringSession getModel() { return model; }
    public SubproblemTableView getTableView() { return tableView; }
    public StepCompletionView getStepCompletionView() { return null; }
    public StepSelectorView getStepSelectorView() { return null; }

    /**
     * This is a stub method that will be implemented later when we add StepSelectorView.
     * For now, it returns null to prevent compilation errors.
     *
     * @return null for now
     */
    public StepSelectorView getStepSelectorView() {
        // Will be implemented later
        return null;
    }


    public SubSequenceView getSubSeqView() {
        return subSeqView;
    }

    /**
     * Set the table view. (Original functionality preserved)
     * Returns the current SubSequenceView instance
     * 
     * Changes (April 17, 2025):
     * Exposed SubSequenceView through a getter to allow dynamic updates
     * (Updating input strings based on user input from InputViews)
     * 
     * @return the SubSequenceView displayed in the tutoring session
     */
    public void setTableView(SubproblemTableView tableView) {
        if (this.tableView != null) { remove(this.tableView); }
        this.tableView = tableView;
        addc(tableView, 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        revalidate();
        repaint();
    }


    /**
     * Create the child GUI components appearing in this frame.
     * FUNCTIONAL CHANGE: Ensures all relevant views get the *same* Problem model instance.
     */
    private void initializeComponents() {
        System.out.println("DEBUG: TutoringSessionView initializing components...");
        variablesView = new VariablesView();
        subproblemView = new JLabel("Subproblem View"); // Original Placeholder
        xView = new JLabel("X View");                 // Original Placeholder
      
        problemInputView = new ProblemInputView(this);

        String initialX = "skullandbones"; String initialY = "lullabybabies";
        subSeqView = new SubSequenceView(initialX, initialY); // Original init
        tableView = new SubproblemTableView(initialX, initialY);
        codeView = new CodeView(tableView); // Original init

        // *** Create the single, shared Problem instance ***
        Problem sharedProblem = new LCSProblem(initialX, initialY);
        System.out.println("DEBUG: TutoringSessionView created sharedProblem: " + Integer.toHexString(sharedProblem.hashCode()));


        // *** Initialize StepViewPanel with the shared Problem ***
        stepViewPanel = new StepViewPanel(sharedProblem);


        // *** Set the shared Problem model on other views ***
        System.out.println("DEBUG: TutoringSessionView setting model on CodeView...");
        codeView.setModel(sharedProblem);
        System.out.println("DEBUG: TutoringSessionView setting model on VariablesView...");
        variablesView.setModel(sharedProblem);
        System.out.println("DEBUG: TutoringSessionView setting model on SubproblemTableView...");
        // *** Set model on tableView - This now handles listener registration ***
        tableView.setModel(sharedProblem);


        // *** Listener addition REMOVED/COMMENTED OUT here ***
        // if (sharedProblem != null) {
        //    sharedProblem.addProblemListener(tableView); // Now handled by tableView.setModel()
        // }


        // We'll add these components later
        // stepCompletionView = new StepCompletionView();
        // stepSelectorView = new StepSelectorView();
         System.out.println("DEBUG: TutoringSessionView components initialized.");
    }


    /**
     * Layout the child components in this view using **ORIGINAL** constraints.
     */
    private void layoutComponents() {
        addc(problemInputView, 0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,  5, 5, 5); 
        addc(codeView, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5);
        // ToDo: Is tableView and subproblemView trying to do the same thing?
        addc(tableView, 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5); // Original
        addc(subproblemView, 2,1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, 5, 5, 5, 5); // Original
        addc(subSeqView, 3, 1, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 5, 5, 5, 5);     // Original
       // addc(xView, 3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, 5, 5, 5, 5);       // Original
        addc(variablesView, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5);

        addc(stepViewPanel, 1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5); // Original



        // We'll add these components to the layout later
        /*
        addc(stepSelectorView, 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
                5, 5, 5, 5);
        addc(stepCompletionView, 2, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                5, 5, 5, 5);
        */

    }


    /**
     * Display the current model in our child components.
     * **FUNCTIONAL CHANGE:** Ensures all views needing the model receive it.
     */
    private void updateView() {
         System.out.println("DEBUG: TutoringSessionView.updateView called.");
        if (model == null) {
            System.out.println("DEBUG: TutoringSessionView.updateView: Main session model is null. Setting child models to null.");
            // Set models to null if session model is null
            stepViewPanel.setModel(null);
            codeView.setModel(null);
            variablesView.setModel(null);
            tableView.setModel(null);
            return;
        }


        Problem currentProblem = model.getProblem();
        System.out.println("DEBUG: TutoringSessionView.updateView: Current problem from session: " + (currentProblem != null ? Integer.toHexString(currentProblem.hashCode()) : "null"));


        // *** Pass the current problem to all relevant views ***
        stepViewPanel.setModel(currentProblem);
        codeView.setModel(currentProblem);
        variablesView.setModel(currentProblem);
        tableView.setModel(currentProblem); // <<< Ensure tableView gets the model


        // Original commented out updates
        /* if (model.currentTask() != null) { stepSelectorView.setTask(model.currentTask().getTask()); } */


        // Refresh layout after potentially changing models
        revalidate();
        repaint();
    }

    void setModel(TutoringSession model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}