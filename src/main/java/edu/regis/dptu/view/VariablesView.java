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

import edu.regis.dptu.model.Problem;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

/**
 *
 * @author danielaflores
 */
public class VariablesView extends GPanel {
    private Problem model;
    private JLabel rName, rValue, cName, cValue, iName, iValue, jName, jValue, lName, lValue;

    public VariablesView() {
        initializeComponents();
        layoutComponents();
    }
    
    public void setModel(Problem model){
        this.model = model;
        updateView();
    }
     
    private void initializeComponents(){
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
    
    private void updateView() {
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
}
