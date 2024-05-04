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

import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.TutoringSession;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author rickb
 */
public class CodeView extends GPanel {

   /**
    * The tutoring session model displayed in this view.
    */
   private TutoringSession model = new TutoringSession();

   //private JLabel test;
   private JList codeview;
   private JButton nextButton;
   private JButton autoButton;

   int i;

   int m;
   int n;

   int mMax;
   int nMax;

   int mIndex;
   int nIndex;

   // defining variables view
   private VariablesView variablesView;

   /**
    * Initialize this view including creating and laying out its child
    * components.
    */
   public CodeView() {
      initializeComponents();
      layoutComponents();

      i = 0;
      m = 1;
      n = 1;
      mMax = 5;
      nMax = 5;
      mIndex = -1;
      nIndex = -1;

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
      //test = new JLabel("Code View Test");

      nextButton = new JButton();

      nextButton.setText("Next");
      nextButton.addActionListener(new java.awt.event.ActionListener() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            nextButtonActionPerformed(evt);
         }
      });

      autoButton = new JButton();

      autoButton.setText("Auto");
      autoButton.addActionListener(new java.awt.event.ActionListener() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            autoButtonActionPerformed(evt);
         }
      });

      ArrayList<String> codelist = new ArrayList<>();

      // Just so it is not hardcoded, code is provided in a file
      // File is in resource folder, but the path may need to be changed
      File file = new File("..\\DpTuApp\\src\\main\\java\\resources\\testcode.txt");

      try {
         Scanner scan = new Scanner(file);

         while (scan.hasNextLine()) {

            codelist.add(scan.nextLine());

         }

      } catch (FileNotFoundException ex) {
         Logger.getLogger(CodeView.class.getName()).log(Level.SEVERE, null, ex);
      }

      String[] code = new String[codelist.size()];

      for (int j = 0; j < codelist.size(); j++) {

         code[j] = codelist.get(j);
      }

      codeview = new JList<>(code); //Sets file contents to JList

      //Cell Renderer changes background of lines to blue
      codeview.setCellRenderer(new CodeViewCellRenderer());

      Dimension preferredSize = codeview.getPreferredSize();
      codeview.setSize(preferredSize);

   }

   /**
    * Layout the child components in this view
    */
   private void layoutComponents() {
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      // Position the code view list
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      add(codeview, gbc);

      // Position the VariablesView below the codeview
      gbc.gridy = 1;
      add(variablesView = new VariablesView(), gbc);

      // Position the nextButton below the VariablesView
      gbc.gridy = 2;
      add(nextButton, gbc);

      // Position the autoButton below the nextButton
      gbc.gridy = 3;
      add(autoButton, gbc);

   }

   /**
    * This renders the lines of the code, one line at a time Needs 2 "blank"
    * (single space) lines after nested for loop
    *
    * @param evt
    */
   private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {

      ListModel listmodel = codeview.getModel();

      if (i == listmodel.getSize()) { //If end of code is reached, start over

         i = 0;
      }

      String line = (String) listmodel.getElementAt(i);

      // Recreates for loop functionality
      if (line.contains("for") && line.contains("m")) {

         mIndex = i;
      }
      if (line.contains("for") && line.contains("n")) {

         nIndex = i;
      }

      if (line.equals(" ")) { // "Blank" line indicates end of for loop

         if (mIndex != -1 && m < mMax) {

            i = mIndex;
            m++;

         } else if (m == mMax) {

            mIndex = -1;
            m = 0;
         } else if (nIndex != -1 && n < nMax) {

            i = nIndex;
            n++;

         } else if (n == nMax) {

            nIndex = -1;
            n = 0;

         }
      }

      codeview.setSelectedIndex(i);
      i++;

   }
   
   

   /**
    * This renders the lines of the code automatically using a SwingWorker
    *
    * @param evt
    */
   private void autoButtonActionPerformed(java.awt.event.ActionEvent evt) {

      ListModel listmodel = codeview.getModel();

      RenderWorker worker = new RenderWorker(codeview, listmodel, i, nMax, mMax, n, m, nIndex, mIndex);

      worker.execute();

   }

   /**
    * Display the current model in our child components.
    */
   public void updateView() {

   }
}
