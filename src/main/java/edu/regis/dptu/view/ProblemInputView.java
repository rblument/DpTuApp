package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.util.logging.Level;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ProblemListener;

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
    private static final Logger log = LoggerFactory.getLogger(ProblemInputView.class);

    /** The logger for the class */
    private static final Logger julLogger = Logger.getLogger(ProblemInputView.class.getName());

    private ProblemListener submitListener;

    private JPanel activeInputPanel;

    public ProblemInputView(ProblemListener listener) {
        super(new BorderLayout());
        submitListener = listener;
        ProblemInputView.julLogger.log(Level.INFO, "Initializing ProblemInputView");
    }

    public void setModel(Problem problem) {
        if (problem == null) {
            throw new NullPointerException("ProblemInputView: problem is null when setting Model");
        }

        ProblemKind kind = problem.getType();
        JPanel currentPanel;

        switch (kind) {
            case LCS_PROBLEM:
                ProblemInputView.julLogger.log(Level.INFO, "Setting currentPanel to LCSInputView");
                currentPanel = new LCSInputView(submitListener);
                break;
            case MATRIX_CHAIN:
                ProblemInputView.julLogger.log(Level.INFO, "Setting currentPanel to MatrixInputView");
                currentPanel = new MatrixInputView();
                break;
            case KNAPSACK_0_1:
                ProblemInputView.julLogger.log(
                        Level.INFO, "Setting currentPanel to KnapsackInputView");
                currentPanel = new KnapsackInputView();
                // TODO: Uncomment and load KnapsackInputView once KnapsackProblem and its view are
                // implemented:
                // KnapsackInputView knapsackInputView = new KnapsackInputView();
                // add(knapsackInputView, BorderLayout.CENTER);
                ProblemInputView.julLogger.log(
                        Level.SEVERE, "Knapsack input view not yet implemented.");
                break;
            default:
                throw new IllegalArgumentException(
                        "ProblemInputView: unsupported problem kind: " + kind);
        }

        swapView(currentPanel);
    }

    private void swapView(JPanel currentView) {
        if (activeInputPanel != null) remove(activeInputPanel);
        activeInputPanel = currentView;
        add(activeInputPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
