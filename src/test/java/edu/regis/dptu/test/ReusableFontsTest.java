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
package edu.regis.dptu.test;

import java.awt.Font;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.util.ReusableFonts;

/**
 * Unit test class for ReusableFonts
 *
 * @author Thomas
 */
public class ReusableFontsTest {

    private static final Font TEST_DIALOG_FONT = new Font("Dialog", Font.PLAIN, 20);
    private static ReusableFonts INSTANCE = ReusableFonts.instance();

    /** Test getting a font with a valid name */
    @Test
    public void testGetValidFont() {
        Font logoFont = INSTANCE.getFont("Logo");

        assertEquals(logoFont, TEST_DIALOG_FONT);
    }
}
