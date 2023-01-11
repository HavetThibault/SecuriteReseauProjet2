/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chaapplication;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thibault
 */
public class CHAApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try 
        {
            new FrameAuthenticationCode().setVisible(true);
        } 
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | UnrecoverableKeyException ex) {
            Logger.getLogger(CHAApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
