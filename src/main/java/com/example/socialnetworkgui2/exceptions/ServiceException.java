package com.example.socialnetworkgui2.exceptions;

public class ServiceException extends Exception {
    /**
     * excepție aruncată în cazuri de eroare la java.service
     * @param message mesajul erorii
     */
    public ServiceException(String message) {
        super(message);
    }
}
