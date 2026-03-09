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

import edu.regis.dptu.model.StepCompletionReply;

public class StepCompletionReplyTest {

    @Test
    public void testFlagsAndPayload() {
        StepCompletionReply reply = new StepCompletionReply();
        reply.setIsCorrect(true);
        reply.setIsRepeatStep(true);
        reply.setIsNewTask(true);
        reply.setIsNewStep(true);
        reply.setIsNextStep(true);
        reply.setData("payload");

        assertTrue(reply.isCorrect());
        assertTrue(reply.getIsCorrect());
        assertTrue(reply.isRepeatStep());
        assertTrue(reply.getIsRepeatStep());
        assertTrue(reply.isNewTask());
        assertTrue(reply.getIsNewTask());
        assertTrue(reply.isNewStep());
        assertTrue(reply.getIsNewStep());
        assertTrue(reply.isIsNextStep());
        assertTrue(reply.getIsNextStep());
        assertEquals("payload", reply.getData());
    }
}
