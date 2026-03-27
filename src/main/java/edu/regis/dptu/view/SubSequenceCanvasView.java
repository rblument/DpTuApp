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

import edu.regis.dptu.util.ReusableFonts;
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
    private boolean loggedMissingHighlightState = false;
    private boolean[] w1Highlighted; // word 1 highlighted letters
    private boolean[] w2Highlighted; // word 2 highlighted letters
    private static final Font WORD_FONT = new Font("Arial", Font.PLAIN, 20);

    /**
     * @param word1
     * @param word2
     */
    public SubSequenceCanvasView(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;

        initHighlightState();

        setLayout(null);
        setPreferredSize(new Dimension(600, 300));

        log.debug(
                "SubSequenceCanvasView initialized: word1Len={}, word2Len={}, highlightStateReady={}",
                this.word1 != null ? this.word1.length() : -1,
                this.word2 != null ? this.word2.length() : -1,
                (w1Highlighted != null && w2Highlighted != null));
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
     * Paint the two input words, then overlay any chars that have been marked as part of the LCS
     * solution path
     *
     * <p>Highlight state is maintained as per-char boolean arrays that persist across repaints. The
     * model/view controller calls highlightAt(xIdx, yIdx) as backtracking commits each character
     * into the solution
     *
     * @param Graphics g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(WORD_FONT);

        if (word1 == null || word2 == null) {
            g.setColor(Color.RED);
            g.drawString("Invalid input!", 20, 30);
            return;
        }

        final int x = 20;
        final int y1 = 30;
        final int y2 = 60;

        // draw the words in black
        g.setColor(Color.BLACK);
        g.drawString(word1, x, y1);
        g.drawString(word2, x, y2);

        // if highlight state isn't initialized yet, nothing to overlay, just render base text
        if (w1Highlighted == null || w2Highlighted == null) {
            if (!loggedMissingHighlightState) {
                log.debug(
                        "paintComponent called before highlight state initialized: word1Highlighted={}, word2Highlighted={}",
                        w1Highlighted == null,
                        w2Highlighted == null);
                loggedMissingHighlightState = true;
            }
            return;
        }
        loggedMissingHighlightState = false; // reset once state exists

        // overlay highlighted characters in green at their exact positions
        g.setColor(new Color(0, 220, 0));
        var fm = g.getFontMetrics();

        // word1 highlights
        int maxI = Math.min(word1.length(), w1Highlighted.length);
        for (int i = 0; i < maxI; i++) {
            if (!w1Highlighted[i]) continue;
            int px = x + fm.stringWidth(word1.substring(0, i));
            g.drawString(String.valueOf(word1.charAt(i)), px, y1);
        }

        // word2 highlights
        int maxJ = Math.min(word2.length(), w2Highlighted.length);
        for (int j = 0; j < maxJ; j++) {
            if (!w2Highlighted[j]) continue;
            int px = x + fm.stringWidth(word2.substring(0, j));
            g.drawString(String.valueOf(word2.charAt(j)), px, y2);
        }
    }

    /**
     * Initializes (or resets) the per-character highlight state for both input words
     *
     * <p>This method should be called whenever the input strings change or when the alg is reset.
     * Each char in word1 and word2 gets a corresponding boolean flag indicating whether it has been
     * "naturally" matched by the LCS alg during execution
     */
    private void initHighlightState() {
        if (word1 == null || word2 == null) {
            log.warn(
                    "initHighlightState called with null input(s): word1Null={}, word2Null={}",
                    word1 == null,
                    word2 == null);
        }

        w1Highlighted = (word1 == null) ? null : new boolean[word1.length()];
        w2Highlighted = (word2 == null) ? null : new boolean[word2.length()];

        log.debug(
                "Highlight state initialized: word1Len={}, word2Len={}",
                word1 != null ? word1.length() : -1,
                word2 != null ? word2.length() : -1);
    }

    /**
     * Highlights the characters at the specified indices in each input word
     *
     * <p>This method is intended to be called by the LCS alg controller when a matching character
     * pair is encountered during step-by-step execution Highlighted chars persist until view is
     * reset
     *
     * @param xIdx zero-based index into word1 (x string)
     * @param yIdx zero-based index into word2 (y string)
     */
    public void highlightAt(int xIdx, int yIdx) {

        if (w1Highlighted == null || w2Highlighted == null) {
            log.warn(
                    "highlightAt called before highlight state initialized: xIdx={}, yIdx={}",
                    xIdx,
                    yIdx);
            return;
        }

        if (xIdx < 0 || xIdx >= w1Highlighted.length) {
            log.error(
                    "highlightAt received out of bounds xIdx: {} (word1Len={})",
                    xIdx,
                    w1Highlighted.length);
            return;
        }
        if (yIdx < 0 || yIdx >= w2Highlighted.length) {
            log.error(
                    "highlightAt received out of bounds yIdx: {} (word2Len={})",
                    yIdx,
                    w2Highlighted.length);
            return;
        }

        // apply persistent highlight
        w1Highlighted[xIdx] = true;
        w2Highlighted[yIdx] = true;

        log.debug(
                "Highlight applied: word1[{}]='{}', word2[{}]='{}'",
                xIdx,
                word1 != null && xIdx < word1.length() ? word1.charAt(xIdx) : '?',
                yIdx,
                word2 != null && yIdx < word2.length() ? word2.charAt(yIdx) : '?');

        repaint();
    }

    /**
     * Updates the first input string (x) displayed on canvas
     *
     * <p>Changes (Feb 15, 2026): - resets any existing character highlights, since previously
     * highlighted indices may no longe rbe valid for new input. LCS is not computed here;
     * highlighting driven externally by the algorithm controller
     *
     * @author Lindsey Cox
     * @param word1 the new first input string (x)
     */
    public void setWord1(String word1) {
        this.word1 = word1;
        initHighlightState(); // reset highlight arrays for the new word(s)

        log.debug(
                "word1 updated: word1Len={}, word2Len={}, highlightsReset={}",
                this.word1 != null ? this.word1.length() : -1,
                this.word2 != null ? this.word2.length() : -1,
                true);
        repaint();
    }

    /**
     * Updates the second input string (y) displayed on canvas
     *
     * <p>Changes (Feb 15, 2026): - resets any existing character highlights, since previously
     * highlighted indices may no longe rbe valid for new input. LCS is not computed here;
     * highlighting driven externally by the algorithm controller
     *
     * @author Lindsey Cox
     * @param word2 The new second input string (y)
     */
    public void setWord2(String word2) {
        this.word2 = word2;
        initHighlightState(); // reset highlight arrays for the new word(s)

        log.debug(
                "word2 updated: word1Len={}, word2Len={}, highlightsReset={}",
                this.word1 != null ? this.word1.length() : -1,
                this.word2 != null ? this.word2.length() : -1,
                true);
        repaint();
    }

    /** */
    public void resetHighlights() {
        initHighlightState();
        repaint();
    }
}
