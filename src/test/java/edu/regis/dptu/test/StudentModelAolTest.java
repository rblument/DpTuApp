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

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.model.aol.Assessment;
import edu.regis.dptu.model.aol.AssessmentLevel;
import edu.regis.dptu.model.aol.StudentModel;

public class StudentModelAolTest {

    @Test
    public void testStudentModelAssessmentAndSessionFlow() {
        StudentModel studentModel = new StudentModel("student@regis.edu");

        KnowledgeComponent outcome = new KnowledgeComponent(55);
        Assessment assessment = new Assessment(outcome, AssessmentLevel.HIGH);
        studentModel.addAssessment(assessment);

        assertTrue(studentModel.containsAssessment(outcome.getId()));
        assertSame(assessment, studentModel.findAssessment(outcome.getId()));

        studentModel.addSession(new TutoringSession("student@regis.edu"));
        assertEquals(1, studentModel.getSessions().size());

        studentModel.setScaffoldLevel(ScaffoldLevel.HIGH);
        assertEquals(ScaffoldLevel.HIGH, studentModel.getScaffoldLevel());

        studentModel.setUserId("student2@regis.edu");
        assertEquals("student2@regis.edu", studentModel.getUserId());

        ArrayList<TutoringSession> sessions = new ArrayList<>();
        sessions.add(new TutoringSession("student2@regis.edu"));
        studentModel.setSessions(sessions);
        assertEquals(1, studentModel.getSessions().size());
    }
}
