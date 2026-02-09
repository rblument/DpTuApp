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
 * Displays the Code Panel section of the Tutoring Session GUI. This panel will display code
 * statements from the CodeModel.
 *
 * @author cadencea
 */
public class CodeView extends GPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(CodeView.class);

    /**
     * Declares the CodeView model (a Problem object) displayed in this view along with the
     * necessary arrayLists for the code statements.
     */
    private Problem model; // Initialize as null, will be set by setModel

    private ArrayList<String> statementStrings;
    private ArrayList<JLabel> statementJLabels;

    /** Used as a background color */
    private static final Color MEDIUM_GRAY = new Color(215, 215, 215);

    /** Track last highlighted line to avoid repetitive debug spam. */
    private int lastHighlightedLine = -1;

    /** Initialize this view including creating and laying out its child components. */
    public CodeView() {
        // Initialize label list
        statementJLabels = new ArrayList<>();

        // Making it look pretty
        setBorder(BorderFactory.createTitledBorder("Pseudocode"));
        setBackground(MEDIUM_GRAY);

        // Components will be initialized and laid out when setModel is called
    }

    /**
     * Returns the model currently displayed in this view.
     *
     * @return a CodeModel
     */
    public Problem getModel() {
        return model;
    }

    /**
     * Display the given model in this view.
     *
     * @param model a CodeModel.
     */
    public void setModel(Problem model) {
        // NOTE: Cannot remove listener from the old model
        this.model = model;

        // Clear previous UI components
        removeAll();

        if (model == null) {
            lastHighlightedLine = -1;
            setVisible(false);
            if (log.isDebugEnabled()) {
                log.debug("CodeView.setModel(null): view hidden and cleared");
            }
            revalidate();
            repaint();
            return;
        }

        if (log.isDebugEnabled()) {
            // Guard is intentional: these getters may be non-trivial depending on model
            // implementation.
            log.debug("CodeView.setModel: modelId={}, type={}", model.getId(), model.getType());
        }

        // Re-initialize components based on the new model
        initializeComponents();
        layoutComponents();

        // Add listener to the new model if it's not null
        // (Assumes duplicates are handled or acceptable.)
        this.model.addProblemListener(this);

        setVisible(true);

        if (log.isDebugEnabled()) {
            int count = (statementStrings != null) ? statementStrings.size() : 0;
            log.debug("CodeView initialized: statementCount={}", count);
        }

        // Update the view to reflect the initial state of the new model
        updateView();

        // Refresh the panel layout
        revalidate();
        repaint();
    }

    /** Create the child GUI components appearing in this frame. */
    private void initializeComponents() {
        // grabs the statement strings from the model and creates JLabels for each in an ArrayList
        if (model != null) {
            statementStrings = model.getCodeStatements();
            statementJLabels = new ArrayList<>(); // Ensure it's a new list
            for (int i = 0; i < statementStrings.size(); i++) {
                statementJLabels.add(new JLabel(statementStrings.get(i)));
            }
        } else {
            // Handle case where model is null (e.g., clear lists)
            statementStrings = new ArrayList<>();
            statementJLabels = new ArrayList<>();
        }
    }

    /**
     * Layout the child components in this view. Since each line will be below the next we will
     * adjust the 3rd parameter in the addc statement.
     *
     * <p>The loop iterates through the statementJLabels list and adds the component.
     */
    private void layoutComponents() {
        for (int i = 0; i < statementJLabels.size(); i++) {
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
                    statementJLabels.get(i),
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
        if (statementJLabels != null) {
            for (JLabel label : statementJLabels) {
                if (label != null) {
                    label.setBackground(null);
                    label.setOpaque(false);
                }
            }
        }

        // Highlight the current line if model and labels are valid
        if (model != null && statementJLabels != null) {
            int nextLineNumber = model.getNextLineNumber();

            // Debug only when the highlighted line changes (avoids spam on repeated repaints).
            if (nextLineNumber != lastHighlightedLine) {
                if (log.isDebugEnabled()) {
                    log.debug("CodeView highlight: {} -> {}", lastHighlightedLine, nextLineNumber);
                }
                lastHighlightedLine = nextLineNumber;
            }

            if (nextLineNumber >= 0 && nextLineNumber < statementJLabels.size()) {
                JLabel currentLabel = statementJLabels.get(nextLineNumber);
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
     * Takes the updated problem and updated the view to match the model's state.
     *
     * <p>For CodeView this highlights the JLabel with the line currently being used in the model.
     *
     * @param problem The updated problem instance.
     */
    @Override
    public void problemUpdated(Problem problem) {
        // We trust the problem passed IS our model because setModel handles listeners
        updateView(); // Call updateView which uses this.model to get the state
    }
}
