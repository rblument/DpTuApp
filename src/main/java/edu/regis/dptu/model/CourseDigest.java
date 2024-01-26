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

/**
 * A descriptive summary of a course as used in a tutoring session, which 
 * eliminates the need to return an entire course to the GUI.
 * 
 * @author rickb
 */
public class CourseDigest extends TitledModel {
    /**
     * Instantiate this digest with default id, title, and description
     */
    public CourseDigest() {
        this(DEFAULT_ID);
    }
    
    /**
     * Instantiate this digest with the given id and empty title and description.
     * 
     * @param id the course's unique id, as determined by the database used to
     *           persist this model.
     */
    public CourseDigest(int id) {
        super(id);
    }
    
    /**
     * Initialize this digest with the given id and title
     * 
     * @param id the course's unique id, as determined by the database used to
     *           persist this model.
     * @param title title of the course
     */
    public CourseDigest(int id, String title) {
        super(id, title);
    }
}
