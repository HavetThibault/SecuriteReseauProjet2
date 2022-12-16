/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author Thibault
 */
public class BeanIsNotRunningException extends Exception
{
    public BeanIsNotRunningException()
    {
        super("Tentative d'appel d'une méthode alors que le Bean n'a pas été lancé !");
    }
}
