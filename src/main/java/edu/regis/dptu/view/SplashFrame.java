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
 * and associated panels for creating new users and signing-in exiting users.
 * 
 * @author rickb
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
     * Allowed consecutive illegal passwords before the user is locked out.
     */
    public static final int MAX_SIGNIN_ATTEMPTS = 3;
    
    /**
     * The single instance of this frame.
     */
    private static final SplashFrame SINGLETON;
    
    /**
     * Create the singleton for this JFrame, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced by executing WelcomeFrame.instance() in the main() method of
     * EthicsCourtTutor.
     */
    static {
        SINGLETON = new SplashFrame();
    }
    
    /**
     * Return the singleton instance of this JFrame.
     * 
     * @return the WelcomeFrame singleton 
     */
    public static SplashFrame instance() {
        return SINGLETON;
    }
    
    /**
     * A CardLayout containing the SPLASH and NEW_USER panels.
     */
    private JPanel cards;
    
    /**
     * The name of the currently selected panel, SPLASH or NEW_USER.
     */
    private String selectedPanel;
    
    /**
     * The splash panel, which displays splash information, sign-in fields,
     * and a link to the create new student account panel.
     */
    private SplashPanel splashPanel;
    
    /**
     * A panel which allows the user to create a new student account with 
     * associated sign-in information.
     */
    private NewAccountPanel newAccountPanel;
    
    /**
     * The number of consecutive illegal passwords attempted by the current
     * user attempting to login (see MAX_SIGNIN_ATTEMPTS).
     */
    protected int signInAttempts = 0;
    
   /**
     * Create and layout the child components in this Spalsh JFrame.
     */
    private SplashFrame() {
        super("DpTu");
        
        setMinimumSize(new Dimension(875,650));
        
        initializeComponents();
        
        this.setContentPane(cards);
        
        selectPanel(SPLASH);
        
        pack();
        
        splashPanel.setInitialFocus();
        
        setVisible(true);
    }
    
    /**
     * Return the student login information displayed in this frame.
     * 
     * See getAccount()
     * 
     * @return a User (userId and password)
     */
    public User getUser() {
       return splashPanel.getModel();
    }
    
    /**
     * Return the user account information 
     * 
     * See getUser()
     * 
     * @return an Account (userId, passwd, first and last name)
     */
    public Account getAccount() {
       return newAccountPanel.getModel();
    }
    
    /**
     * Display to the user the result of an invalid password in a sign in.
     * 
     * Handles an invalid password response from a SignInAction keeping track
     * of the number of user attempts thus far.     * 
     */
    public void invalidPass() {
        if (signInAttempts < MAX_SIGNIN_ATTEMPTS) {
           
            String msg = "Invalid Password attempt " + 
                         String.valueOf(signInAttempts) + " of " + 
                         MAX_SIGNIN_ATTEMPTS;
            
            signInAttempts++;
            
            JOptionPane.showMessageDialog(this, msg, "SignIn Error", JOptionPane.ERROR_MESSAGE);
            
        } else {
            String msg = "You exceeded the max number of sign in attempts\n" +
                         "Please contact the ShaTu administrator";
            
            JOptionPane.showMessageDialog(this, msg, "SignIn Error", JOptionPane.ERROR_MESSAGE);

            // ToDo: What?
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
     * Display the New User panel, which allows the user to create a new
     * student account with associated sign-in information.
     */
    public void selectNewUser() {
        selectPanel(NEW_USER);
    }
    
    /**
     * Reset the text fields in the new account panel to the empty string
     */
    public void clearNewAccountPanel() {
        newAccountPanel.clearFields();
    }
    
    /**
     * Display to the user they entered an unknown user during a sign in.
     */
    public void unknownUser() {
        User user = splashPanel.getModel();
       
        JOptionPane.showMessageDialog(this, 
                user.getUserId() + " is not a known user.\n\nPerhaps, try creating a 'New User' first.",
                "Warning", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Display the card panel with the associated name.
     * 
     * @param name SPLASH or NEW_USER
     */
    private void selectPanel(String name) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, name);
        selectedPanel = name;
        
        if (name.equals(SPLASH)) {
            JButton but = splashPanel.getSigninButton();
        
            SwingUtilities.getRootPane(but).setDefaultButton(but);
        } else {
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
    }
}

