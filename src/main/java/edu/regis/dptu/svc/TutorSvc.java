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
 * Specifies the behaviors provided by the ShaTu tutor.
 * 
 * The actual behaviors are specified by request type within the client request.
 * These requests are documented with 
 * 
 * @author rickb
 */
public interface TutorSvc { 
    /**
     * Handles a request from the client (user interface).
     * 
     * @param clientRequest a ClientRequest encapsulating a request and data.
     * @return a TutorReply encapsulating a status and, possibly, data.
     */
    TutorReply request(ClientRequest clientRequest); 
}

