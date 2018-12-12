/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.services;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jws.WebService;
import javax.naming.NamingException;
import ws.UserQueue;
import ws.util.WsUtil;

/**
 *
 * @author Nonato Dias
 */
@WebService(endpointInterface = "ws.services.ChatInterface")
public class ChatImpl implements ChatInterface{
    UserQueue userQ = null;

    public ChatImpl() {
        createUserQueue();
    }

    @Override
    public Boolean addUser(String user) {
        try {
            return userQ.addUser(user);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<String> getUsers() {
        try {
            return userQ.getAllUsers();
            
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean removeUser(String user) {
        try {
            return userQ.removeUser(user);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean sendMessage(String from, String to, String message) {
        WsUtil.log("Mensagem from "+from + " to "+ to + ": "+ message);
        return null;
    }

    @Override
    public String[] receiveMessage(String user) {
        return null;
    }
    
    public void createUserQueue(){
        try {
            userQ = new UserQueue();
            userQ.init();
            userQ.getAllUsers();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                userQ.close();
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
