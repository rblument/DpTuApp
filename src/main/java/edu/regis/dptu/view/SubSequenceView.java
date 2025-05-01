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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
    private Step step;
    private JButton guessSubmitButton;
    public JButton stepButton;
    public SubSequenceCanvasView canvas;
    private JTextField guessField;

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
//        titleLabel = new JLabel("Subsequence Highlighter");
//        titleLabel.setForeground(Color.BLACK);
//        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lengthLabel1 = new JLabel("x=" + word1.length());
        lengthLabel1.setFont(new Font("Arial", Font.PLAIN, 16));

        lengthLabel2 = new JLabel("y=" + word2.length());
        lengthLabel2.setFont(new Font("Arial", Font.PLAIN, 16));

        stepButton = new JButton("Step Through LCS");
        stepButton.addActionListener(e -> stepCompleted());

        guessField = new JTextField(10);

        guessSubmitButton = new JButton("Guess LCS");
        guessSubmitButton.addActionListener(e -> guessEntered());

        canvas = new SubSequenceCanvasView(word1, word2);
    }

    /**
     * The components of the view are displayed in specific positions.
     */
    public void layoutComponents() {
        setLayout(new BorderLayout());

        // Displaying words
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.Y_AXIS));
        wordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Line 1: x=13     skullandbones
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        line1.add(lengthLabel1);
        line1.add(new JLabel(canvas.getWord1()));

        // canvas 
        canvas.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Line 2: y=13    lullabybabies
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        line2.add(lengthLabel2);
        line2.add(new JLabel(canvas.getWord2()));

        wordPanel.add(Box.createVerticalStrut(10));
        wordPanel.add(line1);
        wordPanel.add(canvas);
        wordPanel.add(line2);
        wordPanel.add(Box.createVerticalStrut(10));
        //add(guessField);

        // Canvas and buttons added
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        //Guess Panel
        JPanel guessPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guessPanel.add(new JLabel("Guess the LCS"));
        guessPanel.add(guessField);
        guessPanel.add(guessSubmitButton);
        // Button added
        guessPanel.add(stepButton);
        bottomPanel.add(guessPanel);

        add(wordPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }

    // Button trigger
    private void stepCompleted() {
        canvas.problemUpdated(canvas.getModel());
    }

    private void guessEntered() {
        canvas.userGuess(guessField);
    }

}
