/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.Hashtable;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Nonato Dias
 */
public class QueueHandler {
    private final Hashtable properties = new Hashtable();
    private Context context = null;
    private QueueConnectionFactory qfactory = null;
    private QueueConnection qconnection = null;
    private QueueSession qsession = null;

    public QueueHandler() {
        properties.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "org.exolab.jms.jndi.InitialContextFactory"
        );
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
    }
    
    public void init() throws NamingException, JMSException{
        context = new InitialContext(properties);
        qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        qconnection = qfactory.createQueueConnection();
        log("qconnection ok");
        qsession = qconnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        log("qsession ok");
    }
    
    public void startQConnection() throws JMSException{
        log("Starting qconnection ...");
        qconnection.start();
        log("qconnection started");
    }
    
    public javax.jms.Queue createQueue(String qName) 
            throws NamingException, JMSException
    {
        log("Creating user "+qName+" ...");
        return qsession.createQueue(qName);
    }
    
    public QueueReceiver createReciver(String qName) 
            throws NamingException, JMSException
    {
        javax.jms.Queue dest = (javax.jms.Queue)context.lookup(qName);
        QueueReceiver qreceiver = qsession.createReceiver(dest);
        return qreceiver;
    }
    
    public QueueSender createSender(String qName) 
            throws NamingException, JMSException
    {
        javax.jms.Queue dest = (javax.jms.Queue) context.lookup(qName);
        QueueSender sender = qsession.createSender(dest);
        return sender;
    }
    
    public TextMessage createMessage(String text) throws JMSException{
        TextMessage message = qsession.createTextMessage();
	message.setText(text);
        return message;
    }
    
    public javax.jms.Queue getQueue(String qName) throws NamingException{
        return (javax.jms.Queue) context.lookup(qName);
    }
    
    public void close() throws NamingException, JMSException{
        context.close();
	qconnection.close();
    }
    
    public void log(String text){
        System.out.println(text);
    }
}
