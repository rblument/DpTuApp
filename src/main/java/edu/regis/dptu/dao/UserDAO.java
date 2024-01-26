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
import edu.regis.dptu.model.Account;
import edu.regis.dptu.model.User;
import edu.regis.dptu.svc.UserSvc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A Data Access Object implementing {@link UserSvc} behaviors.
 *
 * @author rickb
 */
public class UserDAO implements UserSvc {
    /**
     * Data directory containing student user account files.
     */
    private static final String DATA_DIRECTORY = "src/main/java/resources/Data/";
    
    /**
     * Initialize this DAO via the parent constructor.
     */
    public UserDAO() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Account acct) throws IllegalArgException, NonRecoverableException {      
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       
        String userId = acct.getUserId();
        String fileName = fullyQualifedFileName(userId); 
        File file = new File(fileName);
        File absFile = new File(file.getAbsolutePath());

        try {
            absFile.createNewFile();
      
            Path path = Paths.get(fileName);
        
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(acct, writer);                
            } catch (IOException ex) {
                throw new NonRecoverableException("Create User Accountt Writer Error", ex);
            }
            
        }   catch (IOException ex) {
            throw new NonRecoverableException("Create User Accountt File Error", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        //ToDo: add functionality
       throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User retrieve(String userId) throws ObjNotFoundException, NonRecoverableException {
        Gson gson = new Gson();
       
        String fileName = fullyQualifedFileName(userId);
        
        Path path = Paths.get(fileName);
     
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonObject jsonObj = JsonParser.parseReader(reader).getAsJsonObject();
            
            return gson.fromJson(jsonObj, User.class);

        } catch (FileNotFoundException ex) {
            // This not necessarily an error since the user may have a typo
            throw new ObjNotFoundException(String.valueOf(userId));
        } catch (IOException ex) {
            String errMsg = "find user: " + userId;
            throw new NonRecoverableException(errMsg, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User user, String newPassword) throws ObjNotFoundException, NonRecoverableException {
        //ToDo: add functionality
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Return the fully qualified name of the user file for the given user.
     * 
     * @param userId the id of the user whose session file name is returned.
     * @return a String specifying a fully qualified file name.
     */
    private String fullyQualifedFileName(String userId) {
        return DATA_DIRECTORY + "User_" + userId.replace('@', '_').replace('.', '_') + ".json";
    }
}

