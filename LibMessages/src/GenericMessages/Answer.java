/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenericMessages;

import java.io.Serializable;

/**
 *
 * @author Thibault
 */
public class Answer implements Serializable{
    
    private boolean success;
    private String message;
    
    public Answer(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
