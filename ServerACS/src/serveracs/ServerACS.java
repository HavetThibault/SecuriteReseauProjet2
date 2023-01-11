/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveracs;

import CHAMessages.AuthenticationCodeAnswer;
import CHAMessages.AuthenticationCodeRequest;
import GenericMessages.Answer;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author loicx
 */
public class ServerACS {

    static private String CODE_PROVIDER = "BC";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\RealProjet3DSecure\\ACSKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
            
            System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\RealProjet3DSecure\\ACSKeystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
            
            Security.addProvider(new BouncyCastleProvider());

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocketForPortAuth
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(6666);
            SSLServerSocket sslserversocketForPortMoney
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(3333);

            System.out.println("Waiting for clients to send notifications. \n");
            
            new Thread(() -> {
                DoActionsOnAuthPort(sslserversocketForPortAuth);
            }).start();

            new Thread(() -> {
                DoActionsOnMoneyPort(sslserversocketForPortMoney);
            }).start();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static boolean IsCorrectBankAccount(String bankAccount) {
        if ("BE".equals(bankAccount.substring(0, 2))) {
            if (IsDigitOnly(bankAccount.substring(2))) {
                //System.out.println("Bank account correct");
                return true;
            }
        }
        return false;
    }

    private static boolean IsCorrectAuthCode(String authCode) {
        return true;
    }

    private static boolean IsDigitOnly(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String GenerateAuthCode() {
        int length = 6;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        String codeAuth = sb.toString();
//        System.out.println(codeAuth);
        return codeAuth;
    }

    private static BufferedWriter GetBufferedWriter(SSLSocket sslsocket) throws IOException {
        OutputStream outputstream = sslsocket.getOutputStream();
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream));
        return bufferedwriter;
    }

    private static BufferedReader GetBufferedReader(SSLSocket sslsocket) throws IOException {
        InputStream inputstream = sslsocket.getInputStream();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        return bufferedreader;
    }

    private static void DoActionsOnAuthPort(SSLServerSocket sslserversocketForPortAuth) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            try(FileInputStream keyStoreInputStream = new FileInputStream("D:\\SSLCertificates\\RealProjet3DSecure\\ACSKeystore.jks")){
                ks.load(keyStoreInputStream,"changeit".toCharArray());
            } 
            
            PrivateKey rsaPrivateKey = (PrivateKey) ks.getKey("rsaKey1", "changeit".toCharArray());
            PublicKey rsaClientCHAPublicKey = ks.getCertificate("certClientCHA").getPublicKey();
            
            Signature rsaSignature = Signature.getInstance("SHA1withRSA", CODE_PROVIDER);
            
            while (true) {
                SSLSocket sslsocketForPortAuth = (SSLSocket) sslserversocketForPortAuth.accept();
                try
                {
                    ObjectInputStream chaInputStream = new ObjectInputStream(sslsocketForPortAuth.getInputStream());
                    ObjectOutputStream chaOutputStream = new ObjectOutputStream(sslsocketForPortAuth.getOutputStream());
                    AuthenticationCodeRequest chaClientRequest = (AuthenticationCodeRequest)chaInputStream.readObject();
                    System.out.println("Réception d'un client.");
                    System.out.println("Vérification de ses données bancaires...");
                    if(IsCorrectBankAccount(chaClientRequest.getCardNumber()))
                    {
                        System.out.println("Données bancaire correctes.");
                        System.out.println("Vérification de la signature.");
                        rsaSignature.initVerify(rsaClientCHAPublicKey);
                        rsaSignature.update(chaClientRequest.getObjectBytes());

                        if(rsaSignature.verify(chaClientRequest.getSignature()))
                        {
                            System.out.println("Signature correcte.");
                            System.out.println("Génération d'un code d'authentification...");
                            String authenticationCode = generateAuthenticationCode();
                            rsaSignature.initSign(rsaPrivateKey);
                            rsaSignature.update(AuthenticationCodeAnswer.getObjectBytes(authenticationCode));
                            byte[] signature = rsaSignature.sign();
                            System.out.println("Envoi du code d'authentification '" + authenticationCode + "' au clientCHA.");
                            chaOutputStream.writeObject(new AuthenticationCodeAnswer(authenticationCode,signature));

                            System.out.println("Sauvegarde du code d'authentification.");
                            addAuthenticationCode(authenticationCode);
                        }
                        else
                            chaOutputStream.writeObject(new Answer(false, "Bad signature from ClientCHA."));
                    }
                    else
                        chaOutputStream.writeObject(new Answer(false, "Bad card number."));
                }
                catch (IOException | ClassNotFoundException |InvalidKeyException | SignatureException ex){
                    Logger.getLogger(ServerACS.class.getName()).log(Level.SEVERE, null, ex);
                }
                sslsocketForPortAuth.close();
            }
        } 
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException exception) {
            Logger.getLogger(ServerACS.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private static String generateAuthenticationCode() throws NoSuchAlgorithmException
    {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < 7; i++)
            strBuilder.append(String.valueOf(secureRandom.nextInt(10)));
        return strBuilder.toString();
    }
    
    private static synchronized void addAuthenticationCode(String authenticationCode)
    {
        authenticationCodeList.add(authenticationCode);
    }
    
    private static synchronized boolean checkAuthenticationCode(String authenticationCode)
    {
        for(int i = 0; i < authenticationCodeList.size(); i++)
            if(((String)authenticationCodeList.get(i)).equals(authenticationCode))
            {
                authenticationCodeList.remove(i);
                return true;
            }
        return false;
    }
    
    private static ArrayList authenticationCodeList = new ArrayList();

    private static void DoActionsOnMoneyPort(SSLServerSocket sslserversocketForPortMoney) {
        try
        {
            while (true) {
                SSLSocket sslsocketForPortMoney = (SSLSocket) sslserversocketForPortMoney.accept();
                
                BufferedReader bufferedReaderForPortMoney = GetBufferedReader(sslsocketForPortMoney);
                BufferedWriter bufferedWriterForPortMoney = GetBufferedWriter(sslsocketForPortMoney);
                String authenticationCode;
                if ((authenticationCode = bufferedReaderForPortMoney.readLine()) != null) {
                    System.out.println("Received authentication code: " + authenticationCode);
                    System.out.println("Sending a response to client...");

                    if(checkAuthenticationCode(authenticationCode))
                        bufferedWriterForPortMoney.write("ACK");
                    else
                        bufferedWriterForPortMoney.write("NACK");
                    bufferedWriterForPortMoney.newLine();
                }
                bufferedWriterForPortMoney.close();
                bufferedReaderForPortMoney.close();
                sslsocketForPortMoney.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerACS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
