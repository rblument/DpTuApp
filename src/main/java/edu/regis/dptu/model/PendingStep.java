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

import edu.regis.dptu.util.ResourceMgr;

/**
 * The current step within a pending task that a student needs to complete as part of the current
 * tutoring session.
 *
 * <p>A pending step represents the dynamic information associated with a student completing a step,
 * while the associated step is a static step within a course.
 *
 * @author rickb
 */
public class PendingStep {
    private static final Logger log = LoggerFactory.getLogger(PendingStep.class);

    /** The auto-generated database id for this pending step. */
    private int id;

    /** The course step associated with this pending step. */
    private Step step;

    private int currentHintIndex;

    /** If true, the GUI immediately notifies the tutor when the student performs this step. */
    protected boolean notifyTutor;

    /**
     * True, if the student has completed this step, otherwise false.
     *
     * <p>Note, if this is true and notifyTutor is true, the tutor has been notified that the
     * student completed this step. Otherwise, the tutor will be notified when the student completes
     * the parent task associated with this step.
     */
    protected boolean isCompleted;

    public PendingStep(Step step) {
        this(Model.DEFAULT_ID, step);
        log.debug("PendingStep created with default ID {} for step {}", Model.DEFAULT_ID, step);
    }

    public PendingStep(int id, Step step) {
        this.id = id;
        this.step = step;

        isCompleted = false;
        log.debug("PendingStep created with ID {} for step {}", id, step);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        log.debug("PendingStep ID changed from {} to {}", this.id, id);
        this.id = id;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        log.debug("PendingStep step changed from {} to {}", this.step, step);
        this.step = step;
    }

    public int getCurrentHintIndex() {
        return currentHintIndex;
    }

    public void setCurrentHintIndex(int currentHintIndex) {
        log.debug("PendingStep currentHintIndex set to {}", currentHintIndex);
        this.currentHintIndex = currentHintIndex;
    }

    public Hint getCurrentHint() {
        ArrayList<Hint> hints = step.getHints();

        if (hints.isEmpty()) {
            Hint noHint = new Hint();
            noHint.setText(ResourceMgr.instance().string("hint.noneAvailable"));
            log.debug("PendingStep {} has no hints available", id);
            return noHint;
        }

        // Reset the index to 0 if it exceeds the list size
        if (currentHintIndex >= hints.size()) {
            log.debug(
                    "PendingStep {} currentHintIndex {} exceeds hint list size {}. Resetting to 0",
                    id,
                    currentHintIndex,
                    hints.size());
            currentHintIndex = 0;
        }

        Hint hint = hints.get(currentHintIndex);
        log.debug(
                "PendingStep {} returning hint at index {}: {}",
                id,
                currentHintIndex,
                hint.getText());
        return hint;
    }

    public boolean isNotifyTutor() {
        return notifyTutor;
    }

    public void setNotifyTutor(boolean notifyTutor) {
        log.debug("PendingStep {} notifyTutor set to {}", id, notifyTutor);
        this.notifyTutor = notifyTutor;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        log.debug("PendingStep {} isCompleted set to {}", id, isCompleted);
        this.isCompleted = isCompleted;
    }
}
