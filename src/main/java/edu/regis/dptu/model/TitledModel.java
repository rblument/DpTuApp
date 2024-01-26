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
 * A model with a title and description.
 * 
 * @author rickb
 */
public abstract class TitledModel extends Model {
    /**
     * The title or name of this model, which can be displayed to the user.
     */
    protected String title;
    
    /**
     * A brief description of this model, which can be displayed to the user.
     */
    protected String description;
    
    /**
     * Instantiate this model with default id, title, and description
     */
    public TitledModel() {
        this(DEFAULT_ID);
    }
    
    /**
     * Instantiate this model with the given id
     * 
     * @param id the model's unique id, as determined by the database used to
     *           persist this model.
     */
    public TitledModel(int id) {
        this(id, "");
    }
    
    /**
     * Initialize this model with the given id and title.
     * 
     * @param id the model's unique id, as determined by the database used to
     *           persist this model.
     * @param title a String that can be displayed to the user.
     */
    public TitledModel(int id, String title) {
        super(id);
        
        this.title = title;
        description = "";
    }

    /**
     * Return this model's title.
     * 
     * @return a string that can be displayed to the user.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Assign this model's title
     * 
     * @param title a String that can be displayed to the user.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return this model's description
     * 
     * @return a String that can be displayed to the user 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Assign this model's description.
     * 
     * @param description a String that can be displayed to the user.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
