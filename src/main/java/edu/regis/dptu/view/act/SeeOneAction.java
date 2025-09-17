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

import edu.regis.dptu.model.ProblemKind;
import edu.regis.dptu.model.TaskKind;
import edu.regis.dptu.view.MainFrame;
import edu.regis.dptu.view.SplashFrame;
import edu.regis.dptu.view.DashboardPanel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

public class SeeOneAction extends DpTuGuiAction {
    private static final SeeOneAction SINGLETON;
    
    static {
        SINGLETON = new SeeOneAction();
    }

    public static SeeOneAction instance() {
        return SINGLETON;
    }

    private SeeOneAction() {
        super("See One");

        putValue(SHORT_DESCRIPTION, "Start a teaching session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    /**
     * @author EverettCV
     */
    @Override
    public void actionPerformed(ActionEvent evt) {

        // Get the DashboardPanel from the SplashFrame
        DashboardPanel dashboard = SplashFrame.instance().getDashboardPanel();

        if (dashboard == null) {
            System.err.println("DashboardPanel not initialized. Defaulting to LCS_PROBLEM.");
            SplashFrame.instance().selectLessonScreen(ProblemKind.LCS_PROBLEM);
            return;
        }

        // Get the selected TaskKind from the DashboardPanel
        ProblemKind selectedKind = dashboard.getSelectedProblemKind();
        System.out.println("SeeOneAction selected TaskKind: " + selectedKind);

        // Call SplashFrame to transition to the lesson screen with this TaskKind
        SplashFrame.instance().selectLessonScreen(selectedKind);
    }
}
