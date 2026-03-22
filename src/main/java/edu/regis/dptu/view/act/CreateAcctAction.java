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

import com.google.gson.Gson;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.SvcFacade;
import edu.regis.dptu.svc.TutorReply;
import edu.regis.dptu.util.ResourceMgr;
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
     * @return the singleton {@link CreateAcctAction} instance.
     */
    public static CreateAcctAction instance() {
        return SINGLETON;
    }

    /** Initialize this create account action with the "Create Account" text. */
    private CreateAcctAction() {
        super(ResourceMgr.instance().string("action.createAccount.name"));

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.createAccount.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
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
        SplashFrame frame = SplashFrame.instance();
        Account account = frame.getAccount();

        // User-initiated action: INFO is appropriate.
        log.info("Create account requested for userId={}", account.getUserId());

        try {
            Gson gson = new Gson();

            ClientRequest request = new ClientRequest(ServerRequestType.CREATE_ACCOUNT);
            request.setData(gson.toJson(account));

            TutorReply reply = SvcFacade.instance().tutorRequest(request);
            String status = (reply == null) ? null : reply.getStatus();

            log.debug("Create account reply: userId={}, status={}", account.getUserId(), status);

            String msg;
            if ("Created".equals(status)) {
                frame.clearNewAccountPanel();

                msg = ResourceMgr.instance().string("account.create.success");

                JOptionPane.showMessageDialog(frame, msg);

                log.info("Create account succeeded for userId={}", account.getUserId());
                frame.selectSplash();

            } else if ("IllegalUserId".equals(status)) {
                msg =
                        ResourceMgr.instance()
                                .string("account.create.error.duplicateUser", account.getUserId());
                JOptionPane.showMessageDialog(
                        null,
                        msg,
                        ResourceMgr.instance().string("dialog.title.information"),
                        JOptionPane.INFORMATION_MESSAGE);

                log.warn(
                        "Create account rejected (IllegalUserId) for userId={}",
                        account.getUserId());

            } else {
                // Unknown or ERR: service should log, but we still log locally for correlation.
                msg = ResourceMgr.instance().string("error.unexpectedContactSupport");
                JOptionPane.showMessageDialog(
                        null,
                        msg,
                        ResourceMgr.instance().string("dialog.title.error"),
                        JOptionPane.ERROR_MESSAGE);

                log.error(
                        "Create account failed: userId={}, unexpected status={}",
                        account.getUserId(),
                        status);
            }
        } catch (RuntimeException e) {
            // Covers unexpected runtime issues (including service call failures).
            JOptionPane.showMessageDialog(
                    null,
                    ResourceMgr.instance().string("error.unexpectedContactSupport"),
                    ResourceMgr.instance().string("dialog.title.error"),
                    JOptionPane.ERROR_MESSAGE);

            log.error("Create account failed due to exception: userId={}", account.getUserId(), e);
        }
    }
}
