package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.util.ResourceMgr;

/*
 * TreeInputView lets the user input tree nodes and displays them
 */
public class TreeInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(TreeInputView.class);

    //TODO: continue implementation
    public TreeInputView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Label + input field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel(ResourceMgr.instance().string("treeInput.label.entry")), gbc);
    }
}
