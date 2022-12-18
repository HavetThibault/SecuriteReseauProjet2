/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveracs;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author loicx
 */
public class ServerACS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
        try {
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\ServerKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(6666);

            System.out.println("Waiting for clients to send notifications. \n");

            while (true) {
                SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

                InputStream inputstream = sslsocket.getInputStream();
                BufferedReader bufferedReaderForACQ = new BufferedReader(new InputStreamReader(inputstream));

                OutputStream outputstream = sslsocket.getOutputStream();
                BufferedWriter bufferedWriterForACQ = new BufferedWriter(new OutputStreamWriter(outputstream));

                String httpsClientData;
                while ((httpsClientData = bufferedReaderForACQ.readLine()) != null) {

                    System.out.println("Received message: " + httpsClientData);
                    System.out.println("Sending a response to client... \n");
                    bufferedWriterForACQ.write("I got your message, thank you! \n");
                    bufferedWriterForACQ.flush();
                }

                bufferedWriterForACQ.close();
                bufferedReaderForACQ.close();
                sslsocket.close();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
//        try {
//            System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\TrustStore.jks");
//            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//
//            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);
//
//            OutputStream outputstream = sslsocket.getOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputstream);
//            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(objectOutputStream);
//            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
//
//            String message = "Hi I am a simple client looking for attention.";
//            bufferedwriter.write(message + '\n');
//            bufferedwriter.flush();
//
//            InputStream inputstream = sslsocket.getInputStream();
//            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
//            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
//
//            System.out.println(bufferedreader.readLine());
//
//            bufferedwriter.close();
//            bufferedreader.close();
//            sslsocket.close();
//        } 
//        catch (IOException exception) {
//            exception.printStackTrace();
//        }
    }

}
