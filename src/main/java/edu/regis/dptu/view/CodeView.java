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

import edu.regis.dptu.model.TutoringSession;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

/**
 *
 * @author rickb
 */
public class CodeView extends GPanel {
     /**
    * The tutoring session model displayed in this view.
    */
   private TutoringSession model;

   private JLabel test;
   
   // defining variables view
   private VariablesView variablesView;

   /**
    * Initialize this view including creating and laying out its child
    * components.
    */
   public CodeView() {
      initializeComponents();
      layoutComponents();
   }

   /**
    * Return the model currently displayed in this view.
    *
    * @return a TutoringSession
    */
   public TutoringSession getModel() {
      return model;
   }

   /**
    * Display the given model in this view.
    *
    * @param model a TutoringSession.
    */
   public void setModel(TutoringSession model) {
      this.model = model;

      updateView();

   }

   /**
    * Create the child GUI components appearing in this frame.
    */
   private void initializeComponents() {
      test = new JLabel("Code View Test");
   }

   /**
    * Layout the child components in this view
    */
   private void layoutComponents() {
      addc(test, 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            5, 5, 5, 5);
   }

   /**
    * Display the current model in our child components.
    */
   public void updateView() {

    }
}
