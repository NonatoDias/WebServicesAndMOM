/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.jms.JMSException;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 *
 * @author Nonato Dias
 */
public class MessageQueue {
    private QueueHandler handler = null;
    
    public void init() throws NamingException, JMSException, MalformedURLException{
        handler = new QueueHandler();
        handler.log("\nMessageQueue");
        handler = new QueueHandler();
        handler.initSession();
        handler.startQConnection();
    }
    
    public void addMessage(String user, String text) 
            throws NamingException, JMSException{
        QueueSender sender = handler.createSender(user);
        sender.send(
            handler.createMessage(text)
        );
    }
    
    public String getMessage(String user) 
            throws NamingException, JMSException{
        QueueReceiver receiver = handler.createReciver(user);
        TextMessage textMessage = (TextMessage) receiver.receive();
        return textMessage.getText();
    }
    
    public void close() throws NamingException, JMSException {
        handler.close();
    }
}
