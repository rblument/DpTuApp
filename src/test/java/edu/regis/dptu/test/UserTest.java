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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.regis.dptu.model.User;

public class UserTest {

    @Test
    public void testUserConstructorsAndMutators() {
        User user = new User("user@regis.edu", "encrypted");
        user.setUserId("updated@regis.edu");
        user.setPassword("pw2");

        assertEquals("updated@regis.edu", user.getUserId());
        assertEquals("pw2", user.getPassword());
        assertTrue(user.toString().contains("updated@regis.edu"));
    }
}
