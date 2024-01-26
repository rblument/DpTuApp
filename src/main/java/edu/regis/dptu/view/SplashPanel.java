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

import edu.regis.dptu.model.User;
import edu.regis.dptu.util.SHA_256;
import edu.regis.dptu.view.act.NewUserAction;
import edu.regis.dptu.view.act.SignInAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * A splash panel introducing the DpTut tutor, which also allows the user to 
 * sign-in or navigate to another screen to create a new student account.
 * 
 * This panel also provides a simple view of a User model.
 * 
 * @author rickb
 */
public class SplashPanel extends GPanel {
    /**
     * Events of interest occurring in this class are logged to this logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SplashPanel.class.getName());
    
     /**
     * The user model displayed in this view.
     */
    private User model;
    
    /**
     * The user's id of the form "user@university.edu".
     */
    protected JTextField userId;
    
    /**
     * Hidden display of the user's password: *******.
     */
    protected JPasswordField password;
    
    /**
     * Allows the user to attempt to sign-in using their id and password.
     */
    protected JButton signInBut;
    
    /**
     * Displays a panel allowing the user to create a new student account.
     */
    protected JButton createAcctBut;
    
    /**
     * Create and layout the child GUI components in this panel.
     */
    public SplashPanel() {
	super();
        
        model = new User();

	initializeComponents();
	layoutComponents();
        
        // ToDo: TMP DEBUGGING (saves me typing), remove this for real
        userId.setText("");
    }
    
    /**
     * Set the default focus to the user id field.
     */
    public void setInitialFocus() {
        userId.requestFocusInWindow();
    }
    
    /**
     * Update and return the model with the user login information in this view.
     * 
     * @return a User model with the user id and password fields set (the 
     *         password is encrypted)
     */
    public User getModel() {
	model.setUserId(userId.getText());
        
        String encryptedPass = SHA_256.instance().sha256(new String(password.getPassword()));
        
        model.setPassword(encryptedPass);

	return model;
    }

    /**
     * Display the given user (id) in this view, but the encrypted password
     * is not displayed. 
     * 
     * @param model the user (id) to display
     */
    public void setModel(User model) {
	this.model = model;

	userId.setText(model.getUserId());
    }
    
    /**
     * Set the input focus to the user id field.
     */
    public void updateFocus() {
    	userId.requestFocusInWindow();
    }
    
    /**
     * Return a reference to the sign-in button (used by the WelcomeFrame
     * to make it the default button, when this panel is displayed).
     * 
     * @return 
     */
    public JButton getSigninButton() {
        return signInBut;
    }
    
    /**
     * Create the primary child components used in this view
     */
    private void initializeComponents() {
        LoginDocumentListener docListener = new LoginDocumentListener();
        
        userId = new JTextField(20);
	userId.getDocument().addDocumentListener(docListener);

	password = new JPasswordField(20);
	password.getDocument().addDocumentListener(docListener);

	signInBut = new JButton(SignInAction.instance());
	signInBut.setEnabled(false);
        
        createAcctBut = new JButton(NewUserAction.instance());
    }
    
    /**
     * Layout the child components used in this view.
     */
    private void layoutComponents() {
        setBackground(Color.WHITE);

	addc(createHeader(), 0,0, 2,1, 1.0,0.0,
	     GridBagConstraints.NORTHWEST,  GridBagConstraints.HORIZONTAL,
	     5,5,5,5);	

	addc(createOverview(), 0,1, 1,1, 1.0,1.0,
	     GridBagConstraints.NORTHWEST,  GridBagConstraints.BOTH,
	     5,5,5,5);	

	addc(createLogin(), 1,1, 1,1, 0.0,0.0,
	    GridBagConstraints.NORTHWEST,  GridBagConstraints.NONE,
	    10,5,5,5);	

        addc(new JLabel(" "), 0,2, 2,1, 1.0,1.0,
            GridBagConstraints.NORTHWEST,  GridBagConstraints.BOTH,
            5,5,5,5);
        
        JLabel copyright = new JLabel("(C) 2019-2024 Johanna and Richard Blumenthal. All Rights Reserved");
        copyright.setFont(new Font("Dialog", Font.PLAIN, 10));
        addc(copyright, 0,3, 2,1, 1.0,1.0,
		GridBagConstraints.NORTH,  GridBagConstraints.CENTER,
		5,5,5,5);
        
	setSize(490, 400);
    }
    
