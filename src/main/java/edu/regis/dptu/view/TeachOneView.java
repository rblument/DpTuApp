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

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.util.ResourceMgr;

/**
 * The top-level view for a Teach One tutoring session.
 *
 * <p>In Teach One mode the student explains a worked example back to the tutor. This view presents
 * the problem statement and a step-by-step explanation panel so the student can walk through the
 * solution and receive feedback on each step.
 *
 * <p>The layout mirrors {@link SeeOneView} (problem input + step panel), but interaction is
 * student-driven: the student narrates each step rather than observing the tutor's narration.
 *
 * @author Cormac Moss
 */
public class TeachOneView extends GPanel implements ModeView {
    private static final Logger log = LoggerFactory.getLogger(TeachOneView.class);

    /** Displays the problem definition (strings, matrix dimensions, etc.). */
    private ProblemInputView problemInputView;

    /** Displays the current step prompt and accepts the student's explanation. */
    private StepViewPanel stepViewPanel;

    /** Shows the DP table so the student can reference it while explaining. */
    private SubproblemTableView tableView;

    /** Placeholder label shown until a richer explanation input widget is built. */
    private JLabel explanationPromptLabel;

    public TeachOneView() {
        log.info("Initializing TeachOneView");
        initializeComponents();
        layoutComponents();
    }

    /**
     * Binds a {@link Problem} to every child view so they all reflect the same problem state.
     *
     * @param currentProblem the problem the student is explaining
     */
    @Override
    public void setModel(Problem currentProblem) {
        log.info("TeachOneView setting model: problemId={}", currentProblem.getId());
        tableView.setModel(currentProblem);
        stepViewPanel.setModel(currentProblem);
        updateView(currentProblem);
    }

    /**
     * Refreshes the view after a model change.
     *
     * @param currentProblem the updated problem
     */
    public void updateView(Problem currentProblem) {
        log.info("TeachOneView updating view");
        revalidate();
        repaint();
    }

    /**
     * Switches between the standard code view and the backtracking code view.
     *
     * <p>Teach One does not currently use a code view, so this is a no-op kept to satisfy the
     * {@link ModeView} contract. A code view can be wired in here in a future sprint.
     *
     * @param backtrackingOn ignored
     */
    @Override
    public void showBacktrackingPanel(boolean backtrackingOn) {
        log.debug(
                "TeachOneView.showBacktrackingPanel called (no-op): backtrackingOn={}",
                backtrackingOn);
    }

    private void initializeComponents() {
        log.info("TeachOneView initializing components");

        problemInputView =
                new ProblemInputView(
                        problem -> {
                            tableView.setModel(problem);
                            stepViewPanel.setModel(problem);
                        });

        tableView = new SubproblemTableView();
        stepViewPanel = new StepViewPanel();
        explanationPromptLabel =
                new JLabel(ResourceMgr.instance().string("teachOne.explanationPrompt"));
    }

    private void layoutComponents() {
        // Row 0: problem input spans the full width
        addc(
                problemInputView,
                0,
                0,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        // Row 1 left: DP table for reference
        addc(
                tableView,
                0,
                1,
                1,
                1,
                0.5,
                1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                5,
                5,
                5,
                5);

        // Row 1 right: explanation prompt
        addc(
                explanationPromptLabel,
                1,
                1,
                1,
                1,
                0.5,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        // Row 2: step panel spans the full width
        addc(
                stepViewPanel,
                0,
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
                5);
    }
}
