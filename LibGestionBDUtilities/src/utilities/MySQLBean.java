/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;


import java.sql.SQLException;


/**
 *
 * @author Thibault
 */
public class MySQLBean extends DatabaseBean
{
    public static final int NO_DATA_FOUND = 1054;

    
    public MySQLBean()
    {
        super();
    }
    
    public synchronized void initWithServiceName(String IP_or_hostName, String port, String Service, String user, String password) throws SQLException, ClassNotFoundException
    {
        super.initWithServiceName("jdbc:mysql://", IP_or_hostName, port, Service, "com.mysql.cj.jdbc.Driver", user, password);
    }
    
    public synchronized void initWithSID(String IP_or_hostName, String port, String SID, String user, String password) throws SQLException, ClassNotFoundException
    {
        super.initWithSID("jdbc:mysql://", IP_or_hostName, port, SID, "com.mysql.cj.jdbc.Driver", user, password);
    }
}
