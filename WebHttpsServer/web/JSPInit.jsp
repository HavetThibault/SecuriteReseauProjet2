<%-- 
    Document   : JSPInit
    Created on : 14 nov. 2021, 20:56:31
    Author     : akdim
--%>

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
            <form class="m-2" method="GET" action="ServletControler"> 
                <button class="btn btn-secondary" type="submit" value="looseMoney" name="action">Loose some money</button>
            </form>
<!--            <div class="col-6">
                <button type="submit"  class="btn btn-secondary" name="action" value="looseMoney">Loose some money</button>
            </div>-->
        </div>
    </body>
</html>
