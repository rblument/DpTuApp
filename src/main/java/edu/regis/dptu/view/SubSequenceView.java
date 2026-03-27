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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;
import edu.regis.dptu.util.ReusableFonts;
import edu.regis.dptu.util.ResourceMgr;

/**
 * This is the Subsequence view for the TutoringSession View.
 *
 * <p>Displays the current LCS inputs (x, y) and renders a canvas that highlights characters
 * belonging to the LCS as the alg executes. Highlighting is driven by model updates
 * (ProblemListener), specifically when BACKTRACKING
 *
 * @author Sofia Reyes Most recent updates Feb 15, 2026 Lindsey C
 */
class SubSequenceView extends JPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(SubSequenceView.class);

    private static final int MAX_LABEL_CHARS = 20;
    private static final int CHAR_WIDTH_PX = 16;
    private static final int MIN_CANVAS_WIDTH = 100;
    private static final int MAX_CANVAS_WIDTH = 4000;
    private static final int CANV_HORIZONTAL_PADDING = 40;
    private static final int DEFAULT_CANV_HEIGHT = 300;
    // cache the last displayed words so we only reset the canvas when inputs truly change
    // prevents per-step model updates from clearing previously highlighted characters
    private String lastX = null;
    private String lastY = null;

    private JLabel titleLabel, lengthLabel1, lengthLabel2, wordLabel1, wordLabel2;
    private JButton stepButton;
    public SubSequenceCanvasView canvas;

    private JScrollPane canvasScrollPane;

    private Problem model;

    public SubSequenceView() {
        initializeComponents();
        layoutComponents();

        setPreferredSize(new Dimension(600, 300));
    }

    /**
     * The title, words, and length of words are initialized and formatted.
     *
     * @param word1
     * @param word2
     */
    private void initializeComponents() {
        titleLabel = new JLabel(ResourceMgr.instance().string("subSequence.title"));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(ReusableFonts.instance().getFont("18ptBold"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lengthLabel1 = new JLabel();
        lengthLabel1.setFont(ReusableFonts.instance().getFont("16ptLabel"));

        lengthLabel2 = new JLabel();
        lengthLabel2.setFont(ReusableFonts.instance().getFont("16ptLabel"));

        canvas = new SubSequenceCanvasView("word1", "word2");
    }

    /** The components of the view are displayed in specific positions. */
    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        // Display 'Subsequence Highlighter'
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Displaying words
        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Changed (April 17, 2025 - EverettCV): Now loads default value of the first variable
        // originally, but updates the line1 appropriately when inputs change.
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        line1.add(lengthLabel1);
        wordLabel1 = new JLabel();
        wordLabel1.setFont(ReusableFonts.instance().getFont("16ptLabel"));
        line1.add(wordLabel1);

        // Changed (April 17, 2025 - EverettCV): Now loads default value of the second variable
        // originally, but updates the line2 appropriately when inputs change.
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        line2.add(lengthLabel2);
        wordLabel2 = new JLabel();
        wordLabel2.setFont(ReusableFonts.instance().getFont("16ptLabel"));
        line2.add(wordLabel2);

        wordPanel.add(line1);
        wordPanel.add(line2);

        topPanel.add(wordPanel, BorderLayout.SOUTH);

        // nvas added
        canvasScrollPane = new JScrollPane(canvas);
        canvasScrollPane.setBorder(null);
        canvasScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        canvasScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        canvasScrollPane.getViewport().setBackground(Color.WHITE);
        // removed old "Step LCS" button. Highlighting is now driven by the model during
        // execution/backtracking (ProblemListener updates), not manual stepping in this view

        add(topPanel, BorderLayout.NORTH);
        add(canvasScrollPane, BorderLayout.CENTER);
    }

    /**
     * Updates the displayed input strings and lengths when new inputs are submitted.
     *
     * <p>Changes (April 17, 2025): - Dynamically updates all labels and canvas contents. - Forces
     * revalidation and repaint to ensure view reflects new inputs.
     *
     * <p>Note: Updating the inputs resets the canvas highlight state. During normal step execution
     * the inputs do not change, so highlights persist across model updates
     *
     * @author EverettCV Recently updated 2/15/2026 Lindsey C
     * @param word1 Updated first string input
     * @param word2 Updated second string input
     */
    public void updateWords(String word1, String word2) {
        if (word1 == null) word1 = "";
        if (word2 == null) word2 = "";

        // Update lengths
        lengthLabel1.setText(ResourceMgr.instance().string("subSequence.length.n", word1.length()));
        lengthLabel2.setText(ResourceMgr.instance().string("subSequence.length.m", word2.length()));

        String compactOne = compactWord(word1);
        String compactTwo = compactWord(word2);

        wordLabel1.setText(ResourceMgr.instance().string("subSequence.word.x", compactOne));
        wordLabel2.setText(ResourceMgr.instance().string("subSequence.word.y", compactTwo));

        wordLabel1.setToolTipText(word1.isEmpty() ? null : word1);
        wordLabel2.setToolTipText(word2.isEmpty() ? null : word2);

        canvas.setWord1(word1);
        canvas.setWord2(word2);

        int maxLen = Math.max(word1.length(), word2.length());

        int estWidth = maxLen * CHAR_WIDTH_PX;

        int desiredWidth = estWidth + CANV_HORIZONTAL_PADDING;
        desiredWidth = Math.max(MIN_CANVAS_WIDTH, desiredWidth);
        desiredWidth = Math.min(MAX_CANVAS_WIDTH, desiredWidth);

        int desiredHeight = DEFAULT_CANV_HEIGHT;
        if (canvas.getPreferredSize() != null) desiredHeight = canvas.getPreferredSize().height;

        canvas.setPreferredSize(new Dimension(desiredWidth, desiredHeight));
        // Parent revalidate/repaint at the end will refresh the canvas; avoid redundant calls here.
        // canvas.revalidate();
        // canvas.repaint();

        if (canvasScrollPane != null) canvasScrollPane.revalidate();

        revalidate();
        repaint();
    }

    /**
     * Bind this view to a specific problem model. Registers as a ProblemListener so we get notified
     * on updates. Immediately triggers an update to sync the UI with the model.
     *
     * @author Harrison Sherwin
     */
    public void setModel(Problem model) {

        this.model = model;
        // Reset cached input tracking when we bind a new model so the first updateView()
        // call refreshes labels/canvas.
        lastX = null;
        lastY = null;

        if (this.model != null) {

            this.model.addProblemListener(this);

            log.info(
                    "SubSequenceView: model set ({}), updating view",
                    this.model.getClass().getSimpleName());

            updateView();
        } else {
            log.warn("SubSequenceView: setModel called with a null model");
        }
    }

    /**
     * Called automatically whenever the bound Problem changes. Runs on the Swing event dispatch
     * thread via SwingUtilities. Ensures UI refresh is thread-safe.
     *
     * @author Harrison Sherwin
     */
    @Override
    public void problemUpdated(Problem problem) {

        log.debug("SubSequenceView: problemUpdated called");

        // Update the UI on the Swing thread to avoid race conditions.
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Refresh the words displayed in this view based on the model. If the model is an LCSProblem,
     * extract x and y strings. Push those strings into updateWords(), which updates the labels and
     * canvas. For non-LCS problems, log but skip the update.
     *
     * <p>Two independent concerns happen here:
     *
     * <ul>
     *   <li>If x/y changed, update labels + reset canvas highlight state via updateWords().
     *   <li>
     *   <li>If the model reports a committed LCS match (during backtracking), apply a persistent
     *       highlight to the canvas at the reported indices.
     *   <li>
     *       <ul>
     *         <p>We cache lastX/lastY so per-step model updates do not repeatedly reset the canvas
     *         (which would erase previously highlighted chars)
     *
     * @author hsherwin@regis.edu Last updated 2/15/2026 Lindsey C
     */
    private void updateView() {
        if (model instanceof LCSProblem lcs) {
            // Replace nulls with empty strings.
            String x = (lcs.getX() == null) ? "" : lcs.getX();
            String y = (lcs.getY() == null) ? "" : lcs.getY();

            boolean wordsChanged = !x.equals(lastX) || !y.equals(lastY);
            if (wordsChanged) {
                log.debug("SubSequenceView: words changed; updating labels/canvas");
                updateWords(x, y);
                lastX = x;
                lastY = y;
            }

            // Apply the most recently committed solution match from the model (if any).
            // Indices are 0-based string positions (not DP table coordinates).
            int mx = lcs.getLastMatchX();
            int my = lcs.getLastMatchY();
            log.debug("SubSequenceView: lastMatchX={}, lastMatchY={}", mx, my);

            if (mx >= 0 && my >= 0) {
                log.debug("SubSequenceView: applying highlightAt({}, {})", mx, my);
                canvas.highlightAt(mx, my);
            }
        } else if (model != null) {
            log.debug("SubSequenceView: model is not LCSProblem; " + "no word update performed");
        }

        // setModel() handles if(model = null).
    }

    private String compactWord(String word) {
        if (word == null) {
            return "";
        }

        if (word.length() <= MAX_LABEL_CHARS) {
            return word;
        }

        int keep = MAX_LABEL_CHARS / 3;
        String start = word.substring(0, keep);
        String end = word.substring(word.length() - keep);

        return start + "..." + end;
    }
}
