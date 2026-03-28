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
package edu.regis.dptu.util;

import java.awt.Font;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas O'Rourke
 */
public class ReusableFonts {
    /**
     * Notes: Have a list of Font objects that are created somewhere central, which are named. Then
     * any other View class can call from this class to get a certain font by name. This would
     * prevent the like 25-27 instances of new Font objects being created, and would make it easy to
     * standardize fonts and sizes since they will be general fonts that are named.
     */
    private static final Logger log = LoggerFactory.getLogger(ReusableFonts.class);

    private static HashMap<String, Font> fonts = new HashMap<>();

    // The Singleton instance of the Reusable Fonts class
    private static final ReusableFonts SINGLETON;

    // Invoked when this class is loaded
    static {
        SINGLETON = new ReusableFonts();
    }

    private ReusableFonts() {
        setupInitialFonts();
    }

    public static ReusableFonts instance() {
        return SINGLETON;
    }

    /**
     * Gets a Font object based on the named key
     *
     * @param name The key to get the Font from
     * @return The Font object for the specified key
     */
    public Font getFont(String name) {
        Font font = fonts.get(name);

        if (font == null) {
            log.warn("Font with name {} was not successfully retrieved", name);
            throw new IllegalArgumentException("Error: Name " + name + " is not in font list.");
        }

        return font;
    }

    /**
     * Create the set of reusable fonts that will be used by each of the Frames. This can be added
     * to as needed, as long as duplicates aren't created.
     */
    private void setupInitialFonts() {
        log.info("ReusableFonts: Setting up inital fonts");

        fonts.put("Copyright", new Font("Dialog", Font.PLAIN, 10));
        fonts.put("CCIS", new Font("Dialog", Font.PLAIN, 20));
        fonts.put("Logo", new Font("Dialog", Font.PLAIN, 20));
        fonts.put("Header", new Font("Segoe UI", Font.PLAIN, 18));
        fonts.put("Name", new Font("Dialog", Font.PLAIN, 14));
        fonts.put("Description", new Font("Dialog", Font.PLAIN, 12));

        fonts.put("10ptLabel", new Font("Dialog", Font.PLAIN, 10));
        fonts.put("12ptLabel", new Font("Dialog", Font.PLAIN, 12));
        fonts.put("14ptLabel", new Font("Dialog", Font.PLAIN, 14));
        fonts.put("16ptLabel", new Font("Dialog", Font.PLAIN, 16));
        fonts.put("18ptLabel", new Font("Dialog", Font.PLAIN, 18));
        fonts.put("20ptLabel", new Font("Dialog", Font.PLAIN, 20));

        fonts.put("18ptBold", new Font("Segoe UI", Font.BOLD, 18));

        fonts.put("20ptArial", new Font("Arial", Font.PLAIN, 20));

        fonts.put("AnswerField", new Font("Monospaced", Font.PLAIN, 14));
        fonts.put("HintLabel", new Font("Dialog", Font.ITALIC, 12));
        fonts.put("StatusLabel", new Font("Monospaced", Font.BOLD, 12));
        fonts.put("StepsLabel", new Font("Dialog", Font.BOLD, 14));
    }
}
