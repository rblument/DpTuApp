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

import edu.regis.dptu.dao.CourseDAO;
import edu.regis.dptu.dao.SessionDAO;
import edu.regis.dptu.dao.StudentDAO;
import edu.regis.dptu.dao.UserDAO;

/**
 * A singleton providing a concrete implementation of the service factory used
 * to obtain references to various ShaTu tutoring services.
 * 
 * Use of the service factory allows easier changes to how the services are
 * actually implemented without directly affecting the consumers who use them.
 * 
 * @author rickb
 */
public class ServiceFactory {
    /**
     * Return a reference to the course service.
     * 
     * @return CourseSvc
     */
    public static CourseSvc findCourseSvc() {
        return new CourseDAO();
    }
    
    /**
     * Return a reference to the session service.
     * 
     * @return SessionSvc
     */
    public static SessionSvc findSessionSvc() {
        return new SessionDAO();
    }
    
    /**
     * Return a reference to the student service.
     * 
     * @return StudentSvc
     */
    public static StudentSvc findStudentSvc() {
        return new StudentDAO();
    }
    
    /**
     * Return a reference to the user service.
     * 
     * @return UserSvc
     */
    public static UserSvc findUserSvc() {
        return new UserDAO();
    }
}
