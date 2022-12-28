package com.example.socialnetworkgui2.controller;

import com.example.socialnetworkgui2.domain.Message;
import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.service.MessageService;
import com.example.socialnetworkgui2.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxController {
    public TextArea messageText;
    public Button sendMessage;
    @FXML
    private ListView messagesList;
    private UserService service;

    private final ObservableList<Text> messages = FXCollections.observableArrayList();

    String otherUser;

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private MessageService messageService;
    private User loggedUser;

    public void setLoggedUser(User user) {
        loggedUser = user;
    }

    @FXML
    private ChoiceBox friendsList;

    public void setService(UserService service) {
        this.service = service;
    }

    public void init() throws RepoException, InterruptedException {
        messagesList.setItems(messages);
        friendsList.getItems().addAll(service.getFriends(loggedUser));
        friendsList.setValue(friendsList.getItems().get(0));
        friendChoice(null);
        sendMessage.setDisable(true);
        reload();
    }

    public void onSendMessageButtonClick(ActionEvent event) {
        if (friendsList.getValue() == null)
            return;
        String from = loggedUser.getUserName();
        String to = otherUser;
        String content = messageText.getText();
        ///MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "mesaj", from + "//" + to + "//" + content);
        messageService.sendMesssage(from, to, content);
        messageText.setText("");
        reload();
    }

    public void typinig(KeyEvent inputMethodEvent) {
        sendMessage.setDisable(messageText.getText().equals(""));
        if (inputMethodEvent.getCode().equals(KeyCode.ENTER)) {
            onSendMessageButtonClick(null);
        }
    }

    public void friendChoice(ActionEvent event) {
        messageText.setDisable(friendsList.getValue() == null);
        if (friendsList.getValue() != null) {
            otherUser = friendsList.getValue().toString().split(" ")[0];
            reload();
        }
    }

    private void reload() {
        Font b = Font.font("Arial", FontWeight.BOLD, 20);
        Font r = Font.font("Arial", FontWeight.LIGHT, 20);
        Font i = Font.font("Arial", FontWeight.LIGHT, FontPosture.ITALIC, 10);
        List<Message> messagesDB = messageService.getMessages(loggedUser.getUserName(), otherUser);

        List<Text> contents = new ArrayList<>();
        for (Message message : messagesDB) {
            Text from = new Text();
            from.setFont(r);
            Text content = new Text();
            content.setFont(b);
            Text date = new Text();
            date.setFont(i);
            if (message.getFrom().equals(loggedUser.getUserName())) {
                from.setText("you:");
            } else {
                from.setText(message.getFrom()+":");
            }
            content.setText(message.getContent());
            date.setText("(" + message.getDateTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/YY HH:mm")) + ")");
            contents.add(from);
            contents.add(content);
            contents.add(date);
            contents.add(new Text(""));
        }
        messages.clear();
        messages.addAll(contents);
        messagesList.scrollTo(messages.size()+1);
    }
}
