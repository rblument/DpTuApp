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

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.util.XmlMgr;

@SuppressWarnings("Logging")
public class XmlMgrTest {
    private static final Path DATA_DIR = Paths.get("src/main/java/resources/Data");
    private static final Path TEST_XML = DATA_DIR.resolve("XmlMgrTestFile.xml");
    private static final Path TEST_XML_2 = DATA_DIR.resolve("XmlMgrTestOther.xml");
    private static final Path TEST_TXT = DATA_DIR.resolve("XmlMgrIgnore.txt");

    @AfterEach
    public void cleanup() throws Exception {
        Files.deleteIfExists(TEST_XML);
        Files.deleteIfExists(TEST_XML_2);
        Files.deleteIfExists(TEST_TXT);
    }

    @Test
    public void fileAndDirectoryHelpersWork() throws Exception {
        Files.createDirectories(DATA_DIR);
        Files.writeString(TEST_XML, "<root/>", StandardCharsets.UTF_8);
        Files.writeString(TEST_XML_2, "<root/>", StandardCharsets.UTF_8);
        Files.writeString(TEST_TXT, "skip", StandardCharsets.UTF_8);

        XmlMgr mgr = XmlMgr.instance();

        List<File> xmlFiles = mgr.findAllFiles("ignored");
        assertTrue(xmlFiles.stream().anyMatch(f -> f.getName().equals("XmlMgrTestFile.xml")));
        assertTrue(xmlFiles.stream().anyMatch(f -> f.getName().equals("XmlMgrTestOther.xml")));
        assertFalse(xmlFiles.stream().anyMatch(f -> f.getName().endsWith(".txt")));

        assertTrue(mgr.fileExists("XmlMgrTestFile.xml"));
        assertFalse(mgr.fileExists("DoesNotExist.xml"));

        File found = mgr.findFile("XmlMgrTestFile.xml");
        assertTrue(found.exists());

        assertEquals(1, mgr.nextId("AnyPrefix_"));
    }

    @Test
    public void xmlParsingAndAttributeHelpersWork() throws Exception {
        Files.createDirectories(DATA_DIR);
        String xml =
                "<root a='10' f='3.5' b='yes'><child>hello</child><child>world</child><solo>single</solo></root>";
        Files.writeString(TEST_XML, xml, StandardCharsets.UTF_8);

        XmlMgr mgr = XmlMgr.instance();
        Element rootByName = mgr.findRoot("XmlMgrTestFile.xml");
        assertEquals("root", rootByName.getTagName());

        Element child = XmlMgr.getChild(rootByName, "child");
        assertNotNull(child);
        assertEquals("hello", XmlMgr.getContentText(child));

        assertEquals(2, XmlMgr.getChildren(rootByName, "child").size());
        assertEquals("single", XmlMgr.contentText(rootByName, "solo"));
        assertEquals("", XmlMgr.contentText(rootByName, "missing"));

        assertEquals("10", XmlMgr.getAttribute(rootByName, "a"));
        assertEquals("", XmlMgr.getOptAttribute(rootByName, "missing"));
        assertEquals(10, XmlMgr.getIntAttribute(rootByName, "a"));
        assertEquals(-1, XmlMgr.getIntAttribute(rootByName, "missingInt"));
        assertEquals(3.5f, XmlMgr.getFloatAttribute(rootByName, "f"));
        assertEquals(0.0f, XmlMgr.getFloatAttribute(rootByName, "missingFloat"));
        assertTrue(XmlMgr.getBooleanAttribute(rootByName, "b"));
        assertFalse(XmlMgr.getBooleanAttribute(rootByName, "missingBool"));

        Document doc =
                DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(TEST_XML.toFile());
        Element rootByFile = mgr.findRoot(TEST_XML.toFile());
        assertEquals(doc.getDocumentElement().getTagName(), rootByFile.getTagName());
    }

    @Test
    public void openAndDeleteFileWork() throws Exception {
        Files.createDirectories(DATA_DIR);

        XmlMgr mgr = XmlMgr.instance();
        try (FileOutputStream out = mgr.openFile("XmlMgrTestFile.xml")) {
            out.write("<root/>".getBytes(StandardCharsets.UTF_8));
        }

        assertTrue(Files.exists(TEST_XML));
        mgr.deleteFile("XmlMgrTestFile.xml");
        assertTrue(Files.exists(TEST_XML));
    }
}
