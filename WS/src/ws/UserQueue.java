/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.io.*;
import java.net.MalformedURLException;
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
    
    private static String QUEUE_NAME = "queue1";
    private QueueHandler handler = null;
    private javax.jms.Queue queue;
    private QueueSender sender = null;
    private QueueReceiver receiver = null;
    
    public void init() throws NamingException, JMSException, MalformedURLException{
        handler = new QueueHandler();
        handler.createAdmin();
        /*this.onReceived();
        sender = handler.createSender(QUEUE_NAME);
        receiver = handler.createReciver(QUEUE_NAME);*/
    }
    
    public boolean addUser(String user) throws NamingException, JMSException{
        return handler.addDestination(user);
    }
    
    public boolean removeUser(String user) throws NamingException, JMSException{
        return handler.removeDestination(user);
    }
    
    public String [] getAllUsers() throws JMSException{
        String [] aux = {"A"};
        return aux; 
    }
    
    public void onReceived() throws NamingException, JMSException{
        QueueReceiver qreceiver = handler.createReciver(QUEUE_NAME);
        TextMessage textMessage = null;
        Thread t = new Thread(()->{
            while (true) {
                try {
                    String text = ((TextMessage) qreceiver.receive()).getText();
                    handler.log(" Mensagem Recebida: " + text);  
                
                } catch (JMSException ex) {
                   ex.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void close() throws NamingException, JMSException {
        handler.close();
    }
}
