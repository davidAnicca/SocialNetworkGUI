package com.example.socialnetworkgui2.exceptions;

public class RepoException extends Exception {
    /**
     * excepție aruncată în cazuri de eroare la java.repo
     * @param message mesajul erorii
     */
    public RepoException(String message) {
        super(message);
    }
}
