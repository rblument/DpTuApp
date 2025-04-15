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

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A panel containing buttons and controls that allow a user to step forward
 * and backward through the LCS algorithm execution.
 *
 * @author [your name]
 */
public class StepViewPanel extends GPanel implements ActionListener, ChangeListener, ProblemListener {
    
    /**
     * The problem model that this view controls.
     */
    private Problem model;
    
    /**
     * Button to step back one in algorithm execution
     */
    private JButton stepBackButton;
    
    /**
     * Button to step forward one in algorithm execution
     */
    private JButton stepForwardButton;
    
    /**
     * Button to run multiple steps at once
     */
    private JButton runStepsButton;
    
    /**
     * Button to reset the algorithm to its initial state
     */
    private JButton resetButton;
    
    /**
     * Spinner that allows selection of number of steps to execute
     */
    private JSpinner stepsSpinner;
    
    /**
     * Label showing current execution state
     */
    private JLabel statusLabel;
    
    /**
     * Constant for background color
     */
    private static final Color PANEL_BACKGROUND = new Color(240, 240, 240);
    
    /**
     * Initialize this view including creating and laying out its child components.
     */
    public StepViewPanel() {
        initializeComponents();
        layoutComponents();
    }
    
    /**
     * Initialize this view with a specific problem model.
     * 
     * @param problem The LCS problem to control
     */
    public StepViewPanel(Problem problem) {
        this();
        setModel(problem);
    }
    
    /**
     * Returns the model currently displayed in this view.
     *
     * @return a Problem model
     */
    public Problem getModel() {
        return model;
    }

    /**
     * Display the given model in this view.
     *
     * @param model a Problem model
     */
    public void setModel(Problem model) {
        // If we already had a model, remove this view as a listener
        if (this.model != null) {
            this.model.getProblemListeners().remove(this);
        }
        
        this.model = model;
        
        // Add this view as a listener to the new model
        if (this.model != null) {
            this.model.addProblemListener(this);
        }
        
        updateView();
    }
    
    /**
     * Create the child GUI components appearing in this panel.
     */
    private void initializeComponents() {
        // Set the panel appearance
        setBorder(BorderFactory.createTitledBorder("Algorithm Control"));
        setBackground(PANEL_BACKGROUND);
        
        // Step back button
        stepBackButton = new JButton("Step Back");
        stepBackButton.setToolTipText("Go back one step in the algorithm");
        stepBackButton.addActionListener(this);
        stepBackButton.setEnabled(false); // Initially disabled until algorithm starts
        
        // Step forward button
        stepForwardButton = new JButton("Step Forward");
        stepForwardButton.setToolTipText("Advance one step in the algorithm");
        stepForwardButton.addActionListener(this);
        
        // Run steps button
        runStepsButton = new JButton("Run Steps");
        runStepsButton.setToolTipText("Run multiple steps at once");
        runStepsButton.addActionListener(this);
        
        // Reset button
        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Reset algorithm to initial state");
        resetButton.addActionListener(this);
        
        // Steps spinner for selecting multiple steps
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        stepsSpinner = new JSpinner(spinnerModel);
        stepsSpinner.setPreferredSize(new Dimension(60, 25));
        
        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    }
    
    /**
     * Layout the child components in this panel.
     */
    private void layoutComponents() {
        // Create container for step spinner and its label
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        spinnerPanel.setBackground(PANEL_BACKGROUND);
        spinnerPanel.add(new JLabel("Steps:"));
        spinnerPanel.add(stepsSpinner);
        
        // Add components with the GPanel's addc helper method
        addc(stepBackButton, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(stepForwardButton, 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, 
                5, 5, 5, 5);
        
        addc(spinnerPanel, 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(runStepsButton, 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(resetButton, 4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(statusLabel, 5, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                5, 15, 5, 5);
    }
    
    /**
     * Handle action events from buttons.
     * 
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (model == null) {
            return;
        }
        
        Object source = e.getSource();
        
        if (source == stepBackButton) {
            model.undo();
        } else if (source == stepForwardButton) {
            model.step();
        } else if (source == runStepsButton) {
            int steps = (Integer) stepsSpinner.getValue();
            model.step(steps);
        } else if (source == resetButton) {
            model.reset();
        }
    }
    
    /**
     * Handle change events from the steps spinner.
     * 
     * @param e the change event
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        // Currently not needed, but could be used for spinner validation
    }
    
    /**
     * Update the panel based on the current state of the problem model.
     */
    private void updateView() {
        if (model == null) {
            stepBackButton.setEnabled(false);
            stepForwardButton.setEnabled(false);
            runStepsButton.setEnabled(false);
            resetButton.setEnabled(false);
            statusLabel.setText("No model loaded");
            return;
        }
        
        // Enable step back button if we've stepped forward at least once
        stepBackButton.setEnabled(model.getCurrentLineNumber() > 0);
        
        // Update status label with current line number
        statusLabel.setText("Current line: " + model.getCurrentLineNumber());
    }
    
    /**
     * Callback for when the problem model is updated.
     * 
     * @param problem The updated problem
     */
    @Override
    public void problemUpdated(Problem problem) {
        updateView();
    }
}