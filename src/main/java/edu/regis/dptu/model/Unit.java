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
 * A semantically cohesive collection of tasks within a course that a student
 * is expected to master before moving to the next unit using Mastery Learning
 * per VanLehn (2006).
 * 
 * @author rickb
 */
public class Unit extends TitledModel { 
    /**
     * A summary of this unit (title and description).
     */
    private UnitDigest digest;
    
    /**
     * The pedagogical approach used to select the next task within this unit.
     */
    private TaskSelectionKind pedagogy;
    
    /**
     * The sequence order in which this unit appears in its parent course or
     * unit.
     */
    private int sequenceId = DEFAULT_ID;
    
    private ArrayList<Task> tasks;
    
    /**
     * Instantiate this unit with default information
     */
    public Unit() {
        this(DEFAULT_ID);
    }
    
    /**
     * Instantiate this unit with the given id.
     * 
     * @param id a unique id, as determined by the database used to
     *           persist this unit.
     */
    public Unit(int id) {
        super(id);
        
        digest = new UnitDigest();
        pedagogy = TaskSelectionKind.FIXED_SEQUENCE;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public UnitDigest getDigest() {
        UnitDigest digest = new UnitDigest(id);
        
        digest.setTitle(title);
        digest.setDescription(description);
        
        return digest;
    }
    
    public TaskSelectionKind getPedagogy() {
        return pedagogy;
    }

    public void setPedagogy(TaskSelectionKind pedagogy) {
        this.pedagogy = pedagogy;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    /**
     * Return the task, if any, with the given sequence id.
     * 
     * @param sequence the sequence position of the task to find.
     * @return a Task, or null if not found.
     */
    public Task findTaskBySequence(int sequence) {
        for (Task task : tasks)
            if (task.getSequenceId() == sequence)
                return task;
        
        return null;
    }
}

