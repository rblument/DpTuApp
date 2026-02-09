package edu.regis.dptu.view;

import edu.regis.dptu.model.Problem;

@SuppressWarnings("Logging")

public interface ModeView {
    public void setModel(Problem model);

    public void showBacktrackingPanel(boolean backtrackingOn);
}
