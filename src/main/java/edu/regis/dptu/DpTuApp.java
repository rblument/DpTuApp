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

/**
 * A standalone implementation of the Dynamic Programming intelligent tutoring (DpTu) application.
 *
 * @author rickb
 */
public class DpTuApp {
    private static final Logger log = LoggerFactory.getLogger(DpTuApp.class);

    /**
     * Main entry point for the DpTu application, which will start the server.
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
            // Start the socket server for the DpTu tutor.
            new Thread(new DpTuServer()).start();

            log.info("DpTu Server started successfully.");

        } catch (SecurityException e) {
            log.error("Couldn't create Data directory in NetBeans Project.", (Throwable) null);
            log.error("Perhaps, try changing permissions.");
        }
    }
}
