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
 * Created: Jan 26, 2025
 */

# If the DpTuDB exists, drop it. In general, you will lose any existing data.
DROP DATABASE IF EXISTS DpTuDB;

#
# Create a database in MySql
CREATE DATABASE DpTuDB;

# Create user representing the tutor.
CREATE USER 'DpTuTs'@'localhost' IDENTIFIED BY 'DpTu2023';

# Give the DpTuTs tutor the following priveledges.
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON DpTuDB.* TO 'DpTuTs'@'localhost';

# Switch to the DpTuDB
USE DpTuDB;