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
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author danielaflores
 */
public class VariablesView extends GPanel implements ProblemListener{
    private Problem model;
    private JLabel variableNamesLabel;
    //private ArrayList<JLabel> variableNamesLabel;
    private JLabel variableValuesLabel;
    //private ArrayList<JLabel> variableValuesLabel;

    public VariablesView() {
        String x = "skullandbones"; // n == 13
        String y = "lullabybabies";
        
        model = new LCSProblem(x,y);
        
        model.step();
        //initializeComponents();
        //layoutComponents();
    }
     
    public void setModel(Problem model){
        this.model = model;
        model.addProblemListener(this);
        updateView();
    }
    
    public Problem getModel() {
        return model;
    }
     
    /*private void initializeComponents(){
         rName = new JLabel("r = ");
         rValue = new JLabel("13");
         cName = new JLabel("c = ");
         cValue = new JLabel("13");
         jName = new JLabel("j = ");
         jValue = new JLabel("10");
         iName = new JLabel("i = ");
         iValue = new JLabel("10");
         lName = new JLabel("l = ");
         lValue = new JLabel("10");
    }
    
    private void layoutComponents() {
        addc(rName, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(rValue, 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(cName, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(cValue, 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(iName, 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(iValue, 1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(jName, 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(jValue, 1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(lName, 0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
        
        addc(lValue, 1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
    }
    */
    
    private void updateView() {
        removeAll();
        ArrayList<String> pVariableNames = model.getVariableNames();
        //System.out.println(pVariableNames);
        //int pVariableValue = model.getVariableValue(pVariableNames.toString());
        
        GPanel childComponents = new GPanel();
        
        for(int i = 0; i< pVariableNames.size(); i++){
            String localVariableName = pVariableNames.get(i);
            
            //variableNamesLabel = new ArrayList<JLabel> ();
            //variableNamesLabel.add(new JLabel(localVariableName));
            
            variableNamesLabel = new JLabel(localVariableName);
            //variableValuesLabel = new ArrayList<JLabel> ();
            //variableValuesLabel.add(String.valueOf(model.getVariableValue(localVariableName)));
            
            variableValuesLabel = new JLabel(String.valueOf(model.getVariableValue(localVariableName)));
            
            childComponents.addc(variableNamesLabel,0,i,1,1,0.0,0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5,5,5,5);
            
            childComponents.addc(variableValuesLabel,1,i,1,1,0.0,0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5,5,5,5);
        }
        
        JScrollPane scrollPane = new JScrollPane(childComponents);
        childComponents.addc(scrollPane,0,0,1,1,0.0,0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5,5,5,5);

        revalidate();
        repaint();
        
        /*
        if (model == null) {
            return;
        }
        
        // Here you would update the variable labels based on the model
        // For example, if model is an LCSProblem:
        /*
        if (model instanceof LCSProblem) {
            LCSProblem lcsProblem = (LCSProblem) model;
            rValue.setText(String.valueOf(lcsProblem.getR()));
            cValue.setText(String.valueOf(lcsProblem.getC()));
            iValue.setText(String.valueOf(lcsProblem.getI()));
            jValue.setText(String.valueOf(lcsProblem.getJ()));
        }
        */
    }

    @Override
    public void problemUpdated(Problem problem) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}