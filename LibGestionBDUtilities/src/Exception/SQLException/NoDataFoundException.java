/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception.SQLException;

/**
 *
 * @author Thibault
 */
public class NoDataFoundException extends Exception
{
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    private final String message;
    
    public NoDataFoundException(String m)
    {
        message = m;
    }
}
