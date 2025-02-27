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

import edu.regis.dptu.model.aol.OutcomeGranularity;
import java.util.ArrayList;

/**
 * Per VanLehn (2006), a component of knowledge (concept, rule, procedure, 
 * fact) possessed by a student representing a fragment of task-specific 
 * information.
 * 
 * Knowledge components represent the Outcomes in the ShaTu tutor.
 * 
 * @author rickb
 */
public class KnowledgeComponent extends TitledModel {
    /**
     * The Bloom Level associated with this outcome.
     */
    protected BloomLevel bloomLevel;
    
    /**
     * The granularity of this outcome.
     */
    protected OutcomeGranularity granularity;
    
    /**
     * The pedagogy approach associated with this knowledge component outcome.
     * 
     * For Macroadaptation, the exercising locations must be used to select
     * tasks that addresses this component. They can also be used for OTHER,
     * but with this approach, the component was addressed in a task that is
     * part of the fixed sequenced, for example, of a unit.
     */
    protected TaskSelectionKind pedagogy;
    
    /**
     * If true, this outcome represents a knowledge component associated with
     * the SHA-256 application and its underlying concepts, otherwise, it
     * represents knowledge related to using the tutor/GUI itself.
     */
    private boolean isDomainFocus;
    
    /**
     * The tasks and steps that demonstrate/exercise this knowledge component.
     */
    private ArrayList<ExercisingLocation> exercisingLocations;
   
    /**
     * Initialize this outcome with the given id.
     * 
     * @param id an int id (should be generated by the database).
     */
    public KnowledgeComponent(int id) {
        super(id);
        
        exercisingLocations = new ArrayList<>();
    }

    /**
     * Return the bloom level of this outcome.
     * 
     * @return a BloomLevel
     */
    public BloomLevel getBloomLevel() {
        return bloomLevel;
    }
    
    /**
     * Set the bloom level with the given bloomLevel.
     * 
     * @param bloomLevel an BloomLevel data type.
     */
    public void setBloomLevel(BloomLevel bloomLevel) {
        this.bloomLevel = bloomLevel;
    }

    /**
     * Return the granularity of this outcome.
     * 
     * @return a Granularity
     */
    public OutcomeGranularity getGranularity() {
        return granularity;
    }

    /**
     * Set the Granularity with the given granularity.
     * 
     * @param granularity an OutcomeGranularity data type.
     */
    public void setGranularity(OutcomeGranularity granularity) {
        this.granularity = granularity;
    }

    /**
     * Return the pedagogy of this outcome.
     * 
     * @return a Pedagogy
     */
    public TaskSelectionKind getPedagogy() {
        return pedagogy;
    }

    /**
     * Set the pedagogy with the given pedagogy.
     * 
     * @param pegadody an TaskSelectionKind data type.
     */
    public void setPedagogy(TaskSelectionKind pedagogy) {
        this.pedagogy = pedagogy;
    }

    /**
     * Return the true or false of whether domain focus.
     * 
     * @return a isDomainFocus
     */
    public boolean isDomainFocus() {
        return isDomainFocus;
    }
    
    /**
     * Return the domain focus of this outcome.
     * 
     * @return a DomainFocus
     */
    public boolean getIsDomainFocus() {
        return isDomainFocus();
    }

    /**
     * Set the domain focus with the given isDomainFocus.
     * 
     * @param isDomainFocus a boolean isDomainFocus data type.
     */
    public void setIsDomainFocus(boolean isDomainFocus) {
        this.isDomainFocus = isDomainFocus;
    }
    
    /**
     * Add the location to the exersisingLocations arrayList.
     * 
     * @param location a ExercisingLocation data type.
     */
    public void addExercisingLocation(ExercisingLocation location) {
        exercisingLocations.add(location);
    }

    /**
     * Return the exercisingLocations of this outcome.
     * 
     * @return a ArrayList that holds ExistingLocation
     */
    public ArrayList<ExercisingLocation> getExercisingLocations() {
        return exercisingLocations;
    }
    
    /**
     * Set the exercising locations with the given exercisingLocations.
     * 
     * @param exercisingLocations an ArrayList data type that holds exercising location.
     */
    public void setExercisingLocations(ArrayList<ExercisingLocation> exercisingLocations) {
        this.exercisingLocations = exercisingLocations;
    }
}