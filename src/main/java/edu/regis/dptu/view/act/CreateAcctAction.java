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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.SvcFacade;
import edu.regis.dptu.svc.TutorReply;
import edu.regis.dptu.view.SplashFrame;

/**
 * An MVC controller handling a user GUI gesture requesting the creation of a new student account
 * within the NewAccountPanel.
 *
 * @author rickb
 */
public class CreateAcctAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(CreateAcctAction.class);


    /** The single instance of this create account action. */
    private static final CreateAcctAction SINGLETON;

    /**
     * Create the singleton for this action, which occurs when this class is loaded by the Java
     * class loaded, as a result of the class being referenced by executing
     * CreateAcctAction.instance() in the initializeComponents() method of the NewAccountPanel
     * class.
     */
    static {
        SINGLETON = new CreateAcctAction();
    }

    /**
     * Return the singleton instance of this sign-in action.
     *
     * @return
     */
    public static CreateAcctAction instance() {
        return SINGLETON;
    }

    /** Initialize this create account action with the "Create Account" text. */
    private CreateAcctAction() {
        super("Create Account");

        putValue(SHORT_DESCRIPTION, "Create a new user");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        // putValue(ACCELERATOR_KEY, getAcceleratorKeyStroke());
    }

    /**
     * Return the Keystroke used to accelerate this action (Doesn't appear to override the hardware
     * wiring of F1.)
     *
     * @return Ctrl-A
     */
    // @Override
    // public KeyStroke getAcceleratorKeyStroke() {
    //   return KeyStroke.getKeyStroke(KeyEvent.VK_A, CTRL_KEY);
    // }

    /**
     * Handle the user's request to create a new student user account by forwarding the account
     * information in the NewAccountPanel to the tutor.
     *
     * @param evt ignored
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        Gson gson = new Gson();

        SplashFrame frame = SplashFrame.instance();

        Account account = frame.getAccount();

        ClientRequest request = new ClientRequest(ServerRequestType.CREATE_ACCOUNT);
        request.setData(gson.toJson(account));

        TutorReply reply = SvcFacade.instance().tutorRequest(request);

        String msg;
        switch (reply.getStatus()) {
            case "Created":
                frame.clearNewAccountPanel();

                msg =
                        "Student user account successfully created\n\n"
                                + "Press okay and we'll return you to the sign-in screen\n\n"
                                + "Then, please sign-in to the tutor using this account.";

                JOptionPane.showMessageDialog(SplashFrame.instance(), msg);

                frame.selectSplash();
                break;

            case "IllegalUserId":
                msg = "User id already exists: " + account.getUserId();
                JOptionPane.showMessageDialog(
                        null, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
                break;

            default: // "ERR" Error should have been logged in tutor.
                msg = "An unexpected error occurred. Please contact DpTu support";
                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
