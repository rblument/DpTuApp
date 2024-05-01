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

import javax.swing.*;

/**
 * This scrolls through the code automatically in the background
 * 
 * @author Cameron Christner
 */
public class RenderWorker extends SwingWorker{
    
    JList codeview;
    ListModel model;
    int index;
    int nMax;
    int mMax;
    int n;
    int m;
    int nIndex;
    int mIndex;
    
    
    
    public RenderWorker(JList codeview, ListModel model, int i, int nMax, int mMax, int n, int m, int nIndex, int mIndex){
        
        this.codeview = codeview;
        this.model = model;
        this.index = i;
        this.nMax = nMax;
        this.mMax = mMax;
        this.n = n;
        this.m = m;
        this.nIndex = nIndex;
        this.mIndex = mIndex;
          
    }
    

    @Override
    protected Object doInBackground() throws Exception {
        
        for (int i = index; i < model.getSize(); i++) {
            
            if (i == model.getSize()){
            
            i = 0;
        }

        String line = (String) model.getElementAt(i);

        if (line.contains("for") && line.contains("m")) {

            mIndex = i;
        }
        if (line.contains("for") && line.contains("n")) {

            nIndex = i;
        }

        if (line.equals(" ")) {
            
             if (mIndex != -1 && m < mMax) {

                i = mIndex;
                m++;
                
            } else if (m == mMax) {

                mIndex = -1;
                m = 0;
            }  
            
            else if (nIndex != -1 && n < nMax) {

                i = nIndex;
                n++;
                
            } else if (n == nMax) {

                nIndex = -1;
                n = 0;

            }
        }

        codeview.setSelectedIndex(i);
        
        Thread.sleep(100);
        

        }
                
        return null;
    }
    
}
