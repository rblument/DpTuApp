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

import edu.regis.dptu.model.CodeModel;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * Displays the Code Panel section of the Tutoring Session GUI. This panel will
 * display code statements from the CodeModel.
 *
 * TODO- This File is unfinished and may not have full functionality.
 *
 * @author cadencea
 */
public class CodePanel extends GPanel {

    /**
     * Declares the CodeView model displayed in this view along with the
     * necessary arrayLists for the code statements.
     *
     */
    private CodeModel model;
    private ArrayList statementStrings;
    private ArrayList statementJLabels;

    /**
     * Initialize this view including creating and laying out its child
     * components.
     */
    public CodePanel() {
        initializeComponents();
        layoutComponents();
    }

    /**
     * Returns the model currently displayed in this view.
     *
     * @return a CodeModel
     *
     */
    public CodeModel getModel() {
        return model;
    }

    /**
     * Display the given model in this view.
     *
     *
     *
     * @param model a CodeModel.
     */
    public void setModel(CodeModel model) {
        this.model = model;

        updateView();
    }

    /**
     * Create the child GUI components appearing in this frame.
     */
    private void initializeComponents() {
        /**
         * TODO- I can't imagine this is the right place for this model to be
         * initialized but I have not been able to find how to do it and this
         * works so...
         */
        model = new CodeModel();
        /**
         * grabs the statement strings from the model and creates JLabels for
         * each in an ArrayList
         */
        statementStrings = model.getStatementList();
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
     *
     * TODO- Is it bad practice to cast it as a JLabel, it /should/ only have
     * JLabels but is there a more best practice way to achieve this?
     */
    private void layoutComponents() {
        for (int i = 0; i < statementJLabels.size(); i++) {
            addc((JLabel) statementJLabels.get(i), 0, i, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        }
    }

    /**
     * Display the current model in our child components.
     */
    private void updateView() {

    }
}
