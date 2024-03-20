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
 * An MVC controller handling a user GUI gesture requesting to go to the 
 * previous panel, which switches to the previous panel in the GUI
 * (see BackAction).
 * 
 * @author rickb
 */
public class BackAction extends DpTuGuiAction {
    /**
     * The single instance of this new user action.
     */
    private static final BackAction SINGLETON;
    
    /**
     * Create the singleton for this action, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced by executing BackAction.instance() in the 
     * initializeComponents() method of the SplashPanel class.
     */
    static {
        SINGLETON = new BackAction();
    }

    /**
     * Return the singleton instance of this back action.
     * 
     * @return 
     */
    public static BackAction instance() {
	return SINGLETON;
    }

    /**
     * Initialize this back action.
     */
    private BackAction() {
        super("Back");
        
        putValue(SHORT_DESCRIPTION, "Go Back to the previous Panel");
        putValue(MNEMONIC_KEY, KeyEvent.VK_B);
    }
    

    /**
     * Handle the user's request to go to the previous panel by displaying the
     * previous panel.
     * 
     * @param evt ignored
     */
    @Override
    public void actionPerformed(ActionEvent evt) {      
        SplashFrame.instance().selectSplash();
        //This only goes to sign in page, more work needed
    }
}

