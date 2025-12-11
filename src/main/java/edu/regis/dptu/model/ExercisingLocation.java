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
 * Specifies the course, unit, task, and step that pedagogically addresses a knowledge component.
 *
 * @see KnowledgeComponent
 * @author rickb
 */
public class ExercisingLocation extends Model {
    private static final Logger log = LoggerFactory.getLogger(ExercisingLocation.class);

    /** The id of the associated course. */
    private int courseId;

    /** The id of the associated. */
    private int unitId;

    /** The id of the associated task. */
    private int taskId;

    /** The associated step id. */
    private int stepId;

    public ExercisingLocation() {
        this(Model.DEFAULT_ID);
        log.debug("ExercisingLocation created with default id={}", Model.DEFAULT_ID);
    }

    public ExercisingLocation(int id) {
        this.id = id;
        log.debug("ExercisingLocation created with id={}", id);
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        log.debug("Setting courseId={} for ExercisingLocation id={}", courseId, id);
        this.courseId = courseId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        log.debug("Setting unitId={} for ExercisingLocation id={}", unitId, id);
        this.unitId = unitId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        log.debug("Setting taskId={} for ExercisingLocation id={}", taskId, id);
        this.taskId = taskId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        log.debug("Setting stepId={} for ExercisingLocation id={}", stepId, id);
        this.stepId = stepId;
    }
}
