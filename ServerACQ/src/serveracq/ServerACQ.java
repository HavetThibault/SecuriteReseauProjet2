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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.net.ssl.SSLSocketFactory;
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
        try {
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\ServerKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

            System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\TrustStore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            
            System.out.println("Waiting for HTTPS server to send a notification, then i will contact ACS server. \n");

            while (true) {
                SSLSocket sslSocketForHttps = (SSLSocket) sslserversocket.accept();
                SSLSocket sslSocketForACS = (SSLSocket) sslsocketfactory.createSocket("localhost", 6666);

                //HTTPS reader / writer
                InputStream inputstreamForHttps = sslSocketForHttps.getInputStream();
                BufferedReader bufferedReaderForHttps = new BufferedReader(new InputStreamReader(inputstreamForHttps));
                OutputStream outputstreamForHttps = sslSocketForHttps.getOutputStream();
                BufferedWriter bufferedWriterForHttps = new BufferedWriter(new OutputStreamWriter(outputstreamForHttps));

                //ACS reader / writer
                OutputStream outputStreamForACS = sslSocketForACS.getOutputStream();
                BufferedWriter bufferedWriterForACS = new BufferedWriter(new OutputStreamWriter(outputStreamForACS));
                InputStream inputStreamForACS = sslSocketForACS.getInputStream();
                BufferedReader bufferedReaderForACS = new BufferedReader(new InputStreamReader(inputStreamForACS));

                String httpsClientData;
                while ((httpsClientData = bufferedReaderForHttps.readLine()) != null) {
                    System.out.println("Received message: " + httpsClientData);
                    bufferedWriterForHttps.write("I got your message, thank you! \n");
                    bufferedWriterForHttps.flush();
                    System.out.println("Sending the information to ACS server, and waiting for its answer...");
                    String messageForACS = "Hi I am an ACQ client, i'm sending a notification in reaction of an HTTPS contacting me! \n";
                    bufferedWriterForACS.write(messageForACS);
                    bufferedWriterForACS.flush();
                    System.out.println("Response from ACS server : " + bufferedReaderForACS.readLine());
                }

                bufferedWriterForHttps.close();
                bufferedReaderForHttps.close();
                sslSocketForHttps.close();
                bufferedWriterForACS.close();
                bufferedReaderForACS.close();
                sslSocketForACS.close();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
