package edu.regis.dptu.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * Displays the appropriate input view depending on the selected problem type.
 * 
 * Changes (April 17, 2025):
 * - Currently only supports LCSInputView because LCS is the only finished problem type.
 * - MatrixInputView and KnapsackInputView are scaffolded as commented-out placeholders
 *   for future development.
 * @author EverettCV
 */
public class ProblemInputView extends JPanel {

    private TutoringSessionView parentView;

    public ProblemInputView(TutoringSessionView parentView) {
        this.parentView = parentView;
        setLayout(new BorderLayout());

        // Currently only load LCSInputView since only LCS problem is ready
        LCSInputView lcsInputView = new LCSInputView(parentView);
        add(lcsInputView, BorderLayout.CENTER);

        // TODO: Uncomment and dynamically load the appropriate input view
        //       once ProblemType selection and switching is implemented.

        // Example placeholders for future input views:
        // MatrixInputView matrixInputView = new MatrixInputView();
        // add(matrixInputView, BorderLayout.CENTER);

        // KnapsackInputView knapsackInputView = new KnapsackInputView();
        // add(knapsackInputView, BorderLayout.CENTER);
    }
}
