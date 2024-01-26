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
import edu.regis.dptu.model.TutoringSession;

/**
 * Specifies the API for Session life-cycle maintenance (CRUD persistence).
 * 
 * @author rickb
 */
public interface SessionSvc {
    /**
     * Insert the given session into the database.
     * 
     * @param session the TutoringSession to create.
     * @throws IllegalArgException a session for the associated student account
     *                             already exists.
     * @throws NonRecoverableException perhaps see getCause().getErrorCode().
     */
    void create(TutoringSession session) throws IllegalArgException, NonRecoverableException;
    
    /**
     * Return the session with the specified id (this is a full session with all
     * events versus a digest).
     * 
     * @param userId the user id of the session to return (user@university.edu)
     * @return the Session for the given user id
     * @throws ObjNotFoundException no trial with the given id exists
     * @throws NonRecoverableException perhaps see getCause().getErrorCode().
     */
    TutoringSession retrieve(String userId) throws ObjNotFoundException, NonRecoverableException;
    
    /**
     * Update the session in formation in the database using the given session.
     * 
     * @param session a Session containing new information.
     * @throws ObjNotFoundException the session does not exist in the database.
     * @throws NonRecoverableException perhaps see getCause().getErrorCode().
     */
    void update(TutoringSession session) throws ObjNotFoundException, NonRecoverableException;
    
    /**
     * Delete the session from the database for the given student user id.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @throws NonRecoverableException 
     */
    void delete(String userId) throws NonRecoverableException;
}
