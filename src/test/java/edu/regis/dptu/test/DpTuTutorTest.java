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

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepCompletion;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.DpTuTutor;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.TutorReply;

@SuppressWarnings("Logging")
public class DpTuTutorTest {
    private static final Gson GSON = new Gson();

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
