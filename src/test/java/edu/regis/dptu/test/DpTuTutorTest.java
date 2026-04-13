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

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepCompletion;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.Unit;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.AccountSvc;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.CompletedTaskSvc;
import edu.regis.dptu.svc.CourseSvc;
import edu.regis.dptu.svc.DpTuTutor;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.svc.SessionSvc;
import edu.regis.dptu.svc.StudentModelSvc;
import edu.regis.dptu.svc.TutorReply;

@SuppressWarnings("Logging")
public class DpTuTutorTest {
    private static final Gson GSON = new Gson();
    private AccountSvc mockAccountSvc;
    private CourseSvc mockCourseSvc;
    private SessionSvc mockSessionSvc;
    private StudentModelSvc mockStudentModelSvc;
    private CompletedTaskSvc mockCompletedTaskSvc;

    @BeforeEach
    public void setUpServices() {
        mockAccountSvc = mock(AccountSvc.class);
        mockCourseSvc = mock(CourseSvc.class);
        mockSessionSvc = mock(SessionSvc.class);
        mockStudentModelSvc = mock(StudentModelSvc.class);
        mockCompletedTaskSvc = mock(CompletedTaskSvc.class);
    }

    @Test
    public void requestHintThroughRequestDispatcher() {
        DpTuTutor tutor = new DpTuTutor();

        ClientRequest req = new ClientRequest(ServerRequestType.REQUEST_HINT);
        req.setData("{}");

        TutorReply reply = tutor.request(req);

        assertEquals(":ERR", reply.getStatus());
        assertNotNull(reply.getData());
    }

    @Test
    public void completedTaskWithoutAuthenticatedStudentReturnsError() {
        DpTuTutor tutor = new DpTuTutor();

        TutorReply reply = tutor.completedTask("12");

        assertEquals(":ERR", reply.getStatus());
        assertTrue(reply.getData().contains("No authenticated student"));
    }

    @Test
    public void completedTaskWithBadTaskIdReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        setStudent(tutor, new Student(new Account("unit@test.edu")));

        TutorReply reply = tutor.completedTask("not-an-int");

