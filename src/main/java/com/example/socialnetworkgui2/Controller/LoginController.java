package com.example.socialnetworkgui2.Controller;

import com.example.socialnetworkgui2.Controller.MessageAlert;
import com.example.socialnetworkgui2.Main;
import com.example.socialnetworkgui2.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;


public class LoginController {

    private UserService service;
    @FXML
    private TextField userText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button loginButton;

    @FXML
    private Button singUpButton;


    public void setService(UserService service) {
        this.service = service;
    }

    public void onLoginButtonClick(ActionEvent actionEvent) {
        if(service.checkLogin(userText.getText(), passwordText.getText())){
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "confirmare", "ok");
        }else{
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "invalid", "User name or password incorrect");
            passwordText.setText("");
        }
    }

    public void onSingUpButtonClick(ActionEvent actionEvent) throws IOException {
        URL fxmlLocation = Main.class.getResource("singup-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 320, 500);
        Stage stage = new Stage();
        stage.setTitle("Sing Up");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        SingUpController singUpController = fxmlLoader.getController();
        singUpController.setService(service);
        stage.show();
        passwordText.setText("");
        userText.setText("");
    }
}