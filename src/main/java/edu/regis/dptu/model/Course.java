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

import edu.regis.dptu.err.ObjNotFoundException;
import java.util.ArrayList;

/**
 * A course that may be taught by the ShaTu tutor.
 * 
 * @author rickb
 */
public class Course extends TitledModel {  
    /**
     * The primary pedagogical approach initially used to start task selection.
     * 
     * A value of FIXED_SEQUENCE or MASTERY_LEARNING will begin with the
     * first unit sequenced in this course.
     */
    private TaskSelectionKind primaryPedagogy;
    
    /**
     * If non-empty, mastery learning is used in this course to assign tasks
     * from the pool of tasks in these units, which must be completed in order.
     * 
     * The first unit in this list is the current unit.
     */
    private ArrayList<Unit> units;
    
    /**
     * The knowledge component learning outcomes associated with this course.
     */
    protected ArrayList<KnowledgeComponent> outcomes;
    
    /**
     * Initialize this course with a default id, empty units, and outcomes
     */
    public Course() {
        this(DEFAULT_ID);
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
    }

    public TaskSelectionKind getPrimaryPedagogy() {
        return primaryPedagogy;
    }

    public void setPrimaryPedagogy(TaskSelectionKind primaryPedagogy) {
        this.primaryPedagogy = primaryPedagogy;
    }

    public CourseDigest getDigest() {
        CourseDigest digest = new CourseDigest(id, title);
        
        digest.setDescription(description);
        
        return digest;
    }
    
    /**
     * Return the current unit, if any.
     * 
     * It's possible that there are no units since PedagogyKind.MICORADAPTATION
     * may be used to select the next task for the student to complete.
     * 
     * @return the current unit, or null.
     */
    public Unit currentUnit() {
        return units.get(0); // first unit is the current unit.
    }
    
    public void addUnit(Unit module) {
        units.add(module);
    }
    
    public Unit findUnit(int id) throws ObjNotFoundException {
        for (Unit unit : units)
            if (unit.getId() == id)
                return unit;
        
        throw new ObjNotFoundException(String.valueOf(id));
    }
    
    /**
     * Return the unit with the given sequence id.
     * 
     * @param sequenceId the sequence position of the unit to find
     * @return a Unit, or null if no Unit was found.
     */
    public Unit findUnitBySequenceId(int sequenceId) {
        for (Unit unit : units)
            if (unit.getSequenceId() == sequenceId)
                return unit;
                
        return null;
    }
    
    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }
    
    public void addOutcome(KnowledgeComponent outcome) {
        outcomes.add(outcome);
    }

    public ArrayList<KnowledgeComponent> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<KnowledgeComponent> outcomes) {
        this.outcomes = outcomes;
    }
    
    /**
     * Return the knowledge component, if any, in this entity with the given
     * component id.
     * 
     * @param componentId the unique id of a knowledge component
     * @throws ObjNotFoundException no outcome with the given id exists in this course.
     * @return KnowledgeComponent
     */
    public KnowledgeComponent findKnowledgeComponent(int componentId) throws ObjNotFoundException {
        for (KnowledgeComponent outcome : outcomes)
            if (outcome.getId() == componentId)
                return outcome;
    
        throw new ObjNotFoundException("KnowledgeComponent: " + componentId);
    }
}

