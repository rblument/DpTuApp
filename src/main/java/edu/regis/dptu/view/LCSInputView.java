package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

    /** The logger for the class */
    private static final java.util.logging.Logger julLogger =
            java.util.logging.Logger.getLogger(LCSInputView.class.getName());

    private ProblemListener submitListener;

    private JTextField inputField1;
    private JTextField inputField2;
    private JButton submitButton;

    // Variables to store submitted input
    private String string1;
    private String string2;

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

        LCSInputView.julLogger.log(Level.INFO, "Submitted String 1: " + string1);
        LCSInputView.julLogger.log(Level.INFO, "Submitted String 2: " + string2);

        LCSProblem newProblem = new LCSProblem(string1, string2);
        submitListener.problemUpdated(newProblem);
    }
}
