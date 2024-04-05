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

import javax.swing.*;
import java.awt.BorderLayout; 
import java.util.ArrayList;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.model.aol.Assessment; 

/**
 * The StudentModelView class extends JPanel and presents 
 * the student's assessment data in a graphical interface. It utilizes
 * AssessmentTableModel to organize and display the data in a JTable. This view
 * is to provide feedback to students about their learning progress.
 * 
 * @author Ju Lesesne
 */
public class StudentModelView extends JPanel {
    private JTable assessmentsTable;
    private AssessmentTableModel tableModel;
    private StudentModel studentModel;

    public StudentModelView(StudentModel studentModel) {
        this.studentModel = studentModel;
        initializeView();
        bindDataToView();
    }

    private void initializeView() {
        setLayout(new BorderLayout());
        tableModel = new AssessmentTableModel();
        assessmentsTable = new JTable(tableModel);
        add(new JScrollPane(assessmentsTable), BorderLayout.CENTER);
    }

    private void bindDataToView() {
        if (studentModel != null) {
            // Convert assessments HashMap to a List for the table model
            ArrayList<Assessment> list = new ArrayList<>(studentModel.getAssessments().values());
            tableModel.setAssessments(list);
        }
    }
    
    // You may need to add a method here to update `studentModel` and refresh the view accordingly
    public void updateModel(StudentModel newModel) {
        this.studentModel = newModel;
        bindDataToView();
    }
}