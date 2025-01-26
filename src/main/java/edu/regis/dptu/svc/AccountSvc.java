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
package edu.regis.dptu.svc;

import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Account;

/**
 * The API for {@link Account} life-cycle maintenance (CRUD persistence).
 * 
 * @author rickb
 */
public interface AccountSvc {
    /**
     * Insert the give user account into the DB (cannot insert an admin).
     *
     * @param account a user containing the information to insert
     * @throws IllegalArgException an account with the given user id already exists
     * @throws NonRecoverableException also see getCause().getErrorCode().
     */
    void create(Account account) throws IllegalArgException, NonRecoverableException;
    
    /**
     * Return the account, if any, for the given user id.
     * 
     * @param userId the id of the user to return: name@university.edu
     * @return a User with the given id
     * @throws ObjNotFoundException no user with the given id exists
     * @throws NonRecoverableException also see getCause().getErrorCode().
     */
    Account retrieve(String userId) throws ObjNotFoundException, NonRecoverableException;
    
    /**
     * Update the given user's password (requires existing password).
     * 
     * @param account the account that is being updated (the new information
     *   in in this account).
     * @throws ObjNotFoundException user doesn't exists in the database
     * @throws IllegalArgException update an invalid field or non-student account.
     * @throws NonRecoverableException also see getCause().getErrorCode().
     */
    void update(Account account) 
            throws ObjNotFoundException, IllegalArgException, NonRecoverableException;
    
    /**
     * Delete the account for the given user id from the database.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @throws NonRecoverableException 
     */
    void delete(String userId) throws NonRecoverableException;
    
    /**
     * Check if an account with the given user id exists in the database.
     * (less expensive then using retrieve).
     *
     * @param userId user id of the account to find: name@university.edu
     * @return true, if the StudentUser exists in the DB, false otherwise
     * @throws NonRecoverableException (see getCause().getErrorCode()).
     */
    boolean exists(String userId) throws NonRecoverableException;
}
