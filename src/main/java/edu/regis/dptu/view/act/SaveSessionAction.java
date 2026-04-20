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
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.util.ImgFactory;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.MainFrame;

/**
 * Handler for GUI gestures requesting to save the current session.
 *
 * @author rickb
 */
public class SaveSessionAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(SaveSessionAction.class);

    private Supplier<TutoringSession> activeSessionSupplier =
            () -> MainFrame.instance().getView().getModel();
    private Supplier<SessionSvc> sessionSvcSupplier = ServiceFactory::findSessionSvc;
    private Consumer<DialogRequest> dialogPresenter = this::showDialog;

    /**
     * Create the singleton for this action, which occurs when this class is loaded by the Java
     * class loaded, as a result of the class being referenced by executing SaveSession.instance()
     */
    static {
        SINGLETON = new SaveSessionAction();
    }

    /** The singleton for this action. */
    public static SaveSessionAction SINGLETON;

    /**
     * Return the singleton for this action.
     *
     * @return the SaveSessionAction singleton
     */
    public static SaveSessionAction instance() {
        return SINGLETON;
    }

    /** Initialize this action. */
    private SaveSessionAction() {
        super(ResourceMgr.instance().string("action.save.name"));

        putValue(
                SMALL_ICON,
                ImgFactory.createIcon(
                        "Save16.gif", ResourceMgr.instance().string("action.save.iconAlt")));
        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.save.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
    }

    /**
     * Save the current session when this action is invoked.
     *
     * @param evt
     */
    public void actionPerformed(ActionEvent evt) {
        TutoringSession session = activeSessionSupplier.get();

        if (session == null) {
            log.warn("Save requested with no active tutoring session in the UI");
            showInfo(ResourceMgr.instance().string("save.session.error.noActive"));
            return;
        }

        SessionSvc sessionSvc = sessionSvcSupplier.get();

        try {
            sessionSvc.update(session);
            log.info(
                    "Session saved: sessionId={}, userId={}", session.getId(), session.getUserId());
            showInfo(ResourceMgr.instance().string("save.session.success"));
        } catch (ObjNotFoundException notFound) {
            persistMissingSession(session, sessionSvc);
        } catch (NonRecoverableException | RuntimeException ex) {
            log.error(
                    "Failed to save session: sessionId={}, userId={}",
                    session.getId(),
                    session.getUserId(),
                    ex);
            showError(ResourceMgr.instance().string("save.session.error.failed"));
        }
    }

    private void persistMissingSession(TutoringSession session, SessionSvc sessionSvc) {
        try {
            sessionSvc.create(session);
            log.info(
                    "Session did not exist during save; created instead: sessionId={}, userId={}",
                    session.getId(),
                    session.getUserId());
            showInfo(ResourceMgr.instance().string("save.session.success"));
        } catch (IllegalArgException | NonRecoverableException | RuntimeException ex) {
            log.error(
                    "Failed to create missing session during save: sessionId={}, userId={}",
                    session.getId(),
                    session.getUserId(),
                    ex);
            showError(ResourceMgr.instance().string("save.session.error.failed"));
        }
    }

    private void showInfo(String message) {
        dialogPresenter.accept(
                new DialogRequest(
                        message,
                        ResourceMgr.instance().string("dialog.title.information"),
                        JOptionPane.INFORMATION_MESSAGE));
    }

    private void showError(String message) {
        dialogPresenter.accept(
                new DialogRequest(
                        message,
                        ResourceMgr.instance().string("dialog.title.error"),
                        JOptionPane.ERROR_MESSAGE));
    }

    private void showDialog(DialogRequest request) {
        JOptionPane.showMessageDialog(
                MainFrame.instance(), request.message(), request.title(), request.messageType());
    }

    void setActiveSessionSupplierForTest(Supplier<TutoringSession> supplier) {
        activeSessionSupplier = supplier;
    }

    void setSessionSvcSupplierForTest(Supplier<SessionSvc> supplier) {
        sessionSvcSupplier = supplier;
    }

    void setDialogPresenterForTest(Consumer<DialogRequest> presenter) {
        dialogPresenter = presenter;
    }

    void resetTestOverrides() {
        activeSessionSupplier = () -> MainFrame.instance().getView().getModel();
        sessionSvcSupplier = ServiceFactory::findSessionSvc;
        dialogPresenter = this::showDialog;
    }

    static record DialogRequest(String message, String title, int messageType) {}
}
