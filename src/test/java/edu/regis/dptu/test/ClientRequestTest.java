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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;

public class ClientRequestTest {

    @Test
    public void testClientRequestFields() {
        ClientRequest request = new ClientRequest(ServerRequestType.SIGN_IN);
        request.setRequest(ServerRequestType.REQUEST_HINT);
        request.setUserId("student@regis.edu");
        request.setSecurityToken("security-token");
        request.setSessionId("session-id");
        request.setData("{\"key\":\"value\"}");

        assertEquals(ServerRequestType.REQUEST_HINT, request.getRequestType());
        assertEquals("student@regis.edu", request.getUserId());
        assertEquals("security-token", request.getSecurityToken());
        assertEquals("session-id", request.getSessionId());
        assertEquals("{\"key\":\"value\"}", request.getData());
    }
}
