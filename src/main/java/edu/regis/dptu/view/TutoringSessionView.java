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
package edu.regis.dptu.view;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.PendingTask;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.SvcFacade;
import edu.regis.dptu.svc.TutorReply;
import edu.regis.dptu.svc.TutorSvc;

/**
 * Displays a tutoring session (the top-level GUI view for the application). Integrates views and
 * ensures the shared Problem model is distributed correctly. Includes Debug Prints.
 *
 * @author rickb (Modified by Assistant for Functional Integration & Debug)
 */
public class TutoringSessionView extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(TutoringSessionView.class);

    private TutoringSession model;
    private ModeView currentModeView;
    // service used to send requests to tutor/server
    private TutorSvc tutorSvc;

    private static final Map<Mode, ModeView> modeViewStrategies =
            new HashMap<>() {
                {
                    put(Mode.SEE_ONE, new SeeOneView());
                    put(Mode.DO_ONE, new DoOneView());
                }
            };

    public TutoringSession getModel() {
        return model;
    }

    /** no args necessary for MainFrame */
    public TutoringSessionView() {}

    public void setTutorSvc(TutorSvc tutorSvc) {
        this.tutorSvc = tutorSvc;
    }

    /**
     * Sets the current tutoring session model for this view and updates the displayed mode view.
     *
     * <p>This method:
     *
     * <ul>
     *   <li>Replaces the currently displayed {@link ModeView} based on the session's {@link Mode}
     *   <li>Propagates the active {@link edu.regis.dptu.model.Problem} to the selected mode view
     *   <li>Temporarily wires a callback for persisting task completion events to the tutor/server
     *       when running in {@code SEE_ONE} mode
     * </ul>
     *
     * <p><b>Temporary design note:</b> The task completion persistence hook is currently wired
     * through the view layer for expedience. This logic should be migrated to a controller/service
     * layer in a future refactor to improve separation of concerns.
     *
     * @param model the active {@link TutoringSession} to display and bind to this view
     * @throws IllegalArgumentException if no {@link ModeView} is registered for the session's mode
     */
    public void setModel(TutoringSession model) {
        TutoringSessionView.log.info("Setting tutoring session");
        this.model = model;

        if (currentModeView != null) {
            remove((GPanel) currentModeView);
        }

        currentModeView = modeViewStrategies.get(model.getMode());
        if (currentModeView == null) {
            throw new IllegalArgumentException(
                    "TutoringSessionView: No view found for mode: " + model.getMode());
        }

        // TEMP: wire task completion persistence for SEE_ONE mode through CodeView
        // NOTE: This should eventually be moved to a controller/service layer.
        if (currentModeView instanceof SeeOneView) {
            SeeOneView seeOne = (SeeOneView) currentModeView;

            seeOne.setOnCompletedTaskSend(
                    () -> {
                        try {

                            // Session identity/security info is stored in the TutoringSession model
                            String userId = model.getUserId();
                            String token = model.getSecurityToken();

                            // TaskId comes from the current task in session
                            PendingTask currentTask = model.getCurrentTask();
                            if (currentTask == null || currentTask.getTask() == null) {
                                log.debug(
                                        "No current task available in session; cannot send CompletedTask");
                                return;
                            }

                            int taskId = model.getProblem().getTaskId();
                            if (taskId < 0) {
                                log.debug(
                                        "Problem.taskId not set (taskId={}); cannot send CompletedTask",
                                        taskId);
                                return;
                            }

                            // Build and send the CompletedTask request
                            ClientRequest req = new ClientRequest(ServerRequestType.COMPLETED_TASK);
                            req.setUserId(userId);
                            req.setSecurityToken(token);
                            req.setData(String.valueOf(taskId));
                            log.info("Sending COMPLETED_TASK userId={} taskId{}", userId, taskId);

                            TutorReply reply = SvcFacade.instance().tutorRequest(req);
                            log.info(
                                    "COMPLETED_TASK reply status={} data={}",
                                    reply.getStatus(),
                                    reply.getData());

                            if (!":OK".equals(reply.getStatus())) {
                                log.warn(
                                        "CompletedTask failed: status={} data={}",
                                        reply.getStatus(),
                                        reply.getData());
                            } else {
                                log.info(
                                        "CompletedTask persisted userId={} taskId={}",
                                        userId,
                                        taskId);
                            }

                        } catch (Exception ex) {
                            log.error("Error sending CompletedTask", ex);
                        }
                    });
        }

        currentModeView.setModel(model.getProblem());
        add((GPanel) currentModeView);

        revalidate();
        repaint();
    }

    public void showBacktrackingPanel(boolean backtrackingOn) {
        ModeView view = modeViewStrategies.get(model.getMode());
        view.showBacktrackingPanel(backtrackingOn);
    }
}
