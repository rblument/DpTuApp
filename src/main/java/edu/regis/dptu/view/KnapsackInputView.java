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

import edu.regis.dptu.util.ResourceMgr;

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
        log.debug("Initializing KnapsackInputView (template / not yet implemented)");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // TODO: Add input field for knapsack items
        JTextField itemField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.entry")), gbc);
        gbc.gridx = 1;
        add(itemField, gbc);

        // TODO: Add 'Add' button and create/append to list
        JButton addButton = new JButton(ResourceMgr.instance().string("knapsackInput.button.addItem"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(addButton, gbc);
    }
}
