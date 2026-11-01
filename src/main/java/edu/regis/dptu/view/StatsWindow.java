package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.svc.ServiceFactory;
import edu.regis.dptu.util.ResourceMgr;

public class StatsWindow extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(StatsWindow.class);

    public StatsWindow(String title, String userId) {
        super(title);
        setLayout(new BorderLayout());
        add(createStatsLabel(userId), BorderLayout.CENTER);
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createStatsLabel(String userId) {
        try {
            int count = ServiceFactory.findCompletedTaskSvc().countCompleted(userId);
            return new JLabel(
                    ResourceMgr.instance().string("statsWindow.tasksCompleted", count),
                    SwingConstants.CENTER);
        } catch (NonRecoverableException e) {
            log.error("Failed to retrieve completed-task count for userId={}", userId, e);
            return new JLabel(
                    ResourceMgr.instance().string("statsWindow.placeholder"),
                    SwingConstants.CENTER);
        }
    }
}
