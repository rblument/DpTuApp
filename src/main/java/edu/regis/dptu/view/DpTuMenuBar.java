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

import edu.regis.dptu.view.act.SaveSessionAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Menu bar used in the MainFrame.
 * 
 * @author rickb
 */
public class DpTuMenuBar extends JMenuBar {
    public DpTuMenuBar() {
        createFileMenu();
    }
 
    /**
     * Create the File menu appearing in the menubar
     */
    private void createFileMenu() {
        JMenu menu = new JMenu("File");
        
        JMenuItem item = new JMenuItem(SaveSessionAction.instance());

        menu.add(item);
        
        menu.addSeparator();
        
        add(menu);
    }
}

