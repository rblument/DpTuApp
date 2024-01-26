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
package edu.regis.dptu.model;

/**
 * A Decorator wrapping user and student information sans the student model.
 * 
 * Separating user and student allows keeping the user's password separate 
 * from the Student object with the exception of during initial account creation
 * within this account object.
 * 
 * @author rickb
 */
public class Account {
  /**
     * The user's login id (e.g. "name@university.edu").
     */
    protected String userId;

    /**
     * SHA-256 encrypted password.
     */
    protected String password;
    
      /**
     * The first name of this user.
     */
    protected String firstName;
    
    /**
     * The last name of this student user.
     */
    protected String lastName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return an SHA-256 encrypted password.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Initialize with empty strings for all fields.
     */
    public Account() {
        userId = "";
        password = "";
        firstName = "";
        lastName = "";
    }
}
