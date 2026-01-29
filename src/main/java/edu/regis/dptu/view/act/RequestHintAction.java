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

import edu.regis.dptu.view.MainFrame;

/**
 * An MVC controller that handles a student requesting a hint for the current step.
 *
 * <p>Note: This is a stub implementation that will be expanded once StepCompletionView is
 * integrated.
 */
public class RequestHintAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(RequestHintAction.class);

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
        log.info("Hint requested.");
        // Show a simple static hint message to the student
        // Basic implementation for DPTU-45
        String hintText = "Try focusing on how this step connects to the previous subproblem.";
        JOptionPane.showMessageDialog(
                MainFrame.instance(), hintText, "Hint", JOptionPane.INFORMATION_MESSAGE);
    }
}
