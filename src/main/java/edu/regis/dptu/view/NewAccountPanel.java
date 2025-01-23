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

import edu.regis.dptu.model.Account;
import edu.regis.dptu.view.act.CreateAcctAction;
import edu.regis.dptu.view.act.SignInAction;
import edu.regis.dptu.view.act.BackAction;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * New user screen that also allows the student to create an IRBt account
 *
 * @author rickb
 */
public class NewAccountPanel extends GPanel {

    /**
     * Events of interest occurring in this class are logged to this logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NewAccountPanel.class.getName());

    /**
     * A regex pattern used to validate user email ids (e.g. "rick@regis.edu").
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * The account being created and displayed in this panel.
     */
    private Account model;

    /**
     * The editable fields appearing in this dialog.
     */
    protected HintTextField fName;
    protected HintTextField lName;
    protected HintTextField userId;
    protected JPasswordField pass1;
    protected JPasswordField pass2;

    protected JLabel strength;
    protected JLabel msg;

    protected JButton signInBut;
    protected JButton createAcctBut;
    protected JButton backBut;

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
     * @return
     */
    public Account getModel() {
        updateModel();

        return model;
    }

    /**
     * Display the given model in the view, but the MD5 encrypted password is
     * not displayed.
     *
     * @param model
     */
    public void setModel(Account model) {
        this.model = model;

        updateDisplay();
    }

    public JTextField getUserIdComp() {
        return userId;
    }

    public void updateFocus() {
        fName.requestFocusInWindow();
    }
    
    /**
     * Set all of the text fields in this view to the empty string.
     */
    public void clearFields() {
        fName.setText("");
        lName.setText("");
        userId.setText("");
        pass1.setText("");
        pass2.setText("");
    }

    /**
     * Update our model with the current values displayed in this view
     */
    private void updateModel() {
        model.setUserId(userId.getText());
        model.setFirstName(fName.getText());
        model.setLastName(lName.getText());
        model.setPassword(encryptSHA256(new String(pass1.getPassword())));
    }

    /**
     * Update this view with the current values in our model (except the
     * passwords).
     */
    private void updateDisplay() {
        userId.setText(model.getUserId());
        fName.setText(model.getFirstName());
        lName.setText(model.getLastName());
        pass1.setText("");
        pass2.setText("");
    }

    // Used to get focus
    //public JTextField getFNameComp() {
    //return fName;
    //}
    private void initComponents() {
        LoginDocumentListener docListener = new LoginDocumentListener();

        //fName = new JTextField("First", 15);
        fName = new HintTextField("First", 15);
        //fName.setForeground(new Color(230,230,230));
        fName.getDocument().addDocumentListener(docListener);
        //((AbstractDocument) fName.getDocument()).setDocumentFilter(new NameFilter());

        lName = new HintTextField("Last", 30);
        lName.getDocument().addDocumentListener(docListener);

        userId = new HintTextField("userId@university.edu", 10);
        userId.setIsEmailAddr(true);
        userId.getDocument().addDocumentListener(docListener);

        pass1 = new JPasswordField(20);
        pass1.getDocument().addDocumentListener(docListener);

        pass2 = new JPasswordField(20);
        pass2.getDocument().addDocumentListener(docListener);

        //SignInAction act = SignInAction.instance();
        signInBut = new JButton(SignInAction.instance());
        signInBut.setEnabled(true);

        createAcctBut = new JButton(CreateAcctAction.instance());
        createAcctBut.setEnabled(false);
        MainFrame.instance().getRootPane().setDefaultButton(createAcctBut);

        backBut = new JButton(BackAction.instance());
        backBut.setEnabled(true);
        
        strength = new JLabel("(Strength: very poor)");
        strength.setForeground(Color.RED);
        strength.setFont(new Font("Dialog", Font.PLAIN, 10));
    }

    private void layoutPanel() {
        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(300, 400));

        addc(createHeader(), 0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);

        addc(createOverview(), 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                5, 5, 5, 5);

        addc(createLogin(), 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                10, 5, 5, 5);

