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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * A panel containing buttons and controls that allow a user to step forward and backward through
 * the LCS algorithm execution.
 *
 * @author Shamar Henry
 */
public class StepViewPanel extends GPanel implements ProblemListener {

    /** The problem model that this view controls. */
    private Problem model; // Initialize as null

    /** Button to step back one in algorithm execution */
    private JButton stepBackButton;

    /** Button to step forward one in algorithm execution */
    private JButton stepForwardButton;

    /** Button to run multiple steps at once */
    private JButton runStepsButton;

    /** Button to reset the algorithm to its initial state */
    private JButton resetButton;

    /** Spinner that allows selection of number of steps to execute */
    private JSpinner stepsSpinner;

    /** Label showing current execution state */
    private JLabel statusLabel; // Added in new code, assume it's desired

    /** Constant for background color */
    private static final Color PANEL_BACKGROUND =
            new Color(240, 240, 240); // Default or from new code

    /** Initialize this view including creating and laying out its child components. */
    public StepViewPanel() {
        initializeComponents();
        layoutComponents();
        updateView(); // Initial update for button states
    }

    /**
     * Initialize this view with a specific problem model.
     *
     * @param problem The LCS problem to control
     */
    public StepViewPanel(Problem problem) {
        this(); // Call default constructor to initialize/layout components
        setModel(problem); // Set the initial model
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
        this.model = model;

        // Add this view as a listener to the new model
        if (this.model != null) {
            // Assuming addProblemListener handles duplicates or it's acceptable
            // if the same listener is added multiple times if setModel is called repeatedly
            // with the same model instance.
            this.model.addProblemListener(this);

            setVisible(true);
        } else {
            setVisible(false);
        }

        updateView(); // Update button states based on the new model
    }

    /** Create the child GUI components appearing in this panel. */
    private void initializeComponents() {
        // Set the panel appearance
        setBorder(BorderFactory.createTitledBorder("Algorithm Control"));
        setBackground(PANEL_BACKGROUND);

        // Step back button
        stepBackButton = new JButton("Step Back");
        stepBackButton.setToolTipText("Go back one step in the algorithm");
        stepBackButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model != null) {
                            model.undo();
                            // updateView() will be called via problemUpdated listener
                        }
                    }
                });
        stepBackButton.setEnabled(false); // Initially disabled

        // Step forward button
        stepForwardButton = new JButton("Step Forward");
        stepForwardButton.setToolTipText("Advance one step in the algorithm");
        stepForwardButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model != null) {
                            model.step();
                            // updateView() will be called via problemUpdated listener
                        }
                    }
                });
        stepForwardButton.setEnabled(false); // Initially disabled until model is set

        // Run steps button
        runStepsButton = new JButton("Run Steps");
        runStepsButton.setToolTipText("Run multiple steps at once");
        runStepsButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model != null) {
                            int steps = (Integer) stepsSpinner.getValue();
                            model.step(steps);
                            // updateView() will be called via problemUpdated listener
                        }
                    }
                });
        runStepsButton.setEnabled(false); // Initially disabled

        // Reset button
        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Reset algorithm to initial state");
        resetButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model != null) {
                            model.reset();
                            // updateView() will be called via problemUpdated listener
                        }
                    }
                });
        resetButton.setEnabled(false); // Initially disabled

        // Steps spinner for selecting multiple steps
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1); // Default range
        stepsSpinner = new JSpinner(spinnerModel);
        stepsSpinner.setPreferredSize(new Dimension(60, 25)); // Default size

        // Status label (assuming this was added and is desired)
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12)); // Default style
    }

    /** Layout the child components in this panel. */
    private void layoutComponents() {
        // Create container for step spinner and its label
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        spinnerPanel.setBackground(PANEL_BACKGROUND);
        spinnerPanel.add(new JLabel("Steps:"));
        spinnerPanel.add(stepsSpinner);

        // Add components with the GPanel's addc helper method
        addc(
                stepBackButton,
                0,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                stepForwardButton,
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                spinnerPanel,
                2,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                runStepsButton,
                3,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                resetButton,
                4,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        // Add status label if it's part of the layout
        addc(
                statusLabel,
                5,
                0,
                1,
                1,
                1.0,
                0.0, // Give it remaining horizontal space
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                5,
                15,
                5,
                5); // Align East
    }

    /** Update the panel based on the current state of the problem model. */
    private void updateView() {
        boolean modelExists = (model != null);
        // Check if model exists before accessing its state
        boolean canStepBack = modelExists && model.getCurrentLineNumber() > 0;
        boolean canStepForward =
                modelExists; // Could add a check like !model.isFinished() if available
        boolean canRun = modelExists; // Could add a check like !model.isFinished()
        boolean canReset = modelExists;

        stepBackButton.setEnabled(canStepBack);
        stepForwardButton.setEnabled(canStepForward);
        runStepsButton.setEnabled(canRun);
        resetButton.setEnabled(canReset);
        stepsSpinner.setEnabled(canRun); // Enable spinner when running is possible

        if (modelExists) {
            // Update status label with current line number or other relevant info
            statusLabel.setText("Line: " + model.getCurrentLineNumber()); // Example status
        } else {
            statusLabel.setText("No model loaded");
        }
    }

    /**
     * Callback for when the problem model is updated.
     *
     * @param problem The updated problem
     */
    @Override
    public void problemUpdated(Problem problem) {
        updateView(); // Refresh button states and status label
    }
}
