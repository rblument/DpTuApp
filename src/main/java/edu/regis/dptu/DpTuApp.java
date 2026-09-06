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

import edu.regis.dptu.svc.DpTuServer;
import edu.regis.dptu.util.ResourceMgr;
import edu.regis.dptu.view.MainFrame;
import edu.regis.dptu.view.SplashFrame;

/**
This me editing the code in NetBeans, making a simple change, like this comment
cpford3
*/

/**
 * A standalone implementation of the Dynamic Programming intelligent tutoring (DpTu) application.
 *
 * @author rickb
 */
public class DpTuApp {
    private static final Logger log = LoggerFactory.getLogger(DpTuApp.class);

    /**
     * Main entry point for the DpTu application, which will display the UI.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        log.info("DpTuApp initializing…");

        // Initializes the properties from DpTu.properties and sets the locale.
        ResourceMgr.instance();
        log.info("DpTu properties initialization completed.");

        try {
            log.info("Starting DpTu Server (Tutoring Service)...");
            // ToDo: Separate the initialization of client and server
            // Start the socket server for the DpTu tutor.
            new Thread(new DpTuServer()).start();

            // ToDo: This puts the main client UI thread to sleep to give the
            // server a chance to finish starting. This won't be required once
            // we separate the server into its own application that is separate
            // from the GUI client since the server should "always" be running.
            Thread.sleep(4000);

            log.info(" Server is running.");

            log.info(" Starting Client GUI...");

            // Force the creation of the MainFrame singleton, which is not
            // made visible to the user until after they sign-in.
            MainFrame.instance();

            // Force the creation of the SplashFrame, which is displayed and
            // allows the user to sign-in or create a new student account.
            // If sign-in is successful the MainFrame is displayed.
            SplashFrame.instance();

            log.info("DpTu Initialization successful.");

        } catch (InterruptedException ex) {
            log.error("Interrupted during startup", ex);
            Thread.currentThread().interrupt();
        } catch (SecurityException e) {
            log.error("Couldn't create Data directory in NetBeans Project.", (Throwable) null);
            log.error("Perhaps, try changing permissions.");
        }
    }
}
