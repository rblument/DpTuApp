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
package edu.regis.dptu.model.aol;

/**
 * The types of assessments for an Outcome or Knowledge Component.
 * 
 * @author rickb
 */
public enum AssessmentLevel {
    /**
     * Indicates a student hasn't been exposed to an Outcome.
     */
    VERY_LOW("Very Low"),
    
    /**
     * Indicates a student has been exposed to an Outcome, but hasn't 
     * demonstrated any knowledge of it.
     */
    LOW("Low"),
    
    /**
     * Indicates a student has demonstrated appropriate knowledge of an outcome
     * one time.
     */
    MEDIUM("Medium"),
    
    /**
     * Indicates a student has demonstrated appropriate knowledge of an outcome
     * multiple times.
     */
    HIGH("High"),
    
    /**
     * Indicates a student has mastered appropriate knowledge of an outcome.
     */
    VERY_HIGH("Very High"),
    
    /**
     * Indicates a student is in the midst of completing an Outcome.
     */
    IN_PROGRESS("In Progress"),
    
    /**
     * Indicates a student has completed an Outcome.
     */
    COMPLETED("Completed");
    
    /**
     * A GUI displayable string identifying this assessment level.
     */
    private final String title;
    
    AssessmentLevel(String title) {
        this.title = title;
    }
    
    public String title() {
        return title;
    }
}
