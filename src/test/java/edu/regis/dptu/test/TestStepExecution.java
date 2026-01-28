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
        System.out.println("=== Starting LCSProblem Step Execution Test ===");

        // Create an LCSProblem instance with test data
        LCSProblem problem = new LCSProblem("abc", "abd");
        log.debug("Created LCSProblem with sequences: '{}' and '{}'", "abc", "abd");

        // Register as a listener
        problem.addProblemListener(this);
        log.debug("Registered TestStepExecution as ProblemListener");

        System.out.println("\n--- Testing step() ---");
        log.info("--- Testing step() ---");
        System.out.println("Initial state: line " + problem.getNextLineNumber());
        log.debug("Initial next line number: {}", problem.getNextLineNumber());

        // Test stepping forward
        for (int i = 0; i < 5; i++) {
            System.out.println("\nExecuting step " + (i + 1));
            log.debug("Executing step {}", i + 1);
            problem.step();
            System.out.println("Current line: " + problem.getNextLineNumber());
            log.debug("After step {}: current line number {}", i + 1, problem.getNextLineNumber());
        }

        System.out.println("\n--- Testing undo() ---");
        log.info("--- Testing undo() ---");

        // Test undoing
        for (int i = 0; i < 3; i++) {
            System.out.println("\nExecuting undo " + (i + 1));
            log.debug("Executing undo {}", i + 1);
            problem.undo();
            System.out.println("Current line: " + problem.getNextLineNumber());
            log.debug("After undo {}: current line number {}", i + 1, problem.getNextLineNumber());
        }

        System.out.println("\n--- Testing reset() ---");
        // Test resetting
        log.info("--- Testing reset() ---");
        problem.reset();
        System.out.println("After reset: line " + problem.getNextLineNumber());
        log.debug("After reset: current line number {}", problem.getNextLineNumber());

        System.out.println("\n--- Testing multiple steps ---");
        // Test multiple steps
        log.info("--- Testing multiple steps ---");
        problem.step(3);
        System.out.println("After 3 steps: line " + problem.getNextLineNumber());
        log.debug("After 3 steps: current line number {}", problem.getNextLineNumber());

        System.out.println("\n=== Test Complete ===");
        log.info("=== Test Complete ===");
    }

    @Override
    public void problemUpdated(Problem problem) {
        System.out.println(
                "LISTENER: Problem updated notification received. Current line: "
                        + problem.getNextLineNumber());
        log.debug(
                "Problem updated notification received. Current line number: {}",
                problem.getNextLineNumber());
    }
}
