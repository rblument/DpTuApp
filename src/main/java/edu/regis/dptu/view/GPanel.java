/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *   Adapted from Java in a Nutshell (1st Ed.).
 *
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * A utility JPanel with a GridBagLayout and a convenience addc method.
 *
 * <p>Adopted from Java in a Nutshell (1st Ed.).
 *
 * @author rickb
 */
@SuppressWarnings("Logging")
public class GPanel extends JPanel {
    /** Initialize this panel with a GridBagLayout. */
    public GPanel() {
        setLayout(new GridBagLayout());
    }

    /**
     * Add components to this panel using the given constraints. For example, <br>
     * addc(this, JComponent, 0,0, 1,1, 1.0,1.0,<br>
     * &nbsp;&nbsp; GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, &nbsp;&nbsp; 5,5,5,5);
     *
     * @param c the child component
     * @param gridX the column in which the child component is anchored
     * @param gridY the row in which the child component is anchored
     * @param gridWidth the columns spanned by the child component in the bag
     * @param gridHeight the rows spanned by the child component in the bag
     * @param weightx the amount of width scaling during panel expansion
     * @param weighty the amount of height scaling during panel expansion
     * @param anchor a GridBagConstraints anchor, e.g. NORTHWEST
     * @param fill a GridBagConstraints fill, e.g. HORIZONTAL
     * @param top padding added to the inset for the child
     * @param left padding added to the inset for the child
     * @param bottom padding added to the inset for the child
     * @param right padding added to the inset for the child
     */
    public void addc(
            Component c,
            int gridX, // column
            int gridY, // row
            int gridWidth,
            int gridHeight,
            double weightx,
            double weighty,
            int anchor,
            int fill,
            int top,
            int left,
            int bottom,
            int right) {

        Insets insets = new Insets(top, left, bottom, right);

        GridBagConstraints cnstr =
                new GridBagConstraints(
                        gridX,
                        gridY,
                        gridWidth,
                        gridHeight,
                        weightx,
                        weighty,
                        anchor,
                        fill,
                        insets,
                        0,
                        0);

        GridBagLayout l = (GridBagLayout) getLayout();
        l.setConstraints(c, cnstr);

        add(c);
    }
}
