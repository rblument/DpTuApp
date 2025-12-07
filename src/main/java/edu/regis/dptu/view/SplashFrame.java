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

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.User;

/**
 * The first window displayed to a student user, which contains a splash panel and associated panels
 * for creating new users and signing-in existing users.
 *
 * @author rickb (modified)
 */
public class SplashFrame extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(SplashFrame.class);

    /** Name of the splash panel in this frame's primary card layout panel. */
    public static final String SPLASH = "SplashPanel";

    /** Name of the new user panel in this frame's primary card layout panel. */
    public static final String NEW_USER = "NewUserPanel";

    /** Name of the dashboard panel in this frame's primary card layout panel. */
    public static final String DASHBOARD = "DashboardPanel";

    /** Name of the lesson screen panel in the card layout. */
    public static final String LESSON = "LessonMenu";

    /** Allowed consecutive illegal passwords before the user is locked out. */
    public static final int MAX_SIGNIN_ATTEMPTS = 3;

    /** The single instance of this frame. */
    private static final SplashFrame SINGLETON;

    private boolean isFirstLogin = false;

    /** Create the singleton for this JFrame */
    static {
        SINGLETON = new SplashFrame();
    }

    /**
     * Return the singleton instance of this JFrame.
     *
     * @return the SplashFrame singleton
     */
    public static SplashFrame instance() {
        return SINGLETON;
    }

    /**
     * Called if we determined that the user is logging in for the first time
     *
     * @param value true if first login, false otherwise
     */
    public void setIsFirstLogin(boolean value) {
        this.isFirstLogin = value;
    }

    /**
     * Get whether or not this is the first login
     *
     * @return true if first login, false otherwise
     */
    public boolean getIsFirstLogin() {
        boolean reset = isFirstLogin;
        isFirstLogin = false;
        return reset;
    }

    /** All student info should reside here. */
    private Student student;

    /** A CardLayout containing all main panels (SPLASH, NEW_USER, DASHBOARD, etc.). */
    private JPanel cards;

    /**
     * The splash panel, which displays splash information, sign-in fields, and a link to create new
     * student account panel.
     */
    private SplashPanel splashPanel;

    /**
     * A panel which allows the user to create a new student account with associated sign-in
     * information.
     */
    private NewAccountPanel newAccountPanel;

    /** The panel that allows users to select a type of service. */
    private DashboardPanel dashboardPanel;

    /**
     * The number of consecutive illegal passwords attempted by the current user attempting to login
     * (see MAX_SIGNIN_ATTEMPTS).
     */
    protected int signInAttempts = 0;

    /** Create and layout the child components in this Splash JFrame. */
    private SplashFrame() {
        super("DpTu");

        setMinimumSize(new Dimension(875, 650));

        initializeComponents();

        this.setContentPane(cards);

        selectPanel(SPLASH);

        pack();

        splashPanel.setInitialFocus();

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Return the student login information displayed in this frame.
     *
     * @return a User (userId and password)
     */
    public User getUser() {
        return splashPanel.getModel();
    }

    /**
     * Return the user account information
     *
     * @return an Account (userId, passwd, first and last name)
     */
    public Account getAccount() {
        return newAccountPanel.getModel();
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    /** Display to the user the result of an invalid password in a sign in. */
    public void invalidPass() {
        if (signInAttempts < MAX_SIGNIN_ATTEMPTS) {
            String msg =
                    "Invalid Password attempt "
                            + String.valueOf(signInAttempts + 1)
                            + " of "
                            + MAX_SIGNIN_ATTEMPTS;

            signInAttempts++;

            showError("SignIn Error", msg);
        } else {
            String msg =
                    "You exceeded the max number of sign in attempts\n"
                            + "Please contact the DpTu administrator";

            showError("SignIn Error", msg);
            this.dispose();
            System.exit(1);
        }
    }

    /** Convenience method that displays the Splash panel allowing sign in. */
    public void selectSplash() {
        selectPanel(SPLASH);
    }

    /** Display the New User panel */
    public void selectNewUser() {
        selectPanel(NEW_USER);
    }

    /** Reset the text fields in the new account panel to empty string */
    public void clearNewAccountPanel() {
        newAccountPanel.clearFields();
    }

    /**
     * Initialize and show dashboard for the given session.
     *
     * @param firstName - The name of this user.
     */
    public void initializeDashboard(String firstName) {

        // Create new dashboard if it doesn't exist
        if (this.dashboardPanel == null) {
            this.dashboardPanel = new DashboardPanel(firstName);
            this.cards.add(dashboardPanel, DASHBOARD);
        } else {
            // Update existing dashboard
            dashboardPanel.setFirstName(firstName);
        }

        // Make sure the dashboard is visible
        this.setVisible(true);

        // Switch to dashboard view
        selectPanel(DASHBOARD);

        // Revalidate and repaint to ensure proper display
        this.cards.revalidate();
        this.cards.repaint();
    }

    /** Display to the user they entered an unknown user during a sign in. */
    public void unknownUser() {
        User user = splashPanel.getModel();

        showError(
                "Warning",
                user.getUserId()
                        + " is not a known user.\n\n"
                        + "Perhaps, try creating a 'New User' first.");
    }

    /** Select the practice screen panel */
    public void selectPracticeScreen() {
        // TODO: Implement practice screen selection
    }

    /** Handle user logout */
    public void logout() {
        selectSplash();
        // Additional logout cleanup if needed
    }

    /**
     * Display the card panel with the associated name.
     *
     * @param name Name of the panel to display
     */
    private void selectPanel(String name) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, name);

        if (name.equals(SPLASH)) {
            JButton but = splashPanel.getSigninButton();
            SwingUtilities.getRootPane(but).setDefaultButton(but);
        } else if (name.equals(NEW_USER)) {
            newAccountPanel.updateFocus();
        }
    }

    /** Create the child GUI components appearing in this frame. */
    private void initializeComponents() {
        cards = new JPanel(new CardLayout());

        splashPanel = new SplashPanel();
        newAccountPanel = new NewAccountPanel();

        cards.add(splashPanel, SPLASH);
        cards.add(newAccountPanel, NEW_USER);
    }

    /**
     * Basic getter for the dashboardPanel
     *
     * @return the DashboardPanel instance currently used in the SplashFrame
     * @author EverettCV
     */
    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }

    /**
     * Select the lesson screen for the problem. Creates a new TutoringSession
     *
     * @author EverettCV
     * @param ts - TutoringSession to be displayed for this lesson.
     */
    public void selectLessonScreen(TutoringSession ts) {

        MainFrame.instance().getView().setModel(ts);

        // Show the MainFrame (lesson view)
        MainFrame.instance().setVisible(true);
    }

    public void showError(String title, String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, title, JOptionPane.ERROR_MESSAGE);
    }
}
