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
package edu.regis.dptu.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.aol.StudentModel;

public class StudentTest {

    @Test
    public void testStudentAccessors() {
        Account account = new Account("student@regis.edu", "pw");
        Student student = new Student(account);

        assertEquals(account, student.getAccount());
        assertNotNull(student.getStudentModel());
        assertTrue(student.toString().contains("student@regis.edu"));

        StudentModel replacementModel = new StudentModel("replacement@regis.edu");
        student.setStudentModel(replacementModel);
        assertSame(replacementModel, student.getStudentModel());
    }
}
