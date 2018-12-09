/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.net.MalformedURLException;
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
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

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
    
    private String url = "tcp://localhost:3035/";
    private String user = "admin";
    private String password = "openjms";
    
    JmsAdminServerIfc admin = null;

    public QueueHandler() {
        properties.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "org.exolab.jms.jndi.InitialContextFactory"
        );
        properties.put(Context.PROVIDER_URL, url);
    }
    
    public void createAdmin() throws JMSException, MalformedURLException{
        if(admin == null){
            admin = AdminConnectionFactory.create(url, user, password);
            log("New admin ok\n");
        }else{
            logErr("Admin has already been created");
        }
    }
    
    public void initSession() throws NamingException, JMSException{
        context = new InitialContext(properties);
        qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        qconnection = qfactory.createQueueConnection();
        log("qconnection ok");
        qsession = qconnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        log("qsession ok");
    }
    
    public boolean destinationExists(String destination) throws JMSException{
          return admin.destinationExists(destination);
    }
    
    public boolean addDestination(String name) throws JMSException{
        log("adding destination "+name);
        if(!destinationExists(name)){
            admin.addDestination(name, Boolean.TRUE);
            log("Added");
            return true;
        }
        logErr("Sorry, destination "+ name+ " can not be created. It already exists!");
        return false;
    }
    
    public boolean removeDestination(String name) throws JMSException{
        log("Removing destination "+name);
        if (admin.removeDestination(name)) {
            log("Removed");
            return true;
        }
        logErr("Failed to remove destination " + name);
        return false;
    }
    
    public void startQConnection() throws JMSException{
        log("Starting qconnection ...");
        qconnection.start();
        log("qconnection started");
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
    public void logErr(String text){
        System.err.println(text);
    }
    
}
