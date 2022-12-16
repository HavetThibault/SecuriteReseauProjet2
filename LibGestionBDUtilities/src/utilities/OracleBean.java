/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author Thibault
 */
public class OracleBean extends DatabaseBean implements Serializable
{
    public OracleBean()
    {
        super();
    }
    
    public void initWithServiceName(String IP_or_hostName, String port, String Service, String user, String password) throws SQLException, ClassNotFoundException
    {
        super.initWithServiceName("jdbc:oracle:thin:@", IP_or_hostName, port, Service, "oracle.jdbc.OracleDriver", user, password);
    }
    
    public void initWithSID(String IP_or_hostName, String port, String SID, String user, String password) throws SQLException, ClassNotFoundException
    {
        super.initWithSID("jdbc:oracle:thin:@", IP_or_hostName, port, SID, "oracle.jdbc.OracleDriver", user, password);
    }
}
