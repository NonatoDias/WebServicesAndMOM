/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    private ChatInterface inverterservice = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
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
    
    public void addUser(String name){
        inverterservice.addUser(name);
    }

    private void addMessage() {
        addUser(jtfMessage.getText());
        jtfMessage.setText("");
    }
    
}
