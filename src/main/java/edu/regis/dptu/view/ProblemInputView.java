package edu.regis.dptu.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;

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

    private JPanel activeInputPanel;

    public ProblemInputView() {
        super(new BorderLayout());
        System.out.println("Beginning of problemView, inside constructor");
    }

    public void setModel(Problem problem) {
        System.out.println("Inside setModel()");
        if (problem == null) {
            setNullDummy("No problem selected. Choose a problem to begin.");
            return;
        }

        ProblemKind kind = problem.getType();
        JPanel currentPanel;

        switch (kind) {
            case LCS_PROBLEM:
                System.out.println("Setting currentPanel to LCSInputView");
                currentPanel = new LCSInputView();
                break;
            case MATRIX_CHAIN:
                System.out.println("Setting currentPanel to MatrixInputView");
                currentPanel = new MatrixInputView();
                break;
            case KNAPSACK_0_1:
                System.out.println("Setting currentPanel to KnapsackInputView");
                currentPanel = new KnapsackInputView();
                // TODO: Uncomment and load KnapsackInputView once KnapsackProblem and its view are
                // implemented:
                // KnapsackInputView knapsackInputView = new KnapsackInputView();
                // add(knapsackInputView, BorderLayout.CENTER);
                System.out.println("Knapsack input view not yet implemented.");
                break;
            default:
                currentPanel = nullDummy("Unknwon probelm type: " + kind);
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
