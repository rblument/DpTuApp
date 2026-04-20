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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.dao.ProblemDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.DashboardPanel;
import edu.regis.dptu.view.SplashFrame;

public class DoOneAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(DoOneAction.class);

    private static final DoOneAction SINGLETON = new DoOneAction();

    private final ProblemDAO problemDAO;

    public static DoOneAction instance() {
        return SINGLETON;
    }

    private DoOneAction() {
        super(Mode.DO_ONE.title());
        this.problemDAO = new ProblemDAO();

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.doOne.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
    }

    /**
     * @param evt
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        log.debug("DoOneAction triggered");

        ProblemKind kind = null;
        Account account = null;

        try {
            DashboardPanel dashboard = SplashFrame.instance().getDashboardPanel();
            kind = dashboard.getSelectedProblemKind();

            log.debug("Selected ProblemKind for DO_ONE: {}", kind);

            Problem problem = problemDAO.retrieveByKind(kind);

            if (log.isDebugEnabled()) {
                log.debug("Retrieved problem: id={}, type={}", problem.getId(), problem.getType());
            }

            account = SplashFrame.instance().getAccount();

            if (log.isDebugEnabled()) {
                log.debug("Creating DO_ONE TutoringSession for account id={}", account.getUserId());
            }

            TutoringSession ts = new TutoringSession(account, problem);
            ts.setMode(Mode.DO_ONE);

            TutoringSession signedInSession = SplashFrame.instance().getSignedInSession();
            if (signedInSession != null) {
                if (SessionResumeMatcher.canResumeSavedSession(
                        signedInSession, Mode.DO_ONE, problem)) {
                    log.info(
                            "Resuming previously saved DO_ONE sessionId={} for userId={}",
                            signedInSession.getId(),
                            signedInSession.getUserId());
                    SplashFrame.instance().selectLessonScreen(signedInSession);
                    return;
                }

                ts.setId(signedInSession.getId());
                ts.setSecurityToken(signedInSession.getSecurityToken());
                ts.setUserId(signedInSession.getUserId());
                ts.setCourse(signedInSession.getCourse());
                ts.setUnit(signedInSession.getUnit());
                ts.setIsActive(signedInSession.isIsActive());
                ts.setStartDate(signedInSession.getStartDate());
                log.info(
                        "Initialized DO_ONE session with existing sessionId={}, tokenPresent={}, userId={}, hasCourse={}, hasUnit={}",
                        ts.getId(),
                        ts.getSecurityToken() != null,
                        ts.getUserId(),
                        ts.getCourse() != null,
                        ts.getUnit() != null);
            } else {
                log.warn(
                        "No signed-in session found in SplashFrame; "
                                + "DO_ONE session has no authorization context (sessionId or securityToken)");
            }

            /**
             * for future students, in order to initialize a tutoring session, you need to populate
             * the task list for that session none of our constructors (there are three), do this
             * automatically, except for the one that is never called, so you need to do it manually
             * unfortunately, there is no created list of "DoOne" tasks yet, so if you just "ungrey"
             * the Do_One button, and it doesn't compile and you come here, the reason is because
             * there are no taskId's assigned to whatever DoOne tasks you have created TODO: This
             * code is replicated in SeeOne and TeachOne--fix this in the constructor of,
             * TutoringSession(user, problem)
             */
            int taskId = problem.getTaskId();
            if (taskId < 0) {
                log.warn("DO_ONE session created with invalid problem.taskId={}", taskId);
            } else {
                Task task = new Task(taskId);
                task.setProblem(problem);
                try {
                    task.getCurrentStep();
                } catch (IndexOutOfBoundsException e) {
                    log.error(
                            "DO_ONE task scaffold is incomplete: taskId={} has no steps/current step",
                            taskId,
                            e);
                    SplashFrame.instance()
                            .showError(
                                    ResourceMgr.instance().string("dialog.title.error"),
                                    ResourceMgr.instance().string("error.failedToLoadProblem"));
                    return;
                }

                PendingTask pendingTask = new PendingTask(task);
                ts.addTask(pendingTask);
                /**
                 * If some later code expects the PendingTask to also have a current step we might
                 * need smth like this: if (task.getCurrentStep() != null) {
                 * pendingTask.setCurrentStep(new PendingStep(task.getCurrentStep())); }
                 */
                log.info("Initialized DO_ONE session with PendingTask taskId={}", taskId);
            }

            SplashFrame.instance().selectLessonScreen(ts);

            log.debug("DO_ONE session started successfully");

        } catch (ObjNotFoundException | NonRecoverableException e) {
            log.error(
                    "Failed to start DO_ONE session (kind={}, accountId={})",
                    kind,
                    account != null ? account.getUserId() : null,
                    e);

            SplashFrame.instance()
                    .showError(
                            ResourceMgr.instance().string("dialog.title.error"),
                            ResourceMgr.instance().string("error.failedToLoadProblem"));
        }
    }
}
