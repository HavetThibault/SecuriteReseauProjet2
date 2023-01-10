package ServletUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DBClasses.Client;
import DBClasses.ClientDBManagerBean;
import GestionFichier.FichierProperties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
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
 * @author Thibault
 */
@WebServlet(urlPatterns = {"/ServletControler"})
public class ServletControler extends HttpServlet {

    private final static String codeProvider = "BC";

    private ClientDBManagerBean clientDBManagerBean;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // ---------------- PROPERTIES ------------------------
        FichierProperties prop;
        try {
            HashMap map = new HashMap();

            //map.put("IP_SERV_BILLET", "192.168.1.9");
            prop = new FichierProperties("WebHttpsServer", "Serv.properties");
            prop.LoadOrInit(map);
        } catch (IOException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // ----------------------- BEAN DBAirport ------------------------
        // portTicket = Integer.parseInt(prop.getProperty("PORT_TICKET"));
        // IpServBillet = prop.getProperty("IP_SERV_BILLET");
        clientDBManagerBean = new ClientDBManagerBean();
        try {
            clientDBManagerBean.initWithServiceName("localhost", "3306", "projetsecu2dbclients", "root", "oui123");
        } catch (SQLException ex) {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage() + " ** " + ex.getSQLState());
            return;
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage());
            return;
        }
        clientDBManagerBean.run();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);

        String action = request.getParameter("action");
        String lastAction = (String) session.getAttribute("LastAction.Action");
        if (lastAction == null) {
            lastAction = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/WebHttpsServer";
        }

        if (action == null) {
            action = lastAction;
        }
        switch (action) {

            case "Connection":
                String login = request.getParameter("login");
                String pwd = request.getParameter("password");
                try {
                    if (clientDBManagerBean.checkClientPassword(login, pwd)) {
                        session.setAttribute("login", login);
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                    } else {
                        sendErrorMsg("Wrong login or password.", request, response, session);
                    }
                } catch (SQLException ex) {
                    sendErrorMsg("Erreur JDBC : " + ex.getMessage() + " ** " + ex.getSQLState(), request, response, session);
                } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
                    Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "loseMoney":
                SSLSocket sslsocket = GetFreshlyOpenedSSLSocket();
                BufferedWriter bufferedwriter = GetBufferedWriter(sslsocket);
                
                //NOTE A SECURISER POUR LES INJECTIONS ETC
                String authCodeValue = request.getParameter("inputCode");
                System.out.println(authCodeValue);

                String message = "Here's the authentication code sent by the https server: " + authCodeValue;
                bufferedwriter.write(message + '\n');
                bufferedwriter.flush();
                
                BufferedReader bufferedreader = GetBufferedReader(sslsocket);

                System.out.println(bufferedreader.readLine());

                bufferedwriter.close();
                bufferedreader.close();
                sslsocket.close();
                ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                break;

            case "Inscription":
                ServletUtils.redirectStoreURL("/WebHttpsServer/Inscription.html", request, response, session);
                break;

            case "InscriptionRequest":
                String name = (String) request.getParameter("name");
                String firstname = (String) request.getParameter("firstname");
                login = (String) request.getParameter("login");
                String password = (String) request.getParameter("password");
                session.setAttribute("login", login);
                try {
                    if (clientDBManagerBean.insertClient(new Client(login, password, name, firstname))) {
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                    } else {
                        sendErrorMsg("Login already taken.", request, response, session);
                    }
                } catch (SQLException ex) {
                    sendErrorMsg("Erreur JDBC : " + ex.getMessage() + " ** " + ex.getSQLState(), request, response, session);
                } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
                    Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            default:
                break;
        }
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
            lastAction = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/WebHttpsServer";
        }

        if (action == null) {
            action = lastAction;
        }

        switch (action) {
            case "Inscription":
                ServletUtils.redirectStoreURL("/WebHttpsServer/Inscription.html", request, response, session);
                break;

            case "Connection":
                String login = request.getParameter("login");
                String pwd = request.getParameter("password");
                try {
                    if (clientDBManagerBean.checkClientPassword(login, pwd)) {
                        session.setAttribute("login", login);
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                    } else {
                        sendErrorMsg("Wrong login or password.", request, response, session);
                    }
                } catch (SQLException ex) {
                    sendErrorMsg("Erreur JDBC : " + ex.getMessage() + " ** " + ex.getSQLState(), request, response, session);
                } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
                    Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "InscriptionRequest":
                String name = (String) request.getParameter("name");
                String firstname = (String) request.getParameter("firstname");
                login = (String) request.getParameter("login");
                String password = (String) request.getParameter("password");
                session.setAttribute("login", login);
                try {
                    if (clientDBManagerBean.insertClient(new Client(login, password, name, firstname))) {
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                    } else {
                        sendErrorMsg("Login already taken.", request, response, session);
                    }
                } catch (SQLException ex) {
                    sendErrorMsg("Erreur JDBC : " + ex.getMessage() + " ** " + ex.getSQLState(), request, response, session);
                } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
                    Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "Deconnexion":
                ServletUtils.removeAllAttributes(session);
                ServletUtils.redirectStoreURL("/WebHttpsServer", request, response, session);
                break;

            case "Retour":
                response.sendRedirect(lastAction);
                break;

            default:
                break;

        }

        out.close();
    }

    @Override
    public void destroy() {
        clientDBManagerBean.stop();
        try {
            clientDBManagerBean.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendErrorMsg(String errorMsg, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        session.setAttribute("errorMsg", errorMsg);
        ServletUtils.redirect("/WebHttpsServer/JSPError.jsp", request, response);
    }

    private SSLSocket GetFreshlyOpenedSSLSocket() throws IOException {
        System.setProperty("javax.net.ssl.trustStore", "D:\\SSLCertificates\\Projet3DSecure\\HTTPSTrustStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);

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
