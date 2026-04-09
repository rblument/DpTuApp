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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * A controller facade that standardizes requests from the GUI client to the tutor server via a
 * socket connection with JSON encoded request/reply payloads.
 */
public class ControllerFacade {

    private static final ControllerFacade SINGLETON;

    static {
        SINGLETON = new ControllerFacade();
    }

    public static ControllerFacade instance() {
        return SINGLETON;
    }

    private static final Logger log = LoggerFactory.getLogger(ControllerFacade.class);

    private static final String SERVER = "localhost";
    private static final int PORT = 53637;

    private ControllerFacade() {}

    public TutorReply tutorRequest(ClientRequest request) {
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        log.info("*** jsonRequest *" + jsonRequest + "*");

        String jsonReply = send(jsonRequest);

        log.info("*** jsonReply: " + jsonReply);

        return gson.fromJson(jsonReply, TutorReply.class);
    }

    private String send(String request) {
        Socket client = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            client = new Socket(SERVER, PORT);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());
            out.println(request);
            out.flush();
            return in.readLine();
        } catch (UnknownHostException e) {
            log.error("Unknown Host", e);
        } catch (IOException e) {
            log.error("IOException client", e);
        } finally {
            try {
                if (out != null) out.close();
            } finally {
                try {
                    if (in != null) in.close();
                } catch (IOException e) {
                    log.error("Unable to close client socket in", e);
                } finally {
                    try {
                        if (client != null) client.close();
                    } catch (IOException e) {
                        log.error("Unable to close client socket in", e);
                    }
                }
            }
        }

        return "{'status':':ERR','data':'A non-recoverable error occurred in the socket connection (see logs)'}";
    }
}
