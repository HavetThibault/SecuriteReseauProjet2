/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CHAMessages;

import java.util.Date;

/**
 *
 * @author Thibault
 */
public class AuthenticationCodeRequest {

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
