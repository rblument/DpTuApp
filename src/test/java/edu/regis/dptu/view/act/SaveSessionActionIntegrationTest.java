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
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.util.ResourceMgr;

public class SaveSessionActionIntegrationTest {

    @AfterEach
    public void resetActionState() {
        SaveSessionAction.instance().resetTestOverrides();
    }

    @Test
    public void saveActionPersistsSessionAndShowsSuccessMessage() throws Exception {
        SaveSessionAction action = SaveSessionAction.instance();
        SessionSvc mockSessionSvc = mock(SessionSvc.class);
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setId(77);

        AtomicReference<SaveSessionAction.DialogRequest> shownDialog = new AtomicReference<>();

        action.setActiveSessionSupplierForTest(() -> session);
        action.setSessionSvcSupplierForTest(() -> mockSessionSvc);
        action.setDialogPresenterForTest(shownDialog::set);

        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "save"));

        verify(mockSessionSvc).update(session);
        verify(mockSessionSvc, never()).create(session);

        SaveSessionAction.DialogRequest dialog = shownDialog.get();
        assertNotNull(dialog);
        assertEquals(ResourceMgr.instance().string("save.session.success"), dialog.message());
    }

    @Test
    public void saveActionCreatesSessionWhenUpdateReportsMissingSession() throws Exception {
        SaveSessionAction action = SaveSessionAction.instance();
        SessionSvc mockSessionSvc = mock(SessionSvc.class);
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setId(88);

        doUpdateThrowNotFound(mockSessionSvc);

        AtomicReference<SaveSessionAction.DialogRequest> shownDialog = new AtomicReference<>();

        action.setActiveSessionSupplierForTest(() -> session);
        action.setSessionSvcSupplierForTest(() -> mockSessionSvc);
        action.setDialogPresenterForTest(shownDialog::set);

        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "save"));

        verify(mockSessionSvc).update(session);
        verify(mockSessionSvc).create(session);

        SaveSessionAction.DialogRequest dialog = shownDialog.get();
        assertNotNull(dialog);
        assertEquals(ResourceMgr.instance().string("save.session.success"), dialog.message());
    }

    @Test
    public void saveActionShowsInfoWhenNoActiveSessionExists() {
        SaveSessionAction action = SaveSessionAction.instance();

        AtomicReference<SaveSessionAction.DialogRequest> shownDialog = new AtomicReference<>();
        action.setActiveSessionSupplierForTest(() -> null);
        action.setDialogPresenterForTest(shownDialog::set);

        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "save"));

        SaveSessionAction.DialogRequest dialog = shownDialog.get();
        assertNotNull(dialog);
        assertEquals(
                ResourceMgr.instance().string("save.session.error.noActive"), dialog.message());
        assertEquals(ResourceMgr.instance().string("dialog.title.information"), dialog.title());
    }

    @Test
    public void saveActionShowsErrorWhenPersistenceFails() throws Exception {
        SaveSessionAction action = SaveSessionAction.instance();
        SessionSvc mockSessionSvc = mock(SessionSvc.class);
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setId(99);

        doThrow(new NonRecoverableException("database unavailable"))
                .when(mockSessionSvc)
                .update(session);

        AtomicReference<SaveSessionAction.DialogRequest> shownDialog = new AtomicReference<>();

        action.setActiveSessionSupplierForTest(() -> session);
        action.setSessionSvcSupplierForTest(() -> mockSessionSvc);
        action.setDialogPresenterForTest(shownDialog::set);

        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "save"));

        verify(mockSessionSvc).update(session);

        SaveSessionAction.DialogRequest dialog = shownDialog.get();
        assertNotNull(dialog);
        assertEquals(ResourceMgr.instance().string("save.session.error.failed"), dialog.message());
        assertEquals(ResourceMgr.instance().string("dialog.title.error"), dialog.title());
        assertEquals(JOptionPane.ERROR_MESSAGE, dialog.messageType());
    }

    private void doUpdateThrowNotFound(SessionSvc mockSessionSvc) throws Exception {
        doThrow(new ObjNotFoundException("missing session"))
                .when(mockSessionSvc)
                .update(any(TutoringSession.class));
    }
}
