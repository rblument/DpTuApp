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
 *
 * Various aspects of the tutoring session are displayed in the child components
 * of this view.
 *
 * @author rickb
 */
public class TutoringSessionView extends GPanel {

    /**
     * The tutoring session model displayed in this view.
     */
    private TutoringSession model;

    /**
     * Declares each of the views used in the tutorings session view.
     */
    private VariablesView variablesView;
    private JLabel subproblemView; // Assuming these are placeholders as in original
    private JLabel xView;          // Assuming these are placeholders as in original
    private CodeView codeView;

    private ProblemInputView problemInputView;
    private SubSequenceView subSeqView;

    private SubproblemTableView tableView;
    private StepViewPanel stepViewPanel;


    // We need to comment out these for now as they're not ready to be used yet
    // private StepCompletionView stepCompletionView;
    // private StepSelectorView stepSelectorView;

    /**
     * Initialize this view including creating and laying out its child
     * components.
     */
    public TutoringSessionView() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Return the model currently displayed in this view.
     *
     * @return a TutoringSession
     */
    public TutoringSession getModel() {
        return model;
    }

    /**
     * Display the given model in this view.
     *
     * @param model a TutoringSession.
     */
    public void setModel(TutoringSession model) {
        this.model = model;
        updateView(); // Update child views when the main session model changes
    }

    /**
     * Return the table view.
     *
     * @return the SubproblemTableView
     */
    public SubproblemTableView getTableView() {
        return tableView;
    }

    /**
     * This is a stub method that will be implemented later when we add StepCompletionView.
     * For now, it returns null to prevent compilation errors.
     *
     * @return null for now
     */
    public StepCompletionView getStepCompletionView() {
        // Will be implemented later
        return null;
    }

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
        // Remove the old table view if it exists
        if (this.tableView != null) {
            remove(this.tableView);
        }

        // Add the new table view
        this.tableView = tableView;
        addc(tableView, 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);

        // Refresh the layout
        revalidate();
        repaint();
    }

    /**
     * Create the child GUI components appearing in this frame.
     */
    private void initializeComponents() {
        variablesView = new VariablesView();

        subproblemView = new JLabel("Subproblem View"); // Placeholder
        xView = new JLabel("X View");                 // Placeholder
      
        problemInputView = new ProblemInputView(this);

        subSeqView = new SubSequenceView("skullandbones", "lullabybabies"); // Example init

        tableView = new SubproblemTableView("skullandbones", "lullabybabies"); // Example init

        codeView = new CodeView(tableView); // Create CodeView

        // *** FIX: Create the shared Problem instance ***
        // Ideally, this comes from the TutoringSession model later,
        // but for initial setup, create a default one.
        Problem sharedProblem = new LCSProblem("skullandbones", "lullabybabies");

        // *** FIX: Initialize StepViewPanel with the shared Problem ***
        stepViewPanel = new StepViewPanel(sharedProblem);

        // *** FIX: Set the shared Problem model on CodeView ***
        codeView.setModel(sharedProblem);

        // *** FIX: Set the shared Problem model on VariablesView ***
        // Assuming VariablesView also needs the problem model
        variablesView.setModel(sharedProblem); // Need to ensure VariablesView has setModel

        // *** FIX: Add table as listener to the *shared* problem ***
        if (sharedProblem != null) {
           sharedProblem.addProblemListener(tableView);
        }

        // We'll add these components later
        // stepCompletionView = new StepCompletionView();
        // stepSelectorView = new StepSelectorView();
    }

    /**
     * Layout the child components in this view
     */
    private void layoutComponents() {

        // Layout remains largely the same as original
        addc(variablesView, 0, 1, 1, 1, 0.0, 0.0,

        addc(problemInputView, 0, 0, 2, 1, 0.0, 0.0,

                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);                
        addc(codeView, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5); 
        addc(variablesView, 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);   
        addc(tableView, 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);

        addc(subproblemView, 4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
                5, 5, 5, 5);
        addc(xView, 4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
                5, 5, 5, 5);
        addc(subSeqView, 5, 0, 1, 1, 0.5, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                5, 5, 5, 5);
        addc(stepViewPanel, 0, 3, 2, 1, 1.0, 0.0,

                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);

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
     */
    private void updateView() {
        if (model == null) {
            // Optionally disable components or set default states if no session model
            return;
        }

        Problem currentProblem = model.getProblem();

        // *** FIX: Update all relevant views with the current problem ***
        if (currentProblem != null) {
            stepViewPanel.setModel(currentProblem);
            codeView.setModel(currentProblem);
            variablesView.setModel(currentProblem); // Assuming VariablesView needs update

            // No need to explicitly set the model for the table view here,
            // as it's a listener and should update itself via problemUpdated.
        } else {
            // Handle case where the session has no current problem
            // Maybe set models to null or show a default state
            stepViewPanel.setModel(null);
            codeView.setModel(null);
            variablesView.setModel(null);
        }


        // We'll add these updates later
        /*
        // Update the step selector with available steps
        if (model.currentTask() != null) {
            stepSelectorView.setTask(model.currentTask().getTask());
        }
        */
    }
}