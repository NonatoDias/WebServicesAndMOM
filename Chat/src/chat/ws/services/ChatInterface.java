
package ws.services;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ChatInterface", targetNamespace = "http://services.ws/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ChatInterface {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "removeUser", targetNamespace = "http://services.ws/", className = "ws.services.RemoveUser")
    @ResponseWrapper(localName = "removeUserResponse", targetNamespace = "http://services.ws/", className = "ws.services.RemoveUserResponse")
    @Action(input = "http://services.ws/ChatInterface/removeUserRequest", output = "http://services.ws/ChatInterface/removeUserResponse")
    public Boolean removeUser(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sendMessage", targetNamespace = "http://services.ws/", className = "ws.services.SendMessage")
    @ResponseWrapper(localName = "sendMessageResponse", targetNamespace = "http://services.ws/", className = "ws.services.SendMessageResponse")
    @Action(input = "http://services.ws/ChatInterface/sendMessageRequest", output = "http://services.ws/ChatInterface/sendMessageResponse")
    public Boolean sendMessage(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "receiveMessage", targetNamespace = "http://services.ws/", className = "ws.services.ReceiveMessage")
    @ResponseWrapper(localName = "receiveMessageResponse", targetNamespace = "http://services.ws/", className = "ws.services.ReceiveMessageResponse")
    @Action(input = "http://services.ws/ChatInterface/receiveMessageRequest", output = "http://services.ws/ChatInterface/receiveMessageResponse")
    public List<String> receiveMessage(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addUser", targetNamespace = "http://services.ws/", className = "ws.services.AddUser")
    @ResponseWrapper(localName = "addUserResponse", targetNamespace = "http://services.ws/", className = "ws.services.AddUserResponse")
    @Action(input = "http://services.ws/ChatInterface/addUserRequest", output = "http://services.ws/ChatInterface/addUserResponse")
    public Boolean addUser(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getUsers", targetNamespace = "http://services.ws/", className = "ws.services.GetUsers")
    @ResponseWrapper(localName = "getUsersResponse", targetNamespace = "http://services.ws/", className = "ws.services.GetUsersResponse")
    @Action(input = "http://services.ws/ChatInterface/getUsersRequest", output = "http://services.ws/ChatInterface/getUsersResponse")
    public List<String> getUsers();

}
