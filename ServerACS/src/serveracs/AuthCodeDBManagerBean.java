/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveracs;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import utilities.MySQLBean;

/**
 *
 * @author Thibault
 */
public class AuthCodeDBManagerBean extends MySQLBean implements Serializable {

    public AuthCodeDBManagerBean() {
        super();
    }

    /* 
     */
    public synchronized boolean insertAuthCode(String authCode) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        PreparedStatement insertRequest = DBConnection.prepareStatement("INSERT INTO authcodes VALUES (?,?,?)");

        insertRequest.setString(1, authCode);
        insertRequest.setBoolean(2, true);
        
        Calendar calendar = java.util.Calendar.getInstance();
        Date now = calendar.getTime();
        insertRequest.setTimestamp(3, new Timestamp(now.getTime()));
        System.out.println(insertRequest.toString());
        insertRequest.execute();
        Commit();
        return true;
    }
    
    public synchronized boolean checkAuthenticationCode(String authCode) throws SQLException
    {
        PreparedStatement selectRequest = DBConnection.prepareStatement("SELECT COUNT(*) FROM authcodes WHERE ");
        ResultSet rs = selectRequest.getResultSet();
        
        // TODO:
        // Verification de validite (5 minutes) + mettre le code à false
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date nowLess5Minutes = calendar.getTime();
        
        
        
        return rs.getInt("COUNT(*)") == 1;
    }
}
