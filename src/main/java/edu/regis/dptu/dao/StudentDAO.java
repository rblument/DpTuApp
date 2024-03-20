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
import edu.regis.dptu.err.IllegalArgException;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.model.Student;
import edu.regis.dptu.model.aol.StudentModel;
import edu.regis.dptu.svc.StudentSvc;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Data Access Object implementing {@link StudentSvc} behaviors (CRUD persistence).
 * 
 * @author rickb
 */
public class StudentDAO implements StudentSvc {
     /**
     * Data directory containing student user account files.
     */
    private static final String DATA_DIRECTORY = "src/main/java/resources/Data/";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Student student) throws IllegalArgException, NonRecoverableException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String userId = student.getUserId();
       
        String fileName = fullyQualifedFileName(userId);
        
        File file = new File(fileName);
        
        File absFile = new File(file.getAbsolutePath());

        try {
            absFile.createNewFile();
      
            Path path = Paths.get(fileName);
        
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(student, writer);
                
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
    public boolean exists(String userId) throws NonRecoverableException {
        String fileName = fullyQualifedFileName(userId);
        
        File file = new File(fileName);
        
        File absFile = new File(file.getAbsolutePath());
        
        return absFile.exists();
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public Student retrieve(String userId) throws ObjNotFoundException {
       // ToDo: add funcationality
       throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public StudentModel findModelById(String userId) throws ObjNotFoundException, NonRecoverableException {
        // ToDo: add funcationality
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String userId) throws NonRecoverableException {
        // ToDo: add funcationality
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Return the fully qualified name of the user file for the given user.
     * 
     * @param userId the id of the user whose session file name is returned.
     * @return a String specifying a fully qualified file name.
     */
    private String fullyQualifedFileName(String userId) {
        return DATA_DIRECTORY + "Student_" + userId.replace('@', '_').replace('.', '_') + ".json";
    }
}


