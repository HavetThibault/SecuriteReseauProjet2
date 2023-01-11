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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author loicx
 */
public class ServerACQ {

    /**
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     * @throws java.security.NoSuchProviderException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
        try {
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\RealProjet3DSecure\\ACQKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
            
            System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\RealProjet3DSecure\\ACQKeystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocketForHttpsCommunication
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);

            System.out.println("Waiting for an authentication code from HTTPS server. \n");

            DoActionsOnHTTPSCommunicationPort(sslserversocketForHttpsCommunication);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static String RetrieveAuthFromString(String s) {
        return s.substring(s.length() - 6);
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

    private static void DoActionsOnHTTPSCommunicationPort(SSLServerSocket sslserversocketForHttpsCommunication) throws IOException {
        try {
            while (true) {
                SSLSocket sslSocketForHttps = (SSLSocket) sslserversocketForHttpsCommunication.accept();

                //HTTPS reader / writer
                BufferedReader bufferedReaderForHttps = GetBufferedReader(sslSocketForHttps);
                BufferedWriter bufferedWriterForHttps = GetBufferedWriter(sslSocketForHttps);

                String httpsClientData;
                while ((httpsClientData = bufferedReaderForHttps.readLine()) != null) {
                    System.out.println("Received message: " + httpsClientData);
                    SendMessageOnHTTPSCommunicationPort("Message received, analyzing the authentication code... \n", bufferedWriterForHttps);
                    System.out.println("Sending the information to ACS server, and waiting for its answer...");
                    String ACSResponse = AskACSOnMoneyPort(httpsClientData);
                    if (ACSResponse.contains("NACK") || ACSResponse.contains("FAILED")) {
                        SendMessageOnHTTPSCommunicationPort("Authentication refused. \n", bufferedWriterForHttps);
                    } else {
//                        SendMessageOnHTTPSCommunicationPort("Authentication accepted. \n", bufferedWriterForHttps);
                    }

                }

                bufferedWriterForHttps.close();
                bufferedReaderForHttps.close();
                sslSocketForHttps.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void SendMessageOnHTTPSCommunicationPort(String message, BufferedWriter bufferedWriterForHttps) throws IOException {
        bufferedWriterForHttps.write(message);
        bufferedWriterForHttps.flush();
    }

    private static String AskACSOnMoneyPort(String httpsClientData) throws IOException {
        try {
            
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocketForACS = (SSLSocket) sslsocketfactory.createSocket("localhost", 3333);

            //ACS reader / writer
            BufferedReader bufferedReaderForACS = GetBufferedReader(sslSocketForACS);
            BufferedWriter bufferedWriterForACS = GetBufferedWriter(sslSocketForACS);

            String messageForACS = "I got this code from an HTTPS server, is it a correct one : " + RetrieveAuthFromString(httpsClientData) + "\n";
            bufferedWriterForACS.write(messageForACS);
            bufferedWriterForACS.flush();
            String ACSResponse = bufferedReaderForACS.readLine();
            System.out.println("Response from ACS server : " + ACSResponse);

            bufferedWriterForACS.close();
            bufferedReaderForACS.close();
            sslSocketForACS.close();

            return ACSResponse;

        } catch (IOException exception) {
            exception.printStackTrace();
            return "FAILED";
        }
    }

}
