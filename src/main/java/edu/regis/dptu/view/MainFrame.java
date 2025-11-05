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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.view.act.ActionFactory;

/**
 * The primary GUI window in the DpTu application.
 *
 * <p>Most of the display functionality is handled by child component views.
 *
 * @author rickb
 */
public class MainFrame extends JFrame implements WindowListener {
    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);

    /** The singleton instance of this frame. */
    private static final MainFrame SINGLETON;

    // Invoked when this class is loaded
    static {
        ActionFactory.createActions();

        SINGLETON = new MainFrame();
    }

    /**
     * Return the singleton instance of this frame.
     *
     * @return the MainFrame singleton
     */
    public static MainFrame instance() {
        return SINGLETON;
    }

    /** The size of this frame will the size of the user's screen minus this screen size inset. */
    private static final int SCREEN_SIZE_INSET = 50;

    /** The DP tutoring session displayed in this frame. */
    private TutoringSession model;

    /** The primary view displayed in this frame. */
    private TutoringSessionView view;

    /** Initialize and layout the child components displayed in this frame. */
    private MainFrame() {
        super("DpTu");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.width = screenSize.width - SCREEN_SIZE_INSET;
        screenSize.height = screenSize.height - SCREEN_SIZE_INSET - 10;
        setSize(screenSize);
        setLocation(10, 10);

        setJMenuBar(new DpTuMenuBar());

        initializeComponents();
        layoutComponents();

        addWindowListener(this);

        setVisible(false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // see windowClosing()
    }

    /**
     * Update the current model with changes made in this frame and return it.
     *
     * @return the Session model
     */
    public TutoringSession getModel() {
        // Update the model with any changes from the view
        updateModel();

        return model;
    }

    /**
     * Display the given model in this frame.
     *
     * @param model a Session model
     */
    public void setModel(TutoringSession model) {
        this.model = model;
        view.setModel(model);
        updateView();
    }

    /**
     * Get the tutoring session view.
     *
     * @return The TutoringSessionView
     */
    public TutoringSessionView getView() {
        return view;
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        // Save the session before closing
        updateModel();
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    /** Create the child components used in this frame. */
    private void initializeComponents() {
        view = new TutoringSessionView();
    }

    /** Layout the child components used in this frame. */
    private void layoutComponents() {
        setContentPane(view);
    }

    /** Update the current model with any changes made in this frame's view. */
    private void updateModel() {
        // Nothing to do here for now, as the model is updated through actions
    }

    /** Display the current model in this frame's view. */
    private void updateView() {
        // The view will update itself based on the model
    }
}
