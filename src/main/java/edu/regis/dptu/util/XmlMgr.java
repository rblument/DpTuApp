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

import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.err.XmlException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility for reading XML files located in a NetBeans project data directory.
 * 
 * @author rickb
 */
public class XmlMgr {
    /**
     * Log unexpected events to this logger.
     */
    private static final Logger LOGGER = Logger.getLogger(XmlMgr.class.getName());
    
    /**
     * Data directory containing data files within the current NetBeans project.
     */
    private static final String DATA_DIRECTORY = "src/main/java/resources/Data/";
    
    /**
     * The singleton instance of this class.
     */
    public static final XmlMgr SINGLETON = new XmlMgr();
    
    /**
     * Return the singleton object for this class.
     * 
     * @return 
     */
    public static XmlMgr instance() {
        return SINGLETON;
    }
    
    /**
     * Initialize this Xml Manager by setting the default data directory.
     */
    private XmlMgr() {
       // String netBeansDir = System.getProperty("user.dir");
       // dataDirectory =  netBeansDir + "/Data/";
       // System.out.println("Data Directory: " + dataDirectory);
    }
    
    /**
     * Return all of the xml files in the given subdirectory, if any, of 
     * current data directory.
     * 
     * If the subDirectory is an empty string, then all of the "*.xml" files
     * in the current data directory are returned. Otherwise, the "*.xml"
     * files in the given subdirectory of the current data directory are
     * returned, e.g., "/Course" or "/Course/Unit".
     * 
     * @param subDirectory an empty string or path beginning with a '/'
     * @return 
     */
    public List<File>findAllFiles(String fileName) {
        ArrayList<File> xmlFiles = new ArrayList<>();
        
        File folder = new File(DATA_DIRECTORY);
        
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                String lowercaseName = name.toLowerCase();
                
                return lowercaseName.endsWith(".xml");  
            }
        };
        
        
        File[] candidates = folder.listFiles(filter);
        
        for (File file : candidates)
            if (file.isFile())
                xmlFiles.add(file);
                
               
        return xmlFiles; 
    }
    
    /**
     * Return true, if the given named file exists
     * 
     * @param fileName the name of an XML file.
     * @return true if the name file exists, otherwise false
     */
    public boolean fileExists(String fileName) {
        File file = findFile(fileName);
        
        return !((file == null) || (!file.exists()));        
    }
    
    /**
     * Return an open file with the given file name, which you must
     * eventually close.
     * 
     * This opens a file in "edu.regis.nonprofit.data" on the CLASSPATH with
     * a name like "Oranization_n.xml", which might not be a good idea.
     * Pro: We don't mess with the computer's disk outside of this project.
     * Cons: New files don't show up in the NetBeans IDE and can be deleted
     *       accidentally when a new project is imported.
     * 
     * 
     * @param fileName
     * @return the open file, make sure you close it.
     * @throws FileNotFoundException 
     */
    public FileOutputStream openFile(String fileName) throws FileNotFoundException {

        String path = DATA_DIRECTORY + fileName;
            
        return new FileOutputStream(new File(path));
    }
    
    /**
     * Delete the file with the given name
     * 
     * @param fileName String e.g. "Problem_1.xml" 
     */
    public void deleteFile(String fileName) {
        String path = DATA_DIRECTORY + fileName;
        
        File file = findFile(path);
  
        file.delete();
    }
    
    /**
     * Return the file with the given name
     * 
     * @param fileName String e.g. "Problem_1.xml"
     * @return a File object corresponding to the given file name
     */
    public File findFile(String fileName) {
        String dir = DATA_DIRECTORY + fileName;

        File file = new File(dir);
  
        return file;
    }
    
    /**
     * Return the next available FileName id; For example if Foo_1.xml and 
     * Foo_2.xml exist, then return 3.
     * 
     * Scans the existing FileName_n.xml files until a file with 'n'
     * doesn't exists and then 'n' is returned.
     * 
     * @param fileName a file name with and ending underscore preceding the id,
     *                 but without the id.
     * @return an int for the next available id.
     */
    public int nextId(String fileName) {
        int id = 1;
        
        do {
            fileName = fileName + id + ".xml";
            String fullPath = DATA_DIRECTORY + fileName;

            Path path = Paths.get(DATA_DIRECTORY);

            if (Files.exists(path)) {
                return id;
            } else {
                id++;
            }
        
        } while (id < Integer.MAX_VALUE);
        
        return -1; // Should never get here.
    }
    
    /**
     * Return the XML root Element in the given file.
     * 
     * @param fileName a fileName.xml appearing on the CLASSPATH
     * @return the root element in the given file.
     * @throws ObjNotFoundException the file is not on the CLASSPATH
     * @throws XmlException a non-recoverable xml error when parsing the file
     */
    public Element findRoot(String fileName) throws ObjNotFoundException, XmlException {
        try {
            InputStream in = new FileInputStream(findFile(fileName));
 
            if (in == null)
                throw new ObjNotFoundException(DATA_DIRECTORY + fileName);
            
            return findRoot(in);
        
        } catch (FileNotFoundException e) {
            throw new ObjNotFoundException(fileName, e);
        }
    }
    
    /**
     * Return the root XML Element in the given file. 
     * 
     * @param file an xml file 
     * @return the root xml Elemnt in the given file.
     * @throws FileNotFoundException the given file doesn't exist
     * @throws XmlException a non-recoverable xml error when parsing the file
     */
    public Element findRoot(File file) throws FileNotFoundException, XmlException {
        InputStream in = new FileInputStream(file);
        
        return findRoot(in);
    }

    /**
     * Return the named child element in the given parent
     * 
     * @param parent the parent element to search under
     * @param name the element tag name
     * @return the child element or null, if it doesn't exist
     */
    public static Element getChild(Element parent, String name) {
        for (Node child = parent.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            
            if ((child.getNodeType() == Node.ELEMENT_NODE) &&
                 child.getNodeName().equals(name))
                return (Element) child;
        }
        
        return null;
    }
    
    /**
     * Return all of the named child elements of the given parent element
     * 
     * @param parent
     * @param name the tag name of the child
     * @return ArrayList of elements, which may be empty
     */
    public static ArrayList<Element> getChildren(Element parent, String name) {
        ArrayList<Element> children = new ArrayList<>();
        
        for (Node child = parent.getFirstChild(); 
             child != null;
             child = child.getNextSibling()) {
            
            if ((child.getNodeType() == Node.ELEMENT_NODE) &&
                 child.getNodeName().equals(name))
                children.add((Element) child);
        }
        
        return children;
    }

    /**
     * Search the given element for an element tag that appears only once in
     * the document and return the content of the found element.
     * 
     * @param root the root element of the corresponding document to search
     * @param tag an element in the document (e.g. "&lt;Tag>")
     * @return the text content for the tag or an empty string, if not found.
     */
    public static String contentText(Element root, String tag) {
        NodeList nodes = root.getElementsByTagName(tag);
        
        if (nodes.getLength() == 0) {
            LOGGER.log(Level.ALL, "XmlMgr: Unknown tag: {0}", tag);
            return "";
        } else {
            Node node = nodes.item(0);
        
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                return element.getTextContent();
            } else {
                LOGGER.log(Level.ALL, "XmlMgr: Unknown tag type: {1}", tag);
                return "";
            }
        }
    }
    
    /**
     * Returns the content of the given element as a string.
     * 
     * @param element an XML Element such as &lt;Tag>
     * @return a string containing the content of the given element.
     */
    public static String getContentText(Element element) {
        return element.getTextContent();
    }
    
    /**
     * Return the value of the named attribute in the given element
     * (The attribute should exists or a warning is logged)
     * 
     * @param element an XML element, such as &lTag>
     * @param attributeName the name of an attribute in the XML element
     * @return the attribute's value or an empty string, if it the attribute
     *         doesn't exist in the given element.
     */
    public static String getAttribute(Element element, String attributeName) {
        String val = element.getAttribute(attributeName);
        
        if (val.equals(""))
            LOGGER.log(Level.ALL, "Missing or empty attribute {0}", attributeName);
        
        return val;
    }
    
    /**
     * Return the optional attribute value for the named attribute in the 
     * given element
     * 
     * @param element an XML element, such as &lTag>
     * @param attributeName the name of an attribute in the XML element
     * @return the attribute's value or an empty string, if it the attribute
     *         doesn't exist in the given element.
     */
    public static String getOptAttribute(Element element, String attributeName) {
        return element.getAttribute(attributeName);
    }
    
    /**
     * Return the int value of the named attribute in the given element
     * 
     * @param element an XML element, such as &lTag>
     * @param attributeName the name of an attribute in the XML element
     * @return int value of the attribute or -1 if it doesn't exist.
     */
    public static int getIntAttribute(Element element, String attributeName) {
        String val = element.getAttribute(attributeName);
        
        if (val.equals("")) {
            LOGGER.log(Level.ALL, "Missing or empty int attribute {0}", attributeName);
            return -1;
        } else {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.ALL, "Expected an int attribute value: {1}", attributeName);
                return -1;
            }
        }
    }
    
    /**
     * Return the float value of the named attribute in the given element
     * 
     * @param element an XML element, such as &lTag>
     * @param attributeName the name of an attribute in the XML element
     * @return float value of the attribute or -1 if it doesn't exist.
     */
    public static float getFloatAttribute(Element element, String attributeName) {
        String val = element.getAttribute(attributeName);
        
        if (val.equals("")) {
            LOGGER.log(Level.ALL, "Missing or empty float attribute {0}", attributeName);
            return 0.0f;
        } else {
            try {
                return Float.parseFloat(val);
                
            } catch (NumberFormatException e) {
                LOGGER.log(Level.ALL, "Expected a float attribute value: {1}", attributeName);
                return 0.0f;
            }
        }
    }
    
    /**
     * Return the boolean value of the named attribute in the given element
     * 
     * @param element an XML element, such as &lTag>
     * @param attributeName the name of an attribute in the XML element
     * @return boolean value of the attribute with "true" or "yes" returning
     *         true, otherwise false. Also false if it attribute doesn't exist.
     */
    public static boolean getBooleanAttribute(Element element, String attributeName) { 
        String val = element.getAttribute(attributeName).toLowerCase();
       
        switch (val) {
            case "":
                LOGGER.log(Level.ALL, "Missing or empty boolean attribute {0}", attributeName);
                return false;
            case "true":
            case "yes":
                return true;
            case "false":
            case "no":
                return false;
            default:
                LOGGER.log(Level.ALL, "Expected a boolean attribute value: {1}", attributeName);
                return false;
        }
    }
    
    /**
     * Return the root element of the XML document found in the input stream.
     * 
     * @param in an input stream containing an XML document
     * @return an XML Element corresponding to the root of the given document.
     * @throws XmlException 
     */
    private Element findRoot(InputStream in) throws XmlException {
        try {    
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          
            Document doc = dBuilder.parse(in);
			
            //optional, but recommended, see:
            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
        
            return doc.getDocumentElement();
        
        } catch (ParserConfigurationException e) {
            throw new XmlException("XML_ERR_1 ", e);
        } catch (IOException e) {
            throw new XmlException("XML_ERR_2 ", e);
        } catch (SAXException e) {
            throw new XmlException("XML_ERR_3 ", e);
        }
    }
}

