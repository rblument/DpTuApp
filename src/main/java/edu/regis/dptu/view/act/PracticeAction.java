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

import edu.regis.dptu.view.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

public class PracticeAction extends DpTuGuiAction {
    private static final PracticeAction SINGLETON;
    
    static {
        SINGLETON = new PracticeAction();
    }

    public static PracticeAction instance() {
        return SINGLETON;
    }

    private PracticeAction() {
        super("Practice");
        
        putValue(SHORT_DESCRIPTION, "Start a practice session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_P);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MainFrame frame = MainFrame.instance();
        frame.setVisible(true);
        // TODO: Add tutor notification in future sprint
    }
}