        JLabel copyright = new JLabel("(C) 2019-2024 Johanna and Richard Blumenthal. All Rights Reserved");
        copyright.setFont(new Font("Dialog", Font.PLAIN, 10));
        addc(copyright, 0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.CENTER,
                5, 5, 5, 5);

        setSize(490, 400);
    }

    private GPanel createHeader() {
        GPanel panel = new GPanel();
        panel.setBackground(new Color(223, 242, 245));
        

        JLabel ccis = new JLabel("Regis University Department of Computer and Cyber Sciences");
        ccis.setFont(new Font("Dialog", Font.PLAIN, 20));
        ccis.setForeground(Color.BLUE);

        panel.addc(ccis, 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        
        panel.addc(backBut, 2,0, 1,1, 0.0,0.0,
		   GridBagConstraints.EAST, GridBagConstraints.NONE,
		   5,5,5,5);	

        msg = new JLabel("");
        msg.setLabelFor(backBut);
        msg.setFont(new Font("Dialog", Font.PLAIN, 10));
        msg.setForeground(Color.RED);

        return panel;
    }

    private GPanel createOverview() {
        GPanel panel = new GPanel();
        panel.setBackground(Color.WHITE);

        panel.setSize(300, 400);
        panel.setPreferredSize(new Dimension(300, 400));

        JLabel logo = new JLabel("DpTu");
        logo.setFont(new Font("Dialog", Font.PLAIN, 20));
        logo.setForeground(Color.MAGENTA);

        panel.addc(logo, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        JLabel name = new JLabel("A See_1, Do_1, Teach_1 Intelligent Tutoring System.");
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        panel.addc(name, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                0, 5, 5, 5);

        JTextArea descr = new JTextArea();
        descr.setEditable(false);
        descr.setLineWrap(true);
        descr.setWrapStyleWord(true);
        descr.setFont(new Font("Dialog", Font.PLAIN, 12));
	descr.append("DpTu provides individualized tutoring practice focused ");
	descr.append("on understanding Dynamic Programming and the");
        descr.append("underlying computer science concepts upon which it is ");
        descr.append("based.\n\n");
        descr.append("Please sign in or use 'New User' to create a student account.");

        descr.append("\n\n");
        descr.append("Use your university email address as your user id, but ");
        descr.append("DO NOT use your existing university password. Instead,");
        descr.append("use a different password for the DpTu tutor.");
        panel.addc(descr, 0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                15, 5, 5, 5);

        JLabel loginMsg = new JLabel("To use the tutor, you must sign in.");
        panel.addc(loginMsg, 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        panel.addc(new JLabel(" "), 0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                5, 5, 5, 5);

        return panel;
    }

    private GPanel createLogin() {
        GPanel panel = new GPanel();
        panel.setBackground(new Color(223, 242, 245));

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        JLabel label = new JLabel("Name");
        label.setLabelFor(fName);
        panel.addc(label, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        panel.addc(fName, 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);
        panel.addc(lName, 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                5, 5, 5, 5);

        label = new JLabel("User Id");
        label.setLabelFor(userId);

        panel.addc(label, 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        panel.addc(userId, 0, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                0, 5, 5, 5);

        label = new JLabel("Create a Password:");
        label.setLabelFor(pass1);

        panel.addc(label, 0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                15, 5, 5, 5);

        label = new JLabel("(do not use your existing university password!)");
        label.setFont(new Font("Dialog", Font.PLAIN, 10));
        label.setForeground(new Color(200, 200, 200));

        panel.addc(label, 1, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                0, 5, 5, 5);

        panel.addc(pass1, 0, 5, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                0, 5, 5, 5);

        panel.addc(strength, 0, 6, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                0, 5, 5, 5);

        label = new JLabel("(try 6 characters, mixed case, and special chars)");
        label.setFont(new Font("Dialog", Font.PLAIN, 10));
        label.setForeground(new Color(200, 200, 200));
        panel.addc(label, 1, 6, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                0, 5, 5, 5);

        label = new JLabel("Confirm your password:");
        label.setLabelFor(pass1);

        panel.addc(label, 0, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        panel.addc(pass2, 0, 8, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                0, 5, 5, 5);

        panel.addc(createAcctBut, 0, 9, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                10, 5, 5, 5);

        msg = new JLabel("");
        msg.setLabelFor(createAcctBut);
        msg.setFont(new Font("Dialog", Font.PLAIN, 10));
        msg.setForeground(Color.RED);

        panel.addc(msg, 0, 12, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                5, 5, 5, 5);

        return panel;
    }

    private void checkStrength() {

        int points = 0;

        char[] text = pass1.getPassword();

        if (text.length >= 6) {
            points++;
        }

        boolean isSpecial = false;
        boolean isUpper = false;
        boolean isLower = false;
        for (int i = 0; i < text.length; i++) {
            char ch = text[i];

            if (Character.isLowerCase(ch)) {
                isLower = true;
            } else if (Character.isUpperCase(ch)) {
                isUpper = true;
            } else if (Character.isDigit(ch)) {
                isSpecial = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                isSpecial = true;
            }
        }

        if (isSpecial) {
            points++;
        }

        if (isLower && isUpper) {
            points++;
        }

        switch (points) {
            case 0:
                strength.setText("(Strength: Very poor)");
                strength.setForeground(Color.RED);
                break;
            case 1:
                strength.setText("(Strength: Poor)");
                strength.setForeground(Color.RED);
                break;
            case 2:
                strength.setText("(Strength: Moderate)");
                strength.setForeground(Color.YELLOW);
                break;
            default:
                strength.setText("(Strength: Good)");
                strength.setForeground(Color.GREEN);
                break;
        }

    }

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

    /**
     * If the userId or password fields are empty, disable the OK 'Login'
     * button.
     */
    private void enableButtons(Document e) {
        //Document document = (Document)e.getDocument();
        //fName.getDocument().getLength() !=0;

        boolean isValidFName = !fName.isDefaultValue();
        if (isValidFName) {
            fName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
            fName.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        boolean isValidLName = !lName.isDefaultValue();
        if (isValidLName) {
            lName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
            lName.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        boolean isValidUserId = !userId.isDefaultValue();
        if (isValidUserId) {
            Document userIdDoc = userId.getDocument();
            try {
                String email = userIdDoc.getText(0, userIdDoc.getLength());

                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
                isValidUserId = matcher.find();

                if (isValidUserId) {
                    userId.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                } else {
                    userId.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            } catch (BadLocationException er) {
                // Cannot happen since 0 to length
            }
        } else {
            userId.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        boolean isPass1Valid = pass1.getDocument().getLength() == 0;
        if (isPass1Valid) {
            pass1.setBorder(BorderFactory.createLineBorder(Color.RED));
        } else {
            pass1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        boolean isSamePass = samePasswords();
        if (isSamePass) {
            pass2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
            pass2.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (isValidFName && isValidLName && isValidUserId && isSamePass) {
            createAcctBut.setEnabled(true);
            msg.setText("");

        } else {
            createAcctBut.setEnabled(false);
            msg.setText("(* Please fix problems highlighted in red.)");
        }
    }

    /**
     * Listens to changes made to the LoginDialog's userId and password fields
     * in order to appropriate enable the buttons in the dialog.
     */
    public class LoginDocumentListener implements DocumentListener {

        /**
         * As text was insert into the userId or password field, check whether
         * we need to enable or disable the LoginDialog's buttons.
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
         * As text was removed from the userId or password field, check whether
         * we need to enable or disable the LoginDialog's buttons.
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
         * As text was changed in the userId or password field, check whether we
         * need to enable or disable the LoginDialog's buttons.
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

    /**
     * Encrypt the given password using MD5
     */
    private String encryptMD5(String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = password.getBytes();

            m.update(data, 0, data.length);

            BigInteger i = new BigInteger(1, m.digest());

            return String.format("%1$032X", i).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe(e.toString());
        }

        return "";
    }

    /**
     * Encrypt the given password using SHA-256
     *
     * @param base
     * @return
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

}


