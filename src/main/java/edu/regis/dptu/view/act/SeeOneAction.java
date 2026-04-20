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

public class SeeOneAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(SeeOneAction.class);

    private static final SeeOneAction SINGLETON = new SeeOneAction();

    private final ProblemDAO problemDAO;

    public static SeeOneAction instance() {
        return SINGLETON;
    }

    private SeeOneAction() {
        super(Mode.SEE_ONE.title());
        this.problemDAO = new ProblemDAO();

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("action.seeOne.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    /**
     * @author EverettCV
     * @param evt
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        log.debug("SeeOneAction triggered");

        ProblemKind kind = null;
        Account account = null;

        try {
            DashboardPanel dashboard = SplashFrame.instance().getDashboardPanel();
            kind = dashboard.getSelectedProblemKind();

            log.debug("Selected ProblemKind for SEE_ONE: {}", kind);

            Problem problem = problemDAO.retrieveByKind(kind);

            if (log.isDebugEnabled()) {
                // Guard is intentional: avoid calling getters / touching domain object work if
                // DEBUG is off.
                log.debug("Retrieved problem: id={}, type={}", problem.getId(), problem.getType());
            }

            account = SplashFrame.instance().getAccount();

            if (log.isDebugEnabled()) {
                // Guard is intentional: avoid potential non-trivial getters when DEBUG is off.
                log.debug(
                        "Creating SEE_ONE TutoringSession for account id={}", account.getUserId());
            }

            TutoringSession ts = new TutoringSession(account, problem);
            ts.setMode(Mode.SEE_ONE);
            TutoringSession signedInSession = SplashFrame.instance().getSignedInSession();
            log.debug(
                    "SeeOneAction retrieved signedInSession: userId={} sessionId={} tokenPresent={}",
                    signedInSession != null ? signedInSession.getUserId() : null,
                    signedInSession != null ? signedInSession.getId() : null,
                    signedInSession != null && signedInSession.getSecurityToken() != null);

            if (signedInSession != null) {
                if (SessionResumeMatcher.canResumeSavedSession(
                        signedInSession, Mode.SEE_ONE, problem)) {
                    log.info(
                            "Resuming previously saved SEE_ONE sessionId={} for userId={}",
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
                        "Initialized SEE_ONE session with existing sessionId={}, tokenPresent={}, userId={}, hasCourse={}, hasUnit={}",
                        ts.getId(),
                        ts.getSecurityToken() != null,
                        ts.getUserId(),
                        ts.getCourse() != null,
                        ts.getUnit() != null);
            } else {
                log.warn(
                        "No signed-in session found in SplashFrame; "
                                + "SEE_ONE session has no authorization context (sessionId or securityToken)");
            }

            int taskId = problem.getTaskId();
            if (taskId < 0) {
                log.warn("SEE_ONE session created with invalid problem.taskId={}", taskId);
            } else {
                Task task = new Task(taskId);
                task.setProblem(problem);
                PendingTask pendingTask = new PendingTask(task);
                ts.addTask(pendingTask);
                /**
                 * If some later code expects the PendingTask to also have a current step we might
                 * need smth like this: if (task.getCurrentStep() != null) {
                 * pendingTask.setCurrentStep(new PendingStep(task.getCurrentStep())); }
                 */
                log.info("Initialized SEE_ONE session with PendingTask taskId={}", taskId);
            }

            SplashFrame.instance().selectLessonScreen(ts);

            log.debug("SEE_ONE session started successfully");

        } catch (ObjNotFoundException | NonRecoverableException e) {
            log.error(
                    "Failed to start SEE_ONE session (kind={}, accountId={})",
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
