/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TagTools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author Thibault
 */
public class TagNavigationBar extends TagSupport {

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     * @return 
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public int doStartTag() throws JspException 
    {
        JspWriter out = pageContext.getOut();
        String servletControlerURL = "/WebHttpsServer/ServletControler";
        try 
        {
            out.write(
                "<header class=\"p-3 mb-3 border-bottom\">\n" +
                "    <div class=\"container\">\n" +
                "       <div class=\"d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start\">\n" +
                "           <a href=\"/\" class=\"d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none\">\n" +
                "           <img class=\"bi me-2\" width=\"80\" height=\"32\" src=\"Pictures/HeplIcon.png\"/>\n" +
                "       </a>\n" +

                "       <ul class=\"nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0\">\n" +
                "           <li><p class=\"d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none\">EZ SecuRéseaux</p></li>" +        
                "       </ul>\n" +

                "       <div class=\"text-end\">" +  
                "           <form method=\"GET\" action=\"" + servletControlerURL + "\" class=\"nav-link px-2 link-secondary\">\n" +
                "               <button class=\"btn btn-outline-secondary\" type=\"submit\" name=\"action\" value=\"Deconnexion\">Déconnexion</button>" +
                "           </form>" +
                "       </div>" +
                "   </div>\n" +
                "</header>");
        }
        catch (IOException ex) {
            Logger.getLogger(TagNavigationBar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return SKIP_BODY;
    }
    
}
