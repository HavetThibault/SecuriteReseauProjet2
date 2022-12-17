/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CryptoUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 *
 * @author Thibault
 */
public class CryptoUtils {
    
    public static byte[] CalculateSaltedDigestSHA1(long Today, double randomNumber, String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        
        md.update(password.getBytes());
                
        // On se sert d'un byteArrayInputStream pour obtenir les bytes d'un 'long' et d'un 'double'
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        
        bdos.writeLong(Today); 
        bdos.writeDouble(randomNumber);
        
        md.update(baos.toByteArray());
                
        return md.digest();
    }
    
    public static byte[] CalculateDigestSHA1(byte[] subject) throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        md.update(subject);
        return md.digest();
    }
}
