/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utilities.MySQLBean;

/**
 *
 * @author Thibault
 */
public class ClientDBManagerBean extends MySQLBean implements Serializable
{
    public ClientDBManagerBean()
    {
        super();
    }
    
    /* 
    */
    public synchronized boolean insertClient(Client client) throws SQLException
    {
        //vérification si passager existant
        PreparedStatement selectRequest = DBConnection.prepareStatement("SELECT COUNT(*) from Clients where ? like Login");
        selectRequest.setString(1, client.getLogin());
        ResultSet rs = selectRequest.executeQuery();
        rs.next();
        if(rs.getInt("COUNT(*)") == 0)
        {
            // insertion du passager responsable
            PreparedStatement insertRequest = DBConnection.prepareStatement("INSERT INTO Clients VALUES (?,?,?,null,null)");

            insertRequest.setString(1, client.getLogin());
            insertRequest.setString(2, client.getNom());
            insertRequest.setString(3, client.getPrenom());

            insertRequest.execute();
            Commit(); 
            return true;
        }
        return false;
    }
    
    public synchronized boolean checkClientPassword(String login, String pwd) throws SQLException
    {
        PreparedStatement selectRequest = DBConnection.prepareStatement("SELECT Login, Password from Clients WHERE Login LIKE ?");
        selectRequest.setString(1, login);
        ResultSet rs = selectRequest.executeQuery();
        if(rs.next())
        {
            if(pwd.equals(rs.getString("Password")))
                return true;
        }
        return false;
    }
}
