/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServletUtilities;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thibault
 */
public class ServletUtils {
    
    public static void redirectStoreURL(String url, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException
    {
        String redirectUrl = request.getScheme()+"://"+request.getServerName()+ ":"+request.getServerPort() + url;
        response.sendRedirect (redirectUrl);
        session.setAttribute("LastAction.URL", redirectUrl);
    }
    
    public static void redirect(String url, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendRedirect (request.getScheme()+"://"+request.getServerName()+ ":"+request.getServerPort() + url);
    }
    
    public static void removeAllAttributes(HttpSession session)
    {
        Enumeration<String> names = session.getAttributeNames();
        while(names.hasMoreElements())
        {
            session.removeAttribute(names.nextElement());
        }
    }
}
