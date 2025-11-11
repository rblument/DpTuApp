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

import edu.regis.dptu.dao.ProblemDAO;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.view.DashboardPanel;
import edu.regis.dptu.view.SplashFrame;

public class DoOneAction extends DpTuGuiAction {
    private static final DoOneAction SINGLETON;

    private final ProblemDAO problemDAO;

    static {
        SINGLETON = new DoOneAction();
    }

    public static DoOneAction instance() {
        return SINGLETON;
    }

    private DoOneAction() {
        super(Mode.DO_ONE.title());
        this.problemDAO = new ProblemDAO();

        putValue(SHORT_DESCRIPTION, "Start a \"do one\" (practice) session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            DashboardPanel dashboard = SplashFrame.instance().getDashboardPanel();
            ProblemKind kind = dashboard.getSelectedProblemKind();
            Problem problem = problemDAO.retrieveByKind(kind);

            SplashFrame.instance().selectLessonScreen(problem, Mode.DO_ONE);
        } catch (ObjNotFoundException | NonRecoverableException e) {
            DoOneAction.log.error(e.getMessage());
            SplashFrame.instance().showError("Error", "Failed to load problem");
        }
    }
}
