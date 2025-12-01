package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// DPTU-104: MatrixInputView input field already implemented in 103

/*
 * Currently, MatrixInputView is thought to be designed as a single Matrix Input Field with an Add button to append
 * a list with the Matrix constraints, and then that can be passed to a display variable in another View to be visualized and used. NOT IMPLEMENTED OR TESTED, JUST TEMPLATED.
 *
 * Relevant files and helpful templates to follow:
 * ProblemInputView.java
 * LCSInputView.java
 */
public class MatrixInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(MatrixInputView.class);

    public MatrixInputView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // TODO: Add input field for matrix definition
        JTextField matrixField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Matrix Entry:"), gbc);
        gbc.gridx = 1;
        add(matrixField, gbc);

        // TODO: Add 'Add' button and create/append to list
        JButton addButton = new JButton("Add Matrix");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(addButton, gbc);
    }
}
