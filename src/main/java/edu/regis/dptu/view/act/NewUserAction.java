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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.SplashFrame;

/**
 * An MVC controller handling a user GUI gesture requesting to create a new user account, which
 * switches to the new user panel in the GUI (see CreateAcctAction).
 *
 * @author rickb
 */
public class NewUserAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(NewUserAction.class);

    /** The single instance of this new user action. */
    private static final NewUserAction SINGLETON;

    /**
     * Create the singleton for this action, which occurs when this class is loaded by the Java
     * class loader, as a result of the class being referenced by executing SignInAction.instance()
     * in the initializeComponents() method of the SplashPanel class.
     */
    static {
        SINGLETON = new NewUserAction();
    }

    /**
     * Return the singleton instance of this new user action.
     *
     * @return singleton instance
     */
    public static NewUserAction instance() {
        return SINGLETON;
    }

    /** Initialize this new user action. */
    private NewUserAction() {
        super(ResourceMgr.instance().string("action.newUser.name"));

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.newUser.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_U);
    }

    /**
     * Handle the user's request to create a new user by displaying the new user panel.
     *
     * @param evt ignored
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        log.debug("NewUserAction triggered; switching to New User panel");
        SplashFrame.instance().selectNewUser();
    }
}
