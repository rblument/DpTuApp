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
 * Encapsulates a request sent from the GUI controller to the tutor server.
 *
 * @author rickb
 */
public class ClientRequest {
    private ServerRequestType requestType;
    private String userId;
    private String securityToken;
    private String sessionId;
    private String data;

    public ClientRequest() {}

    public ClientRequest(ServerRequestType requestType) {
        this.requestType = requestType;
    }

    public ServerRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(ServerRequestType requestType) {
        this.requestType = requestType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
