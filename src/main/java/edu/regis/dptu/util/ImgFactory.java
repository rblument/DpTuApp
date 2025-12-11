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
     * Create an Image Icon by loading its corresponding png image.
     *
     * @param fileName The file name of the image to load.
     * @param altText  Alternative text for the ImageIcon.
     * @return ImageIcon with the corresponding image, or null if not found.
     */
    public static ImageIcon createIcon(String fileName, String altText) {
        BufferedImage img = createImage(fileName);
        if (img != null) {
            log.debug("Successfully created ImageIcon for file '{}'", fileName);
        } else {
            log.warn("Failed to create ImageIcon for file '{}'", fileName);
        }
        return new ImageIcon(img, altText);
    }

    /**
     * Create a BufferedImage by loading a file from the classpath.
     *
     * @param fileName The file name of the image to load.
     * @return BufferedImage loaded from the classpath, or null if not found.
     */
    public static BufferedImage createImage(String fileName) {
        String path = DIRECTORY + fileName;
        log.debug("Attempting to load image from path: {}", path);

        try {
            BufferedImage img = ImageIO.read(ImgFactory.class.getResourceAsStream(path));
            if (img != null) {
                log.debug("Successfully loaded image '{}'", path);
            } else {
                log.warn("ImageIO.read returned null for '{}'", path);
            }
            return img;
        } catch (IOException e) {
            log.error("IOException while loading image '{}': {}", path, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error while loading image '{}': {}", path, e.getMessage(), e);
            return null;
        }
    }
}
