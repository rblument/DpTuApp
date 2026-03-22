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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.Model;

/**
 * An assessment by the tutor of a knowledge component outcome.
 *
 * @author rickb
 */
public class Assessment extends Model {
    private static final Logger log = LoggerFactory.getLogger(Assessment.class);

    /** The knowledge component assessed in this assessment */
    private KnowledgeComponent outcome;

    /** The student assessment of the outcome being assessed. */
    private AssessmentLevel assessment;

    /**
     * The number of times within a task step the student was exposed to the knowledge component in
     * this assessment.
     */
    private int exposures;

    /**
     * The number of times the student successfully demonstrated knowledge or use of the knowledge
     * component in this assessment.
     */
    private int successess;

    /**
     * The number of hints the student has requested in this knowledge component in this assessment.
     */
    private int hints;

    public Assessment(KnowledgeComponent outcome, AssessmentLevel assessment) {
        this.outcome = outcome;
        this.assessment = assessment;
        log.debug("Assessment created for outcome={} with assessment={}", outcome, assessment);
    }

    public KnowledgeComponent getOutcome() {
        return outcome;
    }

    public void setOutcome(KnowledgeComponent outcome) {
        log.debug("Outcome changed from {} to {}", this.outcome, outcome);
        this.outcome = outcome;
    }

    /**
     * Return the student assessment for this outcome.
     *
     * @return an AssessmentLevel
     */
    public AssessmentLevel getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentLevel assessment) {
        log.debug("Assessment changed from {} to {}", this.assessment, assessment);
        this.assessment = assessment;
    }

    public int getExposures() {
        return exposures;
    }

    public void setExposures(int exposures) {
        log.debug("Exposures set to {} for outcome={}", exposures, outcome);
        this.exposures = exposures;
    }

    /** Increment the number of exposures. */
    public void incrementExposures() {
        exposures++;
        log.debug("Exposures incremented to {} for outcome={}", exposures, outcome);
    }

    public int getSuccessess() {
        return successess;
    }

    public void setSuccessess(int successess) {
        log.debug("Successess set to {} for outcome={}", successess, outcome);
        this.successess = successess;
    }

    public void incrementSuccessess() {
        successess++;
        log.debug("Successess incremented to {} for outcome={}", successess, outcome);
    }

    public int getHints() {
        return hints;
    }

    public void setHints(int hints) {
        log.debug("Hints set to {} for outcome={}", hints, outcome);
        this.hints = hints;
    }

    /** Increment the number of hints. */
    public void incrementHints() {
        hints++;
        log.debug("Hints incremented to {} for outcome={}", hints, outcome);
    }
}
