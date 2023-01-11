/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CHAMessages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thibault
 */
public class AuthenticationCodeRequest implements Serializable{

    static public byte[] getObjectBytes(String cardNumber, Date date)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(baos);
        try 
        {
            dataOutputStream.writeBytes(cardNumber);
            dataOutputStream.writeLong(date.getTime());
        } 
        catch (IOException ex) {
            Logger.getLogger(AuthenticationCodeRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();
    }
    
    public byte[] getObjectBytes()
    {
        return AuthenticationCodeRequest.getObjectBytes(cardNumber, date);
    }
    
    /**
     * @return the signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the today
     */
    public Date getToday() {
        return date;
    }

    /**
     * @param today the today to set
     */
    public void setToday(Date today) {
        this.date = today;
    }
    
    private String cardNumber;
    private Date date;
    private byte[] signature;
    
    public AuthenticationCodeRequest(String cardNumber, Date date, byte[] signature){
        this.cardNumber = cardNumber;
        this.date = date;
        this.signature = signature;
    }
}
