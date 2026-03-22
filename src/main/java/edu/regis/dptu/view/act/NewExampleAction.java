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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.MainFrame;

/**
 * An MVC controller that handles a student requesting a new example for the current step.
 *
 * <p>Note: This is a stub implementation that will be expanded once StepCompletionView is
 * integrated.
 */
public class NewExampleAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(NewExampleAction.class);

    /** The singleton instance of this action. */
    private static final NewExampleAction SINGLETON;

    /** Create the singleton instance on class load. */
    static {
        SINGLETON = new NewExampleAction(ResourceMgr.instance().string("action.newExample.name"));
    }

    /**
     * Return the singleton instance of this action.
     *
     * @return NewExampleAction singleton
     */
    public static NewExampleAction instance() {
        return SINGLETON;
    }

    /**
     * Initialize this new example action with the given name.
     *
     * @param name the name of this action
     */
    public NewExampleAction(String name) {
        super(name);

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.newExample.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_N);

        log.debug("NewExampleAction initialized (stub)");
    }

    /**
     * Handle the action event when a student requests a new example.
     *
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This is a stub implementation
        // The full implementation will be added when StepCompletionView is integrated
        log.debug(
                "New example requested (stub): actionCommand={}",
                e != null ? e.getActionCommand() : "null");

        JOptionPane.showMessageDialog(
                MainFrame.instance(),
                ResourceMgr.instance().string("feature.newExample.unavailable"),
                ResourceMgr.instance().string("dialog.title.featureUnavailable"),
                JOptionPane.INFORMATION_MESSAGE);
    }
}
