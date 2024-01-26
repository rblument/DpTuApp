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
import java.awt.Image;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * Abstract root for all GUI actions in the SpTu application.
 * 
 * Provides support for loading image icons and assigning the GUI controller.
 * 
 * @author rickb
 */
public abstract class DpTuGuiAction extends AbstractAction {
    public DpTuGuiAction(String name) {
        super(name);
    }
    
    /**
     * Load and return the image icon specified by the given image file name,
     * as found in the "img/" directory in the root CLASSPATH.
     * 
     * @param imageFileName name of file with suffix in img/ director
     *                      (e.g., "save16.gif"
     * @param altText
     * @return 
     */
    protected ImageIcon loadIcon(String imageFileName, String altText) {
       // String imgLocation = "/img/" + imageName + "." + suffix;
        
        //URL imageURL = getClass().getResource(imgLocation);
        
        // ToDo: Better error reporting
       // if (imageURL == null)
         //   System.out.println("Unknown resource: " + imgLocation);
         
        Image img = ImgFactory.createImage(imageFileName);
        
        return new ImageIcon(img, altText);
    }
}

