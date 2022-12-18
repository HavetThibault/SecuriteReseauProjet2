/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveracs;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.net.ssl.SSLContext;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author loicx
 */
public class ServerACS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
//        Security.addProvider(new BouncyCastleProvider());
        try {
            System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\TrustStore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

            // Create an SSL context and set it as the default for all SSL connections
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, null, null);
//            SSLContext.setDefault(sslContext);
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);

            // Set the list of enabled cipher suites for the socket
//            String[] cipherSuites = {"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_GCM_SHA384"};
//            sslsocket.setEnabledCipherSuites(cipherSuites);
            // Send a message to the server
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
//            out.println("Hello from the client!");
//            out.flush();
//
//            // Receive a message from the server
//            BufferedReader in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
//            String message = in.readLine();
//            System.out.println("Received message: " + message);
//
//            // Close the socket and the streams
//            sslsocket.close();
//            out.close();
//            in.close();
            OutputStream outputstream = sslsocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(objectOutputStream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            String message = "test";
            bufferedwriter.write(message + '\n');
            bufferedwriter.flush();

            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            System.out.println(bufferedreader.readLine());
//            String string = null;
//            while ((string = bufferedreader.readLine()) != null) {
//                if(bufferedreader.readLine().length() == 0) break;
//                bufferedwriter.write(string + '\n');
//                bufferedwriter.flush();
//            }

            bufferedwriter.close();
            bufferedreader.close();
            sslsocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
