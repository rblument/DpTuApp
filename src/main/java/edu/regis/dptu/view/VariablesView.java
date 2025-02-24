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
package edu.regis.dptu.view;

import edu.regis.dptu.model.CodeModel;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author danielaflores
 */
public class VariablesView extends GPanel{
    
    private JFrame variablesView;
    private JPanel panel;
    private String stringX;
    private String stringY;
    private int i;
    private int j;
    
    public VariablesView(String stringX, String stringY){
        this.stringX = stringX.toUpperCase();
        this.stringY = stringY.toUpperCase();
        updateView();
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        variablesView = new JFrame("Variables");
        variablesView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        variablesView.setSize(400, 200);
        
        JPanel panel = new JPanel();
        JLabel rLabel = new JLabel("r = " + rowColumn(this.stringX));
        JLabel cLabel = new JLabel("c = " + colColumn(this.stringY));
        JLabel jLabel = new JLabel("j = " + j);
        JLabel iLabel = new JLabel("i = " + i);
        JLabel lLabel = new JLabel("L = " + updateView());

        panel.add(rLabel);
        panel.add(cLabel);
        panel.add(jLabel);
        panel.add(iLabel);
        panel.add(lLabel);
        variablesView.add(panel);

        variablesView.setVisible(true);
    }
    
    private void layoutComponents() {
        addc(variablesView, 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL,
                5, 5, 5, 5);
    }
    
    public int rowColumn(String stringX){
        return stringX.length();
    }
    
    public int colColumn(String stringY){
        return stringY.length();
    }
    
    public int[][] updateView() {
        int c = colColumn(stringY);
        int r = rowColumn(stringY);
        int[][] L = new int[i][j];
       
        for(i = 1; i < c - 1; i++){ //c = n
            L[i][-1] = 0;
        }
        for(j = 0; i < r - 1; i++){ //r = m
            L[-1][j] = 0;
        }
        for(i = 0; i < c - 1; i++){ //c = n
            for(j = 0; i < r - 1; i++){ //r = m
                if(stringX.charAt(i) == stringY.charAt(j)){
                    L[i][j]=L[i-1][j-1] + 1;
                } 
                else{
                    L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]); 
                }
            }
        }
        return L;
    }
}
