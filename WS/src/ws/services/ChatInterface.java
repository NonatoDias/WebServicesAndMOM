/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.services;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Nonato Dias
 */
@WebService
public interface ChatInterface {
    @WebMethod Boolean addUser(String user);
    
    @WebMethod String [] getUsers();
    
    @WebMethod Boolean removeUser(String user);
    
    @WebMethod Boolean sendMessage(String from, String to, String message);
    
    @WebMethod String [] receiveMessage(String user);
}
