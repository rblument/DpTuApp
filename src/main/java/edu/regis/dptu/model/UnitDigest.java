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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A descriptive summary of a unit as used in a tutoring session, which eliminates the need to
 * return an entire unit to the GUI.
 *
 * @author rickb
 */
public class UnitDigest extends TitledModel {
    private static final Logger log = LoggerFactory.getLogger(UnitDigest.class);

    /** The courseId to which the unit belongs. */
    private int courseId;

    /** The pedagogical approach initially used to start task selection. */
    private TaskSelectionKind pedagogy;

    /** The index of the unit in the course. */
    private int sequenceIndex;

    /** Instantiate this digest with default id, title, and description */
    public UnitDigest() {
        this(DEFAULT_ID);
        log.debug("UnitDigest() default constructor called, id={}", DEFAULT_ID);
    }

    /**
     * Instantiate this digest with the given id and empty title and description.
     *
     * @param id the digest's unique id, as determined by the database used to persist this model.
     */
    public UnitDigest(int id) {
        super(id);
        log.debug("UnitDigest(int id) constructor called, id={}", id);
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
        log.debug("setCourseId: changing courseId from {} to {}", this.courseId, courseId);
        this.courseId = courseId;
    }

    public void setPedagogy(TaskSelectionKind pedagogy) {
        log.debug("setPedagogy: changing pedagogy from {} to {}", this.pedagogy, pedagogy);
        this.pedagogy = pedagogy;
    }

    public void setSequenceIndex(int sequenceIndex) {
        log.debug("setSequenceIndex: changing sequenceIndex from {} to {}", this.sequenceIndex, sequenceIndex);
        this.sequenceIndex = sequenceIndex;
    }
}
