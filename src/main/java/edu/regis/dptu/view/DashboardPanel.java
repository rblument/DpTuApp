/*
 * DPTu: Dynamic Programming Tutor
 */
package edu.regis.dptu.view;

import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.util.CustomProgressBar;
import edu.regis.dptu.view.act.PracticeAction;
import edu.regis.dptu.view.act.QuizMeAction;
import edu.regis.dptu.view.act.TeachMeAction;
import edu.regis.dptu.model.TaskKind;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox; // Added for problem selector
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DashboardPanel extends GPanel {
    private TutoringSession model;
    private static boolean welcome = false;

    private JButton logOutButton;
    private JButton settingsButton;
    private JButton teachMeButton;
    private JButton practiceButton;
    private JButton quizMeButton;
    private CustomProgressBar teachMeProgressBar;
    private CustomProgressBar practiceProgressBar;
    private CustomProgressBar quizMeProgressBar;
    private JLabel welcomeLabel;

    // ADDED: Problem selector combo box
    private JComboBox<String> problemSelector; // @author EverettCV
    
    private static final Color REGIS_BLUE = new Color(0, 43, 73);
    private static final Color REGIS_GOLD = new Color(241, 196, 0);

    public DashboardPanel(TutoringSession tutoringSession) {
        model = tutoringSession;

        if (!welcome) {
            welcome = true;
            System.out.println("DashboardPanel initialized for user: "
                    + tutoringSession.getStudent().getAccount().getFirstName());
            String welcomeMessage = "Welcome, "
                    + tutoringSession.getStudent().getAccount().getFirstName() + "! "
                    + "Your session has successfully started.";
            JOptionPane.showMessageDialog(
                    null,
                    welcomeMessage,
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        initializeComponents();
        layoutComponents();
    }

    public void setModel(TutoringSession model) {
        this.model = model;
    }

    private void initializeComponents() {
        setBackground(REGIS_BLUE); // Dark blue background

        // Top bar components
        settingsButton = new JButton("Settings");
        settingsButton.setFocusPainted(false);

        welcomeLabel = new JLabel("Welcome, " + model.getStudent().getAccount().getFirstName() + "!");
        welcomeLabel.setForeground(REGIS_GOLD); // Gold color
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        logOutButton = new JButton("Log Out");
        logOutButton.setFocusPainted(false);
        logOutButton.addActionListener(e -> logOutButtonActionPerformed(e));

        // Initialize progress bars
        teachMeProgressBar = new CustomProgressBar();
        teachMeProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        teachMeProgressBar.setValue(100);
        teachMeProgressBar.setString("100%");
        teachMeProgressBar.setStringPainted(true);

        practiceProgressBar = new CustomProgressBar();
        practiceProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        practiceProgressBar.setValue(50);
        practiceProgressBar.setString("50%");
        practiceProgressBar.setStringPainted(true);

        quizMeProgressBar = new CustomProgressBar();
        quizMeProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        quizMeProgressBar.setValue(0);
        quizMeProgressBar.setString("0%");
        quizMeProgressBar.setStringPainted(true);

        // Initialize buttons with new actions
        teachMeButton = new JButton(TeachMeAction.instance());
        teachMeButton.setFocusPainted(false);

        practiceButton = new JButton(PracticeAction.instance());
        practiceButton.setFocusPainted(false);

        quizMeButton = new JButton(QuizMeAction.instance());
        quizMeButton.setFocusPainted(false);

        // ADDED: Problem selector dropdown for choosing problem type (LCS, Matrix, Knapsack)
        problemSelector = new JComboBox<>(new String[]{
            "Longest Common Subsequence",
            "Matrix Chain Multiplication",
            "Knapsack Problem"
        });
        problemSelector.setSelectedIndex(0); // Default to LCS

        // TODO: Hook this selection into TeachMeAction, PracticeAction, QuizMeAction
        // TODO: Replace String-based selection with a proper ProblemType enum for clean future-proofing
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel with settings, welcome message, and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(REGIS_BLUE);
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        topPanel.add(settingsButton, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(logOutButton, BorderLayout.EAST);

        // ADDED: Attach problem selector below the welcome message
        topPanel.add(problemSelector, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Main content panel with three columns
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBackground(REGIS_BLUE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create three columns
        mainPanel.add(createColumn(teachMeProgressBar, teachMeButton, "Teach Me"));
        mainPanel.add(createColumn(practiceProgressBar, practiceButton, "Practice"));
        mainPanel.add(createColumn(quizMeProgressBar, quizMeButton, "Quiz Me"));

        add(mainPanel, BorderLayout.CENTER);

        // Copyright footer
        JLabel copyright = new JLabel("(C) 2019-2025 Johanna and Richard Blumenthal. All Rights Reserved", SwingConstants.CENTER);
        copyright.setForeground(Color.GRAY);
        copyright.setFont(new Font("Dialog", Font.PLAIN, 10));
        copyright.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(copyright, BorderLayout.SOUTH);
    }

    private JPanel createColumn(CustomProgressBar progressBar, JButton button, String labelText) {
        JPanel column = new JPanel(new BorderLayout(0, 5));
        column.setBackground(REGIS_BLUE);
        column.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Progress bar panel takes most of the space
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(REGIS_BLUE);
        progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Make progress bar fill the space while maintaining aspect ratio
        progressBar.setPreferredSize(new Dimension(100, 400));
        progressPanel.add(progressBar, BorderLayout.CENTER);

        column.add(progressPanel, BorderLayout.CENTER);

        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(REGIS_BLUE);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        buttonPanel.add(button, BorderLayout.CENTER);

        column.add(buttonPanel, BorderLayout.SOUTH);

        return column;
    }

    private void practiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SplashFrame.instance().selectPracticeScreen();
    }

    private void teachMeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SplashFrame.instance().selectLessonScreen();
    }

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SplashFrame.instance().logout();
    }

    /**
     * Return the TaskKind corresponding to the currently selected problem in the dropdown.
     * @return TaskKind @author EverettCV
     */
    public TaskKind getSelectedTaskKind() {
        int index = problemSelector.getSelectedIndex();
        switch (index) {
            case 0:
                return TaskKind.LCS_PROBLEM;
            case 1:
                return TaskKind.MATRIX_CHAIN;
            case 2:
                return TaskKind.KNAPSACK_0_1;
            default:
                return TaskKind.LCS_PROBLEM; // Fallback
        }
    }
}
