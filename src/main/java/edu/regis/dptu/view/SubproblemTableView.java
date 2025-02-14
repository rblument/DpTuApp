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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * Creates Subproblem Table to view as student works through algorithm
 * TODO: Add colors as the problem is stepped through to see what cells are 
 *  being considered in the algorithm
 * @author Corey Brantley
 */
public class SubproblemTableView extends GPanel {
    
    JTable table;
    // A table must be in a scroll pane to include the headers
    JScrollPane sp;
    // The table data
    Object[][] tableData;
    // The column headers
    String[] columnHeaders;
    
    
    public SubproblemTableView(String string1, String string2)
    {
        string1 = string1.toUpperCase();
        string2 = string2.toUpperCase();
        buildColumnHeaders(string2);
        buildTableData(string1);
        initializeComponents();
        layoutComponents();
    }
    
    public void updateCellValue(int i, int j, int newValue) {
        table.setValueAt(newValue, i, j + 1);
    }
    
    public Object getCellValue(int i, int j) {
        return table.getValueAt(i, j + 1);
    }
            
    
    private void initializeComponents()
    {

        table = new JTable() {
            // Overriding the prepare renderer to render the first column like a header
            // to create row headers
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
        
        // Don't want sorting
        table.setAutoCreateRowSorter(false);
        final JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));

        
        table.setModel(new DefaultTableModel(tableData, columnHeaders));
        
        // Adding borders to all cells not in the first column
        TableColumnModel model = table.getColumnModel();
        for (int i = 1; i < table.getColumnCount(); i++) {
            TableColumn col = model.getColumn(i);
            col.setCellRenderer(new CustomRenderer(Color.BLACK));
        }
        table.getTableHeader().setPreferredSize(new Dimension(25, 25));
        model.getColumn(0).setPreferredWidth(150);

        sp = new JScrollPane(table);
                
    }
    
    /**
     * Layout scroll pane that contains the table
     * TODO: This may need to be changed to put into the final view that contains
     * the other views for the LCS problem.
     */
    private void layoutComponents() {
        addc(sp, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
    }
    
    /**
     * Builds the column headers for the table, takes in the y string and
     * adds some formatting around each character to look nice in the table along with
     * it's index position. Also adds (i,j) and -1 to the columns
     * @param string The "y" string used in the LCS problem to be displayed
     */
    private void buildColumnHeaders(String string) {
        List<String> headers = new ArrayList<String>();
        
        headers.add("(i,j)");
        headers.add("-1");
        
        List<String> splitString = Arrays.asList(string.split(""));
        
        for (int i = 0; i < splitString.size(); i++) {
            headers.add("<html><center>" + splitString.get(i) + "<br>(" + i + ")");
        }
        
        columnHeaders = new String[headers.size()];
        
        columnHeaders = headers.toArray(columnHeaders);
    }
    
    /**
     * Builds the data for the table. Uses the x string to build a row per character
     * The character and it's index position is the first column of the row and 
     * then black spaces are added for the length of the y character after that. 
     * @param string The "x" string used in the LCS problem to be displayed
     */
    private void buildTableData(String string) {
        List<Object[]> rows = new ArrayList<>();
        List<String> rowHeaders = new ArrayList<String>();
        
        rowHeaders.add("-1");
        rowHeaders.addAll(Arrays.asList(string.split("")));
        
        for (int i = 0; i < rowHeaders.size(); i++) {
            Object[] toadd = new Object[columnHeaders.length + 1];
            if (i > 0) {
                toadd[0] = rowHeaders.get(i) + "  (" + (i - 1) + ")";
            } else {            
                toadd[0] = rowHeaders.get(i);
            }
            
            toadd[1] = 0;
            for (int p = 2; p < toadd.length; p++) {
                if (i == 0 ) {
                    toadd[p] = 0;
                } else {
                    toadd[p] = "";
                }
            }
            rows.add( toadd );
        }
        
        tableData = new Object[rows.size()][columnHeaders.length];
        tableData = rows.toArray(tableData);
        
    }
    
    /**
     * A custom renderer class for rendering cells within the table
     * Currently it is mainly used to set the border of each cell not in the first
     * column. Doing this because the first column is for displaying one of the strings
     * in the LCS problem. I wanted to visually separate that from the rest of the
     * table used for updating values in the problem. 
     */
    class CustomRenderer extends DefaultTableCellRenderer
    {
        Color gridColor;

        public CustomRenderer(Color color)
        {
            gridColor = color;
        }
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column)
        {
            Color color = Color.black;
            setForeground(color);
            setHorizontalAlignment(JLabel.CENTER);
            /**
             * The right and bottom don't always have to be included
             * because as the grid is set for each cell, adjacent cells'
             * top/left look like a cell's bottom/right grids.
            */
            int top = 1;
            int left = 1;
            int right = 0;
            int bottom = 0;
            
            // Cell is at the bottom of the table, include the bottom border
            if (row == table.getRowCount() - 1) {
                bottom = 1;
            }
            // Cell is at the last column of the table, include the right border
            if (column == table.getColumnCount() - 1) {
                right = 1;
            }
            
            setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, gridColor));
            setValue(value);
            return this;
        }
    }
    
    /**
     * Custom header renderer used to render the first column of the table
     * to look similar to the column headers row. This is a way to visually create
     * row headers. 
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {

        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
            renderer.setForeground(Color.red);
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
            
                        
            return renderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);
        }
    }
    
    
    
}
