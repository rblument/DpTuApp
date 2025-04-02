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

import java.util.ArrayList;

/**
 *
 * @author cadencea TODO- This File is unfinished and may not have full
 * functionality.
 */
public class CodeModel extends TitledModel {

    /**
     * Declares the ArrayList that will hold the statements that CodeView will
     * visualize.
     */
    private ArrayList statementList;

    /**
     * Constructor for the Code Model. Initializes the statementList variable
     * that will hold the statements for the code panel.
     *
     * TODO- Currently the lines are painstakingly hard-coded out, at some point
     * we will need to be able to pull from various examples with ideally an
     * easier way to write them out.
     */
    public CodeModel() {
        statementList = new ArrayList();
        statementList.add("<html><pre>"
                + "<b>for</b> i =1 to n-1 <b>do</b>\n"
                + "   L[i,-1] = 0"
                + "</pre></html>");
        statementList.add("<html><pre>"
                + "<b>for</b> j =0 to m-1 <b>do</b>\n"
                + "   L[-1,j] = 0"
                + "</pre></html>");
        statementList.add("<html><pre>"
                + "<b>for</b> i =0 to n-1 <b>do</b>\n"
                + "   <b>for</b> j =0 to m-1 <b>do</b>\n"
                + "       <b>if</b> x<sub>i</sub> = y<sub>j</sub> <b>then</b>\n"
                + "           L[i, j] = L[i-1, j+1] + 1\n"
                + "       <b>else</b>\n"
                + "           L[i, j] = max(L[i-1,j], L[i, j-1])"
                + "</pre></html>");
        statementList.add("<html><pre>"
                + "<b>return</b> array L"
                + "</pre></html>");
    }

    /**
     * Returns the statementList ArrayList holding each code statement.
     *
     * @return statementList
     */
    public ArrayList getStatementList() {
        return statementList;
    }

}
