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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.io.IOException;
import java.net.URL;

public class MainPageController {

    @FXML
    private Button friendsButton;

    @FXML
    private Button requestButton;
    private User loggedUser;
    private UserService service;

    public void setService(UserService service) {
        this.service = service;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    private ListView<String> friendsNRequestsList;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button acceptButton;

    private final ObservableList<String> friendsOrRequests = FXCollections.observableArrayList();

    public void init() throws RepoException {
        userNameLabel.setText("Welcome, " + loggedUser.getUserName());
        friendsNRequestsList.setItems(friendsOrRequests);
        friendsOrRequests.clear();
        friendsOrRequests.addAll(service.getFriends(loggedUser));

        newFriendText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    addFriend(new ActionEvent());
                } catch (RepoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        requestButton.setDisable(false);
        friendsButton.setDisable(true);
        acceptButton.setDisable(true);
        deleteButton.setDisable(true);
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

    @FXML
    private Button addFriendButton;

    @FXML
    private Button chatButton;

    public void addFriend(ActionEvent event) throws RepoException {
        if (newFriendText.getText().strip().equals(""))
            return;
        try {
            service.addFriendship(loggedUser.getUserName(), newFriendText.getText().strip());
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, ":)", "Your request was sent. Wait for confirmation");
        } catch (RepoException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, ":(", e.getMessage());
        }
        newFriendText.setText("");
        update();
    }

    private void update() throws RepoException {
        friendsOrRequests.clear();
        if(requestButton.isDisabled()){
            friendsOrRequests.addAll(service.getRequests(loggedUser));
            friendsOrRequests.addAll(service.getSentRequests(loggedUser));
        }else{
            friendsOrRequests.addAll(service.getFriends(loggedUser));
        }
    }

    public void onFriendsButtonClick(ActionEvent event) throws RepoException {
        friendsButton.setDisable(true);
        requestButton.setDisable(false);
        friendsOrRequests.clear();
        friendsOrRequests.addAll(service.getFriends(loggedUser));
        acceptButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void onRequestsButtonClick(ActionEvent event) throws RepoException {
        friendsButton.setDisable(false);
        requestButton.setDisable(true);
        friendsOrRequests.clear();
        friendsOrRequests.addAll(service.getRequests(loggedUser));
        friendsOrRequests.addAll(service.getSentRequests(loggedUser));
        acceptButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void onListItemSelected(MouseEvent mouseEvent) {
        deleteButton.setDisable(false);
        if(requestButton.disableProperty().getValue()){
            //dac?? sunt ??n lista de requests
            acceptButton.setDisable(false);
        }else{
            //dac?? sunt ??n lista de friends
            acceptButton.setDisable(true);
        }
    }

    public void onDeleteButtonClick(ActionEvent event) throws RepoException {
        if(friendsNRequestsList.getSelectionModel().getSelectedItem() == null)
            return;
        String selected = friendsNRequestsList.getSelectionModel().getSelectedItem();
        service.removeFriendship(loggedUser.getUserName(), selected.split(" ")[0]);
        update();
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes", "Deleted !");
    }

    public void onAcceptButtonClick(ActionEvent event) throws RepoException {
        if(friendsNRequestsList.getSelectionModel().getSelectedItem() == null)
            return;
        String selected = friendsNRequestsList.getSelectionModel().getSelectedItem();
        service.acceptFriendship(selected.split(" ")[0], loggedUser.getUserName());
        friendsOrRequests.clear();
        friendsOrRequests.addAll(service.getRequests(loggedUser));
        friendsOrRequests.addAll(service.getSentRequests(loggedUser));
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes", "Friend request accepted");
    }

    public void onChatButtonClick(ActionEvent event) throws IOException, RepoException, InterruptedException {
        URL fxmlLocation = Main.class.getResource("chat-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 320, 700);
        Stage stage = new Stage();
        stage.setTitle("Chat");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        ChatBoxController chatBoxController = fxmlLoader.getController();
        chatBoxController.setService(service);
        chatBoxController.setMessageService(service.getMessageService());
        chatBoxController.setLoggedUser(loggedUser);
        chatBoxController.init();
        stage.showAndWait();
    }
}
