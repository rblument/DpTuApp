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
 * Specifies the course, unit, task, and step that pedagogically addresses a
 * knowledge component.
 * 
 * @see KnowledgeComponent
 * 
 * @author rickb
 */
public class ExercisingLocation extends Model {
    /**
     * The id of the associated course.
     */
    private int courseId;
    
    /**
     * The id of the associated. 
     */
    private int unitId;
    
    /**
     * The id of the associated task.
     */
    private int taskId;
    
    /**
     * The associated step id.
     */
    private int stepId;
    
    
    public ExercisingLocation() {
        this(Model.DEFAULT_ID);
    }
    
    public ExercisingLocation(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
}
