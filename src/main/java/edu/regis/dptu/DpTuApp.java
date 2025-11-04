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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import edu.regis.dptu.svc.DpTuServer;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.MainFrame;
import edu.regis.dptu.view.SplashFrame;

/**
 * A standalone implementation of the Dynamic Programming intelligent tutoring (DpTu) application.
 *
 * @author rickb
 */
public class DpTuApp {
    private static final Logger log = LoggerFactory.getLogger(DpTuApp.class);

    /** Property file located on the CLASSPATH, which is used to configure the julLogger. */
    private static final String julLogger_PROPERTIES = "/Logging.properties";

    // ./resources/logging.properties
    /** Events of interest occurring in this class are logged to this JUL logger. */
    private static final java.util.logging.Logger julLogger =
            java.util.logging.Logger.getLogger(DpTuApp.class.getName());

    /**
     * Main entry point for the DpTu application, which will display the UI.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        julLogger.info("DpTuApp initializing…");

        // Load JUL configuration from classpath (optional while migrating to SLF4J/Log4j2)
        try (InputStream strm = DpTuApp.class.getResourceAsStream(julLogger_PROPERTIES)) {
            if (strm != null) {
                LogManager.getLogManager().readConfiguration(strm);
                julLogger.info("Message logging initialization completed.");
            } else {
                julLogger.severe("Logging.properties not found at " + julLogger_PROPERTIES + " on the classpath.");
            }
        } catch (IOException e) {
            julLogger.log(java.util.logging.Level.SEVERE, "Error loading logging.properties", e);
        }

        // Initializes the properties from DpTu.properties and sets the locale.
        ResourceMgr.instance();
        julLogger.info("DpTu properties initialization completed.");

        try {
            julLogger.info("Starting DpTu Server (Tutoring Service)...");
            // ToDo: Separate the initialization of client and server
            // Start the socket server for the DpTu tutor.
            new Thread(new DpTuServer()).start();

            // ToDo: This puts the main client UI thread to sleep to give the
            // server a chance to finish starting. This won't be required once
            // we separate the server into its own application that is separate
            // from the GUI client since the server should "always" be running.
            Thread.sleep(4000);

            julLogger.info(" Server is running.");

            julLogger.info(" Starting Client GUI...");

            // Force the creation of the MainFrame singleton, which is not
            // made visible to the user until after they sign-in.
            MainFrame.instance();

            // Force the creation of the SplashFrame, which is displayed and
            // allows the user to sign-in or create a new student account.
            // If sign-in is successful the MainFrame is displayed.
            SplashFrame.instance();

            julLogger.info("DpTu Initialization successful.");

        } catch (InterruptedException ex) {
            julLogger.log(java.util.logging.Level.SEVERE, "Interrupted during startup", ex);
            Thread.currentThread().interrupt();
        } catch (SecurityException e) {
            julLogger.severe("Couldn't create Data directory in NetBeans Project.");
            julLogger.severe("Perhaps, try changing permissions.");
        }
    }
}
