package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.util.ResourceMgr;

// DPTU-104: MatrixInputView input field already implemented in 103

/*
 * MatrixInputView lets the user type matrix sizes and add them to a list.
 * Example input: 10x20, 10 x 20, 10,20, or "10 20".
 * The list can be used later to build a MatrixChainProblem.
 */
public class MatrixInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(MatrixInputView.class);

    // Text field where the user types one matrix size (rows and columns)
    private final JTextField matrixField = new JTextField(20);

    // Backing data: each entry is {rows, cols}
    private final List<int[]> matrixDimensions = new ArrayList<>();

    // Swing list to show what the user has added
    private final DefaultListModel<String> matrixListModel = new DefaultListModel<>();
    private final JList<String> matrixList = new JList<>(matrixListModel);

    public MatrixInputView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Label + input field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(ResourceMgr.instance().string("matrixInput.label.entry")), gbc);

        gbc.gridx = 1;
        add(matrixField, gbc);

        // "Add" button
        JButton addButton = new JButton(ResourceMgr.instance().string("matrixInput.button.add"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(addButton, gbc);

        // "Remove" button
        JButton removeButton =
            new JButton(ResourceMgr.instance().string("matrixInput.button.removeSelected"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(removeButton, gbc);

        // List of matrices (scrollable)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(new JScrollPane(matrixList), gbc);

        // Hook up button actions
        addButton.addActionListener(e -> handleAddMatrix());
        removeButton.addActionListener(e -> handleRemoveSelected());
    }

    /** Reads the text field, checks it, and if valid adds a matrix to the list. */
    private void handleAddMatrix() {
        String text = matrixField.getText().trim();
        if (text.isEmpty()) {
            return; // nothing to add
        }

        try {
            int[] dims = parseMatrixDimensions(text);
            int rows = dims[0];
            int cols = dims[1];

            if (rows <= 0 || cols <= 0) {
                showValidationError(ResourceMgr.instance().string("matrixInput.error.positiveWhole"));
                return;
            }

            // Store in internal list
            matrixDimensions.add(new int[] {rows, cols});

            // Store in visible list in a simple format
            String display = rows + " x " + cols;
            matrixListModel.addElement(display);

            // Clear input for the next one
            matrixField.setText("");

            log.debug("Added matrix dimensions: {} x {}", rows, cols);

        } catch (IllegalArgumentException ex) {
            log.warn("Invalid matrix input: {}", text, ex);
            showValidationError(ResourceMgr.instance().string("matrixInput.error.invalidFormat"));
        }
    }

    /** Removes the selected matrix from both the list on screen and the backing list. */
    private void handleRemoveSelected() {
        int index = matrixList.getSelectedIndex();
        if (index >= 0 && index < matrixDimensions.size()) {
            matrixDimensions.remove(index);
            matrixListModel.remove(index);
            log.debug("Removed matrix at index {}", index);
        }
    }

    /** Turns a string like "10x20", "10 20", or "10,20" into two ints: rows and cols. */
    private int[] parseMatrixDimensions(String input) {
        // Split on x, comma, or whitespace
        String[] tokens = input.toLowerCase().split("[x,\\s]+");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Expected two numbers for rows and cols.");
        }

        int rows = Integer.parseInt(tokens[0]);
        int cols = Integer.parseInt(tokens[1]);
        return new int[] {rows, cols};
    }

    /** Shows a simple warning popup if the input is not valid. */
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                ResourceMgr.instance().string("dialog.title.invalidMatrixInput"),
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns a copy of all matrix sizes that have been added so far. Each entry is {rows, cols}.
     */
    public List<int[]> getMatrixDimensions() {
        return new ArrayList<>(matrixDimensions);
    }

    /**
     * Clears everything in this view: text field and list. Can be called when the problem is reset.
     */
    public void clearAll() {
        matrixDimensions.clear();
        matrixListModel.clear();
        matrixField.setText("");
    }
}
