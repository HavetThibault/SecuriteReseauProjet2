<%-- 
    Document   : JSPError
    Created on : 17 nov. 2021, 17:34:40
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
    <link rel="stylesheet" href="Css/Bootstrap5.0.2.css"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
    <TagTools:TagNavigationBar/>
    <div class="container-sm bg-light">
        <h3 class="m-2">Error</h3>
        <p class=" m-2"><%=(String)session.getAttribute("msgerror")%></p>
        <form method="GET" action="/WebHttpsServer/ServletControler"> 
            <button class="btn btn-secondary m-2" type="submit" value="Retour" name="action">Retour</button
        </form>
    </div>
</body>
</html>
