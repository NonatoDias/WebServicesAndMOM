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
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
    private JFXTextField jtfMessage;

    @FXML
    private JFXButton btnAddMessage;
    
    @FXML
    private JFXButton btnLogout;
    
    @FXML
    private ScrollPane scrollUsers;
    
    @FXML
    private StackPane dialogStackPane;
   
    private String loggedUser = "";
    private ChatInterface chatInterface = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollUsers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ChatImplService service = new ChatImplService();
        chatInterface = service.getChatImplPort();
       
        dialogStackPane.toFront();
        dialogStackPane.setOnMouseClicked((e)->{
            hide();
            showLoginForm();
        });
        showLoginForm();
        
        btnAddMessage.setOnAction((e)->{
            addMessage();
        });
        
        btnLogout.setOnAction((e)->{
            logout();
        });
        
        jtfMessage.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                addMessage();
            }
        });
    }   
    
    public boolean addUser(String name) throws IOException{
        if(name.length() == 0){
            return false;
        }
        
        if(chatInterface.addUser(name)){
            loggedUser = name;
            
            Group groupUser = (Group) FXMLLoader.load(getClass().getResource("userGroupFXML.fxml"));
            ImageView imgView = (ImageView) groupUser.getChildren().get(0);
            //imgView.setImage(new Image(getClass().getResourceAsStream("../img/user-3.png")));
            Label labName = (Label) groupUser.getChildren().get(1); 
            labName.setText(name);
            VBox vbox = (VBox) scrollUsers.getContent();
            vbox.setSpacing(7);
            System.out.println(vbox.getChildren().size());
            vbox.getChildren().add(groupUser);
            return true;
        }
        return false;
    }

    private void addMessage() {
        try {
            addUser(jtfMessage.getText());
            jtfMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void logout() {
        chatInterface.removeUser(loggedUser);
        clearUsers();
        showLoginForm();
    }
    
    private void clearUsers(){
        VBox vbox = (VBox) scrollUsers.getContent();
        vbox.setSpacing(7);
        System.out.println(vbox.getChildren().size());
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
                if(addUser(nameField.getText())){
                    dialogStackPane.setVisible(false);
                    dialog.close();

                }else{
                    showAlert();
                }
            } catch (IOException ex) {
                showAlert();
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
