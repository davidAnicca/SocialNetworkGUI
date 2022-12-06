package com.example.socialnetworkgui2.controller;

import com.example.socialnetworkgui2.Main;
import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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

    public void init() {
        passwordText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onLoginButtonClick(new ActionEvent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (RepoException ignored) {

                }
            }
        });
    }

    public void setService(UserService service) {
        this.service = service;
    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, RepoException {
        if (service.checkLogin(userText.getText(), passwordText.getText())) {
            User loggedUser = service.getUser(userText.getText());
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-page-view.fxml"));
            Scene scene = new Scene(loader.load(), 600, 600);
            Stage stage = new Stage();
            stage.setTitle("Feta");
            stage.setScene(scene);
            stage.resizableProperty().setValue(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            MainPageController mainPageController = loader.getController();
            mainPageController.setService(service);
            mainPageController.setLoggedUser(loggedUser);
            mainPageController.init();
            ((Stage) loginButton.getScene().getWindow()).close();
        } else {
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