/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author loicx
 */
public class AuthCode {
    private String value;
    private Date creationDate;
    private boolean isUsable;
    
    public String getValue(){
        return value;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public Date getCreationDate(){
        return creationDate;
    }
    
    public void setCreationDate(Date date){
        this.creationDate = date;
    }
    
    public boolean getIsUsable(){
        return isUsable;
    }
    
    public void setIsUsable(boolean isUsable){
        this.isUsable = isUsable;
    }
    
    public AuthCode(String value, Date date){
        this.value = value;
        this.creationDate = date;
        
        isUsable = creationDate.compareTo(new Date()) <= 0;
    }
}
