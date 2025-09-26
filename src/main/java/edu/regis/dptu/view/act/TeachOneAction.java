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

import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;

import edu.regis.dptu.view.MainFrame;

public class TeachOneAction extends DpTuGuiAction {
    private static final TeachOneAction SINGLETON;
    
    static {
        SINGLETON = new TeachOneAction();
    }

    public static TeachOneAction instance() {
        return SINGLETON;
    }

    private TeachOneAction() {
        super("Teach One");
        
        putValue(SHORT_DESCRIPTION, "Start a \"teach one\" (quiz) session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_T);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MainFrame frame = MainFrame.instance();
        frame.setVisible(true);
        // TODO: Add tutor notification in future sprint
    }
}
