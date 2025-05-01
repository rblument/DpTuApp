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
import java.awt.BorderLayout;
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

/**
 * This is the Subsequence view for the TutoringSession View. The title, words,
 * and button are displayed. Both words entered to find LCS are shown with the
 * length of each. When the button is pressed, it will display a step-by-step
 * process to finding the LCS and highlight them accordingly.
 *
 * @author Sofia Reyes
 */
public class SubSequenceView extends JPanel {

    private JLabel lengthLabel1, lengthLabel2;
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
     * Initializes UI components including labels, buttons, input fields, and
     * canvas view
     *
     * @param word1
     * @param word2
     */
    public void initializeComponents(String word1, String word2) {

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
     * Lays out the UI elements with panels and layout managers
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

    /**
     * When button is clicked, updates the model about the users progress on the
     * model
     */
    private void stepCompleted() {
        canvas.problemUpdated(canvas.getModel());
    }

    /**
     * When user guesses an input, compares it to LCS in model and determines if
     * it's correct
     */
    private void guessEntered() {
        canvas.userGuess(guessField);
    }

}
