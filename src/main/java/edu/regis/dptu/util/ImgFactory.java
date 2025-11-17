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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides support for loading image icons from files on the CLASSPATH.
 *
 * @author rickb
 */
public class ImgFactory {
    private static final Logger log = LoggerFactory.getLogger(ImgFactory.class);

    /** Directory in the Resource path where the images are located. */
    private static final String DIRECTORY = "/";
   
    /**
     * Create an Image Icon by loading its corresponding PNG image.
     * 
     * @param fileName The filename of the image to load
     * @param altText The alternate text of the image to use
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
            log.error("Couldn't find image file: " + path);
            return null;
        }
    }
}
