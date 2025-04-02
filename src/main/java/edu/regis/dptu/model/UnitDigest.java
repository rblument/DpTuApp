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
 * A descriptive summary of a unit as used in a tutoring session, which 
 * eliminates the need to return an entire unit to the GUI.
 * 
 * @author rickb
 */
public class UnitDigest extends TitledModel {
    /**
     * The courseId to which the unit belongs.
     */
    private int courseId;

    /**
     * The pedagogical approach initially used to start task selection.
     */
    private TaskSelectionKind pedagogy;

    /**
     * The index of the unit in the course.
     */
    private int sequenceIndex;

    /**
     * Instantiate this digest with default id, title, and description
     */
    public UnitDigest() {
        this(DEFAULT_ID);
    }
    
    /**
     * Instantiate this digest with the given id and empty title and description.
     * 
     * @param id the digest's unique id, as determined by the database used to
     *           persist this model.
     */
    public UnitDigest(int id) {
        super(id);
    }

    public int getCourseId() {
        return courseId;
    }

    public TaskSelectionKind getPedagogy() {
        return pedagogy;
    }

    public int getSequenceIndex() {
        return sequenceIndex;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setPedagogy(TaskSelectionKind pedagogy) {
        this.pedagogy = pedagogy;
    }

    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }
}
