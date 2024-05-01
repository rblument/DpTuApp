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

import java.awt.*;
import javax.swing.*;

/**
 * This sets the background of lines to blue if selected
 * 
 * @author Cameron Christner
 */
class CodeViewCellRenderer extends DefaultListCellRenderer {

    public CodeViewCellRenderer() {

        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList paramlist, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(paramlist, value, index, isSelected, cellHasFocus);

        if (isSelected) {

            c.setBackground(Color.BLUE);
        } else {
            c.setBackground(null);
        }

        return this;
    }
}
