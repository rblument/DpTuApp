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
package edu.regis.dptu.view.act;

/**
 * Factory for creating the GUI actions used in the ShaTu user interface.
 * 
 * @author rickb
 */
public class ActionFactory {
    /**
     * Create each of the Java GUI actions by referencing their singleton.
     */
    public static void createActions() {
        CreateAcctAction.instance();
        NewUserAction.instance();
        SaveSessionAction.instance();
        SignInAction.instance();
        
    }
}
