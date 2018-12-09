/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private ScrollPane scrollUsers;
   
    
    private ChatInterface inverterservice = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollUsers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ChatImplService service = new ChatImplService();
        inverterservice = service.getChatImplPort();
        
        btnAddMessage.setOnAction((e)->{
            addMessage();
        });
        
        jtfMessage.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode().equals(KeyCode.ENTER)){
                addMessage();
            }
        });
    }   
    
    public void addUser(String name) throws IOException{
        if(inverterservice.addUser(name)){
            Group groupUser = (Group) FXMLLoader.load(getClass().getResource("userGroupFXML.fxml"));
            ImageView imgView = (ImageView) groupUser.getChildren().get(0);
            //imgView.setImage(new Image(getClass().getResourceAsStream("../img/user-3.png")));
            Label labName = (Label) groupUser.getChildren().get(1); 
            labName.setText(name);
            VBox vbox = (VBox) scrollUsers.getContent();
            vbox.setSpacing(7);
            System.out.println(vbox.getChildren().size());
            vbox.getChildren().add(groupUser);
        }
    }

    private void addMessage() {
        try {
            addUser(jtfMessage.getText());
            jtfMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
