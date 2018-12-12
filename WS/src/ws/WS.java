/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import javax.xml.ws.Endpoint;
import ws.services.ChatImpl;
import ws.util.WsUtil;

/**
 *
 * @author Nonato Dias
 */
public class WS {

    private static String BASE_URL = "http://localhost:9999";
    private static String CHAT_SERVICE = "/chat-service";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        WsUtil.log("Starting Server ...");
        ChatImpl cs = new ChatImpl(); 
        Endpoint.publish(BASE_URL + CHAT_SERVICE, cs);
        WsUtil.log("Listining on "+ BASE_URL + CHAT_SERVICE + "\n");
    }
}
