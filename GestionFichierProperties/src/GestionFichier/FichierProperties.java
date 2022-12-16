package GestionFichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Thibault
 */
public class FichierProperties {

    /**
     * @return the nomFichier
     */
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * @param nomFichier the nomFichier to set
     */
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    /**
     * @return the cheminRepertoire
     */
    public String getCheminRepertoire() {
        return cheminRepertoire;
    }

    /**
     * @param cheminRepertoire the cheminRepertoire to set
     */
    public void setCheminRepertoire(String cheminRepertoire) {
        this.cheminRepertoire = cheminRepertoire;
    }

    /**
     * @param Prop the Prop to set
     */
    public void setProp(Properties Prop) {
        this.Prop = Prop;
    }
    
    public int size(){
        return Prop.size();
    }
    
    // -------------------------------- VARIABLES ---------------------------------
    
    private Properties Prop;
    private String nomFichier;
    private String cheminRepertoire;
    
    // ------------------------------- CONSTRUCTEUR -------------------------------
    
    public FichierProperties(String nomFolder, String nomFichier) throws IOException
    {
        Prop = new Properties();
        
        this.nomFichier = nomFichier;
        System.out.println("Chemin du fichier : " + System.getProperty("user.home") + System.getProperty("file.separator") + nomFolder + System.getProperty("file.separator"));
        this.cheminRepertoire = System.getProperty("user.home") + System.getProperty("file.separator") + nomFolder + System.getProperty("file.separator");
    }
    
    public String getFullPath()
    {
        return cheminRepertoire + nomFichier;
    }
    
    // -------------------------------- FONCTIONALITES ---------------------------------
    
    public void LoadOrInit(HashMap map) throws FileNotFoundException, IOException
    {
        
        if(FichierExists())
        {
            try (InputStreamReader propertiesStreamReader = new InputStreamReader(new FileInputStream(getFullPath()))) 
            {
                System.out.println("Chargement du fichier de properties : " + getFullPath());

                Prop.load(propertiesStreamReader);

                System.out.println("Ajout des champs manquants...");
                Set<String> setExistingPropertiesNames = Prop.stringPropertyNames();

                Iterator propertiesName = map.keySet().iterator();
                try (FileWriter propertyFileWriter = new FileWriter(getFullPath(), true)) 
                {
                    while(propertiesName.hasNext())
                    {
                        String propertyName = (String)propertiesName.next();                        
                        if(!setExistingPropertiesNames.contains(propertyName))
                        {
                            Prop.setProperty(propertyName, nomFichier);
                            propertyFileWriter
                                    .write(propertyName + "=" + map.get(propertyName).toString() + System.getProperty("line.separator"));
                        }
                    }
                }
            }
        }
        else
        {
            System.out.println("Création du fichier de properties : " + getFullPath());
            CreerFichierProperties();
            System.out.println("Initialisation du fichier...");
            InitFichierProperties(map);
            System.out.println("Chargement des properties...");
            try (InputStreamReader propertiesStreamReader = new InputStreamReader(new FileInputStream(getFullPath()))) 
            {
                Prop.load(propertiesStreamReader);
            }
        }

        try (InputStreamReader propertyFileStream = new InputStreamReader(new FileInputStream(getFullPath()))) {
            Prop.load(propertyFileStream);
        }
    }
    
    public void Load() throws IOException
    {
        try (InputStreamReader propertiesStreamReader = new InputStreamReader(new FileInputStream(getFullPath()))) 
        {
            System.out.println("Chargement des properties...");
            Prop.load(propertiesStreamReader);
        }
    }
    
    // Crée le fichier .properties vide et le répertoire s'il n'existe pas deja
    private void CreerFichierProperties() throws IOException
    {
        Path pathRepertoire = Paths.get(cheminRepertoire);
        
        // Creation du repertoire
        Files.createDirectories(pathRepertoire);

        // Creation du fichier .properties vide
        File fichierProp = new File(getFullPath());
    }
    
    // Remplit le fichier properties avec les valeurs de la HashMap passées en paramètre
    private void InitFichierProperties(HashMap map) throws IOException
    {
        // Parcours de la liste des valeurs
        Collection keys = map.keySet();
        
        Iterator iter = keys.iterator();
        
        try (OutputStreamWriter BW = new OutputStreamWriter(new FileOutputStream(getFullPath()))) 
        {
            Object currentValue;
            
            System.out.println("Parcours des valeurs de la hashmap :");
            // Parcours des valeurs 1 par 1 et ajout de celle-ci dans le fichier
            while(iter.hasNext())
            {
                // Ajout de la valeur dans le fichier properties
                currentValue = iter.next();
                
                System.out.println("Objet : " + currentValue.toString());
                
                BW.write(currentValue.toString() + "=" + map.get(currentValue).toString() + System.getProperty("line.separator"));
            }
        }
    }
    
    public boolean FichierExists()
    {
        return Files.exists(Paths.get(getFullPath()));
    }
    
    public String getProperty(String key)
    {
        return Prop.getProperty(key);        
    }
}
