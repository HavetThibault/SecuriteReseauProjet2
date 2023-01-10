/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletUtilities;

import DBClasses.AuthCode;
import DBClasses.AuthCodeDBManagerBean;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author loicx
 */
@WebServlet(urlPatterns = {"/ServletControler"})
public class ServletControler extends HttpServlet {

    private final static String codeProvider = "BC";
    private AuthCodeDBManagerBean authCodeDBManagerBean;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        authCodeDBManagerBean = new AuthCodeDBManagerBean();
        try {
            authCodeDBManagerBean.initWithServiceName("localhost", "3306", "projetsecu2dbclients", "root", "oui123");
        } catch (SQLException ex) {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage() + " ** " + ex.getSQLState());
            return;
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage());
            return;
        }
        authCodeDBManagerBean.run();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);

        String action = request.getParameter("action");
        String lastAction = (String) session.getAttribute("LastAction.Action");
        if (lastAction == null) {
            lastAction = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/ServerCHA";
        }

        if (action == null) {
            action = lastAction;
        }

        switch (action) {
            case "SendBankData":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                SSLSocket sslsocket = GetFreshlyOpenedSSLSocket();
                BufferedWriter bufferedwriter = GetBufferedWriter(sslsocket);

                String message = "I'm sending my bank information : DATE : " + dtf.format(now) + ". BANK ACCOUNT : BE66123412341234";
                bufferedwriter.write(message + '\n');
                bufferedwriter.flush();
                BufferedReader bufferedreader = GetBufferedReader(sslsocket);
                String bufferedString = bufferedreader.readLine();
                if (bufferedString.contains("correct")) {
                    String authCode = RetrieveAuthFromString(bufferedString);

                    try {
                        //authCodeDBManagerBean.insertAuthCode(new AuthCode("255555", new java.util.Date()));
                        authCodeDBManagerBean.insertAuthCode(new AuthCode(authCode, java.util.Date.from(now.atZone(ZoneId.systemDefault()).toInstant())));
                    } catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException ex) {
                        Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ServletUtils.redirectStoreURL("/ServerCHA?ac=" + authCode, request, response, session);
                    System.out.println(bufferedString);

                    bufferedwriter.close();
                    bufferedreader.close();
                    sslsocket.close();
                    break;
                }

                System.out.println(bufferedString);

                bufferedwriter.close();
                bufferedreader.close();
                sslsocket.close();

                ServletUtils.redirectStoreURL("/ServerCHA", request, response, session);
                break;
        }
    }

    private String RetrieveAuthFromString(String s) {
        return s.substring(s.length() - 6);
    }

    private SSLSocket GetFreshlyOpenedSSLSocket() throws IOException {

        System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\Projet3DSecure\\CHATrustStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 6666);
        return sslsocket;
    }

    private BufferedWriter GetBufferedWriter(SSLSocket sslsocket) throws IOException {
        OutputStream outputstream = sslsocket.getOutputStream();
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream));
        return bufferedwriter;
    }

    private BufferedReader GetBufferedReader(SSLSocket sslsocket) throws IOException {
        InputStream inputstream = sslsocket.getInputStream();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        return bufferedreader;
    }

}
