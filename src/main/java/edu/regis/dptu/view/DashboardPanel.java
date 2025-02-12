/*
 * DPTu: Dynamic Programming Tutor
 */
package edu.regis.dptu.view;

import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.util.CustomProgressBar;
import edu.regis.dptu.view.act.PracticeAction;
import edu.regis.dptu.view.act.QuizMeAction;
import edu.regis.dptu.view.act.TeachMeAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
        setBackground(new Color(0, 43, 73)); // Dark blue background

        // Top bar components
        settingsButton = new JButton("Settings");
        settingsButton.setFocusPainted(false);

        welcomeLabel = new JLabel("Welcome, " + model.getStudent().getAccount().getFirstName() + "!");
        welcomeLabel.setForeground(new Color(241, 196, 0)); // Gold color
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
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel with settings, welcome message, and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 43, 73));
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        topPanel.add(settingsButton, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(logOutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // Main content panel with three columns
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBackground(new Color(0, 43, 73));
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
        column.setBackground(new Color(0, 43, 73));
        column.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Progress bar panel takes most of the space
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(new Color(0, 43, 73));
        progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Make progress bar fill the space while maintaining aspect ratio
        progressBar.setPreferredSize(new Dimension(100, 400));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        column.add(progressPanel, BorderLayout.CENTER);
        
        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(0, 43, 73));
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
}