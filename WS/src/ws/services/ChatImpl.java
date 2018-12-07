/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.services;

import javax.jws.WebService;
import ws.util.WsUtil;

/**
 *
 * @author Nonato Dias
 */
@WebService(endpointInterface = "ws.services.ChatInterface")
public class ChatImpl implements ChatInterface{

    @Override
    public Boolean addUser(String user) {
        WsUtil.log("New user added: " + user);
        return true;
    }

    @Override
    public String[] getUsers() {
        String [] users = {"nonato", "jose"};
        return users;
    }

    @Override
    public Boolean removeUser(String user) {
        WsUtil.log("User removed: " + user);
        return null;
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
    
}
