package edu.regis.dptu.view;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.regis.dptu.model.LCSProblem;

/**
 * LCSInputView provides two input fields and a submit button for entering strings in the LCS
 * tutoring problem.
 *
 * <p>Changes (April 17, 2025): - Connected input fields to SubSequenceView and SubproblemTableView
 * to update dynamically. - Submit button functionality added to trigger view updates.
 *
 * @author EverettCV
 */
public class LCSInputView extends JPanel {
    private JTextField inputField1;
    private JTextField inputField2;
    private JButton submitButton;

    // Variables to store submitted input
    private String string1;
    private String string2;

    private boolean inputListenersAttached = false;

    private TutoringSessionView grandparentView;

    public LCSInputView() {

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
     * <p>- Captures input strings - Updates SubSequenceView (updates strings and lengths displayed)
     * - Updates SubproblemTableView (rebuilds the matrix dynamically)
     *
     * <p>Changes (April 17, 2025): - Added dynamic updating of SubSequenceView and
     * SubproblemTableView based on user inputs.
     *
     * @author EverettCV
     */
    public void submitInputs() {
        string1 = inputField1.getText();
        string2 = inputField2.getText();

        Container tempView = this.getParent().getParent();
        // For now, use getParent().getParent() to find TutoringSessionView instance
        // This code is fragile, if you've changed the component heirarchy you
        // Will likely have to edit this as well
        try {
            if (tempView instanceof TutoringSessionView) {
                grandparentView = (TutoringSessionView) tempView;
                LCSProblem newProblem = new LCSProblem(string1, string2);
                grandparentView.getTableView().setModel(newProblem);
                grandparentView.getSubSeqView().setModel(newProblem);
                grandparentView.getStepViewPanel().setModel(newProblem);
                grandparentView.getCodeView().setModel(newProblem);
                grandparentView.getVariablesView().setModel(newProblem);
            } else {
                System.out.println(
                        "LCSInputView: getParent().getParent() did not lead to "
                                + "TutoringSessionView");
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        // TODO: Add input validation (e.g., prevent empty submissions).
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

    public String getString2() {
        return string2;
    }
}
