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
package edu.regis.dptu.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.util.CustomProgressBar;

@SuppressWarnings("Logging")
public class CustomProgressBarTest {
    @Test
    public void paintVerticalAndHorizontalAndStringPainted() {
        CustomProgressBar bar = new CustomProgressBar();
        bar.setSize(120, 200);
        bar.setMaximum(100);
        bar.setValue(60);

        BufferedImage img = new BufferedImage(140, 220, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        bar.setOrientation(CustomProgressBar.VERTICAL);
        bar.setStringPainted(false);
        bar.paint(g2);

        bar.setOrientation(CustomProgressBar.HORIZONTAL);
        bar.setStringPainted(true);
        bar.setString("60%");
        bar.paint(g2);

        g2.dispose();
    }

    @Test
    public void paintHandlesSmallProgressAndColorSetter() {
        CustomProgressBar bar = new CustomProgressBar();
        bar.setSize(100, 40);
        bar.setMaximum(100);
        bar.setValue(1);

        Color color = new Color(10, 20, 30);
        bar.setProgressColor(color);
        assertEquals(color, bar.getProgressColor());

        BufferedImage img = new BufferedImage(120, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        bar.setOrientation(CustomProgressBar.HORIZONTAL);
        bar.paint(g2);
        g2.dispose();
    }
}
