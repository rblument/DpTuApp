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
 * The problem types that that get be tutored to the student.
 *
 * @author rickb
 */
@SuppressWarnings("Logging")
public enum ProblemKind {
    /** A Longest Common Subsequent (LCS) Dynamic Programming problem.. */
    LCS_PROBLEM("Longest Common Subsequence"),

    /** A Matrix Chaining Dynamic Programming problem */
    MATRIX_CHAIN("Matrix Chaining"),

    /** A 0-1 Knapsack Dynamic Programming problem. */
    KNAPSACK_0_1("0/1 Knapsack");

    /** A GUI displayable pretty print string identifying this problem kind. */
    private final String title;

    /**
     * Initialize this problem kind with its title.
     *
     * @param title a GUI displayable pretty print name for this problem kind.
     */
    ProblemKind(String title) {
        this.title = title;
    }

    /**
     * Return the title for this problem kind.
     *
     * @return a GUI displayable pretty print string for this problem kind
     */
    public String title() {
        return title;
    }
}
