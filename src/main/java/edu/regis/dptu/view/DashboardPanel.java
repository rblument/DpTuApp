/*
 * DPTu: Dynamic Programming Tutor
 */
package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.util.CustomProgressBar;
import edu.regis.dptu.view.act.DoOneAction;
import edu.regis.dptu.view.act.SeeOneAction;
import edu.regis.dptu.view.act.TeachOneAction;

public class DashboardPanel extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(DashboardPanel.class);

    private TutoringSession model;

    private JButton logOutButton;
    private JButton settingsButton;
    private JButton seeOneButton;
    private JButton doOneButton;
    private JButton teachOneButton;
    private CustomProgressBar seeOneProgressBar;
    private CustomProgressBar doOneProgressBar;
    private CustomProgressBar teachOneProgressBar;
    private JLabel welcomeLabel;
    private JComboBox<String> problemSelector; // @author EverettCV

    private static final Color REGIS_BLUE = new Color(0, 43, 73);
    private static final Color REGIS_GOLD = new Color(241, 196, 0);

    private static final Logger LOGGER = Logger.getLogger(DashboardPanel.class.getName());

    public DashboardPanel(TutoringSession tutoringSession) {
        model = tutoringSession;

        String welcomeMessage =
                "Welcome, "
                        + tutoringSession.getStudent().getAccount().getFirstName()
                        + "! "
                        + "Your session has successfully started.";
        JOptionPane.showMessageDialog(
                null, welcomeMessage, "Welcome", JOptionPane.INFORMATION_MESSAGE);

        initializeComponents();
        layoutComponents();
    }

    public void setModel(TutoringSession model) {
        this.model = model;
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
        setBackground(REGIS_BLUE); // Dark blue background

        // Top bar components
        settingsButton = new JButton("Settings");
        settingsButton.setFocusPainted(false);

        welcomeLabel =
                new JLabel("Welcome, " + model.getStudent().getAccount().getFirstName() + "!");
        welcomeLabel.setForeground(REGIS_GOLD); // Gold color
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        logOutButton = new JButton("Log Out");
        logOutButton.setFocusPainted(false);
        logOutButton.addActionListener(e -> logOutButtonActionPerformed(e));

        // Initialize progress bars
        seeOneProgressBar = new CustomProgressBar();
        seeOneProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        seeOneProgressBar.setValue(100);
        seeOneProgressBar.setString("100%");
        seeOneProgressBar.setStringPainted(true);

        doOneProgressBar = new CustomProgressBar();
        doOneProgressBar.setOrientation(CustomProgressBar.VERTICAL);
        doOneProgressBar.setValue(50);
        doOneProgressBar.setString("50%");
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
        mainPanel.add(createColumn(seeOneProgressBar, seeOneButton, "See One"));
        mainPanel.add(createColumn(doOneProgressBar, doOneButton, " Do One"));
        mainPanel.add(createColumn(teachOneProgressBar, teachOneButton, "Teach One"));

        add(mainPanel, BorderLayout.CENTER);

        // Copyright footer
        JLabel copyright =
                new JLabel(
                        "(C) 2019-2025 Johanna and Richard Blumenthal. All Rights Reserved",
                        SwingConstants.CENTER);
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

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        SplashFrame.instance().logout();
    }

    /**
     * Enable/disable buttons based on the student's current ScaffoldLevel.
     *
     * @author hsherwin@regis.edu
     */
    private void applyScaffoldLevelRules() {

        // Gracefully handle if the model objects don't exist.
        if (model == null || model.getStudent() == null) {
            LOGGER.log(
                    Level.WARNING,
                    "DashboardPanel: model or student is null, " + "skipping scaffold level rules");
            return;
        }

        // Get the current scaffold level.
        var studentModel = model.getStudent().getStudentModel();
        ScaffoldLevel lvl = studentModel.getScaffoldLevel();
        LOGGER.log(Level.INFO, "DashboardPanel: applying scaffold level rules for {0}", lvl);

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