        assertEquals(":ERR", reply.getStatus());
        assertTrue(reply.getData().contains("Invalid task id"));
    }

    @Test
    public void completedStepCoversAllStepTypes() {
        DpTuTutor tutor = new DpTuTutor();

        TutorReply infoReply = tutor.completedStep(stepCompletionJson(StepSubType.INFO_MESSAGE));
        assertEquals(":StepCompletionReply", infoReply.getStatus());

        TutorReply cellReply = tutor.completedStep(stepCompletionJson(StepSubType.COMPLETE_CELL));
        assertEquals(":StepCompletionReply", cellReply.getStatus());
        assertNotNull(cellReply.getData());

        TutorReply firstRowReply =
                tutor.completedStep(stepCompletionJson(StepSubType.COMPLETE_FIRST_ROW));
        assertEquals(":StepCompletionReply", firstRowReply.getStatus());

        TutorReply firstColReply =
                tutor.completedStep(stepCompletionJson(StepSubType.COMPLETE_FIRST_COL));
        assertEquals(":StepCompletionReply", firstColReply.getStatus());

        TutorReply unknownReply = tutor.completedStep(stepCompletionJson(StepSubType.DEFAULT));
        assertEquals(":ERR", unknownReply.getStatus());
        assertTrue(unknownReply.getData().contains("Unknown step completion"));
    }

    @Test
    public void requestHintDirectMethodReturnsHintPayload() {
        DpTuTutor tutor = new DpTuTutor();

        TutorReply reply = tutor.requestHint("any");

        assertEquals("Hint", reply.getStatus());
        assertEquals("This is a hint from the tutor.", reply.getData());
    }

    @Test
    public void completedTaskThroughRequestDispatcherWithValidSessionReturnsOk() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        Account account = new Account("student@regis.edu", "pass");
        StudentModel studentModel = new StudentModel(account.getUserId());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);
            mockedFactory
                    .when(ServiceFactory::findCompletedTaskSvc)
                    .thenReturn(mockCompletedTaskSvc);

            when(mockSessionSvc.retrieveSecurityToken(account.getUserId())).thenReturn("token-1");
            when(mockAccountSvc.retrieve(account.getUserId())).thenReturn(account);
            when(mockStudentModelSvc.retrieve(account.getUserId())).thenReturn(studentModel);

            ClientRequest request = new ClientRequest(ServerRequestType.COMPLETED_TASK);
            request.setUserId(account.getUserId());
            request.setSecurityToken("token-1");
            request.setData("7");

            TutorReply reply = tutor.request(request);

            assertEquals(":OK", reply.getStatus());
            assertTrue(reply.getData().contains("Completed taskId= 7"));
            verify(mockCompletedTaskSvc).markCompleted(account.getUserId(), 7);
        }
    }

    @Test
    public void completedTaskThroughRequestDispatcherWithBadTokenReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            when(mockSessionSvc.retrieveSecurityToken("student@regis.edu")).thenReturn("expected");

            ClientRequest request = new ClientRequest(ServerRequestType.COMPLETED_TASK);
            request.setUserId("student@regis.edu");
            request.setSecurityToken("wrong");
            request.setData("7");

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Illegal Security Token"));
        }
    }

    @Test
    public void signInWithMatchingPasswordReturnsAuthenticated() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account requestAcct = new Account("student@regis.edu", "pw");
        Account dbAcct = new Account("student@regis.edu", "pw");
        StudentModel studentModel = new StudentModel(dbAcct.getUserId());
        TutoringSession session = new TutoringSession(dbAcct.getUserId());

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);

            when(mockAccountSvc.retrieve(dbAcct.getUserId())).thenReturn(dbAcct);
            when(mockStudentModelSvc.retrieve(dbAcct.getUserId())).thenReturn(studentModel);
            when(mockSessionSvc.retrieve(any(Student.class))).thenReturn(session);

            TutorReply reply = tutor.signIn(GSON.toJson(requestAcct));

            assertEquals("Authenticated", reply.getStatus());
            assertNotNull(reply.getData());
            assertTrue(reply.getData().contains(dbAcct.getUserId()));
        }
    }

    @Test
    public void signInWithUnknownUserReturnsUnknownUser() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account requestAcct = new Account("missing@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            when(mockAccountSvc.retrieve(anyString()))
                    .thenThrow(new ObjNotFoundException("missing"));

            TutorReply reply = tutor.signIn(GSON.toJson(requestAcct));

            assertEquals("UnknownUser", reply.getStatus());
        }
    }

    @Test
    public void createAccountHappyPathReturnsCreated() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("new@regis.edu", "pw");

        Course course = buildCourseForSessionCreation();

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);

            when(mockCourseSvc.retrieve(1)).thenReturn(course);

            TutorReply reply = tutor.createAccount(GSON.toJson(account));

            assertEquals("Created", reply.getStatus());
            verify(mockAccountSvc).create(any(Account.class));
            verify(mockStudentModelSvc).create(any(Student.class));
            verify(mockSessionSvc).create(any(TutoringSession.class));
        }
    }

    @Test
    public void createAccountWithDuplicateUserReturnsIllegalUserId() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("dupe@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            doThrow(new IllegalArgException("exists"))
                    .when(mockAccountSvc)
                    .create(any(Account.class));

            TutorReply reply = tutor.createAccount(GSON.toJson(account));

            assertEquals("IllegalUserId", reply.getStatus());
        }
    }

    @Test
    public void completedTaskWhenPersistenceFailsReturnsErrorReply() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        setStudent(tutor, new Student(new Account("unit@test.edu")));

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory
                    .when(ServiceFactory::findCompletedTaskSvc)
                    .thenReturn(mockCompletedTaskSvc);
            doThrow(new NonRecoverableException("db down"))
                    .when(mockCompletedTaskSvc)
                    .markCompleted(anyString(), anyInt());

            TutorReply reply = tutor.completedTask("99");

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Failed to record completed task"));
        }
    }

    @Test
    public void requestWithMissingSessionReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            when(mockSessionSvc.retrieveSecurityToken("student@regis.edu"))
                    .thenThrow(new ObjNotFoundException("missing session"));

            ClientRequest request = new ClientRequest(ServerRequestType.COMPLETED_TASK);
            request.setUserId("student@regis.edu");
            request.setSecurityToken("token");
            request.setData("5");

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("No session exists for user"));
        }
    }

    @Test
    public void requestWithMissingStudentModelReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("student@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);

            when(mockSessionSvc.retrieveSecurityToken(account.getUserId())).thenReturn("token");
            when(mockAccountSvc.retrieve(account.getUserId())).thenReturn(account);
            when(mockStudentModelSvc.retrieve(account.getUserId()))
                    .thenThrow(new ObjNotFoundException("missing model"));

            ClientRequest request = new ClientRequest(ServerRequestType.COMPLETED_TASK);
            request.setUserId(account.getUserId());
            request.setSecurityToken("token");
            request.setData("3");

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Student model not found"));
        }
    }

    @Test
    public void signInWithInvalidPasswordReturnsInvalidPassword() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account requestAcct = new Account("student@regis.edu", "bad");
        Account dbAcct = new Account("student@regis.edu", "expected");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            when(mockAccountSvc.retrieve(dbAcct.getUserId())).thenReturn(dbAcct);

            TutorReply reply = tutor.signIn(GSON.toJson(requestAcct));

            assertEquals("InvalidPassword", reply.getStatus());
        }
    }

    @Test
    public void signInWithMissingStudentModelReturnsErrorReply() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account requestAcct = new Account("student@regis.edu", "pw");
        Account dbAcct = new Account("student@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);

            when(mockAccountSvc.retrieve(dbAcct.getUserId())).thenReturn(dbAcct);
            when(mockStudentModelSvc.retrieve(dbAcct.getUserId()))
                    .thenThrow(new ObjNotFoundException("missing model"));

            TutorReply reply = tutor.signIn(GSON.toJson(requestAcct));

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Student model not found in sign in"));
        }
    }

    @Test
    public void signInWithServiceFailureReturnsDefaultErrorReply() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account requestAcct = new Account("student@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            when(mockAccountSvc.retrieve(anyString())).thenThrow(new NonRecoverableException("db"));

            TutorReply reply = tutor.signIn(GSON.toJson(requestAcct));

            assertEquals("ERR", reply.getStatus());
        }
    }

    @Test
    public void createAccountWithMissingCourseReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("new@regis.edu", "pw");

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);

            when(mockCourseSvc.retrieve(1)).thenThrow(new ObjNotFoundException("missing course"));

            TutorReply reply = tutor.createAccount(GSON.toJson(account));

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("Unknown course"));
        }
    }

    @Test
    public void requestWithSessionServiceFailureReturnsError() throws Exception {
        DpTuTutor tutor = new DpTuTutor();

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);
            when(mockSessionSvc.retrieveSecurityToken("student@regis.edu"))
                    .thenThrow(new NonRecoverableException("token lookup failed"));

            ClientRequest request = new ClientRequest(ServerRequestType.COMPLETED_TASK);
            request.setUserId("student@regis.edu");
            request.setSecurityToken("token");
            request.setData("3");

            TutorReply reply = tutor.request(request);

            assertEquals(":ERR", reply.getStatus());
            assertTrue(reply.getData().contains("NonRecoverableException"));
        }
    }

    @Test
    public void createAccountWithoutSequenceZeroUnitThrowsNonRecoverable() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("new2@regis.edu", "pw");

        Course course = new Course(1);
        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        course.setExercisingLocations(new ArrayList<>());

        Unit unit = new Unit(2);
        unit.setSequenceId(1);
        course.addUnit(unit);

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);

            when(mockCourseSvc.retrieve(1)).thenReturn(course);

            assertThrows(
                    NonRecoverableException.class, () -> tutor.createAccount(GSON.toJson(account)));
        }
    }

    @Test
    public void createAccountWithoutFirstTaskThrowsNonRecoverable() throws Exception {
        DpTuTutor tutor = new DpTuTutor();
        Account account = new Account("new3@regis.edu", "pw");

        Course course = new Course(1);
        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        course.setExercisingLocations(new ArrayList<>());

        Unit unit = new Unit(1);
        unit.setSequenceId(0);
        unit.setTasks(new ArrayList<>());
        course.addUnit(unit);

        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::findAccountSvc).thenReturn(mockAccountSvc);
            mockedFactory.when(ServiceFactory::findCourseSvc).thenReturn(mockCourseSvc);
            mockedFactory.when(ServiceFactory::findStudentModelSvc).thenReturn(mockStudentModelSvc);
            mockedFactory.when(ServiceFactory::findSessionSvc).thenReturn(mockSessionSvc);

            when(mockCourseSvc.retrieve(1)).thenReturn(course);

            assertThrows(
                    NonRecoverableException.class, () -> tutor.createAccount(GSON.toJson(account)));
        }
    }

    private static Course buildCourseForSessionCreation() {
        Course course = new Course(1);
        course.setPrimaryPedagogy(TaskSelectionKind.FIXED_SEQUENCE);
        course.setExercisingLocations(new ArrayList<>());

        Unit unit = new Unit(1);
        unit.setSequenceId(0);

        Task task = new Task(1);
        task.setSequenceIndex(0);
        task.setProblem(new LCSProblem(101, "AB", "AC"));
        task.addStep(new Step(1, 0, StepSubType.INFO_MESSAGE));

        unit.addTask(task);
        course.addUnit(unit);

        return course;
    }

    private static String stepCompletionJson(StepSubType subType) {
        Step step = new Step(1, 1, subType);
        StepCompletion completion = new StepCompletion(step, "payload");
        return GSON.toJson(completion);
    }

    private static void setStudent(DpTuTutor tutor, Student student) throws Exception {
        Field field = DpTuTutor.class.getDeclaredField("student");
        field.setAccessible(true);
        field.set(tutor, student);
    }
}
