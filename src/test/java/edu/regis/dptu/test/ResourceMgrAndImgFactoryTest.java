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

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.regis.dptu.err.MissingPropertyException;
import edu.regis.dptu.util.ImgFactory;
import edu.regis.dptu.util.ResourceMgr;

@SuppressWarnings("Logging")
public class ResourceMgrAndImgFactoryTest {

    @Test
    public void resourceMgrReturnsLocaleAndKnownProperties() throws Exception {
        ResourceMgr mgr = ResourceMgr.instance();

        assertNotNull(mgr.getLocale());
        assertNotNull(mgr.getProp("language"));
        assertNotNull(mgr.getProp("country"));
    }

    @Test
    public void resourceMgrMissingKeysReturnPlaceholderPattern() {
        ResourceMgr mgr = ResourceMgr.instance();

        String value = mgr.string("not.a.real.key");
        String formatted = mgr.string("not.a.real.key", "arg");

        assertEquals("!!not.a.real.key!!", value);
        assertEquals("!!not.a.real.key!!", formatted);
    }

    @Test
    public void resourceMgrMissingPropertyThrows() {
        ResourceMgr mgr = ResourceMgr.instance();

        assertThrows(MissingPropertyException.class, () -> mgr.getProp("not_a_property"));
    }

    @Test
    public void imgFactoryMissingImagesReturnNullImageAndIconWithAltText() {
        assertNull(ImgFactory.createImage("missing-file.png"));

        assertThrows(
                NullPointerException.class,
                () -> ImgFactory.createIcon("missing-file.png", "fallback"));

        ImageIcon icon = new ImageIcon();
        assertNotNull(icon);
        assertTrue(icon.getIconWidth() <= 0);
    }
}
