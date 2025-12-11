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
 * A user with sign-in credentials consisting of a user id and password.
 *
 * <p>During account creation, an Account object is used instead.
 *
 * @author rickb
 */
public class User extends Model {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    /** The user's login id (e.g. "name@university.edu"). */
    protected String userId;

    /** SHA-256 encrypted password. */
    protected String password;

    // ToDo: Check if this is Needed for gson.fromJson???
    public User() {
        this("", "");
        log.debug("User() default constructor called");
    }

    /**
     * Initialize this user with the given user id and default password.
     *
     * @param userId
     */
    public User(String userId) {
        this(userId, "");
        log.debug("User(String userId) constructor called with userId={}", userId);
    }

    /**
     * Initialize this user with the given user id and password
     *
     * @param userId a String (e.g. "name@university.edu").
     * @param password an SHA-256 encrypted password.
     */
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        log.debug("User(String userId, String password) constructor called with userId={}, password=[PROTECTED]", userId);
    }

    /**
     * Return this user's user id.
     *
     * @return a String with the format "name@university.edu"
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Assign this user's user id.
     *
     * @param userId String "name@university.edu"
     */
    public void setUserId(String userId) {
        log.debug("setUserId: changing userId from {} to {}", this.userId, userId);
        this.userId = userId;
    }

    /**
     * Return this user's password.
     *
     * @return a SHA-256 encrypted String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Assign this user's password.
     *
     * @param password a SHA-256 encrypted String
     */
    public void setPassword(String password) {
        log.debug("setPassword: password updated for userId={}", this.userId);
        this.password = password;
    }

    /**
     * Return the id and user id of this student.
     *
     * @return
     */
    @Override
    public String toString() {
        return "User: " + userId;
    }
}
