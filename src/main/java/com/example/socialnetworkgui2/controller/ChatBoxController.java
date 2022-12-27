package com.example.socialnetworkgui2.controller;

import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatBoxController {
    public TextArea messageText;
    public Button sendMessage;
    @FXML
    private ListView messagesList;
    private UserService service;
    private User loggedUser;

    public void setLoggedUser(User user){
        loggedUser = user;
    }

    @FXML
    private ChoiceBox friendsList;

    public void setService(UserService service) {
        this.service = service;
    }

    public void init() throws RepoException {
        friendsList.getItems().addAll(service.getFriends(loggedUser));
    }

    public void onSendMessageButtonClick(ActionEvent event) {
    }
}
