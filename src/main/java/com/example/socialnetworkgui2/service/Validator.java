package com.example.socialnetworkgui2.service;


import com.example.socialnetworkgui2.utils.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
    /**
     * validează numele unui utilizator
     *
     * @param userName numele de utilizator propus pentru validare
     * @return true dacă numele de utilizator contine doar litere și cifre
     */
    public static boolean userNameValidator(String userName) {
        return userName.matches("[a-zA-Z0-9]+");
    }

    /**
     * valideaza stringul de data
     *
     * @param date string de data care are formatul yyyy-mm-dd
     * @return false daca nu poate fi o data validă din trecut sau astazi
     */
    public static boolean dateValidator(String date) {
        if (!date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))
            return false;
        try {
            LocalDate dateTime = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern(Strings.dateFormat));
            if (dateTime.isAfter(LocalDate.now().plusDays(1)))
                return false;
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean dateValidator(LocalDate date) {
        return date.isBefore(LocalDate.now().minusYears(15));
    }

    /**
     * valideaza un email
     *
     * @param email stringul pentru validare
     * @return false dacă stringul nu reprezinta un email valid
     */
    public static boolean emailValidator(String email) {
        return email.matches("[a-zA-Z0-9_\\-]+@[a-zA-Z0-9].+[a-zA-Z0-9]");
    }
}
