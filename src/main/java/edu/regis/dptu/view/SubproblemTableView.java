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

// Keep ALL original imports
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities; // Keep this import
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Creates Subproblem Table to view as student works through algorithm
 * Retains original appearance and adds update functionality via ProblemListener.
 * Fixes update logic by always calling setValueAt and using fireTableDataChanged.
 *
 * @author Corey Brantley (Modified by Assistant for Update Logic)
 */
public class SubproblemTableView extends GPanel implements ProblemListener {

    // Keep original fields
    private Problem model;
    JTable table;
    JScrollPane sp;
    Object[][] tableData;
    String[] columnHeaders;


    // Keep original constructor
    public SubproblemTableView(String string1, String string2)
    {
        string1 = string1.toUpperCase();
        string2 = string2.toUpperCase();
        buildColumnHeaders(string2);
        buildTableData(string1);
        initializeComponents();
        layoutComponents();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getCellClicked(e.getPoint(), table);
            }
        });
    }

    // Keep original setModel
    public void setModel(Problem model) {
        this.model = model;
        if (this.model != null) {
            this.model.addProblemListener(this);
            updateView();
        }
    }

    // Keep original getCellClicked
    public int[] getCellClicked(Point point, JTable table) {
        int[] cellLocation = new int[2];
        cellLocation[0] = table.rowAtPoint(point);
        cellLocation[1] = table.columnAtPoint(point) - 1;
        if (cellLocation[0] <= -1 || cellLocation[1] < -1) { return new int[]{-1, -1}; }
        // System.out.println("Cell Location: Row: " + cellLocation[0] + " DP Col Index: " + cellLocation[1]); // Debug print removed
        return cellLocation;
    }

    // Keep original updateCellValue
    public void updateCellValue(int i, int j, int newValue) {
        table.setValueAt(newValue, i, j + 1);
    }

    // Keep original getCellValue
    public Object getCellValue(int i, int j) {
        return table.getValueAt(i, j + 1);
    }


    // Keep original initializeComponents
    private void initializeComponents()
    {
        table = new JTable() {
            @Override
            public Component prepareRenderer(
                TableCellRenderer renderer, int row, int col) {
                if (col < 1) {
                    return this.getTableHeader().getDefaultRenderer()
                        .getTableCellRendererComponent(this, this.getValueAt(
                            row, col), false, false, row, col);
                } else {
                    return super.prepareRenderer(renderer, row, col);
                }
            }
        };

        table.setAutoCreateRowSorter(false);
        final JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        table.setModel(new DefaultTableModel(tableData, columnHeaders) {
             @Override
             public boolean isCellEditable(int row, int column) { return false; }
        });

        TableColumnModel columnModel = table.getColumnModel();
        if (columnModel.getColumnCount() > 1) {
            CustomRenderer cellRenderer = new CustomRenderer(Color.BLACK);
            for (int i = 1; i < table.getColumnCount(); i++) {
                TableColumn col = columnModel.getColumn(i);
                col.setCellRenderer(cellRenderer);
                 if (i == 1) { col.setPreferredWidth(30); }
                 else { col.setPreferredWidth(40); }
            }
        }
        if (columnModel.getColumnCount() > 0) {
             columnModel.getColumn(0).setPreferredWidth(150); // Original width
        }
        table.getTableHeader().setPreferredSize(new Dimension(25, 25)); // Original height

        sp = new JScrollPane(table);
    }

    // Keep original layoutComponents
    private void layoutComponents() {
        addc(sp, 0, 0, 1, 1, 0.0, 0.0, // Original constraints
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
    }

    // Keep original buildColumnHeaders
    private void buildColumnHeaders(String string) {
        List<String> headers = new ArrayList<String>();
        headers.add("(i,j)");
        headers.add("-1");
        for (int i = 0; i < string.length(); i++) {
            headers.add("<html><center>" + string.charAt(i) + "<br>(" + i + ")</center></html>");
        }
        columnHeaders = headers.toArray(new String[headers.size()]);
    }

    // Keep original buildTableData
    private void buildTableData(String string) {
        List<Object[]> rows = new ArrayList<>();
        List<String> rowHeaders = new ArrayList<String>();
        rowHeaders.add("-1");
        for(int i = 0; i < string.length(); i++) {
            rowHeaders.add(String.valueOf(string.charAt(i)));
        }
         if (columnHeaders == null) { tableData = new Object[0][0]; return; }
        for (int i = 0; i < rowHeaders.size(); i++) {
            Object[] toadd = new Object[columnHeaders.length];
            if (i > 0) { toadd[0] = rowHeaders.get(i) + "  (" + (i - 1) + ")"; }
            else { toadd[0] = rowHeaders.get(i); }
            toadd[1] = 0;
            for (int p = 2; p < toadd.length; p++) {
                if (i == 0 ) { toadd[p] = 0; }
                else { toadd[p] = ""; }
            }
            rows.add(toadd);
        }
        tableData = rows.toArray(new Object[0][]);
    }

    // Keep problemUpdated
    @Override
    public void problemUpdated(Problem problem) {
        SwingUtilities.invokeLater(this::updateView);
    }

    // <<< updateView modified to REMOVE redundant check & keep fireTableDataChanged >>>
    private void updateView() {
        if (model == null || model.getTableVariable() == null || model.getVariableObject("n") == null || model.getVariableObject("m") == null) {
            return;
        }

        Object tableObj = model.getVariableObject(model.getTableVariable());
        Object nObj = model.getVariableObject("n");
        Object mObj = model.getVariableObject("m");

        if (!(tableObj instanceof int[][]) || !(nObj instanceof Integer) || !(mObj instanceof Integer)) {
             System.err.println("SubproblemTableView: Model variable types are incorrect."); // Keep error for debugging
            return;
        }

        int[][] lTable = (int[][]) tableObj;
        int n = (int) nObj;
        int m = (int) mObj;

        DefaultTableModel dtm = (DefaultTableModel) table.getModel();

        if (dtm.getRowCount() < n + 1 || dtm.getColumnCount() < m + 2) {
             System.err.println("SubproblemTableView: Table dimensions too small."); // Keep error for debugging
            return;
        }

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                int tableRow = i;
                int tableCol = j + 1;

                if (tableRow < dtm.getRowCount() && tableCol < dtm.getColumnCount()) {
                    int value = lTable[i][j];
                    Object displayValue = (value == -1) ? "" : String.valueOf(value);

                    // Always set the value to ensure TableModel events fire
                    dtm.setValueAt(displayValue, tableRow, tableCol);

                } else {
                     System.err.println("ERROR: SubproblemTableView: Attempted to update out-of-bounds cell (" + tableRow + ", " + tableCol + ") during loop.");
                 }
            }
        }

        // Keep this call - it tells the JTable the model *structure* or *all data* might have changed
        dtm.fireTableDataChanged();

        // Removed explicit repaint calls
        // table.repaint();
        // if (sp != null) { sp.repaint(); }
    }

    // Keep original CustomRenderer class
    class CustomRenderer extends DefaultTableCellRenderer {
        Color gridColor;
        public CustomRenderer(Color color){ gridColor = color; }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int top = 1; int left = 1; int right = 0; int bottom = 0;
            if (row == table.getRowCount() - 1) { bottom = 1; }
            if (column == table.getColumnCount() - 1) { right = 1; }
            if (cell instanceof JLabel) {
                 ((JLabel) cell).setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, gridColor));
                 ((JLabel) cell).setHorizontalAlignment(JLabel.CENTER);
            }
            return cell;
        }
    }

    // Keep original HeaderRenderer class
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        DefaultTableCellRenderer renderer;
        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col);
        }
    }

    /**
     * Updates the SubproblemTableView based on new input strings.
     * 
     * Changes (April 17, 2025):
     * Dynamically rebuilds the table headers and data based on updated strings.
     * Resets the table model and reapplies all necessary renderers after update.
     * Forces revalidation and repainting to reflect the changes visually.
     * 
     * Possible enhancements:
     * Check for the string length to see if they are the same as the current table so we don't need to rebuild the 
     * entire table and instead just the headers. Would still need to clear/reset the table however. This would just be minimal optimizations.
     * 
     * @author EverettCV
     * 
     * @param string1 The new first input String (x-axis labels)
     * @param string2 The new second input String (y-axis labels)
     */
    public void updateStrings(String string1, String string2) {
        string1 = string1.toUpperCase();
        string2 = string2.toUpperCase();

        buildColumnHeaders(string2);
        buildTableData(string1);

        table.setModel(new DefaultTableModel(tableData, columnHeaders));

        // Reapply the renderers for custom borders and formatting
        TableColumnModel model = table.getColumnModel();
        for (int i = 1; i < table.getColumnCount(); i++) {
            TableColumn col = model.getColumn(i);
            col.setCellRenderer(new CustomRenderer(Color.BLACK));
        }

        // Reset header renderer for visual consistency
        final JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        revalidate();
        repaint();
    }
    
    
    
}
