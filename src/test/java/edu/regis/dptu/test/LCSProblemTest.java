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

import edu.regis.dptu.model.LCSProblem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rickb
 */
public class LCSProblemTest {
    
    public LCSProblemTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testAll() {
        String x = "skullandbones";
        String y = "lullabybabies";
        
        LCSProblem problem = new LCSProblem(x, y);
        problem.reset();
        
        assertEquals(x.length(), problem.getN());
        assertEquals(y.length(), problem.getM());
        assertEquals(LCSProblem.EXECUTION_STATE.PRE, problem.getExecutionState());
        
        problem.step(); // advance r_loop from Java index -1 to 0;
       
        assertEquals(LCSProblem.EXECUTION_STATE.R_LOOP, problem.getExecutionState());
    
        // Finish the r loop
        for (int i = 0; i <= problem.getN() + 1; i++)
            problem.step();
        
        assertEquals(LCSProblem.EXECUTION_STATE.C_LOOP, problem.getExecutionState());
       
        // advance c from Java index -1 to 1 since c_loop starts at DP cell 0
        problem.step(); 
        
       
        // finish the c loop
        for (int i = 2; i <= problem.getM() + 1; i++)
            problem.step();
        
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
       
        // i == -1 and j == -1, step will assign i = 1 and j = 0
        problem.step();
        
        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
        
        for (int s = 1; s <= problem.getM(); s++) 
            problem.step();
        
        
        problem.step(); // forces exit of j_loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        problem.step(); // begin second iteration of i_loop
        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
        
        // i == 2 and j = 0
        for (int s = 1; s <= problem.getM(); s++)
            problem.step();

        problem.step(); // forces exit of j_loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        problem.step(); // begin third iteration of i_loop
        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
        
        // i == 3 and j = 0
        for (int s = 1; s <= problem.getM(); s++)
            problem.step();

        for (int row = 4; row < problem.getM(); row++) {
            problem.step(); // forces exit of j_loop
            assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
            problem.step(); // begin next iteration of i_loop
            assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
            
            for (int s = 1; s <= problem.getM(); s++) 
                problem.step();
        } 
        
       
        problem.step(); // forces exit of j_loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        
        problem.step(); // begin last iteration of i_loop
        assertEquals(LCSProblem.EXECUTION_STATE.J_LOOP, problem.getExecutionState());
        
        for (int s = 1; s <= problem.getM(); s++) 
            problem.step();
        
        problem.step(); // forces exit of j_loop
        assertEquals(LCSProblem.EXECUTION_STATE.I_LOOP, problem.getExecutionState());
        
        problem.step(); // foce exit of i_loop
        
        assertEquals(LCSProblem.EXECUTION_STATE.POST, problem.getExecutionState());
        
        problem.prettyPrint();
  
    }
}
