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

public class QuizMeAction extends DpTuGuiAction {
    private static final QuizMeAction SINGLETON;
    
    static {
        SINGLETON = new QuizMeAction();
    }

    public static QuizMeAction instance() {
        return SINGLETON;
    }

    private QuizMeAction() {
        super("Quiz Me");
        
        putValue(SHORT_DESCRIPTION, "Start a quiz session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MainFrame frame = MainFrame.instance();
        frame.setVisible(true);
        // TODO: Add tutor notification in future sprint
    }
}
