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
import edu.regis.dptu.model.Mode;
import edu.regis.dptu.util.ResourceMgr;


public class TeachOneAction extends DpTuGuiAction {
    private static final Logger log = LoggerFactory.getLogger(TeachOneAction.class);

    private static final TeachOneAction SINGLETON;

    static {
        SINGLETON = new TeachOneAction();
    }

    public static TeachOneAction instance() {
        return SINGLETON;
    }

    private TeachOneAction() {
        super(Mode.TEACH_ONE.title());

        putValue(SHORT_DESCRIPTION, ResourceMgr.instance().string("mode.teachOne.tooltip"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_T);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        MainFrame frame = MainFrame.instance();
        frame.setVisible(true);
        // TODO: Add tutor notification in future sprint
    }
}
