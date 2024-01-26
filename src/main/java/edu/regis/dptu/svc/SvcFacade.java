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

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Facade that standardizes requests from the GUI Client to the DpTu tutor 
 * server via a socket connection with requests and replies encoded as JSon
 * encoded object strings
 *
 * Conceptually, this facade is part of the Swing-based GUI client.
 *
 * This facade exists to facilitate a subsequent port from the Swing-based GUI
 * to a Restful HTML client. As such, it is currently a logical part of the 
 * Swing-based GUI client, but for a Web-Browser client, it would be implemented
 * as part of the Server Socket Connection, which would forward the HTTML JSon 
 * HttpReqeust to the tutor using POJO message passing (i.e. method invocation).
 * 
 * Currently, the following request types are supported:
 * {"request": "CreateStudentAccount",
 *  "data": {"userId":"userId@regis.edu", 
 *           "password":"sha256Password",
 *           "firstName":"Rick",
 *           "lastName":"Justice"
 *          }
 * }
 * 
 * {"request": "SignIn",
 *  "data":  {"userId": "user email addr"}
 * }
 *
 * @author rickb
 */
public class SvcFacade {
    /**
     * The single instance of the tutor facade.
     */
    private static final SvcFacade SINGLETON;

    /**
     * Create the singleton for this facade, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced be the SvcFacade.instance() method occurring initially
     * in the SignInAction class.
     */
    static {
        SINGLETON = new SvcFacade();
    }
    
    /**
     * Return the singleton implementing the TutorFacade interface.
     * 
     * @return a TutorFacade
     */
    public static SvcFacade instance() {
        return SINGLETON;
    }
    
    /**
     * Handler for logging messages.
     */
    private static final Logger LOGGER = Logger.getLogger(SvcFacade.class.getName());
    
    /**
     * The computer host to which DpTu tutor requests are delegated.
     * 
     * That is, the DpTuServer is executing on this host.
     */
    private static final String SERVER = "localhost";
    
    /**
     * Socket port supporting the DpTu tutoring service protocol
     */
    private static final int PORT = 53637;
    
    
    private SvcFacade() {  
    }

    /**
     * Encodes the given client request as a JSon object and sends it to the
     * tutor returning the tutor's reply.
     * 
     * @param request the ClientRequest being sent to the tutor.
     * @return the TutorReply from the tutor.
     */
    public TutorReply tutorRequest(ClientRequest request) {
        Gson gson = new Gson();
        // ToDo: remove debugging stmt.
        String jsonRequest = gson.toJson(request);
        System.out.println("*** jasonRequest *" + jsonRequest + "*");
  
        String jsonReply = send(jsonRequest);
        
        System.out.println("*** jsonReply: " + jsonReply);
        
        return gson.fromJson(jsonReply, TutorReply.class);
    }
    
    /**
     * Send the given request to the DpTu server and return the reply.
     * 
     * Communication with the SERVER occurs via the socket connection on port PORT.
     * 
     * @param request a JSon encoded ClientRequest object
     * @return a JSon encoded TutorReply from the tutor
     */
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
            LOGGER.log(Level.SEVERE, "Unknown Host", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException client", e);
        } finally {
            // Kludgy, but tries to close an open socket and its associated
            // input and output streams in every possible error scenario
            // If we didn't, it's possible that we're leaking memory.
            try {
                if (out != null)
                    out.close();
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Unable to close client socket in", e);
                } finally {
                    try {
                        if (client != null)
                            client.close();
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Unable to close client socket in", e);
                    }
                }
            }
        }
        
        // Return this as a JSon encoded TutorReply object string.
        return "{'status':':ERR','data':'A non-recoverable error occurred in the socket connection (see logs)'}";
    }
}


