/*
 * DPTu: Dynamic Programming Tutor
 * Main user dashboard view for the DpTu app
 * Displays navigation controls, problem selection, and
 * progress indicators for Se One, Do One, and Teach One modes
 *
 * Responsible for:
 *  Initializing and laying out dashboard UI components
 *  Applying scaffold level rules to enable/disable actions
 *  Displaying student progress indicators
 *
 * Note:
 *   Progress values are provided externally
 *   This class is only responsible for presentation. The progress bars are
 *   made in the CustomProgressBar.java class in util
 *
 * Last Edited: 1/29/2026 Lindsey C
 */

// TODO: the progress bar values are currently hardcoded, so that will need to be fixed!

package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.util.CustomProgressBar;
import edu.regis.dptu.view.act.DoOneAction;
import edu.regis.dptu.view.act.SeeOneAction;
import edu.regis.dptu.view.act.TeachOneAction;

public class DashboardPanel extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(DashboardPanel.class);

    private String firstName;

    private JButton logOutButton;
    private JButton settingsButton;
    private JButton seeOneButton;
    private JButton doOneButton;
    private JButton teachOneButton;
    private JButton seeOneStatsButton;
    private JButton doOneStatsButton;
    private JButton teachOneStatsButton;
    private CustomProgressBar seeOneProgressBar;
    private CustomProgressBar doOneProgressBar;
    private CustomProgressBar teachOneProgressBar;
    private JLabel welcomeLabel;
    private JComboBox<String> problemSelector; // @author EverettCV

    private static final Color BACKGROUND = new Color(32, 88, 96); // dark seafoam green
    private static final Color TEXT = new Color(31, 41, 55); // deep charcoal
    private static final Color FILL = new Color(245, 255, 250); // //soft, pastel green

    public DashboardPanel(String firstName) {
        this.firstName = firstName;

        displayWelcomeDialog();

        initializeComponents();
        layoutComponents();
    }

    /* Greets user by name in JOptionPanne and dashboard header. */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        welcomeLabel.setText("Welcome, " + firstName + "!");
        displayWelcomeDialog();
    }

    /**
     * Return the TaskKind corresponding to the currently selected problem in the drop-down.
     *
     * @return the selected problem kind
     */
    public ProblemKind getSelectedProblemKind() {
        String selectedTitle = problemSelector.getSelectedItem().toString();

        if (selectedTitle == null) {
            return ProblemKind.LCS_PROBLEM;
        }

        return Arrays.stream(ProblemKind.values())
                .filter(kind -> kind.title().equals(selectedTitle))
                .findFirst()
                .orElse(ProblemKind.LCS_PROBLEM);
    }

    private void initializeComponents() {
        setBackground(BACKGROUND); // Dark blue background

        // Top bar components
        settingsButton = new JButton("Settings");
        settingsButton.setFocusPainted(false);

        welcomeLabel = new JLabel("Welcome, " + firstName + "!");
        welcomeLabel.setForeground(FILL);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        logOutButton = new JButton("Log Out");
        logOutButton.setFocusPainted(false);
        logOutButton.addActionListener(e -> logOutButtonActionPerformed(e));

        // Initialize progress bars
        seeOneProgressBar = new CustomProgressBar();
        seeOneProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        seeOneProgressBar.setValue(0);
        seeOneProgressBar.setString("0%");
        seeOneProgressBar.setStringPainted(true);

        doOneProgressBar = new CustomProgressBar();
        doOneProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        doOneProgressBar.setValue(0);
        doOneProgressBar.setString("0%");
        doOneProgressBar.setStringPainted(true);

        teachOneProgressBar = new CustomProgressBar();
        teachOneProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        teachOneProgressBar.setValue(0);
        teachOneProgressBar.setString("0%");
        teachOneProgressBar.setStringPainted(true);

        // Initialize buttons with new actions
        seeOneButton = new JButton(SeeOneAction.instance());
        seeOneButton.setFocusPainted(false);

        doOneButton = new JButton(DoOneAction.instance());
        doOneButton.setFocusPainted(false);

        teachOneButton = new JButton(TeachOneAction.instance());
        teachOneButton.setFocusPainted(false);

        // Stats Buttons
        seeOneStatsButton = new JButton("View Stats");
        seeOneStatsButton.setFocusPainted(false);
        seeOneStatsButton.addActionListener(
                e -> {
                    log.info("See One Stats button pressed");
                    // popup window code will go there later
                });

        doOneStatsButton = new JButton("View Stats");
        doOneStatsButton.setFocusPainted(false);
        doOneStatsButton.addActionListener(
                e -> {
                    log.info("Do One Stats button pressed");
                    // popup window code will go there later
                });

        teachOneStatsButton = new JButton("View Stats");
        teachOneStatsButton.setFocusPainted(false);
        teachOneStatsButton.addActionListener(
                e -> {
                    log.info("Teach One Stats button pressed");
                    // popup window code will go there later
                });

        // Apply scaffold level rules for which buttons are visible.
        applyScaffoldLevelRules();

        problemSelector =
                new JComboBox<String>(
                        Arrays.stream(ProblemKind.values())
                                .map(ProblemKind::title)
                                .toArray(String[]::new));
        problemSelector.setSelectedIndex(0);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel with settings, welcome message, and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND);
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        topPanel.add(settingsButton, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(logOutButton, BorderLayout.EAST);

        // ADDED: Attach problem selector below the welcome message
        topPanel.add(problemSelector, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Main content panel with three columns
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create three columns
        mainPanel.add(
                createColumn(
                        seeOneProgressBar, seeOneButton, seeOneStatsButton, Mode.SEE_ONE.title()));
        mainPanel.add(
                createColumn(doOneProgressBar, doOneButton, doOneStatsButton, Mode.DO_ONE.title()));
        mainPanel.add(
                createColumn(
                        teachOneProgressBar,
                        teachOneButton,
                        teachOneStatsButton,
                        Mode.TEACH_ONE.title()));

        add(mainPanel, BorderLayout.CENTER);

        // Copyright footer
        JLabel copyright =
                new JLabel(
                        "(C) 2019-2025 Johanna and Richard Blumenthal. All Rights Reserved",
                        SwingConstants.CENTER);
        copyright.setForeground(FILL);
        copyright.setFont(new Font("Dialog", Font.PLAIN, 10));
        copyright.setBorder(new EmptyBorder(5, 0, 5, 0));
        add(copyright, BorderLayout.SOUTH);
    }

    private void displayWelcomeDialog() {
        String welcomeMessage =
                "Welcome, " + firstName + "! " + "Your session has successfully started.";
        JOptionPane.showMessageDialog(
                null, welcomeMessage, "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createColumn(
            CustomProgressBar progressBar, JButton button, JButton statsButton, String labelText) {
        JPanel column = new JPanel(new BorderLayout(0, 5));
        column.setBackground(BACKGROUND);
        column.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Progress bar panel takes most of the space
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(BACKGROUND);
        progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Make progress bar fill the space while maintaining aspect ratio
        progressBar.setPreferredSize(new Dimension(100, 400));
        progressPanel.add(progressBar, BorderLayout.CENTER);

        column.add(progressPanel, BorderLayout.CENTER);

        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        buttonPanel.setBackground(BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        buttonPanel.add(button);
        buttonPanel.add(statsButton);

        column.add(buttonPanel, BorderLayout.SOUTH);

        return column;
    }

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SplashFrame.instance().logout();
    }

    /**
     * Enable/disable buttons based on the student's current ScaffoldLevel.
     *
     * @author hsherwin@regis.edu
     */
    private void applyScaffoldLevelRules() {
        Student student = SplashFrame.instance().getStudent();
        StudentModel studentModel;

        // Gracefully handle if the model objects don't exist.
        if (student == null) {
            DashboardPanel.log.warn(
                    "DashboardPanel: student is null, " + "skipping scaffold level rules");
            return;
        }

        // Get the current scaffold level.
        studentModel = student.getStudentModel();
        ScaffoldLevel lvl = studentModel.getScaffoldLevel();
        DashboardPanel.log.info("DashboardPanel: applying scaffold level rules for {}", lvl);

        // Create button enabled booleans.
        boolean seeOneButtonEnabled = false,
                doOneButtonEnabled = false,
                teachOneButtonEnabled = false;

        // Set the button enabled booleans based on the scaffold level.
        switch (lvl) {
            case EXTREME:
                seeOneButtonEnabled = true;
                break;
            case NONE:
                seeOneButtonEnabled = true;
                doOneButtonEnabled = true;
                teachOneButtonEnabled = true;
                break;
            default:
                seeOneButtonEnabled = true;
                doOneButtonEnabled = true;
                break;
        }

        // Use the button enabled booleans to actually enable/disable the buttons.
        seeOneButton.setEnabled(seeOneButtonEnabled);
        doOneButton.setEnabled(doOneButtonEnabled);
        teachOneButton.setEnabled(teachOneButtonEnabled);
    }
}
