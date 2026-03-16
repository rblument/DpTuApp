/**
 * StatsWindow is a simple popup window used to display statistics related to tutoring progress in
 * the DashboardPanel.
 *
 * <p>This window is opened when the user presses a "View Stats" button for one of the tutoring
 * stages (See One, Do One, Teach One). Currently it displays a placeholder message, but it is
 * intended to later contain charts or statistics about task completion.
 *
 * <p>The window is created once and reused if it already exists.
 *
 * @author Lindsey Cox
 */
package edu.regis.dptu.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Logging")
public class StatsWindow extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(StatsWindow.class);

    /**
     * Creates a new StatsWindow with the given title.
     *
     * <p>The window displays a placeholder label where statistics will eventually be rendered.
     *
     * @param title the title displayed in the window frame
     */
    public StatsWindow(String title) {
        super(title);
        setLayout(new BorderLayout());
        JLabel placeholder = new JLabel("Stats will appear here", SwingConstants.CENTER);
        add(placeholder, BorderLayout.CENTER);
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        log.debug("StatsWindow created with title '{}'", title);
    }
}
