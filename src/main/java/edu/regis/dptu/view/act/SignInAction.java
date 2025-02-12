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
package edu.regis.dptu.view.act;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.view.SplashFrame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

/**
 * An (MVC) controller handling a GUI gesture representing a user's request to
 * login to the tutor via the WelcomePanel.
 *
 * If successful, a trial will be started or resumed for the student via launch
 * session.
 *
 * @author rickb
 */
public class SignInAction extends DpTuGuiAction {
    private static final Logger LOGGER = Logger.getLogger(SignInAction.class.getName());
    private static final SignInAction SINGLETON;
    
    static {
        SINGLETON = new SignInAction();
    }

    public static SignInAction instance() {
        return SINGLETON;
    }

    private SignInAction() {
        super("Sign In");
        putValue(SHORT_DESCRIPTION, "Sign-in to the tutor");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Create a temporary test session for development
        Account testAccount = new Account("test@regis.edu");
        testAccount.setFirstName("Test");
        testAccount.setLastName("User");
        
        Student testStudent = new Student(testAccount);
        TutoringSession testSession = new TutoringSession(testStudent);
        
        // Initialize dashboard with test session
        SplashFrame.instance().initializeDashboard(testSession);
    }
}