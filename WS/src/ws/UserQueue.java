/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.io.*;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
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
    private javax.jms.Queue queue;
    
    public void init() throws NamingException, JMSException{
        handler = new QueueHandler();
        handler.init();
        handler.startQConnection();
    }
    
    public void createUser(String name) throws NamingException, JMSException{
        //queue = handler.createSender(name);
        this.onReceived(name);
    }
    
    public void sendMessage(String user, String text) throws NamingException, JMSException{
        handler.log("Sendig message: "+text + " - To: "+ user);
        handler.createSender(user).send(
            handler.createMessage(text)
        );
    }
    
    public void getAllUsers(){
        /*QueueReceiver qreceiver = handler.createReciver("queue1");
        TextMessage textMessage = null;
        String text = ((TextMessage) qreceiver.receive()).getText();
        handler.log(" Mensagem Recebida: " + text);  */
    }
    
    public void onReceived(String qName) throws NamingException, JMSException{
        QueueReceiver qreceiver = handler.createReciver(qName);
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
}
