package com.example.socialnetworkgui2.db;

import com.example.socialnetworkgui2.domain.Friendship;
import com.example.socialnetworkgui2.domain.Message;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MessageRepoDb {

    private String url;
    private String userName;
    private String password;

    public MessageRepoDb(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public void saveMessage(Message message) {
        String sql = "insert into messages (fr, t, content, date_time) values (?, ?, ?, ?)";

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

    public List<Message> getMessages(String u1, String u2) {
        String user1 = "'" + u1 + "'";
        String user2 = "'" + u2 + "'";
        System.out.println(user1);
        System.out.println(user2);
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages\n" +
                     "where (fr="  +user1+ " and t=" +user2+")\n" +
                     "or (fr=" + user2 +" and t=" +user1+ ");");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String from = resultSet.getString("fr");
                String to = resultSet.getString("t");
                String content = resultSet.getString("content");
                LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
                messages.add(new Message(from, to, content, dateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
