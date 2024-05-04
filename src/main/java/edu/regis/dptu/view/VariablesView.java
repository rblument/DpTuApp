package edu.regis.dptu.view;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Represents a JPanel for displaying variables related to a specific algorithm or computation.
 * Provides labeled text fields for each variable.
 * 
 * @author bplewinski
 */
public class VariablesView extends JPanel {

   // Labels and text fields for displaying variables
   private final JLabel labelI = new JLabel("i");
   private final JTextField valueI = new JTextField(3);

   private final JLabel labelJ = new JLabel("j");
   private final JTextField valueJ = new JTextField(3);

   private final JLabel labelXI = new JLabel("x[i]");
   private final JTextField valueXI = new JTextField(3);

   private final JLabel labelYJ = new JLabel("y[j]");
   private final JTextField valueYJ = new JTextField(3);

   private final JLabel labelLPrev1 = new JLabel("L[i-1, j]");
   private final JTextField valueLPrev1 = new JTextField(3);

   private final JLabel labelLPrev2 = new JLabel("L[i, j-1]");
   private final JTextField valueLPrev2 = new JTextField(3);

   private final JLabel labelLCurrent = new JLabel("L[i, j]");
   private final JTextField valueLCurrent = new JTextField(3);

   /**
    * Constructs the VariablesView JPanel and initializes its components.
    */
   public VariablesView() {
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(2, 2, 2, 2);
      gbc.gridx = 0;
      gbc.gridy = 0;

      addComponent(labelI, valueI, gbc, 0);
      addComponent(labelJ, valueJ, gbc, 2);
      addComponent(labelXI, valueXI, gbc, 4);
      addComponent(labelYJ, valueYJ, gbc, 6);
      addComponent(labelLPrev1, valueLPrev1, gbc, 8);
      addComponent(labelLPrev2, valueLPrev2, gbc, 10);
      addComponent(labelLCurrent, valueLCurrent, gbc, 12);

      valueI.setEditable(false);
      valueJ.setEditable(false);
      valueXI.setEditable(false);
      valueYJ.setEditable(false);
      valueLPrev1.setEditable(false);
      valueLPrev2.setEditable(false);
      valueLCurrent.setEditable(false);
   }

   /**
    * Adds a label and a text field to the panel with the given constraints.
    * 
    * @param label The label to be added.
    * @param field The text field to be added.
    * @param gbc The GridBagConstraints specifying the position of the components.
    * @param gridx The horizontal position for the label.
    */
   private void addComponent(JLabel label, JTextField field, GridBagConstraints gbc, int gridx) {
      gbc.gridx = gridx;
      add(label, gbc);
      gbc.gridx = gridx + 1;
      add(field, gbc);
   }
   /**
    * Updates the displayed values of the variables.
    * 
    * @param i The value of variable i.
    * @param j The value of variable j.
    * @param xi The value of variable x[i].
    * @param yj The value of variable y[j].
    * @param Lprev1 The value of variable L[i-1, j].
    * @param Lprev2 The value of variable L[i, j-1].
    * @param Lcurrent The value of variable L[i, j].
    */
   public void updateValues(int i, int j, char xi, char yj, int Lprev1, int Lprev2, int Lcurrent) {
      // Print out the variable values for debugging
      valueI.setText(String.valueOf(i));
      valueJ.setText(String.valueOf(j));
      valueXI.setText(String.valueOf(xi));
      valueYJ.setText(String.valueOf(yj));
      valueLPrev1.setText(String.valueOf(Lprev1));
      valueLPrev2.setText(String.valueOf(Lprev2));
      valueLCurrent.setText(String.valueOf(Lcurrent));
   }
}
