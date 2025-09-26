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

import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

import edu.regis.dptu.view.MainFrame;

/**
 * An MVC controller that handles a student requesting a hint for the current step.
 *
 * <p>Note: This is a stub implementation that will be expanded once StepCompletionView is
 * integrated.
 */
public class RequestHintAction extends DpTuGuiAction {
    /** The singleton instance of this action. */
    private static final RequestHintAction SINGLETON;

    /** Create the singleton instance on class load. */
    static {
        SINGLETON = new RequestHintAction("Hint");
    }

    /**
     * Return the singleton instance of this action.
     *
     * @return RequestHintAction singleton
     */
    public static RequestHintAction instance() {
        return SINGLETON;
    }

    /**
     * Initialize this hint request action with the given name.
     *
     * @param name the name of this action
     */
    public RequestHintAction(String name) {
        super(name);

        putValue(SHORT_DESCRIPTION, "Get a hint for this step");
        putValue(MNEMONIC_KEY, KeyEvent.VK_H);
    }

    /**
     * Handle the action event when a student requests a hint.
     *
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This is a stub implementation
        // The full implementation will be added when StepCompletionView is integrated
        JOptionPane.showMessageDialog(
                MainFrame.instance(),
                "Hint functionality will be available in a future update.",
                "Feature Not Available",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
