/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveracq;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author loicx
 */
public class ServerACQ {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        try {
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\ServerKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

//            Provider[] providers = Security.getProviders();
//            for (int i = 0; i < providers.length; i++) {
//                System.out.println(providers[i].propertyNames());
//            }

            // Create an SSL context and set it as the default for all SSL connections
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, null, null);
//            SSLContext.setDefault(sslContext);

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);

            // Set the list of enabled cipher suites for the server socket
//            String[] cipherSuites = {"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_GCM_SHA384"};
//            sslserversocket.setEnabledCipherSuites(cipherSuites);
//            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();
//
//            // Send a message to the client
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
//            out.println("Hello from the server!");
//            out.flush();
//
//            // Receive a message from the client
//            BufferedReader in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
//            String message = in.readLine();
//            System.out.println("Received message: " + message);
//
//            // Close the socket and the streams
//            sslsocket.close();
//            out.close();
//            in.close();
            while (true) {
                SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

                InputStream inputstream = sslsocket.getInputStream();
                //DataInputStream dataInputStream = new DataInputStream(inputstream);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputstream);
                InputStreamReader inputstreamreader = new InputStreamReader(objectInputStream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

//                new Thread((Runnable) bufferedreader).start();
                String message = bufferedreader.readLine();
                System.out.println("Received message: " + message);
//                PrintWriter out = new PrintWriter(new OutputStreamWriter(sslsocket.getOutputStream()));

                OutputStream outputstream = sslsocket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
                OutputStreamWriter outputstreamwriter = new OutputStreamWriter(objectOutputStream);
                BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
                bufferedwriter.write("I got your message, thank you! \n");
                
                bufferedwriter.newLine();
                bufferedwriter.flush();
//                PrintWriter out = new PrintWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
//                out.println("Hello from the server!");
//                out.flush();
//                new myThread(bufferedreader).start();

                // Close the socket and the streams
                bufferedwriter.close();
                bufferedreader.close();
                sslsocket.close();
            }
            //Note : tjs fermer un stream mais si on ferme le DataInputStream /ObjectInputStream, ca va fermer le inputstream avec, qui va lui meme fermer le socket
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
