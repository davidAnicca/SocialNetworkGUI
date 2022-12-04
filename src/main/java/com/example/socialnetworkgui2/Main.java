package com.example.socialnetworkgui2;

import com.example.socialnetworkgui2.controller.LoginController;
import com.example.socialnetworkgui2.db.FriendshipRepoDb;
import com.example.socialnetworkgui2.db.UserRepoDb;
import com.example.socialnetworkgui2.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    UserRepoDb userRepo;
    FriendshipRepoDb friendshipRepoDb;
    UserService userService;


    @Override
    public void start(Stage stage) throws IOException {
        userRepo = new UserRepoDb("jdbc:postgresql://localhost:5432/SocialNetworkDB",
                                "postgres",
                                "adenozintrifosfat");

        friendshipRepoDb = new FriendshipRepoDb("jdbc:postgresql://localhost:5432/SocialNetworkDB",
                                "postgres",
                                "adenozintrifosfat");

        userService = new UserService(userRepo, friendshipRepoDb);


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 260);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(userService);

    }

    public static void main(String[] args) {
        launch();
//        ConsoleUiAdmin consoleUiAdmin = new ConsoleUiAdmin(
//                new UserService(
//                        new UserRepoDb("jdbc:postgresql://localhost:5432/SocialNetworkDB",
//                                "postgres",
//                                "adenozintrifosfat"),
//                        new FriendshipRepoDb("jdbc:postgresql://localhost:5432/SocialNetworkDB",
//                                "postgres",
//                                "adenozintrifosfat")
//                )
//        );
//        consoleUiAdmin.run();
    }
}