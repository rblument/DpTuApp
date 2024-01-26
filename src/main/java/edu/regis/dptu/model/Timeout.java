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
 * A timeout starts a clock ticking and performs the associated event, if
 * the expected action, task, or step containing the timeout isn't performed.
 * 
 * As a Timeout may appear as an action or expectation created by an agent,
 * all fields are final so that other malicious agents cannot change them.
 * 
 * @author rickb
 */
public class Timeout {
     /**
     * The type of action that initialized this timeout.
     */
    protected final String type;
    
    /**
     * The number of seconds before the event in this timeout is triggered.
     */
    protected final int seconds;
    
    /**
     * The type of event associated with this timeout
     */
    protected final String event;
    
    /**
     * Text that can be displayed to the user that describes this event and
     * what they need to do, if anything.
     */
    protected final String text;
    
    /**
     * Initialize this timeout with the given information.
     * 
     * @param type the type of tutoring action this timeout is associated
     * @param seconds the number of seconds before the timeout triggers
     * @param event the event that is performed when this timeout triggers
     * @param text the text, if any, displayed to the student when this
     *             timeout triggers
     */
    public Timeout(String type, int seconds, String event, String text) {
        this.type = type;
        this.seconds = seconds;
        this.event = event;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getEvent() {
        return event;
    }

    public String getText() {
        return text;
    }
}
