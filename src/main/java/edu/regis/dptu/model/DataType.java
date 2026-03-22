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
package edu.regis.dptu.model;

/**
 * Known primitive data type values that can be stored as the value of a Variable within a Problem.
 *
 * @author rickb
 */
@SuppressWarnings("Logging")
public enum DataType {
    BOOLEAN("boolean"),

    DOUBLE("double"),

    FLOAT("float"),

    INT("int"),

    LONG("long"),

    STRING("String");

    private String prettyName;

    private DataType(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
