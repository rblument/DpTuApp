package edu.regis.dptu.view;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Problem;
import edu.regis.dptu.util.ResourceMgr;

public class SeeOneView extends GPanel implements ModeView {
    private static final Logger log = LoggerFactory.getLogger(SeeOneView.class);

    private VariablesView variablesView;
    private JLabel subproblemView;
    private CodeView codeView;
    private BacktrackingCodeView backtrackingCodeView;

    private ProblemInputView problemInputView;
    private SubSequenceView subSeqView;

    private SubproblemTableView tableView;
    private StepViewPanel stepViewPanel;

    private Runnable onCompletedTaskSend;

    public SeeOneView() {
        SeeOneView.log.info("Initializing SeeOneView");
        initializeComponents();
        layoutComponents();
    }

    public void setModel(Problem currentProblem) {
        subSeqView.setModel(currentProblem);
        stepViewPanel.setModel(currentProblem);
        codeView.setModel(currentProblem);
        backtrackingCodeView.setModel(currentProblem);
        variablesView.setModel(currentProblem);
        tableView.setModel(currentProblem);

        updateView(currentProblem);
    }

    public void updateView(Problem currentProblem) {
        SeeOneView.log.info("SeeOneView updating view");

        revalidate();
        repaint();
    }

    /**
     * Injects the action that should occur when the current problem/task is completed.
     *
     * <p>NOTE: Temporary design. Eventually completion reporting should be handled by a
     * controller/service layer rather than a view.
     *
     * @param r callback executed once when the problem finishes
     */
    public void setOnCompletedTaskSend(Runnable r) {
        this.onCompletedTaskSend = r;
        codeView.setOnTaskCompleted(r);
    }

    private void initializeComponents() {
        SeeOneView.log.info("SeeOneView initializing components");
        variablesView = new VariablesView();
        subproblemView = new JLabel(ResourceMgr.instance().string("seeOne.subproblemView.title"));

        problemInputView =
                new ProblemInputView(
                        problem -> {
                            tableView.setModel(problem);
                            subSeqView.setModel(problem);
                            stepViewPanel.setModel(problem);
                            codeView.setModel(problem);
                            backtrackingCodeView.setModel(problem);
                            variablesView.setModel(problem);
                        });

        subSeqView = new SubSequenceView(); // Original init
        tableView = new SubproblemTableView();
        codeView = new CodeView(); // Original init
        backtrackingCodeView = new BacktrackingCodeView();
        stepViewPanel = new StepViewPanel();
    }
    ;

    public void layoutComponents() {
        addc(
                problemInputView,
                0,
                0,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        addc(
                codeView,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        addc(
                backtrackingCodeView,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        // ToDo: Is tableView and subproblemView trying to do the same thing?
        addc(
                tableView,
                1,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5); // Original
        addc(
                subproblemView,
                2,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.VERTICAL,
                5,
                5,
                5,
                5); // Original
        addc(
                subSeqView,
                3,
                1,
                1,
                1,
                0.5,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                5,
                5,
                5,
                5); // Original
        // addc(xView, 3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
        // GridBagConstraints.VERTICAL, 5, 5, 5, 5);       // Original
        addc(
                variablesView,
                0,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        addc(
                stepViewPanel,
                1,
                2,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5); // Original
    }
    ;

    public void showBacktrackingPanel(boolean backtrackingOn) {
        if (backtrackingOn) {
            codeView.setVisible(false);
            backtrackingCodeView.setVisible(true);
        } else {
            backtrackingCodeView.setVisible(false);
            codeView.setVisible(true);
        }
    }
    ;
}
