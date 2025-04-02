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

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.User;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The first window displayed to a student user, which contains a splash panel 
 * and associated panels for creating new users and signing-in existing users.
 * 
 * @author rickb (modified)
 */
public class SplashFrame extends JFrame {
    /**
     * Name of the splash panel in this frame's primary card layout panel.
     */
    public static final String SPLASH = "SplashPanel";
    
    /**
     * Name of the new user panel in this frame's primary card layout panel.
     */
    public static final String NEW_USER = "NewUserPanel";
    
    /**
     * Name of the dashboard panel in this frame's primary card layout panel.
     */
    public static final String DASHBOARD = "DashboardPanel";

    /**
     * Name of the lesson screen panel in the card layout.
     */
    public static final String LESSON = "LessonMenu";
    
    /**
     * Allowed consecutive illegal passwords before the user is locked out.
     */
    public static final int MAX_SIGNIN_ATTEMPTS = 3;
    
    /**
     * The single instance of this frame.
     */
    private static final SplashFrame SINGLETON;
    
    /**
     * Create the singleton for this JFrame
     */
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
     * The current tutoring session.
     */
    private TutoringSession tutoringSession;
    
    /**
     * A CardLayout containing all main panels (SPLASH, NEW_USER, DASHBOARD, etc.).
     */
    private JPanel cards;
    
    /**
     * The name of the currently selected panel
     */
    private String selectedPanel;
    
    /**
     * The splash panel, which displays splash information, sign-in fields,
     * and a link to create new student account panel.
     */
    private SplashPanel splashPanel;
    
    /**
     * A panel which allows the user to create a new student account with 
     * associated sign-in information.
     */
    private NewAccountPanel newAccountPanel;
    
    /**
     * The panel that allows users to select a type of service.
     */
    private DashboardPanel dashboardPanel;
    
    /**
     * View for lesson session functionality.
     */
    private LessonSessionView lessonSessionView;
    
    /**
     * The number of consecutive illegal passwords attempted by the current
     * user attempting to login (see MAX_SIGNIN_ATTEMPTS).
     */
    protected int signInAttempts = 0;
    
    /**
     * Create and layout the child components in this Splash JFrame.
     */
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
    
    /**
     * Display to the user the result of an invalid password in a sign in.
     */
    public void invalidPass() {
        if (signInAttempts < MAX_SIGNIN_ATTEMPTS) {
            String msg = "Invalid Password attempt " + 
                         String.valueOf(signInAttempts + 1) + " of " + 
                         MAX_SIGNIN_ATTEMPTS;
            
            signInAttempts++;
            
            JOptionPane.showMessageDialog(this, msg, "SignIn Error", 
                    JOptionPane.ERROR_MESSAGE);
            
        } else {
            String msg = "You exceeded the max number of sign in attempts\n" +
                         "Please contact the DpTu administrator";
            
            JOptionPane.showMessageDialog(this, msg, "SignIn Error", 
                    JOptionPane.ERROR_MESSAGE);

            this.dispose();
            System.exit(1);
        }
    }
    
    /**
     * Convenience method that displays the Splash panel allowing sign in.
     */
    public void selectSplash() {
        selectPanel(SPLASH);
    }
    
    /**
     * Display the New User panel
     */
    public void selectNewUser() {
        selectPanel(NEW_USER);
    }
    
    /**
     * Reset the text fields in the new account panel to empty string
     */
    public void clearNewAccountPanel() {
        newAccountPanel.clearFields();
    }
    
    /**
     * Initialize and show dashboard for the given session.
     * @param session The current tutoring session
     */
    public void initializeDashboard(TutoringSession session) {
        if (session == null) {
            System.err.println("TutoringSession is null in initializeDashboard");
            return;
        }

        this.tutoringSession = session;
        
        // Create new dashboard if it doesn't exist
        if (this.dashboardPanel == null) {
            this.dashboardPanel = new DashboardPanel(session);
            this.cards.add(dashboardPanel, DASHBOARD);
        } else {
            // Update existing dashboard
            this.dashboardPanel.setModel(session);
        }
        
        // Make sure the dashboard is visible
        this.setVisible(true);
        
        // Switch to dashboard view
        selectPanel(DASHBOARD);
        
        // Revalidate and repaint to ensure proper display
        this.cards.revalidate();
        this.cards.repaint();
    }
    
    /**
     * Display to the user they entered an unknown user during a sign in.
     */
    public void unknownUser() {
        User user = splashPanel.getModel();
       
        JOptionPane.showMessageDialog(this, 
                user.getUserId() + " is not a known user.\n\n" +
                "Perhaps, try creating a 'New User' first.",
                "Warning", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Select the lesson screen panel
     */
    public void selectLessonScreen() {
        selectPanel(LESSON);
    }
    
    /**
     * Select the practice screen panel
     */
    public void selectPracticeScreen() {
        // TODO: Implement practice screen selection
    }
    
    /**
     * Handle user logout
     */
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
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, name);
        selectedPanel = name;
        
        if (name.equals(SPLASH)) {
            JButton but = splashPanel.getSigninButton();
            SwingUtilities.getRootPane(but).setDefaultButton(but);
        } else if (name.equals(NEW_USER)) {
            newAccountPanel.updateFocus();
        }
    }
    
    /**
     * Create the child GUI components appearing in this frame.
     */
    private void initializeComponents() {
        cards = new JPanel(new CardLayout());
        
        splashPanel = new SplashPanel();
        newAccountPanel = new NewAccountPanel();
        
        cards.add(splashPanel, SPLASH);
        cards.add(newAccountPanel, NEW_USER);
        
        // Initialize lesson view if needed
        lessonSessionView = new LessonSessionView();
        //cards.add(lessonSessionView, LESSON);
    }
}