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

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Displays the result of a student completing a step, which gives the student
 * a suggestion as to what to do next, but allows alternative choices.
 * 
 * @author rickb
 */
public class StepCompletionReplyView extends GPanel implements ActionListener {
    /**
     * Message displayed to the student.
     */
    private JTextArea msg;
    
    /**
     * A CardLayout panel allowing for different configurations of buttons.
     */
    private JPanel buttonPanel;
    
    /**
     * Allows the student to select a new task.
     */
    private JButton nextTaskBut;
    
    /**
     * Allows the student to select to solve the same task/step again.
     */
    private JButton sameProblemBut;
    
    /**
     * Allows the student to select to solve a similar task/step again.
     */
    private JButton similarProblemBut;
    
    /**
     * Allows the student to see the correct answers.
     */
    private JButton correctAnswerBut;
    
    public StepCompletionReplyView() {
        
        initializeComponents();
        layoutComponents();
        
        selectPanel("ButtonPanel_3");
        
                System.out.println("Size: " + correctAnswerBut.getSize());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
        /**
     * Display the child view with the given name.
     * 
     * @param name a StepSelection
     */
    private void selectPanel(String name) {
        CardLayout cl = (CardLayout) buttonPanel.getLayout();
       
        cl.show(buttonPanel, name);
    }
    
    /**
     * Initialize the child components in this view.
     */
    private void initializeComponents() {
        msg = new JTextArea(5,80);
        
        nextTaskBut = new JButton("Move on to Next Task");
    
        sameProblemBut = new JButton("Try Same problem Again");
    
        similarProblemBut = new JButton("Try a Similar Problem");
    
        correctAnswerBut = new JButton("Show the Correct Answer");
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new CardLayout());
        
        buttonPanel.add(initButtonPanel1(), "ButtonPanel_1");
        buttonPanel.add(initButtonPanel2(), "ButtonPanel_2");
        buttonPanel.add(initButtonPanel3(), "ButtonPanel_3");
  
    }
    
    /**
     * Create, layout, and return the first button panel.
     * 
     * @return 
     */
    private GPanel initButtonPanel1() {
        GPanel panel = new GPanel();
        
        panel.addc(nextTaskBut, 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        panel.addc(sameProblemBut, 1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        panel.addc(similarProblemBut, 2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        return panel;
    }
    
    /**
     * Create, layout, and return the second button panel.
     * 
     * @return 
     */
    private GPanel initButtonPanel2() {
        GPanel panel = new GPanel();
        
        panel.addc(sameProblemBut, 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        panel.addc(similarProblemBut, 1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        return panel;
    }
    
    /**
     * Create, layout, and return the third button panel. 
     * 
     * @return 
     */
    private GPanel initButtonPanel3() {
        GPanel panel = new GPanel();
        
        panel.addc(correctAnswerBut, 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        panel.addc(sameProblemBut, 1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        panel.addc(similarProblemBut, 2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    5, 5, 5, 5);
        
        return panel;
    }
    
    /**
     * Layout the primary child components in this view.
     */
    private void layoutComponents() {
        addc(msg, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 20, 5);
        
        addc(buttonPanel, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);
    } 
}
