/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CHAMessages;

import GenericMessages.Answer;
import java.io.Serializable;

/**
 *
 * @author Thibault
 */
public class AuthenticationCodeAnswer extends Answer implements Serializable {

    static public byte[] getObjectBytes(String cardNumber)
    {
        return cardNumber.getBytes();
    }
    
    public byte[] getObjectBytes()
    {
        return AuthenticationCodeAnswer.getObjectBytes(authenticationCode);
    }
    
    /**
     * @return the authenticationCode
     */
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    /**
     * @param authenticationCode the authenticationCode to set
     */
    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
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
    
    private String authenticationCode;
    private byte[] signature;
    
    public AuthenticationCodeAnswer(String authenticationCode, byte[] signature)
    {
        super(true, "");
        this.authenticationCode = authenticationCode;
        this.signature = signature;
    }
}
