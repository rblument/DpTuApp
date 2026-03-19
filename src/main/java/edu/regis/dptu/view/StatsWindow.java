package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import edu.regis.dptu.util.ResourceMgr;

public class StatsWindow extends JFrame {
    public StatsWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        JLabel placeholder =
            new JLabel(ResourceMgr.instance().string("statsWindow.placeholder"), SwingConstants.CENTER);
        add(placeholder, BorderLayout.CENTER);
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
