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

import edu.regis.dptu.model.Task;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Displays the dynamic programming JTable. Contains methods for creating, 
 * setting, and getting values within the table.
 *
 * @author samkc
 */
public class TableView extends GPanel {

    private JTable dpTable;
    private DefaultTableModel tableModel;
    private TableColumnModel columnModel;

    private Task model;
    private JPanel panel;

    //The two strings that will be compared in this problem
    private String sideStr;
    private String topStr;

    //the number of columns and rows in the dpTable.
    private int cols;
    private int rows;

    public TableView() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Create the child GUI components appearing in this frame.
     */
    private void initializeComponents() {
        panel = new JPanel();

        //will need to get strings from task instead of being hardcoded
        String compareString1 = "SKULLANDBONES";
        String compareString2 = "LULLABYBABIES";
        createTable(compareString1, compareString2);

        //testTable();
    }

    /**
     * Arrange the components used in this view.
     */
    private void layoutComponents() {
        addc(panel, 0, 0, 100, 100, 1.0, 1.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                5, 5, 5, 5);
    }

    /**
     * Creates a JTable based on two strings.
     *
     * @param compareString1
     * @param compareString2
     */
    public void createTable(String compareString1, String compareString2) {
        if (dpTable != null) {
            //removes a previous table from the panel before creating a new one
            panel.remove(dpTable);
        }

        sideStr = compareString1;
        topStr = compareString2;

        //get number of rows and columns based on string length. 
        rows = sideStr.length() + 3;
        cols = topStr.length() + 3;
        dpTable = new JTable((rows), (cols));

        //create the table
        tableModel = new DefaultTableModel((rows), (cols)) {
            public boolean isCellEditable(int row, int column) {
                if (row == 0 || column == 0) {
                    return false;
                } else if (row == 1 || column == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        dpTable.setModel(tableModel);
        columnModel = dpTable.getColumnModel();

        dpTable.setRowHeight(22);
        //change preferred column width to be smaller
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(42);
        }

        for (int i = 1; i < rows; i++) {
            if (i == 1) {
                setCell(i, i, "Length");
                for (int j = 2; j < rows; j++) {
                    setCell(j, i, (j - 3));
                }
            } else if (i == 2) {

            } else {
                char val = sideStr.charAt(i - 3);
                setCell(i, 0, val);
            }
        }

        for (int i = 1; i < cols; i++) {
            if (i == 1) {
                for (int j = 2; j < cols; j++) {
                    setCell(i, j, (j - 3));
                }
            } else {
                if (i > 2) {
                    char val = topStr.charAt(i - 3);
                    setCell(0, i, val);
                }
            }
        }

        panel.add(dpTable);
    }

    /**
     * Set an individual cell in the dpTable given the row, column, and value.
     *
     * @param row the row number.
     * @param col the column number.
     * @param val the value to be set in the cell.
     */
    public void setCell(int row, int col, Object val) {
        if (((row >= 0) && (col >= 0)) && ((row < rows) && (col < cols))) {
            dpTable.setValueAt(val, row, col);
        }
    }

    /**
     * Get an individual cell in the dpTable given the row and column.
     *
     * @param row the row number.
     * @param col the column number.
     * @return val the value contained within the cell.
     */
    public Object getCell(int row, int col) {
        Object val;

        if (dpTable.getValueAt(row, col) != null) {
            val = dpTable.getValueAt(row, col);
        } else {
            val = " ";
        }

        return val;
    }

    /**
     * Returns a 2D Object Array containing the value for each cell in the
     * dpTable.
     *
     * @return values
     */
    public Object[][] getTableValues() {

        Object[][] values = new Object[rows][cols];

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                values[i][j] = getCell(i, j);
            }
        }

        return values;
    }

    /**
     * Set each cell in the dpTable to the values contained within a 2D Object
     * array.
     *
     * @param values a 2D array containing the table values.
     */
    public void setTableValues(Object[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                setCell(i, j, values[i][j]);
            }
        }
    }

    /**
     * Print a given 2D Object array to the output panel.
     *
     * @param values
     */
    public void printArray2D(Object[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                System.out.printf("%s ", values[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Clear the dpTable of all cell values except the compared strings & length.
     */
    public void clearTable() {
        for (int i = 2; i < rows; i++) {
            for (int j = 2; j < cols; j++) {
                setCell(i, j, "");
            }
        }
    }

    /**
     * Used for testing during development.
     */
    private void testTable() {
        Object[][] array = new Object[rows][cols];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                setCell(i + 2, j + 2, i + j);
            }
        }

        array = getTableValues();
        printArray2D(array);

        clearTable();
        array = getTableValues();
        printArray2D(array);

        String compareString1 = "HELLOWORLD";
        String compareString2 = "HELLOWORLD";
        createTable(compareString1, compareString2);
        array = getTableValues();
        printArray2D(array);
    }
}
