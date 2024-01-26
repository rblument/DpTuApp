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

/**
 * A user interface gesture/event performed by a student, as part of a Task.
 *
 * Steps in tasks are based on tutoring behaviors in (VanLehn, 2006).
 * 
 * As a Step appears within actions/expectations created by an agent,
 * all fields are final so that other malicious agents cannot change them.
 * 
 * @author rickb
 */
public class Step extends TitledModel {
    /**
     * The scaffold level associated with this step.
     */
    private ScaffoldLevel scaffolding;
    
    /**
     * The type of this step, which determines the object specified in this
     * step's data along with a potential cont
     */
    private StepSubType subType;
    /**
     * Which step this is in the parent task.
     */
    protected int sequenceId = 1;
    
    /**
     * The hint currently available to the student (index into hints).
     */
    protected int currentHintIndex = 0;
    
    /**
     * The hints, if any, associated with this step (in order to be given).
     */
    protected final ArrayList<Hint> hints;
    
    /**
     * The amount of seconds the student can take on this step before the GUI
     * prompts the student concerning their inaction.
     */
    protected Timeout timeout;
    
    /**
     * If true, the GUI immediately notifies the tutor when the student performs
     * this step.
     */
    protected boolean notifyTutor;
    
    /**
     * True, if the student has completed this step, otherwise false.
     * 
     * Note, if this is true and notifyTutor is true, the tutor has been
     * notified that the student completed this step. Otherwise, the tutor
     * will be notified when the student completes the parent task associated
     * with this step.
     */
    protected boolean isCompleted;
    
    /**
     * The knowledge component outcomes demonstrated/exercised by this step.
     */
    protected ArrayList<Integer> exercisedComponentIds;
    
    /**
     * A JSon encoded object specified by out step subtype, which provides the
     * contextual environment (information) related to this step.
     */
    private String data;
    
    /**
     * Instantiate this step with the given information.
     * 
     * @param id
     * @param sequenceId
     * @param subType
     */
    public Step(int id,int sequenceId, StepSubType subType) {
        super(id);
        
        this.sequenceId = sequenceId;

        timeout = null;

        exercisedComponentIds = new ArrayList<>();
        
        hints = new ArrayList<>();
           
        isCompleted = false;
        
        this.subType = subType;
        
    }

    public StepSubType getSubType() {
        return subType;
    }

    public void setSubType(StepSubType subType) {
        this.subType = subType;
    }

    public ScaffoldLevel getScaffolding() {
        return scaffolding;
    }

    public void setScaffolding(ScaffoldLevel scaffolding) {
        this.scaffolding = scaffolding;
    }

    public ArrayList<Hint> getHints() {
        return hints;
    }
    
    public Hint findHintById(int id) {
	for (int i = 0; i < hints.size(); i++)
	    if (hints.get(i).getId() == id)
		return hints.get(i);

	return null;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public Timeout getTimeout() {
        return timeout;
    }
    
    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }
    
    public void addHint(Hint hint) {
        System.out.println("Adding hint");
        hints.add(hint);
    }
    
    public Hint getCurrentHint() {
        return hints.get(currentHintIndex);
    }

    public int getCurrentHintIndex() {
        return currentHintIndex;
    }

    public void setCurrentHintIndex(int currentHint) {
        this.currentHintIndex = currentHint;
    }

    public boolean isNotifyTutor() {
        return notifyTutor;
    }

    public void setNotifyTutor(boolean notifyTutor) {
        this.notifyTutor = notifyTutor;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public void addExercisedComponentId(int componentId) {
        exercisedComponentIds.add(componentId);
    }
    
    public ArrayList<Integer> getExercisedComponentIds() {
        return exercisedComponentIds;
    }

    public void setExercisedComponentIds(ArrayList<Integer> componentIds) {
        this.exercisedComponentIds = componentIds;
    }
    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

