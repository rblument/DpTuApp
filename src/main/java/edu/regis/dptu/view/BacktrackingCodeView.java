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
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * @author Gary
 */
public class BacktrackingCodeView extends GPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(BacktrackingCodeView.class);

    /**
     * Declares the BacktrackingCodeView model (a Problem object) displayed in this view along with
     * the necessary arrayLists for the code statements.
     */
    private Problem model; // Initialize as null, will be set by setModel

    private ArrayList<String> backtrackingStatementStrings;
    private ArrayList<JLabel> backtrackingStatementJLabels;

    // Used as a background color
    private static final Color LT_BLUE = new Color(220, 245, 255);

    /** Constructor. */
    public BacktrackingCodeView() {
        backtrackingStatementJLabels = new ArrayList<>();

        // Making it look pretty
        setBorder(BorderFactory.createTitledBorder("Backtracking Code"));
        setBackground(LT_BLUE);

        // Components will be initialized and laid out when setModel is called
    }

    /**
     * Display the given model in this view.
     *
     * @param model a Problem object
     */
    public void setModel(Problem model) {
        // NOTE: Cannot remove listener from the old model
        this.model = model;

        // Clear previous UI components
        removeAll();

        // Re-initialize components based on the new model
        initializeComponents();
        layoutComponents();

        // Add listener to the new model if it's not null
        // Assuming addProblemListener handles duplicates or it's acceptable
        // if the same listener is added multiple times if setModel is called repeatedly
        // with the same model instance (which shouldn't typically happen).
        this.model.addProblemListener(this);
        setVisible(false); // This view won't be used immediately

        // Update the view to reflect the initial state of the new model
        updateView();

        // Refresh the panel layout
        revalidate();
        repaint();
    }

    /**
     * Uses pseudocode statements from the Problem instance to populate a list of JLabels for
     * display onscreen
     */
    private void initializeComponents() {
        backtrackingStatementStrings = model.getBacktrackingCodeStatements();
        backtrackingStatementJLabels = new ArrayList<>(); // Ensure it's a new list
        for (int i = 0; i < backtrackingStatementStrings.size(); i++) {
            backtrackingStatementJLabels.add(new JLabel(backtrackingStatementStrings.get(i)));
        }
    }

    /**
     * Layout the child components in this view. Since each line will be below the next we will
     * adjust the 3rd parameter in the addc statement.
     *
     * <p>The loop iterates through the statementJLabels list and adds the component.
     */
    private void layoutComponents() {
        for (int i = 0; i < backtrackingStatementJLabels.size(); i++) {
            // Line Numbers
            addc(
                    new JLabel(String.valueOf(i + 1)),
                    0,
                    i,
                    1,
                    1,
                    0.0,
                    0.0,
                    GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL,
                    0,
                    1,
                    0,
                    1);
            // Code Statements
            addc(
                    backtrackingStatementJLabels.get(i),
                    1,
                    i,
                    1,
                    1,
                    1.0,
                    0.0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL,
                    0,
                    1,
                    0,
                    1);
        }
    }

    /** Update the view based on the current state of the model (highlighting). */
    private void updateView() {
        // Reset background of all labels first
        if (backtrackingStatementJLabels != null) {
            for (JLabel label : backtrackingStatementJLabels) {
                if (label != null) {
                    label.setBackground(null);
                    label.setOpaque(false);
                }
            }
        }

        // Highlight the current line if model and labels are valid
        if (model != null && backtrackingStatementJLabels != null) {
            int currentLineNumber = model.getCurrentLineNumber() - model.getBacktrackingStartNum();
            if (currentLineNumber >= 0 && currentLineNumber < backtrackingStatementJLabels.size()) {
                JLabel currentLabel = backtrackingStatementJLabels.get(currentLineNumber);
                if (currentLabel != null) {
                    currentLabel.setBackground(Color.YELLOW);
                    currentLabel.setOpaque(true);
                }
            }
        }
        // Refresh the panel
        revalidate();
        repaint();
    }

    /**
     * Takes the updated problem and updated the view to match the model's state. Highlights the
     * JLabel with the line currently being used in the model.
     *
     * @param problem The updated problem instance.
     */
    @Override
    public void problemUpdated(Problem problem) {
        // We trust the problem passed IS our model because setModel handles listeners
        updateView(); // Call updateView which uses this.model to get the state
    }
}
