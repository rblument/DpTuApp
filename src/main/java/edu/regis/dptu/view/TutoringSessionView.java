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

import java.awt.GridBagConstraints;
import java.util.logging.Level;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.TutoringSession;

/**
 * Displays a tutoring session (the top-level GUI view for the application). Integrates views and
 * ensures the shared Problem model is distributed correctly. Includes Debug Prints.
 *
 * @author rickb (Modified by Assistant for Functional Integration & Debug)
 */
public class TutoringSessionView extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(TutoringSessionView.class);

    /** The logger for the class */
    private static final java.util.logging.Logger julLogger =
            java.util.logging.Logger.getLogger(TutoringSessionView.class.getName());

    private TutoringSession model;
    private VariablesView variablesView;
    private JLabel subproblemView;
    private CodeView codeView;
    private BacktrackingCodeView backtrackingCodeView;

    private ProblemInputView problemInputView;
    private SubSequenceView subSeqView;

    private SubproblemTableView tableView;
    private StepViewPanel stepViewPanel;

    // ...

    /**
     * Initialize this view including creating and laying out its child components. Uses original
     * layout.
     */
    public TutoringSessionView() {
        TutoringSessionView.julLogger.log(Level.INFO, "Initiating TutoringSessionView");
        initializeComponents(); // Creates components and sets up model sharing
        layoutComponents(); // Uses original layout constraints
    }

    // ... (getModel, getTableView, stubs remain the same) ...
    public TutoringSession getModel() {
        return model;
    }

    public CodeView getCodeView() {
        return codeView;
    }

    public BacktrackingCodeView getBacktrackingCodeView() {
        return backtrackingCodeView;
    }

    public SubproblemTableView getTableView() {
        return tableView;
    }

    public StepCompletionView getStepCompletionView() {
        return null;
    }

    // public StepSelectorView getStepSelectorView() { return null; }

    /**
     * This is a stub method that will be implemented later when we add StepSelectorView. For now,
     * it returns null to prevent compilation errors.
     *
     * @return null for now
     */
    public StepSelectorView getStepSelectorView() {
        // Will be implemented later
        return null;
    }

    public StepViewPanel getStepViewPanel() {
        return stepViewPanel;
    }

    public SubSequenceView getSubSeqView() {
        return subSeqView;
    }

    public VariablesView getVariablesView() {
        return variablesView;
    }

    /**
     * Set the table view. (Original functionality preserved) Returns the current SubSequenceView
     * instance
     *
     * <p>Changes (April 17, 2025): Exposed SubSequenceView through a getter to allow dynamic
     * updates (Updating input strings based on user input from InputViews)
     *
     * @param tableView
     */
    public void setTableView(SubproblemTableView tableView) {
        if (this.tableView != null) {
            remove(this.tableView);
        }
        this.tableView = tableView;
        addc(
                tableView,
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        revalidate();
        repaint();
    }

    /**
     * Create the child GUI components appearing in this frame. FUNCTIONAL CHANGE: Ensures all
     * relevant views get the *same* Problem model instance.
     */
    private void initializeComponents() {
        TutoringSessionView.julLogger.log(
                Level.INFO, "TutoringSessionView initializing components");
        variablesView = new VariablesView();
        subproblemView = new JLabel("Subproblem View");

        problemInputView =
                new ProblemInputView(
                        problem -> {
                            tableView.setModel(problem);
                            subSeqView.setModel(problem);
                            stepViewPanel.setModel(problem);
                            codeView.setModel(problem);
                            variablesView.setModel(problem);
                        });

        subSeqView = new SubSequenceView(); // Original init
        tableView = new SubproblemTableView();
        codeView = new CodeView(); // Original init
        backtrackingCodeView = new BacktrackingCodeView();
        stepViewPanel = new StepViewPanel();

        // We'll add these components later
        // stepCompletionView = new StepCompletionView();
        // stepSelectorView = new StepSelectorView();
    }

    /** Layout the child components in this view using **ORIGINAL** constraints. */
    private void layoutComponents() {
        // TODO: Consider dynamic layout switching to avoid LCS-specific views showing for non-LCS
        // problems.

        addc(
                problemInputView,
                0,
                0,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        addc(
                codeView,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        addc(
                backtrackingCodeView,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        // ToDo: Is tableView and subproblemView trying to do the same thing?
        addc(
                tableView,
                1,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5); // Original
        addc(
                subproblemView,
                2,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.VERTICAL,
                5,
                5,
                5,
                5); // Original
        addc(
                subSeqView,
                3,
                1,
                1,
                1,
                0.5,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                5,
                5,
                5,
                5); // Original
        // addc(xView, 3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
        // GridBagConstraints.VERTICAL, 5, 5, 5, 5);       // Original
        addc(
                variablesView,
                0,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        addc(
                stepViewPanel,
                1,
                2,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5); // Original

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

    private void updateView(Problem currentProblem) {
        TutoringSessionView.julLogger.log(Level.INFO, "TutoringSessionView updating view");

        problemInputView.setModel(currentProblem);

        revalidate();
        repaint();
    }

    public void setModel(TutoringSession model) {
        this.model = model;

        Problem currentProblem = null;
        if (model != null) currentProblem = model.getProblem();

        subSeqView.setModel(currentProblem);
        stepViewPanel.setModel(currentProblem);
        codeView.setModel(currentProblem);
        backtrackingCodeView.setModel(currentProblem);
        variablesView.setModel(currentProblem);
        tableView.setModel(currentProblem);

        updateView(currentProblem);
    }

    public void showBacktrackingPanel(boolean backtrackingOn) {
        if (backtrackingOn) {
            codeView.setVisible(false);
            backtrackingCodeView.setVisible(true);
        } else {
            backtrackingCodeView.setVisible(false);
            codeView.setVisible(true);
        }
    }
}
