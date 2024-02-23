package edu.regis.dptu.view;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;

/**
 * Displays variable names as context for the tutoring session. This view is
 * intended to show variable names only.
 *
 * @author Your Name
 */
public class VariablesView extends GPanel {
   
   private JLabel variable1Label;
   private JLabel variable2Label;

   /**
    * Initialize this view including creating and laying out its child
    * components.
    */
   public VariablesView() {
      initializeComponents();
      layoutComponents();
   }

   /**
    * Create the child GUI components appearing in this view.
    */
   private void initializeComponents() {
      // Initialize JLabels
      variable1Label = new JLabel("variable1:");
      variable2Label = new JLabel("variable2:");
      // Initialize more JLabels for additional variables as needed
   }

   /**
    * Layout the child components in this view
    */
   private void layoutComponents() {
      // Add JLabels to the layout as needed
      // For simplicity, you can use a vertical layout (one variable per row)
      addc(variable1Label, 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            5, 5, 5, 5);
      addc(variable2Label, 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            5, 5, 5, 5);
      // Add more JLabels for additional variables as needed
   }

   /**
    * Update the view with variable names. This method can be called whenever
    * the view needs to be updated.
    */
   public void updateVariablesView(String[] variableNames) {
      // Update JLabels with variable names
      variable1Label.setText(variableNames[0]);
      variable2Label.setText(variableNames[1]);
      // Update more JLabels for additional variables as needed
   }
}
