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
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.ProblemListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * This is the Subsequence view for the TutoringSession View. The title, words,
 * and button are displayed. Both words entered to find LCS are shown with the
 * length of each. When the button is pressed, it will display a step-by-step
 * process to finding the LCS and highlight them accordingly.
 *
 * @author Sofia Reyes
 */
 class SubSequenceView extends JPanel implements ProblemListener {
     
    private static final java.util.logging.Logger LOGGER =
            java.util.logging.Logger.getLogger(SubSequenceView.class.getName());

    private JLabel titleLabel, lengthLabel1, lengthLabel2, wordLabel1, wordLabel2;
    private JButton stepButton;
    public SubSequenceCanvasView canvas;
    private Problem model;

    public SubSequenceView(String word1, String word2) {
        initializeComponents(word1, word2);
        layoutComponents();

        setPreferredSize(new Dimension(600, 300));
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

        stepButton = new JButton("Step LCS");
        stepButton.addActionListener(e -> stepThroughLCS());

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

        // Changed (April 17, 2025 - EverettCV): Now loads default value of the first variable originally, but updates the line1 appropriately when inputs change.
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        line1.add(lengthLabel1);
        wordLabel1 = new JLabel(canvas.getWord1());
        line1.add(wordLabel1);

        // Changed (April 17, 2025 - EverettCV): Now loads default value of the second variable originally, but updates the line2 appropriately when inputs change.
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        line2.add(lengthLabel2);
        wordLabel2 = new JLabel(canvas.getWord2());
        line2.add(wordLabel2);

        wordPanel.add(line1);
        wordPanel.add(line2);

        // nvas added
        JPanel canvasPanel = new JPanel();
        canvasPanel.add(canvas);

        // Button added
        JPanel buttonPanel = new JPanel(new FlowLayout());
        canvasPanel.add(stepButton);

        add(titleLabel, BorderLayout.NORTH);
        add(wordPanel, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Button trigger
    private void stepThroughLCS() {
        canvas.highlightLCS();
    }

    /**
     * Updates the displayed input strings and lengths when new inputs are submitted.
     * 
     * Changes (April 17, 2025):
     * - Dynamically updates all labels and canvas contents.
     * - Forces revalidation and repaint to ensure view reflects new inputs.
     * 
     * TODO: In the future, improve resizing to dynamically fit very long words.
     * 
     * @author EverettCV
     * 
     * @param word1 Updated first string input
     * @param word2 Updated second string input
     */
    public void updateWords(String word1, String word2) {
        // Update lengths
        lengthLabel1.setText("x=" + word1.length());
        lengthLabel2.setText("x=" + word2.length());

        wordLabel1.setText(word1);
        wordLabel2.setText(word2);

        canvas.setWord1(word1);
        canvas.setWord2(word2);


        canvas.setPreferredSize(new Dimension(600, 300));
        canvas.revalidate();
        canvas.repaint();

        repaint();
        revalidate();
    }
    
    /**
     * Bind this view to a specific problem model.
     * Registers as a ProblemListener so we get notified on updates.
     * Immediately triggers an update to sync the UI with the model.
     *
     * @author Harrison Sherwin
     */
    public void setModel(Problem model) {
        
        this.model = model;
        
        if (this.model != null) {
            
            this.model.addProblemListener(this);
            
            LOGGER.log(Level.INFO,
                    "SubSequenceView: model set ({0}), updating view",
                    this.model.getClass().getSimpleName());
            
            updateView();
        } else {
            LOGGER.warning("SubSequenceView: setModel called with a null model");
        }
    }
    
    /**
     * Called automatically whenever the bound Problem changes.
     * Runs on the Swing event dispatch thread via SwingUtilities.
     * Ensures UI refresh is thread-safe.
     * 
     * @author Harrison Sherwin
     */
    @Override
    public void problemUpdated(Problem problem) {
         
        LOGGER.log(Level.FINE, "SubSequenceView: problemUpdated called");
        
        // Update the UI on the Swing thread to avoid race conditions.
        SwingUtilities.invokeLater(this::updateView);
    }
    
    /**
     * Refresh the words displayed in this view based on the model.
     * If the model is an LCSProblem, extract x and y strings.
     * Push those strings into updateWords(), which updates the labels and canvas.
     * For non-LCS problems, log but skip the update.
     * 
     * @author hsherwin@regis.edu
     */
    private void updateView() {
        if (model instanceof LCSProblem lcs) {
            // Replace nulls with empty strings.
            String x = (lcs.getX() == null) ? "" : lcs.getX();
            String y = (lcs.getY() == null) ? "" : lcs.getY();

            // Update the view with the current words.
            updateWords(x, y);
        } else if (model != null) {
            LOGGER.log(Level.FINE,
                    "SubSequenceView: model is not LCSProblem; "
                            + "no word update performed");
        }
        
        // setModel() handles if(model = null).
     }
}
