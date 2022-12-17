/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBClasses;

/**
 *
 * @author Thibault
 */
public class Client 
{

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * @return the Login
     */
    public String getLogin() {
        return Login;
    }

    /**
     * @param Login the Login to set
     */
    public void setLogin(String Login) {
        this.Login = Login;
    }
    /**
     * @return the Nom
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Nom to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the Prenom
     */
    public String getFirstname() {
        return Firstname;
    }

    /**
     * @param Firstname the Prenom to set
     */
    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }
    
    private String Name;
    private String Firstname;
    private String Login;
    private String Password;
    
    public Client(String login, String password, String name, String firstname)
    {
        Login = login;
        Name = name;
        Firstname = firstname;
        Password = password;
    }
}
