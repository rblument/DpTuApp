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

import edu.regis.dptu.model.CodeModel;
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
    private JLabel subproblemView;
    private JLabel xView;
    private CodeView codeView;
    private SubproblemTableView tableView;
    
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

        updateView();
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

    /**
     * Set the table view.
     *
     * @param tableView the new SubproblemTableView
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
        subproblemView = new JLabel("Subproblem View");
        xView = new JLabel("X View");
        codeView = new CodeView();
        tableView = new SubproblemTableView("skullandbones", "lullabybabies");
        
        // We'll add these components later
        // stepCompletionView = new StepCompletionView();
        // stepSelectorView = new StepSelectorView();
    }

    /**
     * Layout the child components in this view
     */
    private void layoutComponents() {
        addc(variablesView, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        addc(codeView, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        addc(tableView, 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        addc(subproblemView, 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
                5, 5, 5, 5);
        addc(xView, 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
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
            return;
        }
        
        // Update the code view with the current model
        if (model.getProblem() != null) {
            // Assuming CodeModel can be updated with the problem
            codeView.setModel(new CodeModel());
        }
        
        // Update the variable view with the problem
        if (model.getProblem() != null) {
            variablesView.setModel(model.getProblem());
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