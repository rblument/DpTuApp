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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.AccountSvc;
import edu.regis.dptu.svc.DpTuServer;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.svc.StudentModelSvc;

@SuppressWarnings("Logging")
public class DpTuServerConnectionTest {

    @Test
    public void dpTuConnectionReadsRequestAndWritesReply() throws Exception {
        String requestJson =
                "{\"requestType\":\"REQUEST_HINT\",\"data\":\"{}\",\"userId\":\"student@regis.edu\",\"securityToken\":\"token-1\"}\n";

        ByteArrayInputStream in =
                new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Socket client = mock(Socket.class);
        when(client.getInputStream()).thenReturn(in);
        when(client.getOutputStream()).thenReturn(out);

        Account account = new Account("student@regis.edu", "pw");
        SessionSvc sessionSvc = mock(SessionSvc.class);
        AccountSvc accountSvc = mock(AccountSvc.class);
        StudentModelSvc studentModelSvc = mock(StudentModelSvc.class);
        StudentModel studentModel = new StudentModel(account.getUserId());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(sessionSvc);
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(accountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(studentModelSvc);

            when(sessionSvc.retrieveSecurityToken(account.getUserId())).thenReturn("token-1");
            when(accountSvc.retrieve(account.getUserId())).thenReturn(account);
            when(studentModelSvc.retrieve(account.getUserId())).thenReturn(studentModel);

            DpTuServer server = new DpTuServer();
            Class<?> connClass = Class.forName("edu.regis.dptu.svc.DpTuServer$DpTuConnection");
            Constructor<?> ctor = connClass.getDeclaredConstructor(DpTuServer.class, Socket.class);
            ctor.setAccessible(true);

            Runnable connection = (Runnable) ctor.newInstance(server, client);
            connection.run();
        }

        String replyJson = out.toString(StandardCharsets.UTF_8);
        assertTrue(replyJson.contains("\"status\":\"Hint\""));
        verify(client).close();
    }
}
