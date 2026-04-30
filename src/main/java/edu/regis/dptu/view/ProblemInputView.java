package edu.regis.dptu.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ProblemListener;
import edu.regis.dptu.model.KnapsackProblem;

/**
 * Displays the appropriate input view depending on the selected problem type.
 *
 * <p>Updated April 30, 2025: - Dynamically loads the correct input panel based on the ProblemKind.
 * - Supports LCSInputView and MatrixInputView. - KnapsackInputView is scaffolded for future use. -
 * Added null model fallback to avoid initialization errors when model not yet set.
 *
 * @author EverettCV
 */
public class ProblemInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ProblemInputView.class);

    private ProblemListener submitListener;

    private JPanel activeInputPanel;

    public ProblemInputView(ProblemListener listener) {
        super(new BorderLayout());
        submitListener = listener;
        ProblemInputView.log.info("Initializing ProblemInputView");
    }

    public void setModel(Problem problem) {
        if (problem == null) {
            throw new NullPointerException("ProblemInputView: problem is null when setting Model");
        }

        ProblemKind kind = problem.getType();
        JPanel currentPanel;

        switch (kind) {
            case LCS_PROBLEM:
                ProblemInputView.log.info("Setting currentPanel to LCSInputView");
                LCSProblem lcsProblem = (LCSProblem) problem;
                LCSInputView lcsInputView = new LCSInputView(submitListener);
                lcsInputView.setDefaultStrings(lcsProblem.getX(), lcsProblem.getY());
                currentPanel = lcsInputView;
                break;
            case MATRIX_CHAIN:
                ProblemInputView.log.info("Setting currentPanel to MatrixInputView");
                currentPanel = new MatrixInputView();
                break;
            case KNAPSACK_0_1:
                ProblemInputView.log.info("Setting currentPanel to KnapsackInputView");
                
                KnapsackProblem knapsackProblem = (KnapsackProblem) problem;
                KnapsackInputView knapsackInputView = new KnapsackInputView(submitListener);
                knapsackInputView.setDefaultItems(
                        knapsackProblem.getNames(),
                        knapsackProblem.getWeights(),
                        knapsackProblem.getValues(),
                        knapsackProblem.getCapacity());
                
                
                currentPanel = new KnapsackInputView();
                
                
                
                
                break;
            default:
                throw new IllegalArgumentException(
                        "ProblemInputView: unsupported problem kind: " + kind);
        }

        swapView(currentPanel);
    }

    public LCSInputView getLcsInputView() {
        return (activeInputPanel instanceof LCSInputView) ? (LCSInputView) activeInputPanel : null;
    }
    
    public KnapsackInputView getKnapsackInputView(){
        return (activeInputPanel instanceof KnapsackInputView)
                ? (KnapsackInputView) activeInputPanel
                : null;
    }

    private void swapView(JPanel currentView) {
        if (activeInputPanel != null) remove(activeInputPanel);
        activeInputPanel = currentView;
        add(activeInputPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
