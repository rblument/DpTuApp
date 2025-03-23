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

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.lang.String;

/**
 * Displays the Code Panel section of the Tutoring Session GUI. This panel will
 * display code statements from the CodeModel.
 *
 *
 * @author cadencea
 */
public class CodeView extends GPanel implements ProblemListener {

    /**
     * Declares the CodeView model (a Problem object) displayed in this view along with the
     * necessary arrayLists for the code statements.
     *
     */
    private Problem model;
    private ArrayList statementStrings;
    private ArrayList<JLabel> statementJLabels;

    /**
     * Used as a background color
     */
    private static final Color MEDIUM_GRAY = new Color(215, 215, 215);
    
    /**
     * 
     * Initialize this view including creating and laying out its child
     * components.
     */
    public CodeView() {
        // Temperary model so it can be tested.
        model = new LCSProblem("test","test");
        setModel(model);
        
        // Adds codeView to the list of problem listeners the Problem has so it 
        // can be notified about updates
        model.addProblemListener(this);
        
        //Making it look pretty
        setBorder(BorderFactory.createTitledBorder("Code View"));
        setBackground(MEDIUM_GRAY);
        
        initializeComponents();
        layoutComponents();
    }

    /**
     * Returns the model currently displayed in this view.
     *
     * @return a CodeModel
     *
     */
    public Problem getModel() {
        return model;
    }

    /**
     * Display the given model in this view.
     *
     *
     *
     * @param model a CodeModel.
     */
    public  void setModel(Problem model) {
        this.model = model;

        updateView();
    }

    /**
     * Create the child GUI components appearing in this frame.
     */
    private void initializeComponents() {
        /**
         * grabs the statement strings from the model and creates JLabels for
         * each in an ArrayList
         */
        statementStrings = model.getCodeStatements();
        statementJLabels = new ArrayList();
        for (int i = 0; i < statementStrings.size(); i++) {
            statementJLabels.add(new JLabel(statementStrings.get(i).toString()));
        }
    }

    /**
     * Layout the child components in this view. Since each line will be below
     * the next we will adjust the 3rd parameter in the addc statement.
     *
     * The loop iterates through the statementJLabels list and adds the
     * component.
     */
    private void layoutComponents() {
        for (int i = 0; i < statementJLabels.size(); i++) {
            //Line Numbers
            addc(new JLabel(String.valueOf(i+1)), 0, i, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                    0, 1, 0, 1);
            // Code Statements
            addc(statementJLabels.get(i), 1, i, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    0, 1, 0, 1);
        }
    }

    /**
     * Display the current model in our child components.
     */
    private void updateView() {

    }
    
    /**
     * Takes the updated problem and updated the view to match the model's state.
     * 
     * For CodeView this highlights the JLabel with the line currently 
     * being used in the model.
     * 
     * @param problem 
     */
    public void problemUpdated(Problem problem) {
        
        int currentLineNumber = problem.getCurrentLineNumber();
        statementJLabels.get(currentLineNumber).setBackground(Color.YELLOW);
        statementJLabels.get(currentLineNumber).setOpaque(true);
        
    }
}
