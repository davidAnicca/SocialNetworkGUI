package com.example.socialnetworkgui2.userInterface;


import com.example.socialnetworkgui2.domain.Friendship;
import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.exceptions.ServiceException;
import com.example.socialnetworkgui2.service.UserService;
import com.example.socialnetworkgui2.service.Validator;

import java.util.Scanner;
import java.util.Set;

public class ConsoleUiAdmin {
    private UserService service;

    public ConsoleUiAdmin(UserService service) {
        this.service = service;
    }

    /**
     * ciclul principal al consolei
     */
    public void run() {

        while (true) {
            showMenu();
            if (!choose()) break;
        }
    }

    /**
     * citește din consolă un string
     *
     * @return stringul citit
     */
    private String readFromConsole() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    /**
     * se alege comanda dorita din meniu
     *
     * @return true dacă se dorește continuarea ciclului de consolă
     */
    private boolean choose() {
        System.out.print(">>");
        String opt = readFromConsole();
        switch (opt.strip()) {
            case "":
                break;
            case "0":
                return false;
            case "1":
                addUserUi();
                break;
            case "2":
                removeUserUi();
                break;
            case "3":
                showUsersUi();
                break;
            case "4":
                showFriendshipUi();
                break;
            case "5":
                addFriendshipUi();
                break;
            case "6":
                removeFriendshipUi();
                break;
            case "7":
                showNetworks();
                break;
            case "8":
                showLongestNetwork();
                break;
            default:
                System.out.println("optiune invalida");
        }
        return true;
    }

    /**
     * afisează cea mai lungă rețea din aplicație
     */
    private void showLongestNetwork() {
        showNetwork(service.getLongestNetwork());
    }

    /**
     * afișează toate rețelele din aplicație
     */
    private void showNetworks() {
        for (Set<User> network : service.getNetworks()) {
            showNetwork(network);
        }
    }

    /**
     * pretty print o rețea dată
     *
     * @param network rețeaua dată
     */
    private static void showNetwork(Set<User> network) {

        for (User user : network) {
            System.out.print(user.getUserName() + " - ");
        }
        System.out.println("|");
    }

    /**
     * ștergerea unei relații de prietenie
     */
    private void removeFriendshipUi() {
        System.out.print("user1>>");
        String user1 = readFromConsole();
        if (!Validator.userNameValidator(user1.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        System.out.print("user2>>");
        String user2 = readFromConsole();
        if (!Validator.userNameValidator(user2.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        try {
            service.removeFriendship(user1, user2);
        } catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * adăugarea unei relații de prietenie
     */
    private void addFriendshipUi() {
        System.out.print("user1>>");

        String user1 = readFromConsole();
        if (!Validator.userNameValidator(user1.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        System.out.print("user2>>");
        String user2 = readFromConsole();

        if (!Validator.userNameValidator(user2.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        try {
            service.addFriendship(user1.strip(), user2.strip());
        } catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * afișează toate relațiile de prietenie
     */
    private void showFriendshipUi() {
        for (Friendship friendship : service.getFriendships()) {
            System.out.println(friendship);
        }
    }

    /**
     * afișează toți utilizatorii din aplicație
     */
    private void showUsersUi() {
        for (User user : service.getUsers())
            System.out.println(user);
    }

    /**
     * șterge un utilizator din aplicație
     */
    private void removeUserUi() {
        System.out.print("user name>>");
        String userName = readFromConsole();
        if (!Validator.userNameValidator(userName.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        try {
            service.removeUser(userName);
        } catch (RepoException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * adaugă un utilizator
     */
    private void addUserUi() {
        System.out.print("user name>>");
        String userName = readFromConsole();
        if (!Validator.userNameValidator(userName.strip())) {
            System.out.println("numele trebuie sa contina doar litere si cifre");
            return;
        }
        System.out.print("parola>>");
        String password = readFromConsole();
        System.out.print("email>>");
        String email = readFromConsole();
        if (!Validator.emailValidator(email)) {
            System.out.println("email invalid");
            return;
        }
        System.out.print("data nasterii yyyy-MM-dd>>");
        String date = readFromConsole();
        try {
            service.addUser(userName.strip(), email.strip(), password.strip(), date.strip());
        } catch (RepoException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * afișează meniul aplicației
     */
    private void showMenu() {
        pressEnterToContinue();
        System.out.println("0.Exit");
        System.out.println("1.Adauga utilizator");
        System.out.println("2.Sterge utilizator");
        System.out.println("3.Vezi toti utilizatorii");
        System.out.println("4.Vezi prietenie");
        System.out.println("5. Adauga prietenie");
        System.out.println("6. Sterge prietenie");
        System.out.println("7. Afișează rețele");
        System.out.println("8. Afisează cea mai lungă rețea");
    }

    private void pressEnterToContinue()
    {
        System.out.println("\nApasă enter pentru a continua...");
        try
        {
            System.in.read();
        }
        catch(Exception ignored)
        {}
    }
}
