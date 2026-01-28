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
 * A suggestion as to the next Step the Student should perform in the tutor.
 *
 * @author rickb
 */
public class Hint extends Model {
    private static final Logger log = LoggerFactory.getLogger(Hint.class);

    // Todo: level? Scaffolding??

    /** The hint string, which can be displayed to the student user. */
    private String text = "";

    /** The order in which this hint should be displayed (when multiple hints are available). */
    private int sequenceIndex = 1;

    /** Initialize this hint with a default id. */
    public Hint() {
        this(DEFAULT_ID);
        log.debug("Hint(): created with default ID {}", DEFAULT_ID);
    }

    /**
     * Initialize this hint with the given database id.
     *
     * @param id int database id of this hint.
     */
    public Hint(int id) {
        super(id);
        log.debug("Hint(id={}): created", id);
    }

    public String getText() {
        log.debug("getText(): {}", text);
        return text;
    }

    public void setText(String text) {
        log.debug("setText(): changing from '{}' to '{}'", this.text, text);
        this.text = text;
    }

    public int getSequenceIndex() {
        log.debug("getSequenceIndex(): {}", sequenceIndex);
        return sequenceIndex;
    }

    public void setSequenceIndex(int sequenceIndex) {
        log.debug("setSequenceIndex(): changing from {} to {}", this.sequenceIndex, sequenceIndex);
        this.sequenceIndex = sequenceIndex;
    }
}
