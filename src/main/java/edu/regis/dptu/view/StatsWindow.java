package edu.regis.dptu.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class StatsWindow extends JFrame {
    public StatsWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        JLabel placeholder = new JLabel("Stats will appear here", SwingConstants.CENTER);
        add(placeholder, BorderLayout.CENTER);
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

