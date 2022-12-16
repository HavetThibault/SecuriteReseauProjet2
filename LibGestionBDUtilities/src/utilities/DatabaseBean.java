/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 *
 * @author Thibault
 */
public abstract class DatabaseBean implements Serializable
{

    /**
     * @return the DBConnection
     */
    public Connection getDBConnection() {
        return DBConnection;
    }

    /**
     * @param DBConnection the DBConnection to set
     */
    public void setDBConnection(Connection DBConnection) {
        this.DBConnection = DBConnection;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the IP_or_HostName
     */
    public String getIP_or_HostName() {
        return IP_or_HostName;
    }

    /**
     * @param IP_or_HostName the IP_or_HostName to set
     */
    public void setIP_or_HostName(String IP_or_HostName) {
        this.IP_or_HostName = IP_or_HostName;
    }

    /**
     * @return the SID
     */
    public String getSID() {
        return SID;
    }

    /**
     * @param SID the SID to set
     */
    public void setSID(String SID) {
        this.SID = SID;
    }

    /**
     * @return the Service
     */
    public String getService() {
        return Service;
    }

    /**
     * @param Service the Service to set
     */
    public void setService(String Service) {
        this.Service = Service;
    }

    /**
     * @return the driverClass
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * @param driverClass the driverClass to set
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
            
    protected String user;
    protected String password;
    
    protected transient Connection DBConnection;
    protected transient Statement CurrentStatement;
    
    protected String url;
    
    protected String port;    
    protected String IP_or_HostName;
    
    protected String SID;
    protected String Service;
    
    protected String driverClass;
    
    protected boolean isRunning;
    
    public DatabaseBean()
    {
        DBConnection = null;
        user = null;
        password = null;   
        CurrentStatement = null;
        url = null;
        port = null;
        IP_or_HostName = null;
        SID = null;
        Service = null;
        driverClass = null;
        
        isRunning = false;
    }
    
    public synchronized void initWithSID(String url, String IP_or_HostName, String port, String SID, String driverClass, String user, String pwd) throws ClassNotFoundException, SQLException
    {
        System.out.println("Recherche de la classe : '" + driverClass + "' !");
        Class leDriver = Class.forName(driverClass);
        System.out.println(url + IP_or_HostName + ":" + port + ":" + SID);
        setDBConnection(DriverManager.getConnection(url + IP_or_HostName + ":" + port + ":" + SID,user,pwd));
        
        this.setUser(user);
        this.setPassword(pwd);
        
        this.setUrl(url);
        this.setPort(port);
        this.setIP_or_HostName(IP_or_HostName);
        this.setSID(SID);
        this.setDriverClass(driverClass);
        
        getDBConnection().setAutoCommit(false);
        
        CurrentStatement = getDBConnection().createStatement();
    }
    
    public synchronized void initWithServiceName(String url, String IP_or_HostName, String port, String Service, String driverClass, String user, String pwd) throws ClassNotFoundException, SQLException
    {
        if(DBConnection != null)
        {
            if(DBConnection.isClosed() == false)
                DBConnection.close();
        }
            
        Class leDriver = Class.forName(driverClass);
        System.out.println(url + IP_or_HostName + ":" + port + "/" + Service);
        setDBConnection(DriverManager.getConnection(url + IP_or_HostName + ":" + port + "/" + Service,user,pwd));
        
        this.setUser(user);
        this.setPassword(pwd);
        
        this.setUrl(url);
        this.setPort(port);
        this.setIP_or_HostName(IP_or_HostName);
        this.setService(Service);
        this.setDriverClass(driverClass);
        
        getDBConnection().setAutoCommit(false);
        
        CurrentStatement = getDBConnection().createStatement();
    }
    
    public synchronized void close() throws SQLException
    {
        if(getDBConnection() != null)
        {
            DBConnection.commit();
            getDBConnection().close();
            setDBConnection(null);
        }
    }
    
    public synchronized void run()
    {
        isRunning = true;
    }
    
    public synchronized void stop()
    {
        isRunning = false;
    }
    
    public synchronized ResultSet Execute(String sql) throws SQLException, BeanIsNotRunningException
    {
        if(isRunning)
        {
            if(CurrentStatement.execute(sql))
                return CurrentStatement.getResultSet();
            else
                return null;
        }
        else
            throw new BeanIsNotRunningException();
    }
    
    
    
    public synchronized void Commit() throws SQLException
    {
        if(getDBConnection() != null && isRunning)
            getDBConnection().commit();
    }
    
    public synchronized void Rollback() throws SQLException
    {
        if(getDBConnection() != null && isRunning)
            getDBConnection().rollback();
    }
    
    public synchronized void Save() throws FileNotFoundException, IOException
    {
        StringTokenizer tok = new StringTokenizer(this.getClass().getName(), ".");
        String nomClasse = null;
        
        while(tok.hasMoreElements())
            nomClasse = tok.nextToken();
        
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("./src/" + nomClasse +  ".ser"));

        ois.writeObject(this);
        
        ois.close();
    }
    
    public synchronized void Disconnect() throws SQLException
    {
        DBConnection.close();
    }
    
    public static java.util.Date getAccurateDate(ResultSet resultSet, String field, Calendar calendar) throws SQLException
    {
        long dateTime = resultSet.getTimestamp(field).getTime();
        calendar.setTime(new Date(dateTime));
        return calendar.getTime();
    }
}
