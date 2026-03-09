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

import edu.regis.dptu.dao.AccountDAO;
import edu.regis.dptu.dao.CompletedTaskDAO;
import edu.regis.dptu.dao.CourseDAO;
import edu.regis.dptu.dao.ProblemDAO;
import edu.regis.dptu.dao.SessionDAO;
import edu.regis.dptu.dao.StudentModelDAO;
import edu.regis.dptu.svc.ServiceFactory;

public class ServiceFactoryTest {

    @Test
    public void testFactoryMethodsReturnServices() {
        assertInstanceOf(AccountDAO.class, ServiceFactory.findAccountSvc());
        assertInstanceOf(CourseDAO.class, ServiceFactory.findCourseSvc());
        assertInstanceOf(ProblemDAO.class, ServiceFactory.findProblemSvc());
        assertInstanceOf(SessionDAO.class, ServiceFactory.findSessionSvc());
        assertInstanceOf(StudentModelDAO.class, ServiceFactory.findStudentModelSvc());
        assertInstanceOf(CompletedTaskDAO.class, ServiceFactory.findCompletedTaskSvc());
    }
}

