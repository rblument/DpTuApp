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
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
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
