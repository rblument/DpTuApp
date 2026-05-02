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
package edu.regis.dptu.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.AccountSvc;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.DpTuTutor;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.svc.StudentModelSvc;
import edu.regis.dptu.svc.TutorReply;

/**
 * @author Cormac Moss 2026
 *     <p>tests for save session feature covering: Successful save- server acknowledges with
 *     "SessionSaved" Missing/empty - server returns ":ERR" without touching the DAO Session not
 *     found in DB - ObjNotFoundException surfaces as ":ERR" Database failure -
 *     NonRecoverableException surfaces as ":ERR" Security token mismatch - server rejects with
 *     ":ERR" before calling the DAO End-to-end ServerRequestType routing via DpTuTutor.request().
 */
@SuppressWarnings("Logging")
public class SaveSessionActionTest {

    private static final String USER_ID = "student@regis.edu";
    private static final String SECURITY_TOKEN = "valid-token-abc";
    private static final int SESSION_ID = 42;

    private DpTuTutor tutor;
    private SessionSvc sessionSvc;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        tutor = new DpTuTutor();
        sessionSvc = mock(SessionSvc.class);
        gson = new Gson();
    }

    // --- saveSession() unit-level tests (bypass request() dispatch) ---

    @Test
    public void saveSession_returnsSessionSaved_whenUpdateSucceeds() throws Exception {
        String jsonSession = gson.toJson(buildSession());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            TutorReply reply = tutor.saveSession(jsonSession);

            assertEquals("SessionSaved", reply.getStatus());
            verify(sessionSvc).update(any(TutoringSession.class));
        }
    }

    @Test
    public void saveSession_returnsErr_whenSessionJsonIsNull() throws Exception {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            TutorReply reply = tutor.saveSession(null);

            assertEquals(":ERR", reply.getStatus());
            assertNotNull(reply.getData());
            verify(sessionSvc, never()).update(any());
        }
    }

    @Test
    public void saveSession_returnsErr_whenSessionHasNoUserId() throws Exception {
        String jsonSession = gson.toJson(new TutoringSession(""));

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            TutorReply reply = tutor.saveSession(jsonSession);

            assertEquals(":ERR", reply.getStatus());
            verify(sessionSvc, never()).update(any());
        }
    }

    @Test
    public void saveSession_returnsErr_whenSessionNotFoundInDb() throws Exception {
        String jsonSession = gson.toJson(buildSession());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);
            doThrow(new ObjNotFoundException("not found"))
                    .when(sessionSvc)
                    .update(any(TutoringSession.class));

            TutorReply reply = tutor.saveSession(jsonSession);

            assertEquals(":ERR", reply.getStatus());
        }
    }

    @Test
    public void saveSession_returnsErr_whenDatabaseThrowsNonRecoverableException()
            throws Exception {
        String jsonSession = gson.toJson(buildSession());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);
            doThrow(new NonRecoverableException("db error", null))
                    .when(sessionSvc)
                    .update(any(TutoringSession.class));

            TutorReply reply = tutor.saveSession(jsonSession);

            assertEquals(":ERR", reply.getStatus());
        }
    }

    // --- End-to-end routing through request() ---

    @Test
    public void request_saveSession_routesCorrectlyAndSavesSession() throws Exception {
        ClientRequest request = new ClientRequest(ServerRequestType.SAVE_SESSION);
        request.setUserId(USER_ID);
        request.setSecurityToken(SECURITY_TOKEN);
        request.setSessionId(String.valueOf(SESSION_ID));
        request.setData(gson.toJson(buildSession()));

        AccountSvc accountSvc = mock(AccountSvc.class);
        StudentModelSvc studentModelSvc = mock(StudentModelSvc.class);
        Account account = new Account(USER_ID, "pw");
        StudentModel studentModel = new StudentModel(USER_ID);

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(accountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(studentModelSvc);

            when(sessionSvc.retrieveSecurityToken(USER_ID)).thenReturn(SECURITY_TOKEN);

            when(accountSvc.retrieve(USER_ID)).thenReturn(account);
            when(studentModelSvc.retrieve(USER_ID)).thenReturn(studentModel);

            TutorReply reply = tutor.request(request);

            assertEquals("SessionSaved", reply.getStatus());
            verify(sessionSvc).update(any(TutoringSession.class));
        }
    }

    @Test
    public void request_saveSession_rejectsInvalidSecurityToken() throws Exception {
        ClientRequest request = new ClientRequest(ServerRequestType.SAVE_SESSION);
        request.setUserId(USER_ID);
        request.setSecurityToken("wrong-token");
        request.setData(gson.toJson(buildSession()));

        AccountSvc accountSvc = mock(AccountSvc.class);
        StudentModelSvc studentModelSvc = mock(StudentModelSvc.class);

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(accountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(studentModelSvc);

            when(sessionSvc.retrieveSecurityToken(USER_ID)).thenReturn(SECURITY_TOKEN);

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            verify(sessionSvc, never()).update(any());
        }
    }

    @Test
    public void request_saveSession_returnsErr_whenNoSessionExistsForTokenLookup()
            throws Exception {
        ClientRequest request = new ClientRequest(ServerRequestType.SAVE_SESSION);
        request.setUserId(USER_ID);
        request.setSecurityToken(SECURITY_TOKEN);
        request.setData(gson.toJson(buildSession()));

        AccountSvc accountSvc = mock(AccountSvc.class);
        StudentModelSvc studentModelSvc = mock(StudentModelSvc.class);

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);

            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(accountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(studentModelSvc);

            when(sessionSvc.retrieveSecurityToken(USER_ID))
                    .thenThrow(new ObjNotFoundException("no session"));

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            verify(sessionSvc, never()).update(any());
        }
    }

    // --- Enum contract ---

    @Test
    public void serverRequestType_saveSession_hasCorrectRequestName() {
        assertEquals(":SaveSession", ServerRequestType.SAVE_SESSION.getRequestName());
        assertEquals(":SaveSession", ServerRequestType.SAVE_SESSION.toString());
    }

    // --- Helper ---

    private TutoringSession buildSession() {
        TutoringSession session = new TutoringSession(USER_ID);
        session.setId(SESSION_ID);
        session.setSecurityToken(SECURITY_TOKEN);
        return session;
    }
}
