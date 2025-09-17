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

import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.aol.AssessmentLevel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * A view that displays a list of available steps for the student to select.
 * Steps can be marked with different status indicators (not started, in progress, completed).
 */
public class StepSelectorView extends GPanel {
    
    /**
     * Enum representing the different step selections available.
     */
    public enum StepSelection {
        INIT_FIRST_ROW("Initialize First Row", StepSubType.COMPLETE_FIRST_ROW),
        INIT_FIRST_COL("Initialize First Column", StepSubType.COMPLETE_FIRST_COL),
        GENERAL_CELL("Complete Cell", StepSubType.COMPLETE_CELL),
        MATCH_CELL("Increase Diagonal", StepSubType.INCREASE_DIAGONAL),
        USE_LEFT_CELL("Use Left Value", StepSubType.USE_LEFT),
        USE_UPPER_CELL("Use Upper Value", StepSubType.USE_UPPER);
        
        private final String displayName;
        private final StepSubType stepType;
        private JLabel label;
        
        StepSelection(String displayName, StepSubType stepType) {
            this.displayName = displayName;
            this.stepType = stepType;
        }
        
        public String getDisplayName() {
            return displayName;
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
    
    /**
     * Initialize this step selector view.
     */
    public StepSelectorView() {
        stepAssessmentLevels = new HashMap<>();
        initializeComponents();
        layoutComponents();
    }
    
    /**
     * Set the current task and update the view to show its steps.
     * 
     * @param task The task containing steps to display
     */
    public void setTask(Task task) {
        this.currentTask = task;
        updateView();
    }
    
    /**
     * Set the assessment level for a step.
     * 
     * @param stepType The step sub-type
     * @param level The assessment level
     */
    public void setStepAssessmentLevel(StepSubType stepType, AssessmentLevel level) {
        stepAssessmentLevels.put(stepType.toString(), level.title());
        updateView();
    }
    
    /**
     * Select a step in the UI.
     * 
     * @param selection The step selection to highlight
     */
    public void selectStep(StepSelection selection) {
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
    }
    
    /**
     * Create the child GUI components appearing in this view.
     */
    private void initializeComponents() {
        titleLabel = new JLabel("DP Steps");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        
        stepsPanel = new JPanel();
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
        stepsPanel.setBackground(Color.WHITE);
    }
    
    /**
     * Layout the components in this view.
     */
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
    
    /**
     * Update the view based on the current task.
     */
    private void updateView() {
        stepsPanel.removeAll();
        
        if (currentTask == null) {
            // Add placeholder text when no task is available
            JLabel placeholder = new JLabel("No steps available");
            placeholder.setAlignmentX(Component.LEFT_ALIGNMENT);
            stepsPanel.add(placeholder);
            stepsPanel.revalidate();
            stepsPanel.repaint();
            return;
        }
        
        // Set the title based on the task
        titleLabel.setText(currentTask.getTitle() + " Steps");
        
        // Create labels for step selections
        for (StepSelection selection : StepSelection.values()) {
            JLabel stepLabel = createStepLabel(selection);
            selection.setLabel(stepLabel);
            stepsPanel.add(stepLabel);
            stepsPanel.add(Box.createVerticalStrut(5)); // Add some spacing
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
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add status suffix based on assessment level
        String status = stepAssessmentLevels.get(selection.getStepType().toString());
        if (status != null) {
            if (status.equals(AssessmentLevel.NOT_STARTED.title())) {
                label.setText(label.getText() + " (See One)");
            } else if (status.equals(AssessmentLevel.IN_PROGRESS.title())) {
                label.setText(label.getText() + " (Do One)");
            } else if (status.equals(AssessmentLevel.COMPLETED.title())) {
                label.setText(label.getText() + " ✅");
            }
        }
        
        // Add click listener
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Get the main frame view and tell it to display this step
                TutoringSessionView tsView = MainFrame.instance().getView();
                StepCompletionView scView = tsView.getStepCompletionView();
                
                // Update selection highlighting
                selectStep(selection);
                
                // Show the appropriate view for this step type
                scView.selectStepView(selection.getStepType());
                
                // Find the corresponding step in the task
                if (currentTask != null) {
                    for (Step step : currentTask.getSteps()) {
                        if (step.getSubType() == selection.getStepType()) {
                            scView.setStep(step);
                            break;
                        }
                    }
                }
            }
            
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
        });
        
        return label;
    }
}