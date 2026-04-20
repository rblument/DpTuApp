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

import edu.regis.dptu.model.Mode;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.TutoringSession;

final class SessionResumeMatcher {

    private SessionResumeMatcher() {}

    static boolean canResumeSavedSession(
            TutoringSession signedInSession, Mode expectedMode, Problem selectedProblem) {
        if (signedInSession == null || expectedMode == null || selectedProblem == null) {
            return false;
        }

        if (signedInSession.getMode() != expectedMode) {
            return false;
        }

        Problem savedProblem = signedInSession.getProblem();
        if (savedProblem == null) {
            return false;
        }

        // Require exact problem identity to avoid resuming the wrong problem of the same kind.
        if (savedProblem.getId() != selectedProblem.getId()) {
            return false;
        }

        if (savedProblem.getType() != selectedProblem.getType()) {
            return false;
        }

        return signedInSession.getTasks() != null && !signedInSession.getTasks().isEmpty();
    }
}
