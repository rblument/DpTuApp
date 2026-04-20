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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.svc.ClientRequest;
import edu.regis.dptu.svc.ServerRequestType;
import edu.regis.dptu.svc.SvcFacade;
import edu.regis.dptu.svc.TutorReply;

@SuppressWarnings("Logging")
public class SvcFacadeTest {
    @Test
    public void privateSendReturnsErrorJsonWhenServerUnavailable() throws Exception {
        SvcFacade facade = SvcFacade.instance();
        Method send = SvcFacade.class.getDeclaredMethod("send", String.class);
        send.setAccessible(true);

        String result = (String) send.invoke(facade, "{\"request\":\"ping\"}");

        assertTrue(result.contains(":ERR"));
    }

    @Test
    public void tutorRequestParsesServerReply() throws Exception {
        CountDownLatch serverReady = new CountDownLatch(1);

        Thread serverThread =
                new Thread(
                        () -> {
                            try (ServerSocket serverSocket = new ServerSocket(53637); ) {
                                serverReady.countDown();
                                try (Socket socket = serverSocket.accept();
                                        BufferedReader in =
                                                new BufferedReader(
                                                        new InputStreamReader(
                                                                socket.getInputStream()));
                                        PrintWriter out =
                                                new PrintWriter(socket.getOutputStream(), true)) {
                                    in.readLine();
                                    out.println(
                                            "{\"status\":\"Hint\",\"data\":\"from-test-server\"}");
                                }
                            } catch (Exception ignored) {
                                // Test asserts on client side response.
                            }
                        });

        serverThread.start();
        try {
            assertTrue(
                    serverReady.await(10, TimeUnit.SECONDS), "Server did not start within timeout");
            SvcFacade facade = SvcFacade.instance();
            ClientRequest request = new ClientRequest(ServerRequestType.REQUEST_HINT);
            request.setData("{}");

            TutorReply reply = facade.tutorRequest(request);

            assertEquals("Hint", reply.getStatus());
            assertEquals("from-test-server", reply.getData());
        } finally {
            serverThread.join(2000);
        }
    }
}
