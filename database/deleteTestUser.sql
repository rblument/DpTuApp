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
/**
 * Author:  rickb
 * Created: Feb 21, 2025
 */

DELETE FROM Account WHERE UserId = 'test@regis.edu';

DELETE FROM StudentModel WHERE UserId = 'test@regis.edu';

# Note this will clear every user. It resents the auto increment to zero.
TRUNCATE TABLE Assessment;

TRUNCATE TABLE TutoringSession;

TRUNCATE TABLE PendingTask;

TRUNCATE TABLE PendingStep;