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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.security.CommonPasswords;
import edu.regis.dptu.util.ReusableFonts;
import edu.regis.dptu.view.act.BackAction;
import edu.regis.dptu.view.act.CreateAcctAction;
import edu.regis.dptu.view.act.SignInAction;

/**
 * New user screen that also allows the student to create an IRBt account
 *
 * @author rickb
 */
public class NewAccountPanel extends GPanel {
    /** Events of interest occurring in this class are logged to this logger. */
    private static final Logger log = LoggerFactory.getLogger(NewAccountPanel.class);

    /** Events of interest occurring in this class are logged to this logger. */

    /** A regex pattern used to validate user email ids (e.g. "rick@regis.edu"). */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Color LIGHT_BLUE = new Color(223, 242, 245);

    /** The account being created and displayed in this panel. */
    private Account model;

    /** The editable fields appearing in this dialog. */
    protected HintTextField fName;

    protected HintTextField lName;
    protected HintTextField userId;
    protected JPasswordField pass1;
    protected JPasswordField pass2;
    protected JComboBox secQuestions;
    protected JPasswordField secAnswer;

    protected JLabel strength;
    protected JLabel msg;

    protected JButton signInBut;
    protected JButton createAcctBut;
    protected JButton backBut;

    /** Constructor for the NewAccountPanel class */
    public NewAccountPanel() {
        super();

        model = new Account();

        initComponents();
        layoutPanel();

        enableButtons(fName.getDocument());
    }

    /**
     * Update and return the model with view's information.
     *
     * @return the updated {@link Account} model.
     */
    public Account getModel() {
        updateModel();

        return model;
    }

    /**
     * Display the given model in the view, but the MD5 encrypted password is not displayed.
     *
     * @param model
     */
    public void setModel(Account model) {
        this.model = model;

        updateDisplay();
    }

    /**
     * Returns the JTextField component associated with the user ID.
     *
     * @return the user ID JTextField component
     */
    public JTextField getUserIdComp() {
        return userId;
    }

    /**
     * Requests focus for the first name input field in the window. This method sets the focus to
     * the 'fName' text field.
     */
    public void updateFocus() {
        fName.requestFocusInWindow();
    }

    /** Set all of the text fields in this view to the empty string. */
    public void clearFields() {
        fName.setText("");
        lName.setText("");
        userId.setText("");
        pass1.setText("");
        pass2.setText("");
        secQuestions.setSelectedIndex(0);
        secAnswer.setText("");
    }

    /** Update our model with the current values displayed in this view */
    private void updateModel() {
        model.setUserId(userId.getText());
        model.setFirstName(fName.getText());
        model.setLastName(lName.getText());
        model.setPassword(encryptSHA256(new String(pass1.getPassword())));
        model.setSecurityQuestion(secQuestions.getSelectedIndex());
        model.setSecurityAnswer(encryptSHA256(new String(secAnswer.getPassword())));
    }

    /** Update this view with the current values in our model (except the passwords). */
    private void updateDisplay() {
        userId.setText(model.getUserId());
        fName.setText(model.getFirstName());
        lName.setText(model.getLastName());
        pass1.setText("");
        pass2.setText("");
        // ToDo: This should use the model.
        secQuestions.setSelectedIndex(0);
        secAnswer.setText("");
    }

    private void initComponents() {
        LoginDocumentListener docListener = new LoginDocumentListener();

        // fName = new JTextField("First", 15);
        fName = new HintTextField("First", 15);
        // fName.setForeground(new Color(230,230,230));
        fName.getDocument().addDocumentListener(docListener);
        // ((AbstractDocument) fName.getDocument()).setDocumentFilter(new NameFilter());

        lName = new HintTextField("Last", 30);
        lName.getDocument().addDocumentListener(docListener);

        userId = new HintTextField("userId@university.edu", 10);
        userId.setIsEmailAddr(true);
        userId.getDocument().addDocumentListener(docListener);

        pass1 = new JPasswordField(20);
        pass1.getDocument().addDocumentListener(docListener);

        pass2 = new JPasswordField(20);
        pass2.getDocument().addDocumentListener(docListener);

        String s1[] = {"What city were you born in?", "What is your mother's maiden name?"};
        secQuestions = new JComboBox<String>(s1);

        secAnswer = new JPasswordField(20);
        secAnswer.getDocument().addDocumentListener(docListener);

        // SignInAction act = SignInAction.instance();
        signInBut = new JButton(SignInAction.instance());
        signInBut.setEnabled(true);

        createAcctBut = new JButton(CreateAcctAction.instance());
        createAcctBut.setEnabled(false);
        MainFrame.instance().getRootPane().setDefaultButton(createAcctBut);

        backBut = new JButton(BackAction.instance());
        backBut.setEnabled(true);

        strength = new JLabel("(Strength: very poor)");
        strength.setForeground(Color.RED);
        strength.setFont(ReusableFonts.instance().getFont("10ptLabel"));
    }

