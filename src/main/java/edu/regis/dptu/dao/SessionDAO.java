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
package edu.regis.dptu.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.TutoringSession;
import edu.regis.dptu.svc.SessionSvc;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * A Data Access Object implementing {@link SessionSvc} behaviors.
 * 
 * @author rickb
 */
public class SessionDAO implements SessionSvc {
    /**
     * Data directory containing student session files.
     */
    private static final String DATA_DIRECTORY = "src/main/java/resources/Data/";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(TutoringSession session) throws IllegalArgException, NonRecoverableException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       
        String userId = session.getAccount().getUserId();
        
        String fileName = fullyQualifiedFileName(userId);
        
        File file = new File(fileName);
        
        File newFile = new File(file.getAbsolutePath());

        if (newFile.exists())
            throw new IllegalArgException("A session already exists: " + userId);
        
        try {
            newFile.createNewFile();
      
            Path path = Paths.get(fileName);
        
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                //gson.toJson(session, writer);
                String jsonStr = gson.toJson(session);
                writer.write(jsonStr);
                writer.flush();
                
            } catch (IOException ex) {
                throw new NonRecoverableException("Create Acct Writer Error", ex);
            }
        }   catch (IOException ex) {
            throw new NonRecoverableException("Create Acct File Error", ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TutoringSession retrieve(String userId) throws ObjNotFoundException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       
        Path path = Paths.get(fullyQualifiedFileName(userId));
     
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonObject jsonObj = JsonParser.parseReader(reader).getAsJsonObject();
   
            return gson.fromJson(jsonObj, TutoringSession.class);

        } catch (IOException ex) {
            throw new ObjNotFoundException(String.valueOf(userId));
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * 1. Rename existing session file
     * 2. Create a new session file using the given update
     * 3. Delete the original, but now renamed file.
     */
    @Override
    public void update(TutoringSession session) throws ObjNotFoundException, NonRecoverableException {
        String userId = session.getAccount().getUserId();
        
        String fileName = fullyQualifiedFileName(userId);
        File file = new File(fileName);
        File absFile = new File(file.getAbsolutePath());
        
        if (!absFile.exists())
            throw new ObjNotFoundException("Session doesn't exist in update: " + userId);
        
        // Rename the file before deleting
        String tmpName = DATA_DIRECTORY + "Session_tmp_" + userId.replace('@', '_').replace('.', '_') + ".json";
        File tmpFile = new File(tmpName);
        File destFile = new File(tmpFile.getAbsolutePath());
        absFile.renameTo(destFile);
        
    
        try {
            create(session);
            
            delete(absFile); // Delete the previous, but now renamed file
       
        } catch (IllegalArgException | NonRecoverableException ex) {
            String msg = "Cannot create a deleted session attempting restore: " + userId; 
            Logger.getLogger(SessionDAO.class.getName()).log(Level.SEVERE, msg, ex);
            
            // Set the name back to the original session name before the update
            fileName = fullyQualifiedFileName(userId);
            file = new File(fileName);
            File originalAbsFile = new File(file.getAbsolutePath());
            absFile.renameTo(originalAbsFile);
            
            throw new NonRecoverableException("Session Update Failed", ex);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        String fileName = fullyQualifiedFileName(userId);
        
        File file = new File(fileName);
        
        File absFile = new File(file.getAbsolutePath());

        delete(absFile);
    }
    
    /**
     * Utility that deletes the given a file.
     * 
     * @param file a File with an absolute path to delete.
     */
    private void delete(File file) {
        if (file.exists())
            file.delete();
    }
    
    /**
     * Return the fully qualified name of the session file for the given user.
     * 
     * @param userId the id of the user whose session file name is returned.
     * @return a String specifying a fully qualified file name.
     */
    private String fullyQualifiedFileName(String userId) {
        return DATA_DIRECTORY + "Session_" + userId.replace('@', '_').replace('.', '_') + ".json";
    }
}


