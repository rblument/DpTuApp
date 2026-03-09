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

import edu.regis.dptu.svc.TutorReply;

public class TutorReplyTest {

    @Test
    public void testDefaultAndMutators() {
        TutorReply defaultReply = new TutorReply();
        assertEquals("ERR", defaultReply.getStatus());
        assertEquals("", defaultReply.getData());

        TutorReply reply = new TutorReply("OK", "payload");
        reply.setStatus("UPDATED");
        reply.setData("new-data");

        assertEquals("UPDATED", reply.getStatus());
        assertEquals("new-data", reply.getData());
    }
}

