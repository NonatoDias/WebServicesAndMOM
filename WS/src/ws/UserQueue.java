/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.*;

/**
 *
 * @author Nonato Dias
 */
public class UserQueue {
    
    private QueueHandler handler = null;
    
    public void init() throws NamingException, JMSException, MalformedURLException{
        handler = new QueueHandler();
        handler.createAdmin();
    }
    
    public boolean addUser(String user) throws NamingException, JMSException{
        return handler.addDestination(user);
    }
    
    public boolean removeUser(String user) throws NamingException, JMSException{
        return handler.removeDestination(user);
    }
    
    public ArrayList<String> getAllUsers() throws JMSException{
        return handler.getAllDestinations();
    }

    public void close() throws NamingException, JMSException {
        handler.close();
    }
}
