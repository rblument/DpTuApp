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

import edu.regis.dptu.model.aol.StudentModel;

/**
 * A Student is a user who is being tutored by the ShaTu tutor and
 * consequently has an associated student model.
 * 
 * @author rickb
 */
public class Student {    
    /**
     * The account associated with this student.
     */
    private final Account account;
    
    /**
     * Convenience reference to this Student's student model.
     */
    private StudentModel studentModel;
    
    /**
     * Initialize this student with the given account information.
     * 
     * @param account the account associated with this student.
     */
    public Student(Account account) {
        this.account = account;
        studentModel = new StudentModel(account.getUserId());
    }
  
    /**
     * Return the account associated with this student
     * 
     * @return the Account associated with this student
     */
    public Account getAccount() {
        return account;
    }
    
    /**
     * Return this student's student model
     * 
     * @return a StudentModel
     */
    public StudentModel getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(StudentModel studentModel) {
        this.studentModel = studentModel;
    }

    @Override
    public String toString() {
        return "Student: " + account.getUserId();
    }
}
