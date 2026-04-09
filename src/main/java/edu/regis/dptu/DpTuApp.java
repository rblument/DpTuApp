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
            log.info("Starting Client GUI...");

            // Force the creation of the MainFrame singleton, which is not
            // made visible to the user until after they sign-in.
            MainFrame.instance();

            // Force the creation of the SplashFrame, which is displayed and
            // allows the user to sign-in or create a new student account.
            // If sign-in is successful the MainFrame is displayed.
            SplashFrame.instance();

            log.info("DpTu Initialization successful.");

        } catch (SecurityException e) {
            log.error("Couldn't create Data directory in NetBeans Project.", (Throwable) null);
            log.error("Perhaps, try changing permissions.");
        }
    }
}
