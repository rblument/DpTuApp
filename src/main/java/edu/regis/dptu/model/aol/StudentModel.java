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

import edu.regis.dptu.model.Model;
import edu.regis.dptu.model.ScaffoldLevel;
import java.util.HashMap;

/**
 * Captures the current assessment for each learning outcome in a course, as
 * well as, all tutoring sessions in which the student participated.
 *
 * @author rickb
 */
public class StudentModel extends Model {

   /**
    * Convenience reference to the user id (email) of the student associated
    * with this student model.
    */
   private String userId;

   /**
    * The assessments of outcomes for the student associated with this model.
    * The key is the id of the knowledge component in the associated assessment.
    */
   private HashMap<Integer, Assessment> assessments;

   /**
    * The current scaffolding being used to support the student.
    */
   private ScaffoldLevel scaffoldLevel = ScaffoldLevel.EXTREME;

   /**
    * Instantiate this student model with default information.
    */
   public StudentModel() {
      super(DEFAULT_ID);

      assessments = new HashMap<>();
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Retrieves the assessments for the student. 
    * 
    * @return A HashMap<Integer, Assessment> containing the student's
    * assessments.
    */
   public HashMap<Integer, Assessment> getAssessments() {
      return this.assessments;
   }

   public void addAssessment(int knowledgeComponentId, Assessment assessment) {
      assessments.put(knowledgeComponentId, assessment);
   }

   /**
    * Return whether this student has an assessment for the given outcome.
    *
    * @param outcome
    * @return true if the student has an assessment for the given outcome.
    */
   public boolean containsAssessment(int knowledgeComponentId) {
      return assessments.containsKey(knowledgeComponentId);
   }

   /**
    * Return the student assessment, if any, for the given outcome.
    *
    * @param outcome the Outcome being accessed
    *
    * @return an Assessment of the student.
    */
   public Assessment findAssessment(int knowledgeComponentId) {
      return assessments.get(knowledgeComponentId);
   }

   /**
    * Return the current scaffolding level being used to support the student.
    *
    * @return
    */
   public ScaffoldLevel getScaffoldLevel() {
      return scaffoldLevel;
   }

   public void setScaffoldLevel(ScaffoldLevel scaffoldLevel) {
      this.scaffoldLevel = scaffoldLevel;
   }
}
