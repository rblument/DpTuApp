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
 * A decorator that wraps a user interface request being sent to the tutor
 * (sever), which includes the type of request being mad.
 * 
 * The request type specifies how to interpret the JSon encoded data.
 * 
 * @author rickb
 */
public class ClientRequest {    
    /**
     * The specific type of request being made by the client.
     */
    private ServerRequestType requestType;
    
    /**
     * The id of the user that made this request.
     */
    private String userId;
    
    /**
     * The sing-in session id associated with the user making this request.
     */
    private String sessionId;
    
    /**
     * A JSon encoded object whose format depends on the associated request.
     * (See the requestType documentation.)
     */
    private String data;
    
    /**
     * Initialize an empty client request.
     * 
     * @param requestType the type of request encoded in client request.
     */
    public ClientRequest(ServerRequestType requestType) {
        this.requestType = requestType;
        
        userId = "";
        sessionId = "";
        data = "";
    }

    public ServerRequestType getRequestType() {
        return requestType;
    }

    public void setRequest(ServerRequestType requestType) {
        this.requestType = requestType;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Return the data associated with this request.
     * 
     * @return a JSon encoded object corresponding to the request type.
     *         See the documentation for the data field.
     */
    public String getData() {
        return data;
    }

    /**
     * Set the data.
     * 
     * @param data a JSon encoded object corresponding to the request type.
     *             See the documentation for the data field.
     */
    public void setData(String data) {
        this.data = data;
    }
}