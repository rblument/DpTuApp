package edu.regis.dptu.view;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Currently, KnapsackInputView is thought to be designed as a Single Input field with an Add button
 * to then append it to a list with all inputs, that can then be passed to a display variable in another View,
 * which can be used and visualized there. NOT IMPLEMENTED OR TESTED, ONLY TEMPLATED.
 *
 * Relevant files and helpful templates to follow:
 * ProblemInputView.java
 * LCSInputView.java
 */
public class KnapsackInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(KnapsackInputView.class);

    public KnapsackInputView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // TODO: Add input field for knapsack items
        JTextField itemField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Knapsack Entry:"), gbc);
        gbc.gridx = 1;
        add(itemField, gbc);

        // TODO: Add 'Add' button and create/append to list
        JButton addButton = new JButton("Add Item");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(addButton, gbc);
    }
}
