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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.regis.dptu.svc.DpTuServer;

@SuppressWarnings("Logging")
public class DpTuServerConnectionTest {

    @Test
    public void dpTuConnectionReadsRequestAndWritesReply() throws Exception {
        String requestJson =
                "{\"requestType\":\"CREATE_ACCOUNT\",\"data\":\"{}\",\"userId\":\"\",\"securityToken\":\"\"}\n";

        ByteArrayInputStream in =
                new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Socket client = mock(Socket.class);
        when(client.getInputStream()).thenReturn(in);
        when(client.getOutputStream()).thenReturn(out);

        DpTuServer server = new DpTuServer();
        Class<?> connClass = Class.forName("edu.regis.dptu.svc.DpTuServer$DpTuConnection");
        Constructor<?> ctor = connClass.getDeclaredConstructor(DpTuServer.class, Socket.class);
        ctor.setAccessible(true);

        Runnable connection = (Runnable) ctor.newInstance(server, client);
        connection.run();

        String replyJson = out.toString(StandardCharsets.UTF_8);
        assertTrue(replyJson.contains("status"));
        verify(client).close();
    }
}
