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
import java.awt.FontMetrics;

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

/**
 * This is the Subsequence view for the TutoringSession View. The title, words, and button are
 * displayed. Both words entered to find LCS are shown with the length of each. When the button is
 * pressed, it will display a step-by-step process to finding the LCS and highlight them
 * accordingly.
 *
 * @author Sofia Reyes
 */
class SubSequenceView extends JPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(SubSequenceView.class);

    private static final java.util.logging.Logger julLogger =
            java.util.logging.Logger.getLogger(SubSequenceView.class.getName());

    private static final int MAX_LABEL_CHARS = 20;
    private static final int CHAR_WIDTH_PX = 16;
    private static final int MIN_CANVAS_WIDTH = 100;
    private static final int MAX_CANVAS_WIDTH = 4000;
    private static final int CANv_HORIZONTAL_PADDING = 40;
    private static final int DEFAULT_CANV_HEIGHT = 300;

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
        titleLabel = new JLabel("Subsequence Highlighter");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        lengthLabel1 = new JLabel();
        lengthLabel1.setFont(new Font("Dialog", Font.PLAIN, 16));

        lengthLabel2 = new JLabel();
        lengthLabel2.setFont(new Font("Dialog", Font.PLAIN, 16));

        stepButton = new JButton("Step LCS");
        stepButton.addActionListener(e -> stepThroughLCS());

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
        wordLabel1.setFont(new Font("Dialog", Font.PLAIN, 16));
        line1.add(wordLabel1);

        // Changed (April 17, 2025 - EverettCV): Now loads default value of the second variable
        // originally, but updates the line2 appropriately when inputs change.
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        line2.add(lengthLabel2);
        wordLabel2 = new JLabel();
        wordLabel2.setFont(new Font("Dialog", Font.PLAIN, 16));
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
        // Button added
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(stepButton);

        add(topPanel, BorderLayout.NORTH);
        add(canvasScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Button trigger
    private void stepThroughLCS() {
        canvas.highlightLCS();
    }

    //    public void setModel(Problem currentProblem) {
    //        if (currentProblem != null && currentProblem instanceof LCSProblem) {
    //            this.updateWords(
    //                    ((LCSProblem) currentProblem).getX(), ((LCSProblem)
    // currentProblem).getY());
    //
    //            setVisible(true);
    //        } else if (currentProblem == null) {
    //            setVisible(false);
    //        }
    //    }

    /**
     * Updates the displayed input strings and lengths when new inputs are submitted.
     *
     * <p>Changes (April 17, 2025): - Dynamically updates all labels and canvas contents. - Forces
     * revalidation and repaint to ensure view reflects new inputs.
     *
     * <p>TODO: In the future, improve resizing to dynamically fit very long words.
     *
     * @author EverettCV
     * @param word1 Updated first string input
     * @param word2 Updated second string input
     */
    public void updateWords(String word1, String word2) {
        if (word1 == null) word1 = "";
        if (word2 == null) word2 = "";

        // Update lengths
        lengthLabel1.setText("n=" + word1.length());
        lengthLabel2.setText("m=" + word2.length());

        String compactOne = compactWord(word1);
        String compactTwo = compactWord(word2);

        wordLabel1.setText("x=" + compactOne);
        wordLabel2.setText("y=" + compactTwo);

        wordLabel1.setToolTipText(word1.isEmpty() ? null : word1);
        wordLabel2.setToolTipText(word2.isEmpty() ? null : word2);

        canvas.setWord1(word1);
        canvas.setWord2(word2);

        // Get text font metrics no matter what we change font to
        FontMetrics fm = canvas.getFontMetrics(canvas.getFont());

        int width1 = fm.stringWidth(word1);
        int width2 = fm.stringWidth(word2);
        int maxTextWidth = Math.max(width1, width2);

        int desiredWidth = maxTextWidth + CANv_HORIZONTAL_PADDING;
        desiredWidth = Math.max(MIN_CANVAS_WIDTH, desiredWidth);
        desiredWidth = Math.min(MAX_CANVAS_WIDTH, desiredWidth);

        int desiredHeight = DEFAULT_CANV_HEIGHT;
        if (canvas.getPreferredSize() != null) desiredHeight = canvas.getPreferredSize().height;

        canvas.setPreferredSize(new Dimension(desiredWidth, desiredHeight));
        canvas.revalidate();
        canvas.repaint();

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

        if (this.model != null) {

            this.model.addProblemListener(this);

            julLogger.log(
                    java.util.logging.Level.INFO,
                    "SubSequenceView: model set ({0}), updating view",
                    this.model.getClass().getSimpleName());

            updateView();
        } else {
            julLogger.warning("SubSequenceView: setModel called with a null model");
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

        julLogger.log(java.util.logging.Level.FINE, "SubSequenceView: problemUpdated called");

        // Update the UI on the Swing thread to avoid race conditions.
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Refresh the words displayed in this view based on the model. If the model is an LCSProblem,
     * extract x and y strings. Push those strings into updateWords(), which updates the labels and
     * canvas. For non-LCS problems, log but skip the update.
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
            julLogger.log(
                    java.util.logging.Level.FINE,
                    "SubSequenceView: model is not LCSProblem; " + "no word update performed");
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
