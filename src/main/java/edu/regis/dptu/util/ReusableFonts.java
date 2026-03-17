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
 *
 * @author Thomas O'Rourke
 */
public class ReusableFonts {
    /**
     * Notes:
     * Have a list of Font objects that are created somewhere central, which are named.
     * Then any other View class can call from this class to get a certain font by name.
     * This would prevent the like 25-27 instances of new Font objects being created, 
     * and would make it easy to standardize fonts and sizes since they will be general fonts that are named.
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
     * Adds a Font to the reusable fonts map with a named key
     * 
     * @param name The key to add the Font under
     * @param font The Font object to be added
     */
    public void addFont(String name, Font font) {
        fonts.put(name, font);
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
            log.debug("Font with name {} was not successfully retrieved", name);
        }
        
        return font;
    }
    
    /**
     * Gets the full HashMap of String/Font Key/Value pairs
     * 
     * @return The full HashMap of String/Font Key/Value pairs
     */
    public HashMap<String, Font> getAllFonts() {
        return fonts;
    }
    
    /**
     * Create the set of reusable fonts that will be used by each of the Frames.
     * This can be added to as needed, as long as duplicates aren't created.
     */
    private void setupInitialFonts() {
        log.info("ReusableFonts: Setting up inital fonts");
        
        addFont("Copyright", new Font("Dialog", Font.PLAIN, 10));
        addFont("CCIS", new Font("Dialog", Font.PLAIN, 20));
        addFont("Logo", new Font("Dialog", Font.PLAIN, 20));
        addFont("Header", new Font("Segoe UI", Font.PLAIN, 18));
        addFont("Name", new Font("Dialog", Font.PLAIN, 14));
        addFont("Description", new Font("Dialog", Font.PLAIN, 12));
        
        addFont("10ptLabel", new Font("Dialog", Font.PLAIN, 10));
        addFont("12ptLabel", new Font("Dialog", Font.PLAIN, 12));
        addFont("14ptLabel", new Font("Dialog", Font.PLAIN, 14));
        addFont("16ptLabel", new Font("Dialog", Font.PLAIN, 16));
        addFont("18ptLabel", new Font("Dialog", Font.PLAIN, 18));
        addFont("20ptLabel", new Font("Dialog", Font.PLAIN, 20));
        
        addFont("18ptBold", new Font("Segoe UI", Font.BOLD, 18));
        
        addFont("20ptArial", new Font("Arial", Font.PLAIN, 20));
        
        addFont("AnswerField", new Font("Monospaced", Font.PLAIN, 14));
        addFont("HintLabel", new Font("Dialog", Font.ITALIC, 12));
        addFont("StatusLabel", new Font("Monospaced", Font.BOLD, 12));
        addFont("StepsLabel", new Font("Dialog", Font.BOLD, 14));
    }
}
