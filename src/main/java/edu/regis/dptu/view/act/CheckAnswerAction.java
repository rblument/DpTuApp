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
 * An MVC controller that handles a student submitting an answer for a step.
 *
 * <p>Note: This is a stub implementation that will be expanded once StepCompletionView is
 * integrated.
 */
public class CheckAnswerAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(CheckAnswerAction.class);

    /** The singleton instance of this action. */
    private static final CheckAnswerAction SINGLETON;

    /** Create the singleton instance on class load. */
    static {
        SINGLETON = new CheckAnswerAction("Check Answer");
    }

    /**
     * Return the singleton instance of this action.
     *
     * @return CheckAnswerAction singleton
     */
    public static CheckAnswerAction instance() {
        return SINGLETON;
    }

    /**
     * Initialize this check answer action with the given name.
     *
     * @param name the name of this action
     */
    public CheckAnswerAction(String name) {
        super(name);

        putValue(SHORT_DESCRIPTION, "Check your answer for this step");
        putValue(MNEMONIC_KEY, KeyEvent.VK_C);
    }

    /**
     * Handle the action event when a student submits an answer.
     *
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // This is a stub implementation
        // The full implementation will be added when StepCompletionView is integrated
        JOptionPane.showMessageDialog(
                MainFrame.instance(),
                "Answer checking functionality will be available in a future update.",
                "Feature Not Available",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
