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

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.ObjNotFoundException;

/**
 * A course that may be taught by the ShaTu tutor.
 *
 * @author rickb
 */
public class Course extends TitledModel {
    private static final Logger log = LoggerFactory.getLogger(Course.class);

    /**
     * The primary pedagogical approach initially used to start task selection.
     *
     * <p>A value of FIXED_SEQUENCE or MASTERY_LEARNING will begin with the first unit sequenced in
     * this course.
     */
    private TaskSelectionKind primaryPedagogy;

    /**
     * If non-empty, mastery learning is used in this course to assign tasks from the pool of tasks
     * in these units, which must be completed in order.
     *
     * <p>The first unit in this list is the current unit.
     */
    private ArrayList<Unit> units;

    /** The knowledge component learning outcomes associated with this course. */
    protected ArrayList<KnowledgeComponent> outcomes;

    /**
     * A convenience list of mappings between outcomes and the locations (tasks and step) within
     * this course where these outcomes are addressed.
     */
    protected ArrayList<ExercisingLocation> exercisingLocations;

    /** Initialize this course with a default id, empty units, and outcomes */
    public Course() {
        this(DEFAULT_ID);
        log.debug("Course created with default id");
    }

    /**
     * Initialize this course with the given id, empty units, and outcomes.
     *
     * @param id an int specifying the unique database id of this course.
     */
    public Course(int id) {
        super(id);

        units = new ArrayList<>();
        outcomes = new ArrayList<>();
        log.debug("Course created with id={}", id);
    }

    public TaskSelectionKind getPrimaryPedagogy() {
        log.debug("getPrimaryPedagogy called, returning {}", primaryPedagogy);
        return primaryPedagogy;
    }

    public void setPrimaryPedagogy(TaskSelectionKind primaryPedagogy) {
        log.debug("Primary pedagogy set from {} to {}", this.primaryPedagogy, primaryPedagogy);
        this.primaryPedagogy = primaryPedagogy;
    }

    public CourseDigest getDigest() {
        log.debug("Creating CourseDigest for course id={}", id);
        CourseDigest digest = new CourseDigest(id, title);

        digest.setPrimaryPedagogy(primaryPedagogy);
        digest.setDescription(description);

        return digest;
    }

    /**
     * Return the current unit, if any.
     *
     * <p>It's possible that there are no units since PedagogyKind.MICORADAPTATION may be used to
     * select the next task for the student to complete.
     *
     * @return the current unit, or null.
     */
    public Unit currentUnit() {
        if (units.isEmpty()) {
            log.warn("currentUnit called but no units exist in course id={}", id);
            return null;
        }
        log.debug("Returning current unit id={} for course id={}", units.get(0).getId(), id);
        return units.get(0);
    }

    public void addUnit(Unit module) {
        units.add(module);
        log.debug("Added unit id={} to course id={}", module.getId(), id);
    }

    public Unit findUnit(int id) throws ObjNotFoundException {
        for (Unit unit : units) {
            if (unit.getId() == id) {
                log.debug("Unit id={} found in course id={}", id, this.id);
                return unit;
            }
        }
        log.error("Unit id={} not found in course id={}", id, this.id);
        throw new ObjNotFoundException(String.valueOf(id));
    }

    /**
     * Return the unit with the given sequence id.
     *
     * @param sequenceId the sequence position of the unit to find
     * @return a Unit, or null if no Unit was found.
     */
    public Unit findUnitBySequenceId(int sequenceId) {
        for (Unit unit : units) {
            if (unit.getSequenceId() == sequenceId) {
                log.debug("Unit with sequenceId={} found in course id={}", sequenceId, id);
                return unit;
            }
        }
        log.warn("Unit with sequenceId={} not found in course id={}", sequenceId, id);
        return null;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        log.debug("Setting units for course id={}, count={}", id, units.size());
        this.units = units;
    }

    public void addOutcome(KnowledgeComponent outcome) {
        outcomes.add(outcome);
        log.debug("Added KnowledgeComponent id={} to course id={}", outcome.getId(), id);
    }

    public ArrayList<KnowledgeComponent> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<KnowledgeComponent> outcomes) {
        log.debug("Setting outcomes for course id={}, count={}", id, outcomes.size());
        this.outcomes = outcomes;
    }

    public ArrayList<ExercisingLocation> getExercisingLocations() {
        return exercisingLocations;
    }

    public void setExercisingLocations(ArrayList<ExercisingLocation> exercisingLocations) {
        log.debug(
                "Setting exercisingLocations for course id={}, count={}",
                id,
                exercisingLocations.size());
        this.exercisingLocations = exercisingLocations;
    }

    /**
     * Return the knowledge component, if any, in this entity with the given component id.
     *
     * @param componentId the unique id of a knowledge component
     * @throws ObjNotFoundException no outcome with the given id exists in this course.
     * @return KnowledgeComponent
     */
    public KnowledgeComponent findKnowledgeComponent(int componentId) throws ObjNotFoundException {
        for (KnowledgeComponent outcome : outcomes) {
            if (outcome.getId() == componentId) {
                log.debug("KnowledgeComponent id={} found in course id={}", componentId, id);
                return outcome;
            }
        }
        log.error("KnowledgeComponent id={} not found in course id={}", componentId, id);
        throw new ObjNotFoundException("KnowledgeComponent: " + componentId);
    }

    /**
     * Return the exercising location, if any, in this course with the given id.
     *
     * @param id the id of an exercising location
     * @return an ExercisingLocation or null
     */
    public ExercisingLocation findLocation(int id) {
        for (ExercisingLocation location : exercisingLocations) {
            if (location.getId() == id) {
                log.debug("ExercisingLocation id={} found in course id={}", id, this.id);
                return location;
            }
        }
        log.warn("ExercisingLocation id={} not found in course id={}", id, this.id);
        return null;
    }
}
