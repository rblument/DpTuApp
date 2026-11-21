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
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.ProblemListener;
import edu.regis.dptu.model.TransitiveClosureProblem;
import edu.regis.dptu.util.ResourceMgr;

/**
 * New view for a Transitive Closure problem Based off of the LCSInputView and KnapsackInputView
 * classes
 *
 * @author curtis
 */
public class TransitiveClosureInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(TransitiveClosureInputView.class);

    public static final int MIN_VERTICES = 1;
    public static final int MAX_VERTICES = 10;

    private final ProblemListener submitListener;

    private final JSpinner sizeSpinner =
            new JSpinner(new SpinnerNumberModel(4, MIN_VERTICES, MAX_VERTICES, 1));
    private final JTextField labelsField = new JTextField(20);
    private final JPanel matrixPanel = new JPanel();

    private JTextField[][] cells = new JTextField[0][0];

    public TransitiveClosureInputView(ProblemListener listener) {
        super(new BorderLayout(6, 6));
        submitListener = listener;
        log.debug("Initializing TransitiveClosureInputView");

        ResourceMgr res = ResourceMgr.instance();

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel(res.string("transitiveClosureInput.label.size")));
        top.add(sizeSpinner);
        JButton resizeButton = new JButton(res.string("transitiveClosureInput.button.resize"));
        top.add(resizeButton);
        top.add(new JLabel(res.string("transitiveClosureInput.label.labels")));
        top.add(labelsField);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(matrixPanel), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JLabel(res.string("transitiveClosureInput.label.matrix")));
        JButton clearButton = new JButton(res.string("transitiveClosureInput.button.clear"));
        bottom.add(clearButton);
        JButton submitButton = new JButton(res.string("transitiveClosureInput.button.submit"));
        bottom.add(submitButton);
        add(bottom, BorderLayout.SOUTH);

        resizeButton.addActionListener(e -> resizeMatrix((Integer) sizeSpinner.getValue()));
        clearButton.addActionListener(e -> clearCells());
        submitButton.addActionListener(e -> submitInputs());

        resizeMatrix((Integer) sizeSpinner.getValue());
    }

    // Populate the view from the loaded problem
    public void setDefaults(TransitiveClosureProblem problem) {
        int[][] adjacency = problem.getAdjacencyMatrix();
        int n = adjacency.length;

        sizeSpinner.setValue(n);
        resizeMatrix(n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) cells[i][j].setText(String.valueOf(adjacency[i][j]));

        labelsField.setText(String.join(", ", problem.getLabels()));
    }

    // Validate inputs and construct a new problem
    public void submitInputs() {
        ResourceMgr res = ResourceMgr.instance();
        int n = cells.length;

        if ((Integer) sizeSpinner.getValue() != n) {
            showValidationError(res.string("transitiveClosureInput.error.sizeMismatch"));
            return;
        }

        int[][] adjacency = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String text = cells[i][j].getText().trim();
                if (text.isEmpty()) {
                    adjacency[i][j] = 0;
                } else if ("0".equals(text) || "1".equals(text)) {
                    adjacency[i][j] = Integer.parseInt(text);
                } else {
                    showValidationError(res.string("transitiveClosureInput.error.cell", i, j));
                    return;
                }
            }
        }

        String[] labels = parseLabels(n);
        if (labels == null) return;

        TransitiveClosureProblem problem = new TransitiveClosureProblem(adjacency, labels);
        log.debug("Submitting TransitiveClosureProblem: n={}", n);
        submitListener.problemUpdated(problem);
    }

    // Return null if the labels are invalid
    private String[] parseLabels(int n) {
        String text = labelsField.getText().trim();
        if (text.isEmpty()) {
            String[] defaults = new String[n];
            for (int i = 0; i < n; i++) defaults[i] = String.valueOf(i);
            return defaults;
        }

        String[] labels = text.split(",", -1);
        for (int i = 0; i < labels.length; i++) {
            labels[i] = labels[i].trim();
            if (labels[i].isEmpty()) {
                showValidationError(
                        ResourceMgr.instance().string("transitiveClosureInput.error.emptyLabel"));
                return null;
            }
        }

        if (labels.length != n) {
            showValidationError(
                    ResourceMgr.instance()
                            .string("transitiveClosureInput.error.labelCount", labels.length, n));
            return null;
        }

        return labels;
    }

    private void resizeMatrix(int n) {
        JTextField[][] old = cells;
        cells = new JTextField[n][n];

        matrixPanel.removeAll();
        matrixPanel.setLayout(new GridLayout(n + 1, n + 1, 2, 2));

        matrixPanel.add(new JLabel(""));
        for (int j = 0; j < n; j++) matrixPanel.add(new JLabel(String.valueOf(j), JLabel.CENTER));

        for (int i = 0; i < n; i++) {
            matrixPanel.add(new JLabel(String.valueOf(i), JLabel.CENTER));
            for (int j = 0; j < n; j++) {
                JTextField cell = new JTextField(2);
                cell.setHorizontalAlignment(JTextField.CENTER);
                boolean carried = i < old.length && j < old.length;
                cell.setText(carried ? old[i][j].getText() : "0");
                cells[i][j] = cell;
                matrixPanel.add(cell);
            }
        }

        matrixPanel.revalidate();
        matrixPanel.repaint();
        log.debug("Transitive Closure matrix resized to {}x{}", n, n);
    }

    private void clearCells() {
        for (JTextField[] row : cells) for (JTextField cell : row) cell.setText("0");
        labelsField.setText("");
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                ResourceMgr.instance().string("dialog.title.invalidTransitiveClosureInput"),
                JOptionPane.WARNING_MESSAGE);
    }
}
