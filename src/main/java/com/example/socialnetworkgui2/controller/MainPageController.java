package com.example.socialnetworkgui2.controller;

import com.example.socialnetworkgui2.Main;
import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainPageController {

    private User logggedUser;
    private UserService service;

    public void setService(UserService service) {
        this.service = service;
    }

    public void setLoggedUser(User loggedUser) {
        this.logggedUser = loggedUser;
    }

    @FXML
    private ListView<String> friendsList;

    @FXML
    private Label userNameLabel;

    private final ObservableList<String> friends = FXCollections.observableArrayList();

    public void init() throws RepoException {
        userNameLabel.setText("Welcome, " + logggedUser.getUserName());
        friendsList.setItems(friends);
        friends.clear();
        friends.addAll(service.getFriends(logggedUser.getUserName()));

        newFriendText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addFriend(new ActionEvent());
            }
        });
    }

    @FXML
    private Button logoutButton;

    @FXML
    private void onLogoutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 260);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
        LoginController loginController = loader.getController();
        loginController.init();
        loginController.setService(service);
        ((Stage) logoutButton.getScene().getWindow()).close();
    }

    @FXML
    private TextField newFriendText;

    public void addFriend(ActionEvent event) {
        if (newFriendText.getText().strip().equals(""))
            return;
        try {
            service.addFriendship(logggedUser.getUserName(), newFriendText.getText().strip());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, ":)", "Your request was sent. Wait for confirmation");
        } catch (RepoException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, ":(", e.getMessage());
        }
    }
}
