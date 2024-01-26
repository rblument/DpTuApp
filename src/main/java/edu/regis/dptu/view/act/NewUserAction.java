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

import edu.regis.dptu.view.SplashFrame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

/**
 * An MVC controller handling a user GUI gesture requesting to create a 
 * new user account, which switches to the new user panel in the GUI
 * (see CreateAcctAction).
 * 
 * @author rickb
 */
public class NewUserAction extends DpTuGuiAction {
    /**
     * The single instance of this new user action.
     */
    private static final NewUserAction SINGLETON;
    
    /**
     * Create the singleton for this action, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced by executing SignInAction.instance() in the 
     * initializeComponents() method of the SplashPanel class.
     */
    static {
        SINGLETON = new NewUserAction();
    }

    /**
     * Return the singleton instance of this new user action.
     * 
     * @return 
     */
    public static NewUserAction instance() {
	return SINGLETON;
    }

    /**
     * Initialize this new user action.
     */
    private NewUserAction() {
        super("New User");
        
        putValue(SHORT_DESCRIPTION, "Request to create a new user");
        putValue(MNEMONIC_KEY, KeyEvent.VK_U);
       // putValue(ACCELERATOR_KEY, getAcceleratorKeyStroke());
    }
    

    /**
     * Handle the user's request to create a new user by displaying the new
     * user panel.
     * 
     * @param evt ignored
     */
    @Override
    public void actionPerformed(ActionEvent evt) {      
        SplashFrame.instance().selectNewUser();       
    }
}

