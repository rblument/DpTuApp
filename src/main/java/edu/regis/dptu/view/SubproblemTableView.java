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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ProblemListener;

/**
 * Creates Subproblem Table to view as student works through algorithm. Retains original appearance
 * and adds update functionality via ProblemListener. Fixes update logic by always calling
 * setValueAt and using fireTableDataChanged.
 *
 * @author Corey Brantley (Modified by Assistant for Update Logic)
 */
public class SubproblemTableView extends GPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(SubproblemTableView.class);

    // Model representing the dynamic programming problem
    private Problem model;
    JTable table; // Table component to display subproblems
    JScrollPane sp; // Scroll pane containing the table
    Object[][] tableData; // 2D array holding table cell values
    String[] columnHeaders; // Array holding the table's column headers

    /** Constructor */
    public SubproblemTableView() {

        // Initialize Swing components and layout
        initializeComponents();
        layoutComponents();

        // Add mouse listener to detect cell clicks
        table.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        getCellClicked(e.getPoint(), table);
                    }
                });
    }

    /**
     * Sets the problem model and registers this view as a listener.
     *
     * @param model The Problem instance to observe
     */
    public void setModel(Problem model) {
        this.model = model;
        if (this.model != null) {
            this.model.addProblemListener(this);
            // When the Problem changes, we should updateStrings() to match
            ProblemKind pKind = model.getType();
            switch (pKind) {
                case MATRIX_CHAIN:
                    // TODO
                    break;
                case KNAPSACK_0_1:
                    // TODO
                    break;
                default: // i.e. LCS_PROBLEM
                    String s1 = ((LCSProblem) model).getX();
                    String s2 = ((LCSProblem) model).getY();
                    updateStrings(s1, s2);
            }
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            updateView();

            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    /**
     * Determines which cell was clicked based on a Point coordinate.
     *
     * @param point The mouse click location
     * @param table The JTable being clicked
     * @return A two-element array: [rowIndex, columnIndexAdjusted]
     */
    public int[] getCellClicked(Point point, JTable table) {
        int[] cellLocation = new int[2];
        cellLocation[0] = table.rowAtPoint(point);
        cellLocation[1] = table.columnAtPoint(point) - 1; // Adjust for header column
        if (cellLocation[0] <= -1 || cellLocation[1] < -1) {
            // Click was outside valid cells
            return new int[] {-1, -1};
        }
        return cellLocation;
    }

    /**
     * Updates a single cell's value in the JTable.
     *
     * @param i Row index (unadjusted)
     * @param j Column index (unadjusted)
     * @param newValue New integer value to display
     */
    public void updateCellValue(int i, int j, int newValue) {
        table.setValueAt(newValue, i, j + 1); // +1 to skip header column
    }

    /**
     * Retrieves a cell's current value from the JTable.
     *
     * @param i Row index (unadjusted)
     * @param j Column index (unadjusted)
     * @return The Object stored at the specified cell
     */
    public Object getCellValue(int i, int j) {
        return table.getValueAt(i, j + 1);
    }

    /** Initializes the JTable, its model, renderers, and JScrollPane. */
    private void initializeComponents() {
        // Custom JTable to render the first column as header style
        table =
                new JTable() {
                    @Override
                    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                        Component component = super.prepareRenderer(renderer, row, col);
                        if (col < 1) {
                            // Use header renderer for first column
                            return this.getTableHeader()
                                    .getDefaultRenderer()
                                    .getTableCellRendererComponent(
                                            this,
                                            this.getValueAt(row, col),
                                            false,
                                            false,
                                            row,
                                            col);
                        }
                        // For highlighting during backtracking phase
                        int[][] bTable = (int[][]) model.getVariableObject("b");
                        switch (bTable[row][col - 1]) {
                            case 0:
                                component.setBackground(Color.GRAY); // miss
                                break;
                            case 1:
                                component.setBackground(Color.YELLOW); // hit
                                break;
                            case 2:
                                component.setBackground(Color.GREEN); // added
                                break;
                            default:
                                // i.e. if bTable[row][col] == -1
                                component.setBackground(Color.WHITE); // unvisited
                                break;
                        }

                        return component;
                    }
                };

        table.setAutoCreateRowSorter(false);

        // Set custom header renderer to center-align header text
        final JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        // Set non-editable table model with initial data
        table.setModel(
                new DefaultTableModel(tableData, columnHeaders) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });

        // Configure column widths and cell renderers
        /* TODO: this code doesn't seem to do anything? But I think it might
        be useful if someone wants to play with setAutoResizeMode. The table
        becomes unreadable with a long y-string.
        */
        TableColumnModel columnModel = table.getColumnModel();
        if (columnModel.getColumnCount() > 1) {
            CustomRenderer cellRenderer = new CustomRenderer(Color.BLACK);
            for (int i = 1; i < table.getColumnCount(); i++) {
                TableColumn col = columnModel.getColumn(i);
                col.setCellRenderer(cellRenderer);
                if (i == 1) {
                    col.setPreferredWidth(30);
                } else {
                    col.setPreferredWidth(40);
                }
            }
        }
        if (columnModel.getColumnCount() > 0) {
            // Set width for the first "header" column
            columnModel.getColumn(0).setPreferredWidth(150);
        }

        // Set header height
        table.getTableHeader().setPreferredSize(new Dimension(25, 45));

        // Wrap table in scroll pane for overflow
        sp = new JScrollPane(table);
    }

    /** Adds components to this panel using GridBagLayout constraints. */
    private void layoutComponents() {
        addc(
                sp,
                0,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);
    }

    /**
     * Constructs the table's column header labels based on input characters.
     *
     * @param string String whose characters become column labels
     */
    private void buildColumnHeaders(String string) {
        List<String> headers = new ArrayList<String>();
        headers.add("(i,j)"); // Top-left corner label
        headers.add("-1"); // Base case column
        for (int i = 0; i < string.length(); i++) {
            // HTML formatting to center label and index
            headers.add("<html><center>" + string.charAt(i) + "<br>(" + i + ")</center></html>");
        }
        columnHeaders = headers.toArray(new String[headers.size()]);
    }

    /**
     * Builds the initial table data array with row labels and empty/default cells.
     *
     * @param string String whose characters become row labels
     */
    private void buildTableData(String string) {
        List<Object[]> rows = new ArrayList<>();
        List<String> rowHeaders = new ArrayList<String>();
        rowHeaders.add("-1"); // Base case row label
        for (int i = 0; i < string.length(); i++) {
            rowHeaders.add(String.valueOf(string.charAt(i)));
        }
        if (columnHeaders == null) {
            tableData = new Object[0][0];
            return;
        }
        // Create each row's data array
        for (int i = 0; i < rowHeaders.size(); i++) {
            Object[] toadd = new Object[columnHeaders.length];
            if (i > 0) {
                // Format row label with index
                toadd[0] = rowHeaders.get(i) + "  (" + (i - 1) + ")";
            } else {
                toadd[0] = rowHeaders.get(i);
            }
            rows.add(toadd);
        }
        tableData = rows.toArray(new Object[0][]);
    }

    /** Called when the Problem model is updated. Ensures update on EDT. */
    @Override
    public void problemUpdated(Problem problem) {
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Refreshes the JTable contents based on the model's current DP table. Ensures all cells are
     * updated and fires a table data change event.
     */
    private void updateView() {
        // Validate model and required variables
        if (model == null
                || model.getTableVariable() == null
                || model.getVariableObject("n") == null
                || model.getVariableObject("m") == null) {
            return;
        }
        Object tableObj = model.getVariableObject(model.getTableVariable());
        Object nObj = model.getVariableObject("n");
        Object mObj = model.getVariableObject("m");
        // Type check for DP table and indices
        if (!(tableObj instanceof int[][])
                || !(nObj instanceof Integer)
                || !(mObj instanceof Integer)) {
            log.error("SubproblemTableView: Model variable types are incorrect.");
            return;
        }
        int[][] lTable = (int[][]) tableObj;
        int n = (int) nObj;
        int m = (int) mObj;
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        // Ensure table has sufficient size
        if (dtm.getRowCount() < n + 1 || dtm.getColumnCount() < m + 2) {
            log.error("SubproblemTableView: Table dimensions too small.");
            return;
        }
        // Iterate through DP table and update each cell
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                int tableRow = i;
                int tableCol = j + 1; // Offset to skip header column
                if (tableRow < dtm.getRowCount() && tableCol < dtm.getColumnCount()) {
                    int value = lTable[i][j];
                    Object displayValue = (value == -1) ? "" : String.valueOf(value);
                    dtm.setValueAt(displayValue, tableRow, tableCol);
                } else {
                    log.error(
                            "ERROR: SubproblemTableView: Attempted to update out-of-bounds cell ("
                                    + tableRow
                                    + ", "
                                    + tableCol
                                    + ") during loop.");
                }
            }
        }
        // Notify JTable that data has changed
        dtm.fireTableDataChanged();
    }

    /** Custom cell renderer to draw grid lines and center-align text. */
    class CustomRenderer extends DefaultTableCellRenderer {
        Color gridColor;

        public CustomRenderer(Color color) {
            gridColor = color;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Component cell =
                    super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
            // Set border thickness: top & left by default, bottom on last row, right on last column
            int top = 1, left = 1, right = 0, bottom = 0;
            if (row == table.getRowCount() - 1) bottom = 1;
            if (column == table.getColumnCount() - 1) right = 1;
            if (cell instanceof JLabel) {
                ((JLabel) cell)
                        .setBorder(
                                BorderFactory.createMatteBorder(
                                        top, left, bottom, right, gridColor));
                ((JLabel) cell).setHorizontalAlignment(JLabel.CENTER);
            }
            return cell;
        }
    }

    /** Header renderer to center-align column header text. */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int col) {
            return renderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
        }
    }

    /**
     * Rebuilds table headers and data when input strings change. Dynamically reapplies renderers
     * and refreshes the view.
     *
     * @param string1 The new first input String (x-axis labels)
     * @param string2 The new second input String (y-axis labels)
     */
    public void updateStrings(String string1, String string2) {
        string1 = string1.toUpperCase();
        string2 = string2.toUpperCase();

        buildColumnHeaders(string2);
        buildTableData(string1);

        // Replace model with new data
        table.setModel(new DefaultTableModel(tableData, columnHeaders));

        // Reapply cell renderers
        TableColumnModel model = table.getColumnModel();
        for (int i = 1; i < table.getColumnCount(); i++) {
            TableColumn col = model.getColumn(i);
            col.setCellRenderer(new CustomRenderer(Color.BLACK));
        }

        // Reset header renderer
        final JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        revalidate();
        repaint();
    }
}
