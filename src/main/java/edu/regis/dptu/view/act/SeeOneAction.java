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
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.view.DashboardPanel;
import edu.regis.dptu.view.SplashFrame;

public class SeeOneAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(SeeOneAction.class);

    private static final SeeOneAction SINGLETON;

    private final ProblemDAO problemDAO;

    static {
        SINGLETON = new SeeOneAction();
    }

    public static SeeOneAction instance() {
        return SINGLETON;
    }

    private SeeOneAction() {
        super("See One");
        this.problemDAO = new ProblemDAO();

        putValue(SHORT_DESCRIPTION, "Start a teaching session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    /**
     * @author EverettCV
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            DashboardPanel dashboard = SplashFrame.instance().getDashboardPanel();
            ProblemKind kind = dashboard.getSelectedProblemKind();

            Problem problem = problemDAO.retrieveByKind(kind);

            SplashFrame.instance().selectLessonScreen(problem);

        } catch (ObjNotFoundException | NonRecoverableException e) {
            SeeOneAction.julLogger.log(java.util.logging.Level.SEVERE, e.getMessage());
            SplashFrame.instance().showError("Error", "Failed to load problem");
        }
    }
}
