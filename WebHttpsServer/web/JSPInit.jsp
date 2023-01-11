<%@page language="java" %> 
<%@page contentType="text/html; charset=ISO-8859-1"%> 
<%@page pageEncoding="UTF-8"%> 
<%@page import="java.util.*" %> 
<%@page import="java.text.*" %> 
<%@taglib uri="/WEB-INF/tlds/TagTools.tld" prefix="TagTools" %>
<!DOCTYPE html>

<html> 
    <head> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
        <link rel="stylesheet" href="Css/Bootstrap5.0.2.css"/>
        <title>Accueil</title> 
    </head> 
    <body>
        <TagTools:TagNavigationBar/>
        <div class="text-center">
            <div class="container-md col-3">
                <form class="m-2" method="POST" action="ServletControler"> 
                    <label for="inputPassword5" class="form-label m-2">Authentication code (6 characters):</label>
                    <input type="password" name="inputCode" id="inputPassword5" class="form-control m-2">
                    <button class="btn btn-secondary" type="submit" value="loseMoney" name="action">Lose some money</button>
                </form>
                <%
                    if(session.getAttribute("infoMsg") != null)
                    {
                        out.write("<label class=\"m-2\">");
                        out.write((String)session.getAttribute("infoMsg"));
                        out.write("</label>");
                    }
                %>
            </div>
        </div>
    </body>
</html>
