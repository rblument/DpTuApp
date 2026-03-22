/**
 * Service interface for persisting and querying per-student task completion state
 *
 * <p>This service provides a durable source of truth for student progress across sessions It is
 * used to record when a student completes a task and to retrieve completion data for computing
 * progress bars and dashboard summaries
 *
 * @author Lindsey Cox
 * @date 2/4/2026
 */
package edu.regis.dptu.svc;

import edu.regis.dptu.err.NonRecoverableException;

@SuppressWarnings("Logging")
public interface CompletedTaskSvc {
    /**
     * Persist completion of a task for the given user
     *
     * @param userId the user who completed the task
     * @param taskId the completed task
     * @throws NonRecoverableException if completion cannot be persisted
     */
    void markCompleted(String userId, int taskId) throws NonRecoverableException;

    /**
     * return the total number of completed tasks by the given user
     *
     * @param userId
     * @return the number of completed tasks for hte user
     * @throws NonRecoverableException if count cant be retrieved
     */
    int countCompleted(String userId) throws NonRecoverableException;
}
