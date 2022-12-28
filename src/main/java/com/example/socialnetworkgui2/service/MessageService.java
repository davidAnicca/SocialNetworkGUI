package com.example.socialnetworkgui2.service;

import com.example.socialnetworkgui2.db.MessageRepoDb;
import com.example.socialnetworkgui2.domain.Message;

import java.util.List;

public class MessageService {

    MessageRepoDb messageRepoDb;

    public MessageService(MessageRepoDb messageRepoDb) {
        this.messageRepoDb = messageRepoDb;
    }

    public void sendMesssage(String from, String to, String content){
        messageRepoDb.saveMessage(new Message(from, to, content));
    }

    public List<Message> getMessages(String user1, String user2){
        return messageRepoDb.getMessages(user1, user2);
    }
}
