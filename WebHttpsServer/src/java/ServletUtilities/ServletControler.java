package ServletUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DBClasses.ClientDBManagerBean;
import GestionFichier.FichierProperties;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void init (ServletConfig config) throws ServletException 
    { 
        super.init(config);
        
        // ---------------- PROPERTIES ------------------------
        FichierProperties prop;
        try 
        {
            HashMap map = new HashMap();

            //map.put("IP_SERV_BILLET", "192.168.1.9");
            
            prop = new FichierProperties("WebHttpsServer", "Serv.properties");
            prop.LoadOrInit(map);
        } 
        catch (IOException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
            return ;
        }
        
        // ----------------------- BEAN DBAirport ------------------------
        // portTicket = Integer.parseInt(prop.getProperty("PORT_TICKET"));
        // IpServBillet = prop.getProperty("IP_SERV_BILLET");
        
        clientDBManagerBean = new ClientDBManagerBean();
        try 
        {
            clientDBManagerBean.initWithServiceName("localhost", "3306", "projetsecu2dbclients", "root", "root");
        } 
        catch (SQLException ex) 
        {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage() + " ** " + ex.getSQLState());
            return;
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("Erreur Connexion MySql : " + ex.getMessage());
            return;
        }
        clientDBManagerBean.run(); 
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8"); 
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        
        String action = request.getParameter("action");
        String lastAction = (String)session.getAttribute("LastAction.Action");
        if(lastAction == null)
            lastAction = request.getScheme()+"://"+request.getServerName()+ ":"+request.getServerPort() + "/";
        
        if(action == null)
            action = lastAction;
        
        try
        {
            switch(action){
                case "Inscription":
                    break;

                case "Connexion":
                    String name = request.getParameter("nom");
                    String pwd = request.getParameter("motdepasse");
                    if(clientDBManagerBean.checkClientPassword(name, pwd))
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPInit.jsp", request, response, session);
                    else
                        ServletUtils.redirectStoreURL("/WebHttpsServer/JSPError.jsp", request, response, session);
                    break;

                case "DemandeInscription":
                    break;

                case "Deconnexion":
                    break;
                    
                case "Paiement":
                    break;
                    
                case "Retour":
                    response.sendRedirect((String)session.getAttribute("LastAction.URL"));
                    break;
            }
        }
        catch(SQLException ex)
        {
            session.setAttribute("msgerror", "Erreur JDBC-OBDC : " + ex.getMessage() + " ** " + ex.getSQLState());
            ServletUtils.redirect("/WebHttpsServer/JSPError.jsp", request, response);
        }
        
        out.close();
    }
    
    @Override
    public void destroy()
    {
        clientDBManagerBean.stop();
        try 
        {
            clientDBManagerBean.close();
        } 
        catch (SQLException ex) {
            Logger.getLogger(ServletControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
