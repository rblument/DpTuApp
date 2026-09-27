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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.AccountSvc;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.DpTuTutor;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.svc.StudentModelSvc;
import edu.regis.dptu.svc.TutorReply;

@SuppressWarnings("Logging")
public class NewExampleDispatchTest {
    private static final Gson GSON = new Gson();

    private SessionSvc mockSessionSvc;
    private AccountSvc mockAccountSvc;
    private StudentModelSvc mockStudentModelSvc;

    @BeforeEach
    public void setUpServices() {
        mockSessionSvc = mock(SessionSvc.class);
        mockAccountSvc = mock(AccountSvc.class);
        mockStudentModelSvc = mock(StudentModelSvc.class);
    }

    @Test
    public void newExampleWithValidSessionReturnsExampleStep() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        Account account = new Account("student@regis.edu", "pass");
        StudentModel studentModel = new StudentModel(account.getUserId());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);

            when(mockSessionSvc.retrieveSecurityToken(account.getUserId())).thenReturn("token-1");
            when(mockAccountSvc.retrieve(account.getUserId())).thenReturn(account);
            when(mockStudentModelSvc.retrieve(account.getUserId())).thenReturn(studentModel);

            ClientRequest request = new ClientRequest(ServerRequestType.NEW_EXAMPLE);
            request.setUserId(account.getUserId());
            request.setSecurityToken("token-1");
            request.setData("{}");

            TutorReply reply = tutor.request(request);

            // Session verification succeeds and newExample(String) returns a fresh example step.
            assertEquals(":NewExample", reply.getStatus());
            assertNotNull(reply.getData());

            Step nextStep = GSON.fromJson(reply.getData(), Step.class);
            assertNotNull(nextStep);
            assertEquals(StepSubType.COMPLETE_CELL, nextStep.getSubType());
        }
    }

    @Test
    public void newExampleWithInvalidTokenReturnsIllegalSecurityToken() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            when(mockSessionSvc.retrieveSecurityToken(anyString())).thenReturn("expected-token");

            ClientRequest request = new ClientRequest(ServerRequestType.NEW_EXAMPLE);
            request.setUserId("student@regis.edu");
            request.setSecurityToken("wrong-token");
            request.setData("{}");

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Illegal Security Token"));
        }
    }
}
