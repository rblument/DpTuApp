package edu.regis.dptu.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Displays the appropriate input view depending on the selected problem type.
 *
 * <p>Updated April 30, 2025: - Dynamically loads the correct input panel based on the Problem's
 * TaskKind. - Supports LCSInputView and MatrixInputView. - KnapsackInputView is scaffolded for
 * future use. - Added null model fallback to avoid initialization errors when model not yet set.
 *
 * @author EverettCV
 */
public class ProblemInputView extends JPanel {
    
    private static final Logger LOGGER = Logger.getLogger(ProblemInputView.class.getName());

    private JPanel activeInputPanel;

    public ProblemInputView() {
        super(new BorderLayout());
    }

    public void setModel(Problem problem) {
        if (problem == null) {
            setNullDummy("No problem selected. Choose a problem to begin.");
            return;
        }

        ProblemKind kind = problem.getType();
        JPanel currentPanel;

        switch (kind) {
            case LCS_PROBLEM:
                LOGGER.log(Level.INFO, "Setting currentPanel to LCSInputView");
                currentPanel = new LCSInputView();
                break;
            case MATRIX_CHAIN:
                LOGGER.log(Level.INFO, "Setting currentPanel to MatrixInputView");
                currentPanel = new MatrixInputView();
                break;
            case KNAPSACK_0_1:
                LOGGER.log(Level.INFO, "Setting currentPanel to KnapsackInputView");
                currentPanel = new KnapsackInputView();
                // TODO: Uncomment and load KnapsackInputView once KnapsackProblem and its view are
                // implemented:
                // KnapsackInputView knapsackInputView = new KnapsackInputView();
                // add(knapsackInputView, BorderLayout.CENTER);
                break;
            default:
                currentPanel = nullDummy("Unknown problem type: " + kind);
                break;
        }

        swapView(currentPanel);
    }

    private void setNullDummy(String text) {
        swapView(nullDummy(text));
    }

    private JPanel nullDummy(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(text, JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void swapView(JPanel currentView) {
        if (activeInputPanel != null) remove(activeInputPanel);
        activeInputPanel = currentView;
        add(activeInputPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
