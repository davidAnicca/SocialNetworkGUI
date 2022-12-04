package com.example.socialnetworkgui2.Controller;

import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.exceptions.ServiceException;
import com.example.socialnetworkgui2.service.UserService;
import com.example.socialnetworkgui2.utils.Strings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SingUpController {
    UserService service;

    public void setService(UserService service) {
        this.service = service;
    }

    @FXML
    private TextField userText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private PasswordField passwordText2;
    @FXML
    private TextField emailText;
    @FXML
    private DatePicker birthDate;
    @FXML
    private Button singUpButton;

    public void onSingUpButtonClick(ActionEvent actionEvent) {
        if (!passwordText.getText().equals(passwordText2.getText())) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Passwords are not the same");
            passwordText.setText("");
            passwordText2.setText("");
            return;
        }
        try {
            if (birthDate.getValue() == null) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Select a valid birth date\nMaybe click enter in the date picker text");
                return;
            }
            service.addUser(userText.getText(), emailText.getText(), passwordText.getText(), birthDate.getValue());
            //totul ok
            MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Singed Up", "Your Account was created!\nSing in Again to continue");
            ((Stage) singUpButton.getScene().getWindow()).close();
        } catch (RepoException | ServiceException e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
}
