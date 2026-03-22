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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Decorator wrapping user and student information sans the student model.
 *
 * <p>Separating user and student allows keeping the user's password separate from the Student
 * object with the exception of during initial account creation within this account object.
 *
 * @author rickb
 */
public class Account {
    private static final Logger log = LoggerFactory.getLogger(Account.class);

    /** The user's login id (e.g. "name@university.edu"). */
    protected String userId;

    /** SHA-256 encrypted password. */
    protected String password;

    /** The first name of this user. */
    protected String firstName;

    /** The last name of this student user. */
    protected String lastName;

    /** The security question the user chose to answer. */
    protected int securityQuestion;

    /** An SHA-256 encrypted answer to the the security question. */
    protected String securityAnswer;

    /** True, if this user is a student. */
    protected boolean isStudent;

    /** Initialize this user account with default information. */
    public Account() {
        this("", "", 0, "");
        log.debug("Account created with default values");
    }

    /**
     * Initialize this user with the given user id and default password.
     *
     * @param userId
     */
    public Account(String userId) {
        this(userId, "", 0, "");
        log.debug("Account created with userId={}", userId);
    }

    /**
     * Initialize this user with the given user id and default password.
     *
     * @param userId
     * @param password an SHA-256 encrypted password.
     */
    public Account(String userId, String password) {
        this(userId, password, 0, "");
        log.debug("Account created with userId={} and password set", userId);
    }

    /**
     * Initialize this user with the given user id and password
     *
     * @param userId a String (e.g. "name@university.edu").
     * @param password an SHA-256 encrypted password.
     * @param securityQuestion
     * @param securityAnswer
     */
    public Account(String userId, String password, int securityQuestion, String securityAnswer) {
        this.userId = userId;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        isStudent = true;
        log.debug(
                "Account created: userId={}, securityQuestion={}, isStudent={}",
                userId,
                securityQuestion,
                isStudent);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        log.debug("UserId changed from {} to {}", this.userId, userId);
        this.userId = userId;
    }

    /**
     * @return an SHA-256 encrypted password.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        log.debug("Password updated for userId={}", userId);
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        log.debug("FirstName changed from {} to {}", this.firstName, firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        log.debug("LastName changed from {} to {}", this.lastName, lastName);
        this.lastName = lastName;
    }

    /**
     * Return this user's security question.
     *
     * @return int representing the question the user answered
     */
    public int getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Assign this user's security question.
     *
     * @param securityQuestion
     */
    public void setSecurityQuestion(int securityQuestion) {
        log.debug(
                "SecurityQuestion changed from {} to {}", this.securityQuestion, securityQuestion);
        this.securityQuestion = securityQuestion;
    }

    /**
     * Return this user's security answer.
     *
     * @return a SHA-256 encrypted String
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Assign this user's security answer.
     *
     * @param securityAnswer
     */
    public void setSecurityAnswer(String securityAnswer) {
        log.debug("SecurityAnswer updated for userId={}", userId);
        this.securityAnswer = securityAnswer;
    }

    /**
     * Return whether this user is a student.
     *
     * @return true, this user is a student, false otherwise
     */
    public boolean isStudent() {
        return isStudent;
    }

    /**
     * Return whether this user is a student.
     *
     * @return true, this user is a student, false otherwise
     */
    public boolean getIsStudent() {
        return isStudent;
    }

    /**
     * Assign whether this user is a student.
     *
     * @param isStudent true, the user is a student.
     */
    public void setIsStudent(boolean isStudent) {
        log.debug("isStudent changed from {} to {}", this.isStudent, isStudent);
        this.isStudent = isStudent;
    }

    public void clear() {
        log.debug("Clearing account data for userId={}", userId);
        this.userId = null;
        this.password = null;
        this.firstName = null;
        this.lastName = null;
        this.securityQuestion = 0;
        this.securityAnswer = null;
    }

    /**
     * Return the id and user id of this student.
     *
     * @return a human-readable string representation of this account.
     */
    @Override
    public String toString() {
        log.debug("toString called for userId={}", userId);
        return "User Account: " + userId;
    }
}
