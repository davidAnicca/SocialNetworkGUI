package com.example.socialnetworkgui2.db;



import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserRepoDb {
    /**
     * java.repo pentru useri
     */
    private final Set<User> users = new HashSet<>();
    private String url;
    private String userName;
    private String password;

    /**
     * Obține toți userii din aplicație
     *
     * @return o mulțime cu userii care se găsesc în aplicație
     */
    public Set<User> getUsers() {
        getUsersDb();
        return users;
    }

    /**
     * constructor
     *
     * @param url      fișierul din care se preiau toți userii salvați
     * @param userName
     * @param password
     */
    public UserRepoDb(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        getUsersDb();
    }

    /**
     * preia userii din db
     */
    private void getUsersDb() {
        users.clear();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String userId = resultSet.getString("user_name");
                String userPassword = resultSet.getString("password");
                String userEmail = resultSet.getString("email");
                LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
                User user = new User(userId, userEmail, userPassword, birthDate.atStartOfDay());
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Caută un user în java.repo
     *
     * @param user userul care trebuie căutat
     * @return true dacă userul a fost găsit
     */
    public User find(User user) {
        for (User user1 : users) {
            if (Objects.equals(user1, user))
                return user1;
        }
        return null;
    }

    /**
     * salvează toți userii în fișier
     */
    private void saveUserInDb(User user) {
        String sql = "insert into users (user_name, email, birth_date, password) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setDate(3, Date.valueOf(user.getBirthDate().toLocalDate()));
            ps.setString(4, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * adaugă un utilizator în aplicație
     *
     * @param user utilizatorul care ar trebui adăugat
     * @throws RepoException dacă utilizatorul există deja
     */
    public void addUser(User user) throws RepoException {
        if (!users.add(user)) {
            throw new RepoException("Userul există deja\n");
        }
        saveUserInDb(user);
    }

    /**
     * șterge un utilizator din aplicație
     *
     * @param user utilizatorul care trebuie șters
     * @throws RepoException dacă utilizatorul nu există
     */
    public void removeUser(User user) throws RepoException {
        if (!users.remove(user)) {
            throw new RepoException("Userul nu există\n");
        }
        removeUserFromDb(user);
    }

    private void removeUserFromDb(User user) {
        String sql = "delete from users where user_name=?";

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
