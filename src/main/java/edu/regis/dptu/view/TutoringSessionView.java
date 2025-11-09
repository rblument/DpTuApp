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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.TutoringSession;

/**
 * Displays a tutoring session (the top-level GUI view for the application). Integrates views and
 * ensures the shared Problem model is distributed correctly. Includes Debug Prints.
 *
 * @author rickb (Modified by Assistant for Functional Integration & Debug)
 */
public class TutoringSessionView extends GPanel {
    private static final Logger log = LoggerFactory.getLogger(TutoringSessionView.class);

    private TutoringSession model;
    private ModeView currentModeView;

    private static final Map<Mode, ModeView> modeViewStrategies =
            new HashMap<>() {
                {
                    put(Mode.SEE_ONE, new SeeOneView());
                    put(Mode.DO_ONE, new DoOneView());
                }
            };

    public TutoringSession getModel() {
        return model;
    }

    public void setModel(TutoringSession model) {
        TutoringSessionView.log.info("Setting tutoring session");
        this.model = model;

        if (currentModeView != null) {
            remove((GPanel) currentModeView);
        }

        currentModeView = modeViewStrategies.get(model.getMode());
        if (currentModeView == null) {
            throw new IllegalArgumentException(
                    "TutoringSessionView: No view found for mode: " + model.getMode());
        }

        currentModeView.setModel(model.getProblem());
        add((GPanel) currentModeView);

        revalidate();
        repaint();
    }

    public void showBacktrackingPanel(boolean backtrackingOn) {
        ModeView view = modeViewStrategies.get(model.getMode());
        view.showBacktrackingPanel(backtrackingOn);
    }
}
