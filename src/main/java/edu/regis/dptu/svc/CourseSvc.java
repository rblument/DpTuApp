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

import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Course;

/**
 * Specifies the API for Course life-cycle maintenance (database persistence).
 * 
 * @author rickb
 */
public interface CourseSvc {
    /**
     * Locate and return the course with the given id.
     *
     * @param id  integer key of the course to load.
     * @return The course with the given id.
     * @exception ObjNotFoundException No course with the given id exists.
     * @throws NonRecoverableException see the documentation for this exception.
     */
    Course retrieve(int id) throws ObjNotFoundException, NonRecoverableException;
}