    private GPanel createHeader() {
	GPanel panel = new GPanel();
	panel.setBackground(new Color(223,242,245));

	JLabel ccis = new JLabel("Regis University Department of Computer and Cyber Sciences");
	ccis.setFont(new Font("Dialog", Font.PLAIN, 20));
	ccis.setForeground(Color.BLUE);
	
	panel.addc(ccis , 0,0, 1,1, 1.0,1.0,
	     GridBagConstraints.NORTHWEST,  GridBagConstraints.HORIZONTAL,
	     5,5,5,5);

	JLabel newLabel = new JLabel("New to DpTu?");
	newLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
	newLabel.setForeground(Color.GRAY);

	panel.addc(newLabel , 1,0, 1,1, 0.0,0.0,
	     GridBagConstraints.EAST,  GridBagConstraints.NONE,
	     5,5,5,5);	

	panel.addc(createAcctBut, 2,0, 1,1, 0.0,0.0,
		   GridBagConstraints.EAST, GridBagConstraints.NONE,
		   5,5,5,5);	

	return panel;
    }
    
     private GPanel createLogin() {
	GPanel panel = new GPanel();
	panel.setBackground(new Color(223,242,245));

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        JLabel label = new JLabel("Sign in");
	panel.addc(label, 0,0, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		   5,5,5,5);	

        label = new JLabel("User Id");
	label.setLabelFor(userId);

	panel.addc(label, 0,1, 1,1, 1.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		   5,5,5,5);

 	panel.addc(userId, 0,2, 2,1, 1.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
		   0,5,5,5);

        label = new JLabel("Password:");
	label.setLabelFor(password);

	panel.addc(label, 0,3, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		   15,5,5,5);

	panel.addc(password, 0,4, 2,1, 1.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
		   0,5,5,5);

	panel.addc(signInBut, 0,5, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		   15,5,5,5);

	return panel;
    }

    
    private GPanel createOverview() {
	GPanel panel = new GPanel();
	panel.setBackground(Color.WHITE);

	panel.setSize(300, 400);
	panel.setPreferredSize(new Dimension(300,400));

	JLabel logo = new JLabel("DpTu: Dynamic Programming Tutor");
	logo.setFont(new Font("Dialog", Font.PLAIN, 20));
	logo.setForeground(Color.MAGENTA);

	panel.addc(logo, 0,0, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST,  GridBagConstraints.NONE,
		   5,5,5,5);	

	JLabel name = new JLabel("A See_1, Do_1, Teach_1 Intelligent Tutoring System.");
	name.setFont(new Font("Dialog", Font.PLAIN, 14));
	panel.addc(name, 0,1, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST,  GridBagConstraints.NONE,
		   0,5,5,5);	

	
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
        
	panel.addc(descr, 0,2, 1,1, 1.0,1.0,
		   GridBagConstraints.NORTHWEST,  GridBagConstraints.BOTH,
		   15,5,5,5);

	JLabel loginMsg = new JLabel("To use the tutor, you must sign in.");
	panel.addc(loginMsg, 0,3, 1,1, 0.0,0.0,
		   GridBagConstraints.NORTHWEST,  GridBagConstraints.NONE,
		   5,5,5,5);	
	
	panel.addc(new JLabel(" "), 0,4, 1,1, 1.0,1.0,
		   GridBagConstraints.NORTHWEST,  GridBagConstraints.BOTH,
		   5,5,5,5);
        
        return panel;
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
	    enableButtons(e);
	}

	/**
	 * As text was removed from the userId or password field, check whether
	 * we need to enable or disable the LoginDialog's buttons.
	 */
        @Override
	public void removeUpdate(DocumentEvent e) {
	    enableButtons(e);
	}

	/**
	 * As text was changed in the userId or password field, check whether
	 * we need to enable or disable the LoginDialog's buttons.
	 */
        @Override
	public void changedUpdate(DocumentEvent e) {
	    enableButtons(e);
	}

	/**
	 * If the userId or password fields are empty, disable the OK 'Login'
	 * button.
	 */
	private void enableButtons(DocumentEvent e) {
            Document document = (Document) e.getDocument();

	    if ((userId.getDocument().getLength() == 0) ||
		(password.getDocument().getLength() == 0)) {
		signInBut.setEnabled(false);

	    } else {
		signInBut.setEnabled(true);
	    }
	}
    }
}
