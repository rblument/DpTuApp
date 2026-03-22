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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.regis.dptu.model.Account;

/** Unit tests for {@link Account}. */
@SuppressWarnings("Logging")
class AccountTest {

    @Test
    void testSettersGettersAndClear() {
        Account a = new Account();

        a.setUserId("hsherwin");
        a.setFirstName("Harrison");
        a.setLastName("Sherwin");
        a.setPassword("secret");
        a.setSecurityQuestion(0);
        a.setSecurityAnswer("Denver");
        a.setIsStudent(false);

        assertEquals("hsherwin", a.getUserId());
        assertEquals("Harrison", a.getFirstName());
        assertEquals("Sherwin", a.getLastName());
        assertEquals("secret", a.getPassword());
        assertEquals(0, a.getSecurityQuestion());
        assertEquals("Denver", a.getSecurityAnswer());
        assertFalse(a.isStudent());
        assertFalse(a.getIsStudent());

        a.clear();

        assertNull(a.getUserId());
        assertNull(a.getFirstName());
        assertNull(a.getLastName());
        assertNull(a.getPassword());
        assertEquals(0, a.getSecurityQuestion());
        assertNull(a.getSecurityAnswer());

        // clear() should NOT modify student status (per Account.clear()).
        assertFalse(a.isStudent());
        assertFalse(a.getIsStudent());
    }

    @Test
    void testToStringDoesNotThrow() {
        Account a = new Account();
        a.setUserId("u1");
        a.setFirstName("F");
        a.setLastName("L");

        String s = a.toString();

        assertNotNull(s);
        assertTrue(
                s.contains("u1") || s.contains("User"), "toString should include identifying info");
    }
}
