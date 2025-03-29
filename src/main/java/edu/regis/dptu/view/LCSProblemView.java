package edu.regis.dptu.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import edu.regis.dptu.model.LCSProblem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LCSProblemView extends JPanel {

    private JTextField string1Input;
    private JTextField string2Input;
    private JTable matrixTable;
    private JButton submitButton;
    private LCSProblem lcsProblem;
    private int lastUpdatedRow = -1;
    private int lastUpdatedCol = -1;

    public LCSProblemView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label for instructions
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Enter two strings to compare for LCS:"), gbc);

        // String 1 input field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        string1Input = new JTextField(20);
        add(string1Input, gbc);

        // String 2 input field
        gbc.gridx = 0;
        gbc.gridy = 2;
        string2Input = new JTextField(20);
        add(string2Input, gbc);

        // Submit button
        gbc.gridx = 0;
        gbc.gridy = 3;
        submitButton = new JButton("Submit");
        add(submitButton, gbc);

        // Table for displaying matrix
        matrixTable = new JTable();
        matrixTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        JScrollPane tableScrollPane = new JScrollPane(matrixTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 300));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(tableScrollPane, gbc);

        // Button ActionListener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string1 = string1Input.getText();
                String string2 = string2Input.getText();
                lcsProblem = new LCSProblem(string1, string2);
                
                // Reset the table with the new problem
                updateTable();

                // Using SwingWorker to prevent freezing the UI
                SwingWorker<Void, int[][]> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        while (!lcsProblem.isComplete()) {
                            lcsProblem.step();
                            publish(extractMatrix(lcsProblem)); // Pass the matrix for UI update
                            try {
                                Thread.sleep(200); // Slow down the process for visualization
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<int[][]> matrices) {
                        updateTable();
                    }

                    @Override
                    protected void done() {
                        String result = lcsProblem.getResult();
                        JOptionPane.showMessageDialog(null, "LCS Calculation Complete!\nLCS Result: " + result);
                    }
                };
                worker.execute();
            }
        });
    }

    /**
     * Updates the JTable to display the current state of the matrix.
     */
    private void updateTable() {
        int rows = lcsProblem.getN() + 1;
        int cols = lcsProblem.getM() + 1;

        String[] columnNames = new String[cols + 1];
        columnNames[0] = "";
        for (int j = 1; j <= cols; j++) {
            columnNames[j] = j <= lcsProblem.getY().length() ? String.valueOf(lcsProblem.getY().charAt(j - 1)) : "";
        }

        Object[][] tableData = new Object[rows + 1][cols + 1];
        tableData[0][0] = "";
        
        for (int i = 1; i <= rows; i++) {
            tableData[i][0] = i <= lcsProblem.getX().length() ? String.valueOf(lcsProblem.getX().charAt(i - 1)) : "";
        }

        for (int i = 0; i <= lcsProblem.getN(); i++) {
            for (int j = 0; j <= lcsProblem.getM(); j++) {
                tableData[i + 1][j + 1] = lcsProblem.getValueAt(i, j);
            }
        }

        matrixTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
    }

    /**
     * 
     * @param lcsProblem
     * @return
     * 
     * @author EverettCV
     */
    private int[][] extractMatrix(LCSProblem lcsProblem) {
        int row = lcsProblem.getN() + 1;
        int col = lcsProblem.getM() + 1;

        int[][] matrixCopy = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixCopy[i][j] = lcsProblem.getValueAt(i, j);
            }
        }
        return matrixCopy;
    }

    /**
     * Custom Cell Renderer for highlighting the most recently updated cell.
     */
    private class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row == lastUpdatedRow && column == lastUpdatedCol) {
                cell.setBackground(Color.YELLOW);  // Highlight the updated cell
            } else {
                cell.setBackground(Color.WHITE);  // Normal background color
            }
            return cell;
        }
    }
}
