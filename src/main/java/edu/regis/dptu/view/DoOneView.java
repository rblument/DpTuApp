package edu.regis.dptu.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;

public class DoOneView extends GPanel implements ModeView {
    private static final Logger log = LoggerFactory.getLogger(DoOneView.class);

    public DoOneView() {
        DoOneView.log.info("Initializing DoOneView");
        initializeComponents();
        layoutComponents();
    }

    public void setModel(Problem currentProblem) {
        DoOneView.log.info("DoOneView setting model");
    }

    public void updateView(Problem currentProblem) {
        DoOneView.log.info("DoOneView updating view");

        revalidate();
        repaint();
    }

    private void initializeComponents() {
        DoOneView.log.info("DoOneView initializing components");
    }
    ;

    private void layoutComponents() {
        DoOneView.log.info("DoOneView layout components");
    }
    ;

    public void showBacktrackingPanel(boolean backtrackingOn) {
        DoOneView.log.info("DoOneView showBacktrackingPanel");
    }
    ;
}
