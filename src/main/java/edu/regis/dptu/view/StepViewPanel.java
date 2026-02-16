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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * A panel containing buttons and controls that allow a user to step forward and backward through
 * the LCS algorithm execution.
 *
 * @author Shamar Henry
 */
public class StepViewPanel extends GPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(StepViewPanel.class);

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

    private JButton backtrackButton;

    /** Spinner that allows selection of number of steps to execute */
    private JSpinner stepsSpinner;

    /** Label showing current execution state */
    private JLabel statusLabel;

    /** Constant for background color */
    private static final Color PANEL_BACKGROUND = new Color(240, 240, 240);

    /** Track status label line to avoid unnecessary debug chatter (optional). */
    private int lastDisplayedLine = Integer.MIN_VALUE;

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
        this.model = model;

        if (this.model != null) {
            this.model.addProblemListener(this);
            setVisible(true);

            if (log.isDebugEnabled()) {
                // Guard is intentional: avoid touching domain object getters when DEBUG is off.
                log.debug(
                        "StepViewPanel.setModel: modelId={}, type={}",
                        model.getId(),
                        model.getType());
            }
        } else {
            setVisible(false);
            log.debug("StepViewPanel.setModel(null): view hidden");
        }

        lastDisplayedLine = Integer.MIN_VALUE;
        updateView();
    }

    /** Create the child GUI components appearing in this panel. */
    private void initializeComponents() {
        setBorder(BorderFactory.createTitledBorder("Algorithm Control"));
        setBackground(PANEL_BACKGROUND);

        stepBackButton = new JButton("Step Back");
        stepBackButton.setToolTipText("Go back one step in the algorithm");
        stepBackButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model == null) {
                            log.warn("Step Back clicked with no model loaded");
                            return;
                        }

                        log.debug("Step Back clicked");

                        // When we step back from backtracking alg into LCS alg
                        if (model.undoingBacktrackButton()) {
                            MainFrame.instance().getView().showBacktrackingPanel(false);
                            updateView();
                            log.debug("Exited backtracking panel via Step Back");
                        } else {
                            model.undo();
                        }
                    }
                });
        stepBackButton.setEnabled(true);

        stepForwardButton = new JButton("Step Forward");
        stepForwardButton.setToolTipText("Advance one step in the algorithm");
        stepForwardButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model == null) {
                            log.warn("Step Forward clicked with no model loaded");
                            return;
                        }
                        log.debug("Step Forward clicked");
                        model.step();
                        // updateView() will be called via problemUpdated listener
                    }
                });
        stepForwardButton.setEnabled(false);

        runStepsButton = new JButton("Run Steps");
        runStepsButton.setToolTipText("Run multiple steps at once");
        runStepsButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model == null) {
                            log.warn("Run Steps clicked with no model loaded");
                            return;
                        }
                        int steps = (Integer) stepsSpinner.getValue();
                        log.debug("Run Steps clicked: steps={}", steps);
                        model.step(steps);
                        // updateView() will be called via problemUpdated listener
                    }
                });
        runStepsButton.setEnabled(false);

        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Reset algorithm to initial state");
        resetButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model == null) {
                            log.warn("Reset clicked with no model loaded");
                            return;
                        }
                        log.debug("Reset clicked");
                        model.reset();
                        MainFrame.instance().getView().showBacktrackingPanel(false);
                        // updateView() will be called via problemUpdated listener
                    }
                });
        resetButton.setEnabled(false);

        backtrackButton = new JButton("Backtrack");
        backtrackButton.setToolTipText("Find the problem solution");
        backtrackButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (model == null) {
                            log.warn("Backtrack clicked with no model loaded");
                            return;
                        }
                        log.debug("Backtrack clicked");
                        MainFrame.instance().getView().showBacktrackingPanel(true);
                        model.backtrackingOn();
                    }
                });
        backtrackButton.setEnabled(false);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        stepsSpinner = new JSpinner(spinnerModel);
        stepsSpinner.setPreferredSize(new Dimension(60, 25));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
    }

    /** Layout the child components in this panel. */
    private void layoutComponents() {
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        spinnerPanel.setBackground(PANEL_BACKGROUND);
        spinnerPanel.add(new JLabel("Steps:"));
        spinnerPanel.add(stepsSpinner);

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
        addc(
                backtrackButton,
                5,
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
                statusLabel,
                6,
                0,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.EAST,
                GridBagConstraints.NONE,
                5,
                15,
                5,
                5);
    }

    /** Update the panel based on the current state of the problem model. */
    private void updateView() {
        boolean modelExists = (model != null);

        boolean canStepBack = modelExists && model.canStepBack();
        boolean canStepForward = modelExists && !model.hasFinished();
        boolean canRun = modelExists && !model.hasFinished();
        boolean canReset = modelExists;
        boolean canBacktrack = modelExists && model.backtrackReady();

        stepBackButton.setEnabled(canStepBack);
        stepForwardButton.setEnabled(canStepForward);
        runStepsButton.setEnabled(canRun);
        resetButton.setEnabled(canReset);
        stepsSpinner.setEnabled(canRun);
        backtrackButton.setEnabled(canBacktrack);

        if (!modelExists) {
            statusLabel.setText("No model loaded");
            lastDisplayedLine = Integer.MIN_VALUE;
            return;
        }

        if (model.hasFinished()) {
            statusLabel.setText("Finished!");
            lastDisplayedLine = Integer.MIN_VALUE;
            return;
        }

        // Guard only if DEBUG is enabled (formatting work + getter calls)
        int displayNum = (model.getNextLineNumber() % model.getBacktrackingStartNum()) + 1;
        statusLabel.setText(" Line: " + String.format("%2d", displayNum));

        if (log.isDebugEnabled() && displayNum != lastDisplayedLine) {
            log.debug("StepViewPanel status updated: displayLine={}", displayNum);
            lastDisplayedLine = displayNum;
        }
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
