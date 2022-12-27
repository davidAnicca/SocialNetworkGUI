package com.example.socialnetworkgui2.db;

import com.example.socialnetworkgui2.domain.Message;

import java.sql.*;
import java.util.Set;

public class MessageRepoDb {

    private String url;
    private String userName;
    private String password;

    public MessageRepoDb(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public void saveMessage(Message message){
        String sql = "insert into messages (from, to, content, date_time) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, message.getFrom());
            ps.setString(2, message.getTo());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, message.getDateTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Message> getMessages(){
        return null;
    }

}
