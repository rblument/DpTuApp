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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepCompletion;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.view.act.CheckAnswerAction;
import edu.regis.dptu.view.act.NewExampleAction;
import edu.regis.dptu.view.act.RequestHintAction;

/**
 * Base view for step completion interactions, providing common functionality for different step
 * types (cell completion, row initialization, etc.).
 */
public class StepCompletionView extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(StepCompletionView.class);


    // The current step being worked on
    protected Step currentStep;

    // Input field for student answers
    protected JTextField answerField;

    // Buttons for interaction
    protected JButton checkButton;
    protected JButton newExampleButton;
    protected JButton hintButton;

    // Status/hint display
    protected JLabel statusLabel;
    protected JLabel hintLabel;

    // Panel to hold different step type views
    protected JPanel cardPanel;
    protected CardLayout cardLayout;

    /** Initialize this step completion view with default components. */
    public StepCompletionView() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Set the current step being worked on.
     *
     * @param step The step to display and work with
     */
    public void setStep(Step step) {
        this.currentStep = step;
        updateView();
    }

    /**
     * Create a StepCompletion object from the current user input.
     *
     * @return A StepCompletion object containing the user's answer
     */
    public StepCompletion createStepCompletion() {
        if (currentStep == null) {
            return null;
        }

        String answer = answerField.getText();
        StepCompletion completion = new StepCompletion(currentStep, answer);

        // Set appropriate metadata based on step type
        if (currentStep.getHints() != null && !currentStep.getHints().isEmpty()) {
            completion.setHintsGiven(currentStep.getCurrentHintIndex());
        }

        return completion;
    }

    /**
     * Display a hint to the user.
     *
     * @param hintText The hint to display
     */
    public void showHint(String hintText) {
        hintLabel.setText(hintText);
        hintLabel.setVisible(true);
    }

    /**
     * Show a status message to the user (correct/incorrect feedback).
     *
     * @param message The message to display
     * @param isCorrect Whether the answer was correct (affects styling)
     */
    public void showStatus(String message, boolean isCorrect) {
        statusLabel.setText(message);
        statusLabel.setForeground(isCorrect ? new java.awt.Color(0, 128, 0) : java.awt.Color.RED);
        statusLabel.setVisible(true);
    }

    /** Create the child GUI components appearing in this view. */
    private void initializeComponents() {
        // Answer input field
        answerField = new JTextField(10);
        answerField.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Action buttons
        checkButton = new JButton(new CheckAnswerAction("Check Answer"));
        newExampleButton = new JButton(new NewExampleAction("New Example"));
        hintButton = new JButton(new RequestHintAction("Hint"));

        // Status and hint labels
        statusLabel = new JLabel();
        statusLabel.setVisible(false);

        hintLabel = new JLabel();
        hintLabel.setVisible(false);
        hintLabel.setFont(new Font("Dialog", Font.ITALIC, 12));

        // Card panel for different step types
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
    }

    /** Layout the components in this view. */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top section - status/instructions
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.NORTH);
        topPanel.add(hintLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center - card panel with step-specific views
        add(cardPanel, BorderLayout.CENTER);

        // Bottom - controls
        JPanel controlPanel = new JPanel();
        controlPanel.add(answerField);
        controlPanel.add(checkButton);
        controlPanel.add(newExampleButton);
        controlPanel.add(hintButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /** Update the view based on the current step. */
    private void updateView() {
        if (currentStep == null) {
            return;
        }

        // Update UI based on step type
        StepSubType subType = currentStep.getSubType();

        // Select the appropriate card based on step type
        cardLayout.show(cardPanel, subType.toString());

        // Reset UI state
        answerField.setText("");
        statusLabel.setVisible(false);
        hintLabel.setVisible(false);
    }

    /**
     * Select a specific step subtype view.
     *
     * @param subType The step subtype to display
     */
    public void selectStepView(StepSubType subType) {
        cardLayout.show(cardPanel, subType.toString());
    }
}