    /**
     * Layout the panel by setting the background, preferred size, adding necessary components and
     * necessary layout constraints
     */
    private void layoutPanel() {
        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(300, 400));

        addc(
                createHeader(),
                0,
                0,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        addc(
                createOverview(),
                0,
                1,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                5,
                5,
                5,
                5);

        addc(
                createLogin(),
                1,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                10,
                5,
                5,
                5);

        JLabel copyright =
                new JLabel("(C) 2019-2025 Johanna and Richard Blumenthal. All Rights Reserved");
        copyright.setFont(ReusableFonts.instance().getFont("Copyright"));
        addc(
                copyright,
                0,
                2,
                2,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTH,
                GridBagConstraints.CENTER,
                5,
                5,
                5,
                5);

        setSize(490, 400);
    }

    /**
     * Creates and returns a header panel containing the title and header components.
     *
     * @return a GPanel object representing the header section.
     */
    private GPanel createHeader() {
        GPanel panel = new GPanel();
        panel.setBackground(LIGHT_BLUE);

        JLabel ccis = new JLabel("Regis University Department of Computer and Cyber Sciences");
        ccis.setFont(ReusableFonts.instance().getFont("CCIS"));
        ccis.setForeground(Color.BLUE);

        panel.addc(
                ccis,
                0,
                0,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);

        return panel;
    }

