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
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.aol.StudentModel;

/**
 * Specifies the API for {@link Student} life-cycle maintenance (CRUD persistence).
 * 
 * @author rickb
 */
public interface StudentSvc  {
    /**
     * Insert the given {@link Student} and {@link StudentModel} into the database.
     * 
     * @param student the student's email address is used as the student id
     *                (email: user@university.edu)
     * @throws IllegalArgException a student with the given user id already exists
     * @throws NonRecoverableException perhaps see getCause().getErrorCode()
     */
    void create(Student student) throws IllegalArgException, NonRecoverableException;
    
    /**
     * Return whether a student with the given user id exists. 
     * 
     * The idea is that this will execute quickly since it avoid loading the
     * student model.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @return true if the given student exists, otherwise false
     * @throws NonRecoverableException
     */
    boolean exists(String userId) throws NonRecoverableException;
    
    /**
     * Return the {@link Student} with the given user id.
     * 
     * See exists(String) for a faster check as to whether a student exists.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @return the desired student
     * @throws ObjNotFoundException no student with the give user id exists
     * @throws NonRecoverableException perhaps see getCause().getErrorCode()
     */
    Student retrieve(String userId) throws ObjNotFoundException, NonRecoverableException;
    
    /**
     * Return the {@link StudentModel} for the given user id.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @return the StudentModel for the Student with the given user id
     * @throws ObjNotFoundException No student with the given user id exists

     * @throws NonRecoverableException perhaps see getCause().getErrorCode()
     */
    StudentModel findModelById(String userId) throws ObjNotFoundException, 
            NonRecoverableException;
    
    /**
     * Delete the student from the database including the student's account,
     * current session, and student model.
     * 
     * @param userId the student's user id (email: user@university.edu)
     * @throws NonRecoverableException 
     */
    void delete(String userId) throws NonRecoverableException;
}
