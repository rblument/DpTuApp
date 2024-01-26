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
package edu.regis.dptu.svc;

/**
 * A decorator that wraps a tutor reply to a user interface request.
 * 
 * The the initial request time specifies how to interpret the JSon encoded 
 * data.
 * 
 * @author rickb
 */
public class TutorReply {
    /**
     * The status of this reply (of particular note is ERR)
     */
    private String status;
    
    /**
     * A JSon encoded object whose format depends on the associated request to
     * which this is a reply.
     */
    private String data = "";
    
    /**
     * A reply from the tutor with an ERR status.
     */
    public TutorReply() {
        this("ERR");
    }
    
    /**
     * A reply from the tutor with the given status
     * 
     * @param status 
     */
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
