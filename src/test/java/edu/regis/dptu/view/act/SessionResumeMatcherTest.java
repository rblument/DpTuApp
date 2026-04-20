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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingStep;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TutoringSession;

public class SessionResumeMatcherTest {

    @Test
    public void resumeAllowedWhenModeProblemAndProgressMatch() {
        Problem selectedProblem = new edu.regis.dptu.model.LCSProblem(55, "A", "B");
        TutoringSession session = sessionWithModeAndProblem(Mode.SEE_ONE, selectedProblem);
        session.addTask(taskWithPendingStep(13, 21));

        assertTrue(
                SessionResumeMatcher.canResumeSavedSession(session, Mode.SEE_ONE, selectedProblem));
    }

    @Test
    public void resumeDeniedWhenProblemIdDiffersEvenIfTypeMatches() {
        Problem savedProblem = new edu.regis.dptu.model.LCSProblem(55, "A", "B");
        Problem selectedProblem = new edu.regis.dptu.model.LCSProblem(56, "A", "B");
        TutoringSession session = sessionWithModeAndProblem(Mode.SEE_ONE, savedProblem);
        session.addTask(taskWithPendingStep(13, 21));

        assertFalse(
                SessionResumeMatcher.canResumeSavedSession(session, Mode.SEE_ONE, selectedProblem));
    }

    @Test
    public void resumeDeniedWhenModeDiffers() {
        Problem selectedProblem = new edu.regis.dptu.model.LCSProblem(55, "A", "B");
        TutoringSession session = sessionWithModeAndProblem(Mode.DO_ONE, selectedProblem);
        session.addTask(taskWithPendingStep(13, 21));

        assertFalse(
                SessionResumeMatcher.canResumeSavedSession(session, Mode.SEE_ONE, selectedProblem));
    }

    @Test
    public void resumeDeniedWhenNoPendingProgressExists() {
        Problem selectedProblem = new edu.regis.dptu.model.LCSProblem(55, "A", "B");
        TutoringSession session = sessionWithModeAndProblem(Mode.SEE_ONE, selectedProblem);

        assertFalse(
                SessionResumeMatcher.canResumeSavedSession(session, Mode.SEE_ONE, selectedProblem));
    }

    @Test
    public void resumeDeniedWhenSavedProblemMissing() {
        Problem selectedProblem = new edu.regis.dptu.model.LCSProblem(55, "A", "B");
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setMode(Mode.SEE_ONE);
        session.addTask(taskWithPendingStep(13, 21));

        assertFalse(
                SessionResumeMatcher.canResumeSavedSession(session, Mode.SEE_ONE, selectedProblem));
    }

    private TutoringSession sessionWithModeAndProblem(Mode mode, Problem problem) {
        TutoringSession session = new TutoringSession("student@regis.edu");
        session.setMode(mode);
        session.setProblem(problem);
        return session;
    }

    private PendingTask taskWithPendingStep(int taskId, int stepId) {
        Task task = new Task(taskId);
        Step step = new Step(stepId, 0, StepSubType.INFO_MESSAGE);
        task.addStep(step);

        PendingTask pendingTask = new PendingTask(task);
        pendingTask.setCurrentStep(new PendingStep(step));
        return pendingTask;
    }
}
