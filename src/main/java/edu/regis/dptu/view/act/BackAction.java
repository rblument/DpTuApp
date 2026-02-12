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

import edu.regis.dptu.view.SplashFrame;

/**
 * An MVC controller handling a user GUI gesture requesting to go to the previous panel.
 *
 * @author rickb
 */
public class BackAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(BackAction.class);

    /** The single instance of this back action. */
    private static final BackAction SINGLETON = new BackAction();

    /** Return the singleton instance of this back action. */
    public static BackAction instance() {
        return SINGLETON;
    }

    /** Initialize this back action. */
    private BackAction() {
        super("Back");

        putValue(SHORT_DESCRIPTION, "Go Back to the previous Panel");
        putValue(MNEMONIC_KEY, KeyEvent.VK_B);
    }

    /**
     * Handle the user's request to go to the previous panel by displaying the previous panel.
     *
     * @param evt ignored
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        log.debug("BackAction triggered: returning to splash panel");

        SplashFrame.instance().selectSplash();

        log.debug("BackAction complete: splash panel selected");
        // Note: currently always returns to sign-in panel
    }
}
