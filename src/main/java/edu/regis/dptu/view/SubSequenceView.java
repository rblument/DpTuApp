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
import edu.regis.dptu.model.Step;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This is the Subsequence view for the TutoringSession View. The title, words,
 * and button are displayed. Both words entered to find LCS are shown with the
 * length of each. When the button is pressed, it will display a step-by-step
 * process to finding the LCS and highlight them accordingly.
 *
 * @author Sofia Reyes
 */
public class SubSequenceView extends JPanel {

    private JLabel titleLabel, lengthLabel1, lengthLabel2;
    private JButton lcsButton;
    private Step step;
    public JButton stepButton;
    public SubSequenceCanvasView canvas;

    public SubSequenceView(String word1, String word2) {
        initializeComponents(word1, word2);
        layoutComponents();

        setPreferredSize(new Dimension(300, 100));
    }

    /**
     * The title, words, and length of words are initialized and formatted.
     *
     * @param word1
     * @param word2
     */
    public void initializeComponents(String word1, String word2) {
        titleLabel = new JLabel("Subsequence Highlighter");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lengthLabel1 = new JLabel("x=" + word1.length());
        lengthLabel1.setFont(new Font("Arial", Font.PLAIN, 16));

        lengthLabel2 = new JLabel("y=" + word2.length());
        lengthLabel2.setFont(new Font("Arial", Font.PLAIN, 16));

        lcsButton = new JButton("Step LCS");
        lcsButton.addActionListener(e -> stepThroughLCS());
        
        stepButton = new JButton("Step Completed");
        stepButton.addActionListener(e -> stepCompleted());

        canvas = new SubSequenceCanvasView(word1, word2);
    }

    /**
     * The components of the view are displayed in specific positions.
     */
    public void layoutComponents() {
        setLayout(new BorderLayout());

        // Display 'Subsequence Highlighter'
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Displaying words
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Line 1: x=13     skullandbones
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        line1.add(lengthLabel1);
        line1.add(new JLabel(canvas.getWord1()));

        // Line 2: y=13    lullabybabies
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        line2.add(lengthLabel2);
        line2.add(new JLabel(canvas.getWord2()));

        wordPanel.add(titleLabel);
        wordPanel.add(line1);
        wordPanel.add(line2);

        // nvas added
        JPanel canvasPanel = new JPanel();
        canvasPanel.add(canvas);

        // Button added
        //JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        //buttonPanel.add(lcsButton);
       canvasPanel.add(stepButton, BorderLayout.SOUTH);

        add(titleLabel, BorderLayout.NORTH);
        add(wordPanel, BorderLayout.CENTER);
        add(canvasPanel, BorderLayout.SOUTH);
        //add(buttonPanel, BorderLayout.EAST);
    }

    // Button trigger
    private void stepThroughLCS() {
        canvas.highlightLCS();
    }
    
    private void stepCompleted() {
        canvas.problemUpdated(canvas.getModel());
    }
}
