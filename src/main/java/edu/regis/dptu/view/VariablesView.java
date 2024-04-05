package edu.regis.dptu.view;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author bplewinski
 */
public class VariablesView extends JPanel {

   // Labels for displaying variable names and their values
   private JLabel labelSequenceId;
   private JLabel labelCurrentStepIndex;

   // Text fields to display the values of the variables
   private JTextField textFieldSequenceId;
   private JTextField textFieldCurrentStepIndex;

   public VariablesView() {
      
      initComponents();
   }

   private void initComponents() {
      setLayout(new GridLayout(2, 2)); // 2 rows, 2 columns

      // Initialize labels for variable names
      labelSequenceId = new JLabel("Sequence ID:");
      labelCurrentStepIndex = new JLabel("Current Step Index:");

      // Initialize text fields for variable values
      textFieldSequenceId = new JTextField();
      textFieldCurrentStepIndex = new JTextField();

      // Make text fields uneditable
      textFieldSequenceId.setEditable(false);
      textFieldCurrentStepIndex.setEditable(false);

      // Add labels and text fields to the panel
      add(labelSequenceId);
      add(textFieldSequenceId);
      add(labelCurrentStepIndex);
      add(textFieldCurrentStepIndex);
   }

   // Method to update the values of variables displayed in the view
   public void updateVariables(int sequenceId, int currentStepIndex) {
      // Update the text fields with the provided variables
      textFieldSequenceId.setText(String.valueOf(sequenceId));
      textFieldCurrentStepIndex.setText(String.valueOf(currentStepIndex));
   }
}

