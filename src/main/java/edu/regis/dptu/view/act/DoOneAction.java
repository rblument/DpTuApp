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

import edu.regis.dptu.view.MainFrame;

public class DoOneAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(DoOneAction.class);

    private static final DoOneAction SINGLETON;

    static {
        SINGLETON = new DoOneAction();
    }

    public static DoOneAction instance() {
        return SINGLETON;
    }

    private DoOneAction() {
        super("Do One");

        putValue(SHORT_DESCRIPTION, "Start a \"do one\" (practice) session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MainFrame frame = MainFrame.instance();
        frame.setVisible(true);
        // TODO: Add tutor notification in future sprint
    }
}
