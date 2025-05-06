package edu.regis.dptu.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import edu.regis.dptu.model.TaskKind;
import edu.regis.dptu.model.Problem;

/**
 * Displays the appropriate input view depending on the selected problem type.
 * 
 * Updated April 30, 2025:
 * - Dynamically loads the correct input panel based on the Problem's TaskKind.
 * - Supports LCSInputView and MatrixInputView.
 * - KnapsackInputView is scaffolded for future use.
 * - Added null model fallback to avoid initialization errors when model not yet set.
 * 
 * @author EverettCV
 */
public class ProblemInputView extends JPanel {

    private TutoringSessionView parentView;

    public ProblemInputView(TutoringSessionView parentView) {
        this.parentView = parentView;
        setLayout(new BorderLayout());

        // Safely get the problem from the parent model (can be null during early initialization)
        Problem problem = (parentView.getModel() != null) ? parentView.getModel().getProblem() : null;

        // If no model/problem exists yet, fallback to LCSInputView by default
        if (problem == null) {
            System.out.println("No TutoringSession or Problem found. Defaulting to LCSInputView.");
            LCSInputView lcsInputView = new LCSInputView(parentView);
            add(lcsInputView, BorderLayout.CENTER);
            return;
        }

        // Otherwise, dynamically load the correct input view based on the Problem's TaskKind
        TaskKind kind = problem.getKind();

        switch (kind) {
            case LCS_PROBLEM:
                LCSInputView lcsInputView = new LCSInputView(parentView);
                add(lcsInputView, BorderLayout.CENTER);
                break;

            case MATRIX_CHAIN:
                MatrixInputView matrixInputView = new MatrixInputView(parentView);
                add(matrixInputView, BorderLayout.CENTER);
                break;

            case KNAPSACK_0_1:
                // TODO: Uncomment and load KnapsackInputView once KnapsackProblem and its view are implemented:
                // KnapsackInputView knapsackInputView = new KnapsackInputView();
                // add(knapsackInputView, BorderLayout.CENTER);
                System.out.println("Knapsack input view not yet implemented.");
                break;

            default:
                System.out.println("Unrecognized problem type. Defaulting to LCS input view.");
                LCSInputView defaultView = new LCSInputView(parentView);
                add(defaultView, BorderLayout.CENTER);
        }
    }
}
