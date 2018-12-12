/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.sun.javaws.ui.ApplicationIconGenerator;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ws.services.ChatImplService;
import ws.services.ChatInterface;

/**
 *
 * @author Nonato Dias
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private Label labelRemittee;
    
    @FXML
    private Label labelMe;
    
    @FXML
    private JFXTextField jtfMessage;

    @FXML
    private JFXButton btnAddMessage;
    
    @FXML
    private JFXButton btnLogout;
    
    @FXML
    private ScrollPane scrollUsers;
    
    @FXML
    private ScrollPane scrollMessages;
    
    @FXML
    private StackPane dialogStackPane;
   
    private String loggedUser = "";
    private String remittee = "";
    private ChatInterface chatInterface = null;
    private boolean isLogged = false;
    
    private Thread tReceiveMsg = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollUsers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMessages.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ChatImplService service = new ChatImplService();
        chatInterface = service.getChatImplPort();
       
        dialogStackPane.toFront();
        dialogStackPane.setOnMouseClicked((e)->{
            hide();
            showLoginForm();
        });
        showLoginForm();
        
        btnAddMessage.setOnAction((e)->{
            sendMessage(jtfMessage.getText(), 0);
            jtfMessage.setText("");
        });
        
        btnLogout.setOnAction((e)->{
            logout();
        });
        
        jtfMessage.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                sendMessage(jtfMessage.getText(), 0);
                jtfMessage.setText("");
            }
        });
       
    }   
    
    public void sync(){
        System.out.println("Server sync ...");
        Thread tUsers = new Thread(()->{
            while (true) {
                if(loggedUser.length() > 0){
                    try {
                        Platform.runLater(()->{
                            try {
                                showUsers();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        tUsers.setDaemon(true);
        tUsers.start();
    }
    
    public void showUsers() throws IOException{
        List<String> users = chatInterface.getUsers();
        clearUsers();
        for(String u : users){
            addUser(u);
        }
    }
    
    public void addUser(String name) throws IOException{
        Group groupUser = (Group) FXMLLoader.load(getClass().getResource("userGroupFXML.fxml"));
        ImageView imgView = (ImageView) groupUser.getChildren().get(0);
        //imgView.setImage(new Image(getClass().getResourceAsStream("../img/user-3.png")));
        Label labName = (Label) groupUser.getChildren().get(1); 
        labName.setText(name);
        VBox vbox = (VBox) scrollUsers.getContent();
        vbox.setSpacing(7);
        
        groupUser.setOnMouseClicked((e)->{
            Group source = (Group) e.getSource();
            Label u = (Label) source.getChildren().get(1);
            remittee = u.getText();
            labelRemittee.setText(remittee);
            clearMessages();
            System.out.println(u.getText());
        });
        vbox.getChildren().add(groupUser);
    }

    private void addMessage(String msg, int who) {
        try {
            Group groupMsg = (Group) FXMLLoader.load(getClass().getResource(
                who == 1 ? "messageFXML.fxml" : "myMessageFXML.fxml"
            ));
            ImageView imgView = (ImageView) groupMsg.getChildren().get(0);
            //imgView.setImage(new Image(getClass().getResourceAsStream("../img/user-3.png")));
            Label labName = (Label) groupMsg.getChildren().get(1); 
            labName.setText(msg);
            VBox vbox = (VBox) scrollMessages.getContent();
            vbox.setSpacing(10);
            vbox.getChildren().add(groupMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            scrollMessages.layout();
            scrollMessages.setVvalue(1.0);
        });
    }
    
    private void sendMessage(String msg, int who) {
        if(msg.length() == 0 || remittee.length() == 0){
            return;
        }
         
        if(chatInterface.sendMessage(loggedUser, remittee, remittee + ": "+msg)){
            addMessage(msg, who);
        }
    }
    
    private boolean login(String name) throws IOException{
        if(name.length() == 0){
            return false;
        }
        
        if(chatInterface.addUser(name)){
            loggedUser = name;
            labelMe.setText("Eu: " + name);
            sync();
            tReceiveMsg = new Thread(()->{
                System.out.println("chat.FXMLDocumentController.login()_______");
                while (true) {
                    String msg = chatInterface.receiveMessage(name);
                    try {
                        if(msg.length()>0){
                            Platform.runLater(()->{
                                addMessage(msg, 1);
                            });
                        }
                    } catch (Exception e) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            tReceiveMsg.setDaemon(true);
            tReceiveMsg.start();
            
            return true;
        }
        return false;
    }
    
    private void logout() {
        chatInterface.removeUser(loggedUser);
        clearUsers();
        loggedUser = "";
        tReceiveMsg.interrupt();
        clearMessages();
        showLoginForm();
    }
    
    private void clearUsers(){
        VBox vbox = (VBox) scrollUsers.getContent();
        vbox.setSpacing(7);
        vbox.getChildren().setAll();
    }
   
    private void clearMessages(){
        VBox vbox = (VBox) scrollMessages.getContent();
        vbox.setSpacing(10);
        vbox.getChildren().setAll();
    }
    
    public void showLoginForm(){
        dialogStackPane.setVisible(true);
        
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField nameField = new JFXTextField();
        nameField.setText("");
        content.setHeading(new Text("Login (Nome do usuário)"));
        content.setBody(nameField);
        
        JFXDialog dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btnDialogOK = new JFXButton("OK");
        btnDialogOK.setOnAction((e)->{
            try {
                if(login(nameField.getText())){
                    dialogStackPane.setVisible(false);
                    dialog.close();

                }else{
                    showAlert();
                }
            } catch (IOException ex) {
                showAlert();
            }
        });
        nameField.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                try {
                    if(login(nameField.getText())){
                        dialogStackPane.setVisible(false);
                        dialog.close();

                    }else{
                        showAlert();
                    }
                } catch (IOException ex) {
                    showAlert();
                }
            }
        });
        content.setActions(btnDialogOK);
        dialog.show();
    }
    
    public void showAlert(){
        dialogStackPane.setVisible(true);
        
        JFXDialogLayout content = new JFXDialogLayout();
        JFXTextField nameField = new JFXTextField();
        nameField.setText("");
        content.setHeading(new Text("Atenção"));
        content.setBody(new Text("Este usuário já está logado"));
        
        JFXDialog dialog = new JFXDialog(dialogStackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btnDialogOK = new JFXButton("OK");
        btnDialogOK.setOnAction((e)->{
            showLoginForm();
        });
        content.setActions(btnDialogOK);
        dialog.show();
    }
    
    public void hide(){
        dialogStackPane.setVisible(false);
    }
}
