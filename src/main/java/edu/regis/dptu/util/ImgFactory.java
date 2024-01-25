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

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Provides support for loading image icons from files on the CLASSPATH.
 * 
 * @author rickb
 */
public class ImgFactory {
    /**
     * Directory in the Resource path where the images are located.
     */
    private static final String DIRECTORY = "/";
    
    /**
     * File extension for the associated image.
     */
    private static final String SUFFIX = ".png";
    
    /**
     * Create an Image Icon by loading its corresponding png image.
     * 
     * @param n The id of the image to load (e.g. 0 - 127).
     * @return ImageIcon with the corresponding seven segment display.
     */
    public static ImageIcon createIcon(String fileName, String altText) {
        return new ImageIcon(createImage(fileName), altText);
    
    }
    
    public static BufferedImage createImage(String fileName) {
        String path = DIRECTORY + fileName;
 
        try {
            return ImageIO.read(ImgFactory.class.getResourceAsStream(path));
            
        } catch (IOException e) {
            System.err.println("Couldn't find image file: " + path);  
            return null;
        }
    }
}


