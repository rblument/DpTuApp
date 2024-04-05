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

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import edu.regis.dptu.model.aol.Assessment;

/**
 * The AssessmentTableModel class extends AbstractTableModel to provide a custom
 * table model for displaying student assessments. It encapsulates the
 * functionality required to display assessments in a JTable, including the
 * number of rows, columns, and the data in each cell. The model is designed to
 * be used with StudentModelView to present a cohesive and interactive user
 * interface for viewing assessments.
 * 
 * @author Ju Lesesne
 */
class AssessmentTableModel extends AbstractTableModel {

   private final List<Assessment> assessments = new ArrayList<>();
   private final String[] columnNames = {"ID", "Title", "Assessment Level"};

   public void setAssessments(List<Assessment> assessments) {
      this.assessments.clear();
      this.assessments.addAll(assessments);
      fireTableDataChanged(); // Notify the table that the data has changed
   }

   @Override
   public int getRowCount() {
      return assessments.size();
   }

   @Override
   public int getColumnCount() {
      return columnNames.length;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex) {
      Assessment assessment = assessments.get(rowIndex);
      switch (columnIndex) {
         case 0:
            return assessment.getOutcome().getId();
         case 1:
            return assessment.getOutcome().getTitle();
         case 2:
            return assessment.getAssessment().title();
         default:
            return "";
      }
   }

   @Override
   public String getColumnName(int column) {
      return columnNames[column];
   }
}
