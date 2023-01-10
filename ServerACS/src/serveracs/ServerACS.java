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
import java.util.Random;
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
            System.setProperty("javax.net.ssl.keyStore", "D:\\SSLCertificates\\Projet3DSecure\\AuthServerKeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

            SSLServerSocketFactory sslserversocketfactory
                    = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocketForPortAuth
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(6666);
            SSLServerSocket sslserversocketForPortMoney
                    = (SSLServerSocket) sslserversocketfactory.createServerSocket(3333);

            System.out.println("Waiting for clients to send notifications. \n");
            
            new Thread(() -> {
                try {
                    DoActionsOnAuthPort(sslserversocketForPortAuth);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    DoActionsOnMoneyPort(sslserversocketForPortMoney);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static boolean IsCorrectBankAccount(String message) {
        String bankAccount = message.substring(message.length() - 16, message.length());
//        System.out.println(bankAccount);
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

    private static void DoActionsOnAuthPort(SSLServerSocket sslserversocketForPortAuth) throws IOException {
        try {
            while (true) {
                SSLSocket sslsocketForPortAuth = (SSLSocket) sslserversocketForPortAuth.accept();

                BufferedReader bufferedReaderForPortAuth = GetBufferedReader(sslsocketForPortAuth);
                BufferedWriter bufferedWriterForPortAuth = GetBufferedWriter(sslsocketForPortAuth);

                String httpsClientData;
                while ((httpsClientData = bufferedReaderForPortAuth.readLine()) != null) {
                    System.out.println("Received message: " + httpsClientData);
                    System.out.println("Sending a response to client... \n");

                    if (httpsClientData.contains("BANK ACCOUNT")) {
                        if (IsCorrectBankAccount(httpsClientData)) {
                            String authCode = GenerateAuthCode();
                            bufferedWriterForPortAuth.write("Your bank account number is correct, here is your authentication code : " + authCode + "\n");
                            bufferedWriterForPortAuth.flush();
                        }
                    } else {
                        bufferedWriterForPortAuth.write("I don't understand your request! \n");
                        bufferedWriterForPortAuth.flush();
                    }
                }

                bufferedWriterForPortAuth.close();
                bufferedReaderForPortAuth.close();
                sslsocketForPortAuth.close();

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void DoActionsOnMoneyPort(SSLServerSocket sslserversocketForPortMoney) throws IOException {
        try {
            while (true) {
                SSLSocket sslsocketForPortMoney = (SSLSocket) sslserversocketForPortMoney.accept();

                BufferedReader bufferedReaderForPortMoney = GetBufferedReader(sslsocketForPortMoney);
                BufferedWriter bufferedWriterForPortMoney = GetBufferedWriter(sslsocketForPortMoney);
                String httpsClientData;
                while ((httpsClientData = bufferedReaderForPortMoney.readLine()) != null) {
                    System.out.println("Received message: " + httpsClientData);
                    System.out.println("Sending a response to client... \n");

                    if (httpsClientData.contains("is it a correct one")) {
                        if (IsCorrectAuthCode(httpsClientData)) {
                            bufferedWriterForPortMoney.write("ACK : Correct Authentication Code. \n");
                            bufferedWriterForPortMoney.flush();
                        } else {
                            bufferedWriterForPortMoney.write("NACK : Incorrect Authentication Code. \n");
                            bufferedWriterForPortMoney.flush();
                        }
                    } else {
                        bufferedWriterForPortMoney.write("I don't understand your request \n");
                        bufferedWriterForPortMoney.flush();
                    }
                }
                bufferedWriterForPortMoney.close();
                bufferedReaderForPortMoney.close();
                sslsocketForPortMoney.close();

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
