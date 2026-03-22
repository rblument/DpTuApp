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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.aol.AssessmentLevel;
import edu.regis.dptu.util.ResourceMgr;

/**
 * A view that displays a list of available steps for the student to select. Steps can be marked
 * with different status indicators (not started, in progress, completed).
 */
public class StepSelectorView extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(StepSelectorView.class);

    /** Enum representing the different step selections available. */
    public enum StepSelection {
        INIT_FIRST_ROW("stepSelector.step.initFirstRow", StepSubType.COMPLETE_FIRST_ROW),
        INIT_FIRST_COL("stepSelector.step.initFirstCol", StepSubType.COMPLETE_FIRST_COL),
        GENERAL_CELL("stepSelector.step.generalCell", StepSubType.COMPLETE_CELL),
        MATCH_CELL("stepSelector.step.matchCell", StepSubType.INCREASE_DIAGONAL),
        USE_LEFT_CELL("stepSelector.step.useLeftCell", StepSubType.USE_LEFT),
        USE_UPPER_CELL("stepSelector.step.useUpperCell", StepSubType.USE_UPPER);

        private final String displayKey;
        private final StepSubType stepType;
        private JLabel label;

        StepSelection(String displayKey, StepSubType stepType) {
            this.displayKey = displayKey;
            this.stepType = stepType;
        }

        public String getDisplayName() {
            return ResourceMgr.instance().string(displayKey);
        }

        public StepSubType getStepType() {
            return stepType;
        }

        public JLabel getLabel() {
            return label;
        }

        public void setLabel(JLabel label) {
            this.label = label;
        }
    }

    // Current task
    private Task currentTask;

    // UI Components
    private JPanel stepsPanel;
    private JLabel titleLabel;

    // Track the currently selected step
    private StepSelection currentSelection;

    // Track step assessment levels
    private Map<String, String> stepAssessmentLevels;

    /** Initialize this step selector view. */
    public StepSelectorView() {
        stepAssessmentLevels = new HashMap<>();
        initializeComponents();
        layoutComponents();

        log.debug("StepSelectorView initialized");
    }

    /**
     * Set the current task and update the view to show its steps.
     *
     * @param task The task containing steps to display
     */
    public void setTask(Task task) {
        this.currentTask = task;

        if (task == null) {
            log.debug("StepSelectorView setTask(null)");
        } else {
            // Avoid calling potentially expensive/verbose toString(); log title only.
            log.debug("StepSelectorView setTask: title={}", task.getTitle());
        }

        updateView();
    }

    /**
     * Set the assessment level for a step.
     *
     * @param stepType The step sub-type
     * @param level The assessment level
     */
    public void setStepAssessmentLevel(StepSubType stepType, AssessmentLevel level) {
        String prev = stepAssessmentLevels.put(stepType.toString(), level.title());
        log.debug("Step assessment updated: stepType={}, {} -> {}", stepType, prev, level);

        updateView();
    }

    /**
     * Select a step in the UI.
     *
     * @param selection The step selection to highlight
     */
    public void selectStep(StepSelection selection) {
        StepSelection prev = currentSelection;

        // Deselect the current selection
        if (currentSelection != null && currentSelection.getLabel() != null) {
            currentSelection.getLabel().setBackground(new Color(230, 230, 230));
            currentSelection.getLabel().setForeground(Color.BLACK);
        }

        // Select the new selection
        currentSelection = selection;
        if (currentSelection != null && currentSelection.getLabel() != null) {
            currentSelection.getLabel().setBackground(new Color(100, 149, 237)); // Cornflower blue
            currentSelection.getLabel().setForeground(Color.WHITE);
        }

        log.debug("Step selected: {} -> {}", prev, currentSelection);
    }

    /** Create the child GUI components appearing in this view. */
    private void initializeComponents() {
        titleLabel = new JLabel(ResourceMgr.instance().string("stepSelector.title"));
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 14));

        stepsPanel = new JPanel();
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
        stepsPanel.setBackground(Color.WHITE);
    }

    /** Layout the components in this view. */
    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(stepsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(200, 300));

        add(scrollPane, BorderLayout.CENTER);
    }

    /** Update the view based on the current task. */
    private void updateView() {
        stepsPanel.removeAll();

        if (currentTask == null) {
            JLabel placeholder = new JLabel(ResourceMgr.instance().string("stepSelector.noSteps"));
            placeholder.setAlignmentX(Component.LEFT_ALIGNMENT);
            stepsPanel.add(placeholder);
            stepsPanel.revalidate();
            stepsPanel.repaint();
            return;
        }

        titleLabel.setText(
                ResourceMgr.instance().string("stepSelector.taskTitle", currentTask.getTitle()));

        for (StepSelection selection : StepSelection.values()) {
            JLabel stepLabel = createStepLabel(selection);
            selection.setLabel(stepLabel);
            stepsPanel.add(stepLabel);
            stepsPanel.add(Box.createVerticalStrut(5));
        }

        stepsPanel.revalidate();
        stepsPanel.repaint();
    }

    /**
     * Create a label for a step selection.
     *
     * @param selection The step selection
     * @return The created JLabel
     */
    private JLabel createStepLabel(StepSelection selection) {
        JLabel label = new JLabel(selection.getDisplayName());
        label.setOpaque(true);
        label.setBackground(new Color(230, 230, 230));
        label.setForeground(Color.BLACK);
        label.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        String status = stepAssessmentLevels.get(selection.getStepType().toString());
        if (status != null) {
            if (status.equals(AssessmentLevel.NOT_STARTED.title())) {
                label.setText(label.getText() + " (" + Mode.SEE_ONE.title() + ")");
            } else if (status.equals(AssessmentLevel.IN_PROGRESS.title())) {
                label.setText(label.getText() + " (" + Mode.DO_ONE.title() + ")");
            } else if (status.equals(AssessmentLevel.COMPLETED.title())) {
                label.setText(label.getText() + " ✅");
            }
        }

        label.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (currentSelection != selection) {
                            label.setBackground(new Color(220, 220, 220));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (currentSelection != selection) {
                            label.setBackground(new Color(230, 230, 230));
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // This class doesn't currently wire selection to the rest of the app,
                        // but logging the click is still useful during integration.
                        log.debug("Step label clicked: stepType={}", selection.getStepType());
                        selectStep(selection);
                    }
                });

        return label;
    }
}