    /**
     * Creates and returns an overview panel that provides introductory information and
     * instructions.
     *
     * @return a GPanel object representing the overview section.
     */
    private GPanel createOverview() {
        GPanel panel = new GPanel();
        panel.setBackground(Color.WHITE);

        panel.setSize(300, 400);
        panel.setPreferredSize(new Dimension(300, 400));

        JLabel logo = new JLabel("DpTu");
        logo.setFont(ReusableFonts.instance().getFont("Logo"));
        logo.setForeground(Color.MAGENTA);

        panel.addc(
                logo,
                0,
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

        JLabel name = new JLabel("A See_1, Do_1, Teach_1 Intelligent Tutoring System.");
        name.setFont(ReusableFonts.instance().getFont("Name"));
        panel.addc(
                name,
                0,
                1,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                0,
                5,
                5,
                5);

        JTextArea descr = new JTextArea();
        descr.setEditable(false);
        descr.setLineWrap(true);
        descr.setWrapStyleWord(true);
        descr.setFont(ReusableFonts.instance().getFont("Description"));
        descr.append("DpTu provides individualized tutoring practice focused ");
        descr.append("on understanding Dynamic Programming and the");
        descr.append("underlying computer science concepts upon which it is ");
        descr.append("based.\n\n");
        descr.append("Please sign in or use 'New User' to create a student account.");

        descr.append("\n\n");
        descr.append("Use your university email address as your user id, but ");
        descr.append("DO NOT use your existing university password. Instead,");
        descr.append("use a different password for the DpTu tutor.");
        panel.addc(
                descr,
                0,
                2,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                15,
                5,
                5,
                5);

        JLabel loginMsg = new JLabel("To use the tutor, you must sign in.");
        panel.addc(
                loginMsg,
                0,
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

        panel.addc(
                new JLabel(" "),
                0,
                4,
                1,
                1,
                1.0,
                1.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH,
                5,
                5,
                5,
                5);

        return panel;
    }

    /**
     * Creates and returns a login panel containing all login-related components such as text
     * fields, labels, and buttons.
     *
     * @return a GPanel object representing the login section.
     */
    private GPanel createLogin() {
        GPanel panel = new GPanel();
        panel.setBackground(new Color(241, 196, 0));

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        JLabel label = new JLabel("Name");
        label.setLabelFor(fName);
        panel.addc(
                label,
                0,
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

        panel.addc(
                fName,
                0,
                1,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                5,
                5,
                5,
                5);
        panel.addc(
                lName,
                1,
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

        label = new JLabel("User Id:");
        label.setLabelFor(userId);

        panel.addc(
                label,
                0,
                2,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        panel.addc(
                userId,
                0,
                3,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        label = new JLabel("Create a Password:");
        label.setLabelFor(pass1);

        panel.addc(
                label,
                0,
                4,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                15,
                5,
                5,
                5);

        label = new JLabel("(do not use your existing university password!)");
        label.setFont(ReusableFonts.instance().getFont("10ptLabel"));
        label.setForeground(new Color(75, 66, 66));

        panel.addc(
                label,
                1,
                4,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE,
                0,
                5,
                5,
                5);

        panel.addc(
                pass1,
                0,
                5,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        panel.addc(
                strength,
                0,
                6,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        label = new JLabel("(try 6 characters, mixed case, and special chars)");
        label.setFont(ReusableFonts.instance().getFont("10ptLabel"));
        label.setForeground(new Color(75, 66, 66));
        panel.addc(
                label,
                1,
                6,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        label = new JLabel("Confirm your password:");
        label.setLabelFor(pass1);

        panel.addc(
                label,
                0,
                7,
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

        panel.addc(
                pass2,
                0,
                8,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        label = new JLabel("Choose Security Question:");
        label.setLabelFor(secQuestions);

        panel.addc(
                label,
                0,
                9,
                1,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                15,
                5,
                5,
                5);

        panel.addc(
                secQuestions,
                0,
                10,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        label = new JLabel("Answer:");
        label.setLabelFor(secAnswer);

        panel.addc(
                label,
                0,
                11,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                15,
                5,
                5,
                5);

        panel.addc(
                secAnswer,
                0,
                12,
                2,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                0,
                5,
                5,
                5);

        msg = new JLabel("");
        // msg.setLabelFor(backBut);
        msg.setFont(ReusableFonts.instance().getFont("10ptLabel"));
        msg.setForeground(new Color(173, 7, 1));

        panel.addc(
                msg,
                0,
                13,
                2,
                1,
                0.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                5,
                5,
                5,
                5);

        panel.addc(
                backBut,
                0,
                14,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                10,
                5,
                5,
                5);
        panel.addc(
                createAcctBut,
                1,
                14,
                1,
                1,
                1.0,
                0.0,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                10,
                5,
                5,
                5);

        return panel;
    }

    /**
     * Evaluates the strength of the password entered in the password field and updates the strength
     * indicator accordingly.
     */
    private void checkStrength() {

        log.trace("Checking password strength");

        char[] pwd = pass1.getPassword();
        int len = pwd.length;

        if (len == 0) {
            strength.setText("(Strength: Very poor)");
            strength.setForeground(Color.RED);

            log.trace("Password had very poor strength: empty password");
            return;
        }

        if (CommonPasswords.isCommon(pwd)) {
            strength.setText("(Strength: Very poor – common password)");
            strength.setForeground(Color.RED);

            log.trace("Password had very poor strength: common password");
            return;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (char c : pwd) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSymbol = true;
        }

        int score = 0;

        if (len >= 8) score += 2;
        if (len >= 12) score += 1;
        if (hasLower) score += 1;
        if (hasUpper) score += 1;
        if (hasDigit) score += 1;
        if (hasSymbol) score += 1;

        if (score <= 2) {
            strength.setText("(Strength: Very poor)");
            strength.setForeground(Color.RED);
        } else if (score <= 4) {
            strength.setText("(Strength: Poor)");
            strength.setForeground(Color.RED);
        } else if (score <= 6) {
            strength.setText("(Strength: Moderate)");
            strength.setForeground(Color.ORANGE);
        } else {
            strength.setText("(Strength: Strong)");
            strength.setForeground(Color.GREEN);
        }

        log.trace("Password strength evaluated with score {}: {}", score, strength.getText());
    }

    /**
     * Compares the passwords entered in the two password fields to determine if they are identical.
     *
     * @return {@code True} if both password fields contain the same characters; {@code False}
     *     otherwise.
     */
    private boolean samePasswords() {
        char[] text1 = pass1.getPassword();
        char[] text2 = pass2.getPassword();

        if (text1.length == text2.length) {
            if (text1.length == 0) {
                return false;

            } else {
                for (int i = 0; i < text1.length; i++) {
                    if (text1[i] != text2[i]) {
                        return false;
                    }
                }

                return true;
            }

        } else {
            return false;
        }
    }

    /** If the userId or password fields are empty, disable the OK 'Login' button. */
    private void enableButtons(Document e) {
        // Document document = (Document)e.getDocument();
        // fName.getDocument().getLength() !=0;

        boolean isFNameValid = isFirstNameValid();
        boolean isLNameValid = isLastNameValid();
        boolean isUserIdValid = isEmailValid();
        boolean isPass1Valid = isPasswordValid();
        boolean isPass2Valid = isConfirmPassValid();
        boolean isSecAnswerValid = isSecurityAnswerValid();

        // Set invalid fields border color to red
        fName.setBorder(BorderFactory.createLineBorder(isFNameValid ? Color.BLACK : Color.RED));
        lName.setBorder(BorderFactory.createLineBorder(isLNameValid ? Color.BLACK : Color.RED));
        userId.setBorder(BorderFactory.createLineBorder(isUserIdValid ? Color.BLACK : Color.RED));
        pass1.setBorder(BorderFactory.createLineBorder(isPass1Valid ? Color.BLACK : Color.RED));
        pass2.setBorder(BorderFactory.createLineBorder(isPass2Valid ? Color.BLACK : Color.RED));
        secAnswer.setBorder(
                BorderFactory.createLineBorder(isSecAnswerValid ? Color.BLACK : Color.RED));

        /*Display hint for which field is invalid.
        If you want to change the priority of which message shows first,
        change the order of if/else chain*/
        if (!isFNameValid) {
            msg.setText(("Invalid first name"));
        } else if (!isLNameValid) {
            msg.setText("Invalid last name");
        } else if (!isUserIdValid) {
            msg.setText("Invalid email");
        } else if (!isPass1Valid) {
            msg.setText("Invalid password");
        } else if (!isPass2Valid) {
            msg.setText("Passwords do not match");
        } else if (!isSecAnswerValid) {
            msg.setText("Invalid answer to security question");
        } else {
            msg.setText("");
        }

        boolean allValid =
                isFNameValid
                        && isLNameValid
                        && isUserIdValid
                        && isPass1Valid
                        && isPass2Valid
                        && isSecAnswerValid;
        createAcctBut.setEnabled(allValid);
    }

    /**
     * Listens to changes made to the LoginDialog's userId and password fields in order to
     * appropriate enable the buttons in the dialog.
     */
    public class LoginDocumentListener implements DocumentListener {

        /**
         * As text was insert into the userId or password field, check whether we need to enable or
         * disable the LoginDialog's buttons.
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            Component comp = SplashFrame.instance().getFocusOwner();

            if (comp == pass1) {
                checkStrength();
            }

            enableButtons(e.getDocument());
        }

        /**
         * As text was removed from the userId or password field, check whether we need to enable or
         * disable the LoginDialog's buttons.
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            Component comp = MainFrame.instance().getFocusOwner();

            if (comp == pass1) {
                checkStrength();
            }

            enableButtons(e.getDocument());
        }

        /**
         * As text was changed in the userId or password field, check whether we need to enable or
         * disable the LoginDialog's buttons.
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            Component comp = MainFrame.instance().getFocusOwner();

            if (comp == pass1) {
                checkStrength();
            }

            enableButtons(e.getDocument());
        }
    }

    /** Encrypt the given password using MD5 */
    public static String encryptMD5(String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = password.getBytes();

            m.update(data, 0, data.length);

            BigInteger i = new BigInteger(1, m.digest());

            return String.format("%1$032X", i).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not available", e);
        }

        return "";
    }

    /**
     * Encrypt the given password using SHA-256
     *
     * @param base the clear-text password.
     * @return the lower-case hex representation of the SHA-256 digest.
     */
    public static String encryptSHA256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks the validity of the first name field
     *
     * @return true if valid, false otherwise
     */
    private boolean isFirstNameValid() {
        return !fName.isDefaultValue() && !fName.getText().trim().isEmpty();
    }

    /**
     * Checks the validity of the last name field
     *
     * @return true if valid, false otherwise
     */
    private boolean isLastNameValid() {
        return !lName.isDefaultValue() && !lName.getText().trim().isEmpty();
    }

    /**
     * Checks the validity of the User id field
     *
     * @return true if valid, false otherwise
     */
    private boolean isEmailValid() {
        try {
            String email = userId.getDocument().getText(0, userId.getDocument().getLength());
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            return matcher.find() && !email.equals(userId.getHint());
        } catch (BadLocationException e) {
            return false;
        }
    }

    /**
     * returns the validity of the password field
     *
     * @return true if valid, false otherwise
     */
    private boolean isPasswordValid() {
        return pass1.getPassword().length > 0;
    }

    /**
     * Checks the validity of the confirm password field
     *
     * @return true if valid, false otherwise
     */
    private boolean isConfirmPassValid() {
        return samePasswords();
    }

    /**
     * Checks the validity of the Security answer field
     *
     * @return true if valid, false otherwise
     */
    private boolean isSecurityAnswerValid() {
        return secAnswer.getPassword().length > 0;
    }
}
