/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    public synchronized boolean insertAuthCode(AuthCode authCode) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        PreparedStatement insertRequest = DBConnection.prepareStatement("INSERT INTO authcodes VALUES (?,?,?)");

        insertRequest.setString(1, authCode.getValue());
        insertRequest.setString(2, dateFormat.format(authCode.getCreationDate()));
        insertRequest.setInt(3, authCode.getIsUsable() ? 1 : 0);
        System.out.println(insertRequest.toString());
        insertRequest.execute();
        Commit();
        return true;
    }
}
