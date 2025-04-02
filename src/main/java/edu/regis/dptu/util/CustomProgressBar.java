/*
 * DPTu: Dynamic Programming Tutor
 * 
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 */
package edu.regis.dptu.util;

import javax.swing.JProgressBar;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class CustomProgressBar extends JProgressBar {
    private static final int ARC_WIDTH = 10;
    private static final int ARC_HEIGHT = 10;
    private Color progressColor = new Color(241, 196, 0);
    
    public CustomProgressBar() {
        setOpaque(false);
        setPreferredSize(new Dimension(100, 200));
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw background
        g2d.setColor(getBackground());
        RoundRectangle2D background = new RoundRectangle2D.Double(
                0, 0, width, height, ARC_WIDTH, ARC_HEIGHT);
        g2d.fill(background);
        
        // Calculate progress dimensions
        int progressHeight;
        if (orientation == VERTICAL) {
            progressHeight = (int) ((height - 4) * ((double) getValue() / getMaximum()));
            
            // Only create gradient if there is actual progress to show
            if (progressHeight > 1) {
                // Create gradient paint for progress - ensure start and end points are different
                Point2D start = new Point2D.Float(0, height - progressHeight);
                Point2D end = new Point2D.Float(0, height);
                
                // Ensure start and end points are different
                if (Math.abs(start.getY() - end.getY()) > 1) {
                    float[] dist = {0.0f, 1.0f};
                    Color[] colors = {progressColor, progressColor.darker()};
                    LinearGradientPaint gradient = new LinearGradientPaint(
                            start, end, dist, colors);
                    
                    // Draw progress
                    g2d.setPaint(gradient);
                    RoundRectangle2D progress = new RoundRectangle2D.Double(
                            2, height - progressHeight, width - 4, progressHeight - 2, 
                            ARC_WIDTH, ARC_HEIGHT);
                    g2d.fill(progress);
                } else {
                    // If gradient points would be too close, just use solid color
                    g2d.setColor(progressColor);
                    RoundRectangle2D progress = new RoundRectangle2D.Double(
                            2, height - progressHeight, width - 4, progressHeight - 2, 
                            ARC_WIDTH, ARC_HEIGHT);
                    g2d.fill(progress);
                }
            }
        } else {
            progressHeight = (int) ((width - 4) * ((double) getValue() / getMaximum()));
            
            // Only create gradient if there is actual progress to show
            if (progressHeight > 1) {
                // Create gradient paint for progress - ensure start and end points are different
                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(progressHeight, 0);
                
                // Ensure start and end points are different
                if (Math.abs(start.getX() - end.getX()) > 1) {
                    float[] dist = {0.0f, 1.0f};
                    Color[] colors = {progressColor, progressColor.darker()};
                    LinearGradientPaint gradient = new LinearGradientPaint(
                            start, end, dist, colors);
                    
                    // Draw progress
                    g2d.setPaint(gradient);
                    RoundRectangle2D progress = new RoundRectangle2D.Double(
                            2, 2, progressHeight - 2, height - 4, 
                            ARC_WIDTH, ARC_HEIGHT);
                    g2d.fill(progress);
                } else {
                    // If gradient points would be too close, just use solid color
                    g2d.setColor(progressColor);
                    RoundRectangle2D progress = new RoundRectangle2D.Double(
                            2, 2, progressHeight - 2, height - 4, 
                            ARC_WIDTH, ARC_HEIGHT);
                    g2d.fill(progress);
                }
            }
        }
        
        g2d.dispose();
        
        // Paint the string if necessary
        if (isStringPainted()) {
            g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(getForeground());
            String progressString = getString();
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int stringWidth = fm.stringWidth(progressString);
            int stringHeight = fm.getAscent();
            int x = (width - stringWidth) / 2;
            int y = (height + stringHeight) / 2;
            g2d.drawString(progressString, x, y);
            g2d.dispose();
        }
    }

    public void setProgressColor(Color color) {
        this.progressColor = color;
        repaint();
    }
    
    public Color getProgressColor() {
        return progressColor;
    }
}