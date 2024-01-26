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

import edu.regis.dptu.util.ImgFactory;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.KeyStroke;

/**
 * Handler for GUI gestures requesting to save the current session.
 * 
 * @author rickb
 */
public class SaveSessionAction extends DpTuGuiAction {
    
    /**
     * Create the singleton for this action, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced by executing SaveSession.instance()
     */
    static {
        SINGLETON = new SaveSessionAction();
    }
    /**
     * The singleton for this action.
     */
    public static SaveSessionAction SINGLETON;
    
    /**
     * Return the singleton for this action.
     * 
     * @return the SaveSessionAction singleton
     */
    public static SaveSessionAction instance() {
        return SINGLETON;
    }
    
    /**
     * Initialize this action.
     */
    private SaveSessionAction() {
        super("Save");
        
        putValue(SMALL_ICON, ImgFactory.createIcon("Save16.gif", "Save Tutoring Session"));
        putValue(SHORT_DESCRIPTION, "Save the current tutoring session");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
    }
    
    /**
     * Save the current session when this action is invoked.
     * 
     * @param evt 
     */
    public void actionPerformed(ActionEvent evt) {
        // ToDo: what happens on a save
        System.out.println("Save not implemented");
       // GuiController.instance().getStepView().selectPanel("RotateView");
    }
}

