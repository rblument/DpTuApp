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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A reply from the tutor server, used by client controllers.
 *
 * @author rickb
 */
public class TutorReply {
    private static final Logger log = LoggerFactory.getLogger(TutorReply.class);
    private String status;
    private String data;

    public TutorReply() {}

    public TutorReply(String status) {
        this.status = status;
    }

    public TutorReply(String status, String data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
