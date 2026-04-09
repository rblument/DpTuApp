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
package edu.regis.dptu.controller;

/**
 * The legal requests that can be made to the tutor server.
 *
 * @author rickb
 */
@SuppressWarnings("Logging")
public enum ServerRequestType {
    COMPLETED_STEP(":CompletedStep"),
    COMPLETED_TASK(":CompletedTask"),
    CREATE_ACCOUNT(":CreateAccount"),
    NEW_EXAMPLE(":NewExample"),
    SIGN_IN(":SignIn"),
    REQUEST_HINT(":RequestHint");

    private final String requestName;

    ServerRequestType(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestName() {
        return requestName;
    }

    @Override
    public String toString() {
        return requestName;
    }
}
