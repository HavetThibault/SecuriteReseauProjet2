/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
    public synchronized boolean insertClient(Client client) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        // Vérification si passager existant
        PreparedStatement selectRequest = DBConnection.prepareStatement("SELECT COUNT(*) from Clients where ? like Login");
        selectRequest.setString(1, client.getLogin());
        ResultSet rs = selectRequest.executeQuery();
        rs.next();
        if(rs.getInt("COUNT(*)") == 0)
        {
            // Insertion du passager responsable
            PreparedStatement insertRequest = DBConnection.prepareStatement("INSERT INTO Clients VALUES (?,?,?,?,?,?)");
            
            insertRequest.setString(1, client.getLogin());
            insertRequest.setString(2, client.getName());
            insertRequest.setString(3, client.getFirstname());
            double passwordRandomNumber = new SecureRandom().nextDouble();           
            long passwordDate = new Date().getTime();
            byte[] saltedHashedPwd =  CryptoUtils.CryptoUtils.CalculateSaltedDigestSHA1(passwordDate, passwordRandomNumber, client.getPassword());
            
            insertRequest.setLong(4, passwordDate);
            insertRequest.setDouble(5, passwordRandomNumber);
            insertRequest.setBytes(6, saltedHashedPwd);
            System.out.println(insertRequest.toString());
            insertRequest.execute();
            Commit(); 
            return true;
        }
        return false;
    }
    
    public synchronized boolean checkClientPassword(String login, String pwd) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        PreparedStatement selectRequest = DBConnection.prepareStatement("SELECT Login, PasswordDate, PasswordRandomNumber, Password from Clients WHERE Login LIKE ?");
        selectRequest.setString(1, login);
        ResultSet rs = selectRequest.executeQuery();
        if(rs.next())
        {
            double passwordRandomNumber = rs.getDouble("PasswordRandomNumber");
            long passwordDate = rs.getLong("PasswordDate");
            byte[] saltedHashedPwd =  CryptoUtils.CryptoUtils.CalculateSaltedDigestSHA1(passwordDate, passwordRandomNumber, pwd);
            System.out.println("Comparing : " + rs.getBytes("Password") + " and " + saltedHashedPwd);
            if(MessageDigest.isEqual(rs.getBytes("Password"), saltedHashedPwd))
                return true;
        }
        return false;
    }
}
