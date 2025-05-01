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

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;
import edu.regis.dptu.model.Step;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This is the Subsequence Canvas view. Two words are displayed aside each
 * other, and shows a step-by-step process to finding the LCS.
 *
 * @author Sofia Reyes
 */
public class SubSequenceCanvasView extends JPanel implements ProblemListener {

    private String mainSeq;
    private String subSeq;
    private String lcs;
    private int highlightIndex = 0;
    private Problem model;
    private Step step;
    private int arrowIndex;

    /**
     * Constructor that initiates the sequences, LCS, and model
     *
     * @param word1
     * @param word2
     */
    public SubSequenceCanvasView(String word1, String word2) {
        mainSeq = word1;
        subSeq = word2;
        lcs = findLCS(mainSeq, subSeq);

        // create problem
        model = new LCSProblem(mainSeq, subSeq);
        model.addProblemListener(this);

        setLayout(null);
        setPreferredSize(new Dimension(100, 100));
    }

    /**
     * This returns the first word being used to compare lcs.
     *
     * @return mainSeq
     */
    public String getWord1() {
        return mainSeq;
    }

    /**
     * This returns the second word being used to compare lcs.
     *
     * @return subSeq
     */
    public String getWord2() {
        return subSeq;
    }

    /**
     * Returns the updated model to the Problem
     *
     * @return
     */
    public Problem getModel() {
        return model;
    }

    /**
     * This is the logic to find the longest common sequence (LCS). Builds a DP
     * table and backtracks to construct the LCS string
     *
     * @param main
     * @param sub
     */
    private String findLCS(String main, String sub) {
        int[][] dp = new int[main.length() + 1][sub.length() + 1];

        for (int i = 1; i <= main.length(); i++) {
            for (int j = 1; j <= sub.length(); j++) {
                if (main.charAt(i - 1) == sub.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // Backtracking
        StringBuilder lcsBuild = new StringBuilder();
        int i = main.length(), j = sub.length();
        while (i > 0 && j > 0) {
            if (main.charAt(i - 1) == sub.charAt(j - 1)) {
                lcsBuild.append(main.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcsBuild.reverse().toString();
    }

    /**
     * When the button is pressed, moves the highlight forward
     */
    public void highlightLCS() {
        if (highlightIndex < lcs.length() - 1) {
            highlightIndex++;
            repaint();
        }
    }

    /**
     * This displays the two words onto the canvas view, and when the button is
     * clicked, then it will find the common letters shared.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new Font("Arial", Font.PLAIN, 20));

        if (mainSeq == null || subSeq == null) {
            g.setColor(Color.RED);
            g.drawString("Invalid input!", 20, 30);
        }

        int x1 = 20;
        int y1 = 30;
        g2.setColor(Color.BLACK);
        g2.drawString(mainSeq, x1, y1);

        int x2 = 20;
        int y2 = 60;
        g2.drawString(subSeq, x2, y2);

        g2.setColor(Color.red);
        int count = 0;
        int lcsIndex = 0;
        for (char c : lcs.toCharArray()) {
            int idx1 = mainSeq.indexOf(c, lcsIndex);
            int idx2 = subSeq.indexOf(c, lcsIndex);

            if (count < highlightIndex && idx1 != -1 && idx2 != -1) {
                g2.drawString(String.valueOf(c), x1 + g.getFontMetrics().stringWidth(mainSeq.substring(0, idx1)), y1);
                g2.drawString(String.valueOf(c), x2 + g.getFontMetrics().stringWidth(subSeq.substring(0, idx2)), y2);
                lcsIndex = idx1 + 1;
                count++;
            }

        }

    }

    // Previous code to show both words in canvas view. this worked before adding button aspects
//        int x1 = 20;
//        int y1 = 30;
//        g.setColor(Color.BLACK);
//        g.drawString(mainSeq, x1, y1);
//
//        int x2 = 20;
//        int y2 = 60;
//        g.drawString(subSeq, x2, y2);
//
//        g.setColor(Color.RED);
//        int lcsIndex = 0;
//        for (char c : lcs.toCharArray()) {
//            int idx1 = mainSeq.indexOf(c, lcsIndex);
//            int idx2 = subSeq.indexOf(c, lcsIndex);
//            if (idx1 != -1 && idx2 != -1) {
//                g.drawString(String.valueOf(c), x1 + g.getFontMetrics().stringWidth(mainSeq.substring(0, idx1)), y1);
//                g.drawString(String.valueOf(c), x2 + g.getFontMetrics().stringWidth(subSeq.substring(0, idx2)), y2);
//                lcsIndex = idx1 + 1;
//            }
//        }
    /**
     * When the button is pressed, update the model to the current step in the
     * problem, then display the next step.
     *
     * @param problem
     */
    @Override
    public void problemUpdated(Problem problem) {
        this.model = problem;

        highlightLCS();
    }

    /**
     * Evaluates the users guess
     *
     * @param guess
     */
    public void userGuess(JTextField guess) {
        String userGuess = guess.getText().trim();
        System.out.println("User guessed: '" + userGuess + "', actual LCS: '" + lcs + "'");
        if (userGuess.equalsIgnoreCase(lcs)) {
            JOptionPane.showMessageDialog(this, "Correct! You found the LCS: " + lcs);

            // TODO: update the students progress, display another word?
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect. Lets walk through it. Click the 'Step Through LCS' button.");
        }
    }
}
