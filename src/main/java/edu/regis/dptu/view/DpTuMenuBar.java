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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.act.SaveSessionAction;

/**
 * Menu bar used in the MainFrame. Added logging to track menu initialization.
 *
 * @author rickb
 */
public class DpTuMenuBar extends JMenuBar {
    private static final Logger log = LoggerFactory.getLogger(DpTuMenuBar.class);

    /**
     * Constructor for the DpTuMenuBar class. Initializes the menu bar by creating the File menu.
     */
    public DpTuMenuBar() {
        log.info("Initializing DpTuMenuBar...");
        createFileMenu();
        log.info("DpTuMenuBar initialized successfully.");
    }

    /** Create the File menu appearing in the menubar */
    private void createFileMenu() {
        log.debug("Creating 'File' menu...");

        JMenu menu = new JMenu(ResourceMgr.instance().string("menu.file"));
        log.debug("'File' menu created.");

        JMenuItem item = new JMenuItem(SaveSessionAction.instance());
        log.debug("'Save Session' menu item created and action attached.");

        menu.add(item);
        log.debug("'Save Session' menu item added to 'File' menu.");

        menu.addSeparator();
        log.debug("Separator added to 'File' menu.");

        add(menu);
        log.debug("'File' menu added to menu bar.");
    }
}
