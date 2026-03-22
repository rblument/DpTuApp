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

package edu.regis.dptu.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.LCSProblem;
import edu.regis.dptu.model.Problem;
import edu.regis.dptu.model.ProblemListener;

/**
 * A simple standalone test class for verifying LCSProblem step execution. Run this class to test if
 * the step(), undo(), and reset() methods are working correctly.
 *
 * @author [your name]
 */
public class TestStepExecution implements ProblemListener {
    private static final Logger log = LoggerFactory.getLogger(TestStepExecution.class);

    /** Main entry point for the test */
    public static void main(String[] args) {
        new TestStepExecution().runTest();
    }

    /** Run a simple test of LCSProblem step execution */
    public void runTest() {
        log.info("=== Starting LCSProblem Step Execution Test ===");

        // Create an LCSProblem instance with test data
        LCSProblem problem = new LCSProblem("abc", "abd");
        log.debug("Created LCSProblem with sequences: '{}' and '{}'", "abc", "abd");

        // Register as a listener
        problem.addProblemListener(this);
        log.debug("Registered TestStepExecution as ProblemListener");

        log.info("--- Testing step() ---");
        log.debug("Initial next line number: {}", problem.getNextLineNumber());

        // Test stepping forward
        for (int i = 0; i < 5; i++) {
            log.debug("Executing step {}", i + 1);
            problem.step();
            log.debug("After step {}: current line number {}", i + 1, problem.getNextLineNumber());
        }

        log.info("--- Testing undo() ---");

        // Test undoing
        for (int i = 0; i < 3; i++) {
            log.debug("Executing undo {}", i + 1);
            problem.undo();
            log.debug("After undo {}: current line number {}", i + 1, problem.getNextLineNumber());
        }

        // Test resetting
        log.info("--- Testing reset() ---");
        problem.reset();
        log.debug("After reset: current line number {}", problem.getNextLineNumber());

        // Test multiple steps
        log.info("--- Testing multiple steps ---");
        problem.step(3);
        log.debug("After 3 steps: current line number {}", problem.getNextLineNumber());

        log.info("=== Test Complete ===");
    }

    @Override
    public void problemUpdated(Problem problem) {
        log.debug(
                "Problem updated notification received. Current line number: {}",
                problem.getNextLineNumber());
    }
}
