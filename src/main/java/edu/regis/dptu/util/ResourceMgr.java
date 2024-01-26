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

import edu.regis.dptu.err.MissingPropertyException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility Decorator wrapping the application's properties and resource 
 * bundle associated with the current locale, which is specified in the 
 * DICE.properties file on the CLASSPATH (see PROPERTY_FILE_PATH).
 * 
 * @author Rickb
 */
public class ResourceMgr {
    /**
     * Logger for any errors occurring in this Singleton.
     */
    private static final Logger LOGGER = Logger.getLogger("ResourceMgr.class");
    
    /**
     * The singleton instance of this ResourceMgr.
     */
    private static final ResourceMgr SINGLETON;
    
    /**
     * Location of the DICE.properties file on the CLASSPATH.
     */
    private static final String PROPERTY_FILE_PATH = "/ShaTu.properties";
    
    /**
     * Name of the property in the DICE.properties filed specifying the default
     * language displayed in the user interface.
     */
    private static final String LANGUAGE_PROP = "language";
    
    /**
     * Name of the property in the DICE.properties file specifying the locale 
     * language variant displayed in the user interface.
     */
    private static final String COUNTRY_PROP = "country";
  
    /**
     * Location of the UI text resources for a given locale (on CLASSPATH).
     */
   // private static final String UI_RESOURCE_PATH = "edu.regis.dptu.resources.Msgs";
    private static final String UI_RESOURCE_PATH = "Msgs";
    
    /**
     * Create the singleton for this manager, which occurs when this class
     * is loaded by the Java class loaded, as a result of the class being 
     * referenced by executing ResourceMgr.instance() in the main() method.
     */
    static {
        SINGLETON = new ResourceMgr();
    }
    
    /**
     * Return the single instance object of this Resource Manager
     * 
     * @return single ResourceMgr object 
     */
    public static ResourceMgr instance() {
        return SINGLETON;
    }
    
    /**
     * Local cache of properties loaded from the ShaTu.properties file.
     */
    private final Properties properties;
    
    /**
     * The current locale, as specified in the ShaTu.properties file
     */
    private Locale locale;
    
    /**
     * Map from property keys to all text messages displayed in the UI
     * according to the current locale (see locale above).
     */
    private ResourceBundle msgs;
    
    /**
     * Initialize this mgr by reading the properties and setting the GUI locale.
     */
    private ResourceMgr() {
        properties = new Properties();

        loadProps(PROPERTY_FILE_PATH);

        try {
            locale = new Locale(getProp(LANGUAGE_PROP), getProp(COUNTRY_PROP));

            msgs =  ResourceBundle.getBundle(UI_RESOURCE_PATH, locale);
       
        } catch (MissingPropertyException e) {
            LOGGER.log(Level.SEVERE, "Missing Property", e);
        }
    }
    
    /**
     * Return the current local, as specified in the NonProfit.properties file
     * 
     * @return the current Locale 
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Return the current local specific UI text message for the given key.
     * 
     * @param key unique identifier for a UI text display message
     * @return the locale specific String for the given key
     */
    public String string(String key) {
        return msgs.getString(key);
    }
    
    /**
     * Return the cached property value for the given property.
     *
     * @param name
     * @return The value of the given property.
     * @exception MissingPropertyException The given property wasn't found.
     */
    public String getProp(String name) throws MissingPropertyException {
	if (properties.containsKey(name)) {
	    return properties.getProperty(name);
	} else {
	    throw new MissingPropertyException(name);
	}
    }
    
    /**
     * Load and cache the properties found in the given properties file.
     * 
     * @param path location of property file, e.g. "./ShaTu.properties"
     */
    private void loadProps(String path) {
	try {
            InputStream strm = getClass().getResourceAsStream(path);

	    if (strm == null)
		throw new FileNotFoundException("property file '" + 
						path +
						"' not found in the classpath");

	    properties.load(strm);

	} catch (FileNotFoundException e) {
	    LOGGER.log(Level.ALL, "PropertyMgr-ERR_100: {0}", e.toString());
	} catch (IOException e) {
	    LOGGER.log(Level.ALL, "PropertyMgr-ERR_101: {0}", e.toString());
	}
    }
}
