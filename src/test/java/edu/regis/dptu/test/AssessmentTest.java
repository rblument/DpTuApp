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

import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.aol.Assessment;
import edu.regis.dptu.model.aol.AssessmentLevel;

public class AssessmentTest {

    @Test
    public void testAssessmentMutatorsAndCounters() {
        KnowledgeComponent outcome = new KnowledgeComponent(101);
        Assessment assessment = new Assessment(outcome, AssessmentLevel.LOW);

        assessment.incrementExposures();
        assessment.incrementSuccessess();
        assessment.incrementHints();
        assessment.setAssessment(AssessmentLevel.MEDIUM);

        assertEquals(outcome, assessment.getOutcome());
        assertEquals(AssessmentLevel.MEDIUM, assessment.getAssessment());
        assertEquals(1, assessment.getExposures());
        assertEquals(1, assessment.getSuccessess());
        assertEquals(1, assessment.getHints());
    }
}
