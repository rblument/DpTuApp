package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.ProblemListener;

/**
 * LCSInputView provides two input fields and a submit button for entering strings in the LCS
 * tutoring problem. Updates all views accordingly.
 *
 * @author EverettCV
 */
public class LCSInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(LCSInputView.class);

    private ProblemListener submitListener;

    private JTextField inputField1;
    private JTextField inputField2;
    private JButton submitButton;

    // Variables to store submitted input
    private String string1;
    private String string2;

    private boolean inputListenersAttached = false;

    public LCSInputView(ProblemListener listener) {
        submitListener = listener;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 8, 8);
        gbc.anchor = GridBagConstraints.NORTH;

        // First input
        inputField1 = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("String 1:"), gbc);
        gbc.gridx = 1;
        add(inputField1, gbc);

        // Second input
        inputField2 = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("String 2:"), gbc);
        gbc.gridx = 1;
        add(inputField2, gbc);

        // Submit button positioned at bottom right
        submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(submitButton, gbc);

        // Listener to process inputs when Submit is clicked
        submitButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        submitInputs();
                    }
                });

        // Listen for change in input boxes
        DocumentListener inputBoxChange =
                new DocumentListener() {
                    private void onChange() {
                        submitButton.setEnabled(true);
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        onChange();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        onChange();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {}
                };

        if (!inputListenersAttached) {
            inputListenersAttached = true;
            inputField1.getDocument().addDocumentListener(inputBoxChange);
            inputField2.getDocument().addDocumentListener(inputBoxChange);
        }
    }

    /**
     * Handles user input when Submit button is clicked.
     *
     * <p>Captures input strings - Updates all Views
     */
    public void submitInputs() {
        string1 = inputField1.getText();
        string2 = inputField2.getText();
        // Remove whitespace
        string1 = string1.replaceAll("\\s", "");
        string2 = string2.replaceAll("\\s", "");

        if (string1.length() > 0 && string2.length() > 0) {
            LCSProblem newProblem = new LCSProblem(string1, string2);
            submitListener.problemUpdated(newProblem);
        } else {
            String msg = "Strings must contain at least one letter, number, or symbol.";
            JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Set the strings that should be shown when LCSInputView is created
     *
     * @param string1 First String
     * @param string2 Second String
     */
    public void setDefaultStrings(String string1, String string2) {
        inputField1.setText(string1);
        inputField2.setText(string2);
        System.out.println("DEBUG: STRING1 is " + string1);
        System.out.println("DEBUG: LCSObject is " + this);
        submitButton.setEnabled(false);
    }

    // Getters for future use if needed
    public String getString1() {
        return string1;
    }
}
