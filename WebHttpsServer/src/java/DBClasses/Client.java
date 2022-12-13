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
     * @return the RegistreNational
     */
    public String getRegistreNational() {
        return RegistreNational;
    }

    /**
     * @param RegistreNational the RegistreNational to set
     */
    public void setRegistreNational(String RegistreNational) {
        this.RegistreNational = RegistreNational;
    }

    /**
     * @return the Nom
     */
    public String getNom() {
        return Nom;
    }

    /**
     * @param Nom the Nom to set
     */
    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    /**
     * @return the Prenom
     */
    public String getPrenom() {
        return Prenom;
    }

    /**
     * @param Prenom the Prenom to set
     */
    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }
    
    private String Nom;
    private String Prenom;
    private String RegistreNational;
    
    public Client(String registreNational, String nom, String prenom)
    {
        RegistreNational = registreNational;
        Nom = nom;
        Prenom = prenom;
    }
}
