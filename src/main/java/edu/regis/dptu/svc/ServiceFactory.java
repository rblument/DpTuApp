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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.dao.AccountDAO;
import edu.regis.dptu.dao.CourseDAO;
import edu.regis.dptu.dao.ProblemDAO;
import edu.regis.dptu.dao.SessionDAO;
import edu.regis.dptu.dao.StudentModelDAO;
import edu.regis.dptu.dao.CompletedTaskDAO;

/**
 * A singleton providing a concrete implementation of the service factory used to obtain references
 * to various ShaTu tutoring services.
 *
 * <p>Use of the service factory allows easier changes to how the services are actually implemented
 * without directly affecting the consumers who use them.
 *
 * @author rickb
 */
public class ServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(ServiceFactory.class);

    /**
     * Return a reference to a User service.
     *
     * @return AccountSvc
     */
    public static AccountSvc findAccountSvc() {
        log.debug("ServiceFactory: Retrieving AccountSvc instance");
        return new AccountDAO();
    }

    /**
     * Return a reference to a Course service.
     *
     * @return CourseSvc
     */
    public static CourseSvc findCourseSvc() {
        log.debug("ServiceFactory: Retrieving CourseSvc instance");
        return new CourseDAO();
    }

    /**
     * Return a reference to a Problem service.
     *
     * @return ProblemSvc
     */
    public static ProblemSvc findProblemSvc() {
        log.debug("ServiceFactory: Retrieving ProblemSvc instance");
        return new ProblemDAO();
    }

    /**
     * Return a reference to the session service.
     *
     * @return SessionSvc
     */
    public static SessionSvc findSessionSvc() {
        log.debug("ServiceFactory: Retrieving SessionSvc instance");
        return new SessionDAO();
    }

    /**
     * Return a reference to the student service.
     *
     * @return StudentSvc
     */
    // ToDo: add this support ala ShaTu
    public static StudentModelSvc findStudentModelSvc() {
        log.debug("ServiceFactory: Retrieving StudentModelSvc instance");
        return new StudentModelDAO();
    }

    /**
     * return a reference to the completed task svc
     * 
     * @return CompletedTaskSvc
     */
    public static CompletedTaskSvc findCompletedTaskSvc() {
        log.debug("ServiceFactory: Retrieving CompletedTaskSvc instance");
        return new CompletedTaskDAO();
    }
}
