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
package edu.regis.dptu.view.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.TutoringSession;

final class SessionResumeMatcher {

    private static final Logger log = LoggerFactory.getLogger(SessionResumeMatcher.class);

    private SessionResumeMatcher() {}

    static boolean canResumeSavedSession(
            TutoringSession signedInSession, Mode expectedMode, Problem selectedProblem) {
        if (signedInSession == null || expectedMode == null || selectedProblem == null) {
            log.debug("Resume denied: null session/mode/problem input");
            return false;
        }

        if (signedInSession.getMode() != expectedMode) {
            log.debug(
                    "Resume denied: mode mismatch expected={} actual={}",
                    expectedMode,
                    signedInSession.getMode());
            return false;
        }

        Problem savedProblem = signedInSession.getProblem();
        if (savedProblem == null) {
            log.debug("Resume denied: signed-in session has no problem");
            return false;
        }

        // Require exact problem identity to avoid resuming the wrong problem of the same kind.
        if (savedProblem.getId() != selectedProblem.getId()) {
            log.debug(
                    "Resume denied: problem id mismatch selected={} saved={}",
                    selectedProblem.getId(),
                    savedProblem.getId());
            return false;
        }

        if (savedProblem.getType() != selectedProblem.getType()) {
            log.debug(
                    "Resume denied: problem type mismatch selected={} saved={}",
                    selectedProblem.getType(),
                    savedProblem.getType());
            return false;
        }

        boolean hasPendingProgress =
                signedInSession.getTasks() != null && !signedInSession.getTasks().isEmpty();
        if (!hasPendingProgress) {
            log.debug("Resume denied: no pending tasks in signed-in session");
        }
        return hasPendingProgress;
    }
}
