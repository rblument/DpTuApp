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

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * @author danielaflores
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
            iName,
            iValue,
            jName,
            jValue,
            xrName,
            xrValue,
            ycName,
            ycValue,
            lcsName,
            lcsValue;

    public VariablesView() {
        initializeComponents();
        layoutComponents();
    }

    public void setModel(Problem model) {
        this.problem = model;

        if (this.problem != null) {
            this.problem.addProblemListener(this);
        }

        updateView();
    }

    private void initializeComponents() {
        // These will need to vary by problem type
        rName = new JLabel("row = ");
        rValue = new JLabel();
        cName = new JLabel("col = ");
        cValue = new JLabel();
        jName = new JLabel("j = ");
        jValue = new JLabel();
        iName = new JLabel("i = ");
        iValue = new JLabel();
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
                iName,
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
                iValue,
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
                jName,
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
                jValue,
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
                nName,
                0,
                4,
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
                4,
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
                5,
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
                5,
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
    }

    private void updateView() {
        if (problem == null) {
            return;
        }

        switch (problem.getType()) {
            case LCS_PROBLEM:
                int r = problem.getVariableValue("r");
                int c = problem.getVariableValue("c");
                int i = problem.getVariableValue("i");
                int j = problem.getVariableValue("j");
                int lineNum = problem.getNextLineNumber();
                String x = ((LCSProblem) problem).getX();
                String y = ((LCSProblem) problem).getY();

                /*
                -1 is a flag value that says "this variables is not in play right now",
                so we will not display that
                Also, the table starts with -1, but Java arrays start with 0, so we
                must adjust
                */
                String rText = (r > -1) ? String.valueOf(problem.getVariableValue("r") - 1) : "";
                String cText = (c > -1) ? String.valueOf(problem.getVariableValue("c") - 1) : "";
                String iText = (i > -1) ? String.valueOf(problem.getVariableValue("i") - 1) : "";
                String jText = (j > -1) ? String.valueOf(problem.getVariableValue("j") - 1) : "";
                String xrText;
                String ycText;

                // -----------------------EXCEPTIONS-----------------------------

                // We want to see the chars when we're on the relevant line
                if (lineNum == 7) {
                    xrText = String.valueOf(x.charAt(i - 1));
                    ycText = String.valueOf(y.charAt(j - 1));
                } else if (lineNum == 103 || lineNum == 104) {
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
                    iText = (i == -1) ? String.valueOf(i + 1) : String.valueOf(i);
                }
                if (lineNum == 6) {
                    jText = (j == -1) ? String.valueOf(j + 1) : String.valueOf(j);
                }

                rValue.setText(rText);
                cValue.setText(cText);
                iValue.setText(iText);
                jValue.setText(jText);
                nValue.setText(String.valueOf(x.length()));
                mValue.setText(String.valueOf(y.length()));
                xrValue.setText(xrText);
                ycValue.setText(ycText);
                lcsValue.setText(((LCSProblem) problem).getCurrentLcs());
        }
    }

    @Override
    public void problemUpdated(Problem model) {
        updateView();
    }
}
