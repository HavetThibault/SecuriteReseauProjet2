/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CHAMessages;

/**
 *
 * @author Thibault
 */
public class AuthenticationCodeAnswer {

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
        this.authenticationCode = authenticationCode;
        this.signature = signature;
    }
}
