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

package edu.regis.dptu;

import edu.regis.dptu.svc.DpTuServer;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.MainFrame;
import edu.regis.dptu.view.SplashFrame;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A standalone implementation of the Dynamic Programming intelligent tutoring 
 * (DpTu) application.
 * 
 * @author rickb
 */
public class DpTuApp {
 /**
     * Property file located on the CLASSPATH, which is used to configure the LOGGER.
     */
    private static final String LOGGER_PROPERTIES = "/Logging.properties";
    // ./resources/logging.properties
    
    /**
     * Events of interest occurring in this class are logged to this logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DpTuApp.class.getName());
    
    /**
     * Main entry point for the DpTut application, which will display the UI.
     * 
     * @param args ignored
     */
    public static void main(String[] args) {
        LOGGER.info("DpTuApp Initializing...:");
        
        try {
            final InputStream strm = DpTuApp.class.getResourceAsStream(LOGGER_PROPERTIES);
        
            LogManager.getLogManager().readConfiguration(strm);
            
            LOGGER.info("Message logging initialization completed.");
        } catch (IOException e) {
        
            LOGGER.severe("Error loading ./logging.properties");
            LOGGER.severe(e.getMessage());
        }      
        
        // Initializes the properties from DpTu.properties and sets the locale.
        ResourceMgr.instance();
        
        LOGGER.info("DpTu properties initialization completed.");
        
        System.out.println("Finished initializing");
        
        try {
            LOGGER.info(" Starting DpTu Server (Tutoring Service)...");
            // ToDo: Separate the initialization of client and server
            // Start the socket server for the DpTu tutor.
            (new Thread(new DpTuServer())).start();
            
            // ToDo: This puts the main client UI thread to sleep to give the 
            // server a chance to finish starting. This won't be required once 
            // we separate the server into its own application that is separate
            // from the GUI client since the server should "always" be running.
            Thread.sleep(4000);
                
            LOGGER.info(" Server is running.");
                
            LOGGER.info(" Starting Client GUI...");
            
            // Force the creation of the MainFrame singleton, which is not
            // made visible to the user until after they sign-in.
            MainFrame.instance();

            // Force the creation of the SplashFrame, which is displayed and
            // allows the user to sign-in or create a new student account.
            // If sign-in is successful the MainFrame is displayed.
            SplashFrame.instance();            
                
            LOGGER.info("DpTu Initialization successful.");
                
            } catch (InterruptedException ex) {
                Logger.getLogger(DpTuApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException e) {
            LOGGER.severe("Couldn't create Data directory in NetBeans Project.");
            LOGGER.severe("Perhaps, try changing permissions.");
        }
    }
}
