package ServletUtilities;


import java.security.Security;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Web application lifecycle listener.
 *
 * @author Thibault
 */
public class InitServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
