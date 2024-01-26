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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A JTextField with default text appearing in grey that disappears when a
 * user enters anything, but reappears if the all user enter text is removed.
 * 
 * This field also intercepts focus events and moves the caret to offset 0,
 * if the user hasn't entered any data.
 * 
 * TODO: Check if newer versions of Java provide this type of field.
 *
 * @author rickb
 */
public class HintTextField extends JTextField {
    /**
     * The initial default 'hint' displayed as gray text in the field.
     */
    protected String hint = "";

    /**
     * A light gray color to display initial default text.
     */
    protected Color hintColor = new Color(200,200,200);

    /**
     * True, if the field contains only the default hint text (i.e. no user
     * entered text) and false, otherwise.
     */
    private boolean isFirstEdit = true;
    
    /**
     * True, if the field contains an email address.
     */
    private boolean isEmailAddr = false;

    /**
     * Initialize this text field with the default hint of "enter".
     */
    public HintTextField() {
	super();

	init("enter");
    }

    /**
     * Initialize this text field with the given default prompt.
     * 
     * @param text 
     */
    public HintTextField(String text) {
	super(text);

	init(text);
    }

    /**
     * Initialize this text field with the default "enter" hint and columns.
     * 
     * @param columns 
     */
    public HintTextField(int columns) {
	super(columns);

	init("enter");
    }

    /**
     * Initialize this text field with the given hint text and columns
     * 
     * @param text
     * @param columns 
     */
    public HintTextField(String text, int columns) {
	super(text, columns);

	init(text);
    }

    private void init(String hint) {
	this.hint = hint;

	setForeground(hintColor);

	((AbstractDocument) getDocument()).setDocumentFilter(new NameFilter());

	addMouseListener(new CaretMouseListener());
    }

    public String getHint() {
	return hint;
    }

    public void setHint(String hint) {
	this.hint = hint;
    }
    
    public boolean getIsEmailAddr() {
        return isEmailAddr;
    }
    
    public void setIsEmailAddr(boolean isEmailAddr) {
        this.isEmailAddr = isEmailAddr;
    }

    public boolean isDefaultValue() {
	return getText().equals(hint);
    }

    /**
     * A filter that manages the hint text by removing it when the user has
     * entered one or more legal characters and restoring it when the user
     * has removed all legal characters.
     */
    public class NameFilter extends DocumentFilter {

	private boolean isLetterOrHyphen(String str) {
	    boolean isValid = true;
	    for (int i = 0; i < str.length(); i++)
		if (!Character.isLetter(str.charAt(i)) &&
		    (str.charAt(i) != '-'))
		    return false;

	    return true;
	}
        
        private boolean isEmailChar(String str) {
            boolean isValid = true;
	    for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
		if (!Character.isLetter(ch) &&
                    !Character.isDigit(ch) &&
		    (ch != '-') &&
                    (ch != '_') &&
                    (ch != '@') &&
                    (ch != '.'))
		    return false;
            }

	    return true;
        }

	// Called when direct inserts into the document are made.
	public void insertString(DocumentFilter.FilterBypass fb, int offs, 
				 String str, AttributeSet s) 
	    throws BadLocationException
	{
	}

	/**
	 * If the user enters a non-space initial character, replace the
	 * default hint text with the user's character switching to black.
	 * 
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offs, int len,
			    String str, AttributeSet s)
	    throws BadLocationException
	{
	    if (isFirstEdit) {
		if ((offs == 0) && (str.length() == 1)) {
		    if (Character.isLetter(str.charAt(0))) {
			isFirstEdit = false;
			setForeground(Color.BLACK);
			super.replace(fb,0,hint.length(),str,s);
		    }
		} else if ((isLetterOrHyphen(str)) ||
                           (isEmailAddr && isEmailChar(str))) {
		    isFirstEdit = false;
		    setForeground(Color.BLACK);

		    super.replace(fb,0,hint.length(),str,s);
		}
	    } else {
		String txt = getText();
		if ((offs == 0) && (txt.length() == len)) { // replace all txt
		    if ((str.length() == 1) && 
			!Character.isLetter(str.charAt(0)))
		    {
			// with hint text if non-letter
			super.replace(fb,0,len,hint,s);

			setForeground(hintColor);
			isFirstEdit = true;

			setCaretPosition(0);

		    } else {
			super.replace(fb,offs,len,str,s);
		    }

		} else {
		    if (isLetterOrHyphen(str) ||
                        (isEmailAddr && isEmailChar(str)))
			super.replace(fb,offs,len,str,s);
		}
	    }
	}

	

	public void remove(DocumentFilter.FilterBypass fb, int offs, int len) 
	    throws BadLocationException
	{
	    if (!isFirstEdit) {
		String txt = getText();
		// Removing last user entered char or all text
		if ((txt.length() == 1) || (txt.length() == len)) {
		    setText(hint);
		    setForeground(hintColor);
		    isFirstEdit = true;
		    setCaretPosition(0);
		} else {
		    super.remove(fb,offs,len);
		}
	    }
	}
    }

    public class CaretMouseListener extends MouseAdapter {
	public void mousePressed(final MouseEvent e) {
	    SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			JTextField tf = (JTextField) e.getSource();
		    
			if (isFirstEdit) {
			    tf.setCaretPosition(0);
			} else {
			    int offset = tf.viewToModel(e.getPoint());
			    tf.setCaretPosition(offset);
			}
		    }
		}
		);
	}
    }

}

