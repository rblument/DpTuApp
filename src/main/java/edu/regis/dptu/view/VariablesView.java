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

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * So much of this code is specific to LCSProblem, it might be worthwhile to write separate variable
 * views for each problem type. -Gary 10/2025
 */
public class VariablesView extends GPanel implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(VariablesView.class);

    private Problem problem;
    private JLabel nName,
            nValue,
            mName,
            mValue,
            rName,
            rValue,
            cName,
            cValue,
            xrName,
            xrValue,
            ycName,
            ycValue,
            lcsName,
            lcsValue;

    public VariablesView() {
        log.info("Initializing VariablesView components...");
        initializeComponents();
        layoutComponents();
        log.info("VariablesView initialized successfully.");
    }

    public void setModel(Problem model) {
        log.info("Setting model for VariablesView: {}", model);
        this.problem = model;

        if (this.problem != null) {
            this.problem.addProblemListener(this);
            log.debug("Added VariablesView as listener to problem updates.");
        }

        updateView();
    }

    private void initializeComponents() {
        log.debug("Initializing JLabel components...");
        rName = new JLabel("row = ");
        rValue = new JLabel();
        cName = new JLabel("col = ");
        cValue = new JLabel();
        nName = new JLabel("n = ");
        nValue = new JLabel();
        mName = new JLabel("m = ");
        mValue = new JLabel();
        xrName = new JLabel("x[row] = ");
        xrValue = new JLabel();
        ycName = new JLabel("y[col] = ");
        ycValue = new JLabel();
        lcsName = new JLabel("lcs = ");
        lcsValue = new JLabel();
        log.debug("JLabel components initialized.");
    }

    private void layoutComponents() {
        addc(
                rName,
                0,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                rValue,
                1,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                cName,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                cValue,
                1,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                nName,
                0,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                nValue,
                1,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                mName,
                0,
                3,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                mValue,
                1,
                3,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                xrName,
                2,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                xrValue,
                3,
                0,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                ycName,
                2,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                ycValue,
                3,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                lcsName,
                2,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        addc(
                lcsValue,
                3,
                2,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);
        log.debug("Laying out components with GridBagConstraints...");
        log.debug("Component layout complete.");
    }

    private void updateView() {
        if (problem == null) {
            log.warn("updateView called but problem is null.");
            return;
        }

        switch (problem.getType()) {
            case LCS_PROBLEM:
                int r = problem.getVariableValue("r");
                int c = problem.getVariableValue("c");
                int lineNum = problem.getNextLineNumber();
                String x = ((LCSProblem) problem).getX();
                String y = ((LCSProblem) problem).getY();
                String rText;
                String cText;
                String xrText;
                String ycText;

                /*
                -1 is a flag value that says "this variables is not in play right now",
                so we will not display that
                Also, the table starts with -1, but Java arrays start with 0, so we
                must adjust
                */
                rText = (r > -1) ? String.valueOf(problem.getVariableValue("r") - 1) : "";
                cText = (c > -1) ? String.valueOf(problem.getVariableValue("c") - 1) : "";

                // -----------------------EXCEPTIONS----------------------------

                // We want to see the chars, but only when we're on the relevant line

                if (lineNum == 7 || lineNum == 103 || lineNum == 104) {
                    xrText = String.valueOf(x.charAt(r - 1));
                    ycText = String.valueOf(y.charAt(c - 1));
                } else {
                    xrText = "";
                    ycText = "";
                }

                // We want to see the while loop values before they are set
                if (lineNum == 101) {
                    rText = String.valueOf(problem.getVariableValue("n") - 1);
                    cText = String.valueOf(problem.getVariableValue("m") - 1);
                }

                /*
                We want the for loops to display their variable's value before it is set
                */
                if (lineNum == 1) rText = String.valueOf(r);
                if (lineNum == 3) {
                    cText = (c == -1) ? String.valueOf(c + 1) : String.valueOf(c);
                }
                if (lineNum == 5) {
                    rText = (r == -1) ? String.valueOf(r + 1) : String.valueOf(r);
                }
                if (lineNum == 6) {
                    cText = (c == -1) ? String.valueOf(c + 1) : String.valueOf(c);
                }

                rValue.setText(rText);
                cValue.setText(cText);
                nValue.setText(String.valueOf(x.length()));
                mValue.setText(String.valueOf(y.length()));
                xrValue.setText(xrText);
                ycValue.setText(ycText);
                lcsValue.setText(((LCSProblem) problem).getCurrentLcs());

                log.debug(
                        "Variables updated: r={}, c={}, n={}, m={}, x[row]={}, y[col]={}, lcs={}",
                        rText,
                        cText,
                        x.length(),
                        y.length(),
                        xrText,
                        ycText,
                        ((LCSProblem) problem).getCurrentLcs());

                break;
            default:
                // Other problem types coming... soon?
                log.warn("updateView called for unsupported problem type: {}", problem.getType());
        }
    }

    @Override
    public void problemUpdated(Problem model) {
        log.debug("Received problemUpdated event from model: {}", model);
        updateView();
    }
}
