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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Subsequence Canvas view. Two words are displayed aside each other, and shows a
 * step-by-step process to finding the LCS.
 *
 * @author Sofia Reyes
 */
public class SubSequenceCanvasView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(SubSequenceCanvasView.class);

    private String word1;
    private String word2;
    private String lcs;
    private int highlightIndex = 0;

    /**
     * @param word1
     * @param word2
     */
    public SubSequenceCanvasView(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;

        lcs = findLCS(this.word1, this.word2);

        setLayout(null);
        setPreferredSize(new Dimension(600, 300));

        log.debug(
                "SubSequenceCanvasView initialized: word1Len={}, word2Len={}, lcsLen={}",
                this.word1 != null ? this.word1.length() : -1,
                this.word2 != null ? this.word2.length() : -1,
                this.lcs != null ? this.lcs.length() : -1
        );
    }

    /**
     * This returns the first word being used to compare lcs.
     *
     * @return mainSeq
     */
    public String getWord1() {
        return word1;
    }

    /**
     * This returns the second word being used to compare lcs.
     *
     * @return subSeq
     */
    public String getWord2() {
        return word2;
    }

    /**
     * This is the logic to find the longest common sequence (LCS).
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
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcsBuild.reverse().toString();
    }

    /** When the button is pressed, it highlights the sequence of the two words. */
    public void highlightLCS() {
        if (lcs == null) {
            log.warn("highlightLCS called but lcs is null");
            return;
        }

        if (highlightIndex < lcs.length()) {
            highlightIndex++;
            log.debug("Highlight advanced: highlightIndex={}/{}", highlightIndex, lcs.length());
            repaint();
        } else {
            log.debug("Highlight already complete: highlightIndex={}/{}", highlightIndex, lcs.length());
        }
    }

    /**
     * This displays the two words onto the canvas view, and when the button is clicked, then it
     * will find the common letters shared.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        if (word1 == null || word2 == null) {
            g.setColor(Color.RED);
            g.drawString("Invalid input!", 20, 30);
        }

        // draw the words in black
        int x1 = 20;
        int y1 = 30;
        g.setColor(Color.BLACK);
        g.drawString(word1, x1, y1);

        int x2 = 20;
        int y2 = 60;
        g.drawString(word2, x2, y2);

        // draw over the black with green
        g.setColor(new Color(0, 220, 0));
        int count = 0;

        int idx1 = word1.length() - 1;
        int idx2 = word2.length() - 1;

        if (lcs == null) {
            return;
        }

        for (int i = lcs.length() - 1; i >= 0; i--) {
            idx1 = word1.lastIndexOf(lcs.charAt(i), idx1);
            idx2 = word2.lastIndexOf(lcs.charAt(i), idx2);
            if (count < highlightIndex && idx1 != -1 && idx2 != -1) {
                g.drawString(
                        String.valueOf(lcs.charAt(i)),
                        x1 + g.getFontMetrics().stringWidth(word1.substring(0, idx1)),
                        y1);
                g.drawString(
                        String.valueOf(lcs.charAt(i)),
                        x2 + g.getFontMetrics().stringWidth(word2.substring(0, idx2)),
                        y2);
                count++;
            }
            idx1--;
            idx2--;
        }
    }

    /**
     * Updates the first string (main sequence) displayed on the canvas.
     *
     * <p>Changes (April 17, 2025): - Dynamically updates mainSeq. - Recomputes LCS based on the
     * updated string. - Resets highlight progress for fresh stepping through LCS.
     *
     * @author EverettCV
     * @param word1 The new main sequence string
     */
    public void setWord1(String word1) {
        this.word1 = word1;
        lcs = findLCS(this.word1, word2);
        highlightIndex = 0;
        repaint();
    }

    /**
     * Updates the second string (sub sequence) displayed on the canvas.
     *
     * <p>Changes (April 17, 2025): - Dynamically updates subSeq. - Recomputes LCS based on the
     * updated string. - Resets highlight progress for fresh stepping through LCS.
     *
     * @author EverettCV
     * @param word2 The new sub sequence string
     */
    public void setWord2(String word2) {
        this.word2 = word2;
        lcs = findLCS(word1, this.word2);
        highlightIndex = 0;
                this.lcs != null ? this.lcs.length() : -1
        );

        repaint();
    }
}
