package com.example.socialnetworkgui2.service;


import com.example.socialnetworkgui2.db.FriendshipRepoDb;
import com.example.socialnetworkgui2.db.UserRepoDb;
import com.example.socialnetworkgui2.domain.Friendship;
import com.example.socialnetworkgui2.domain.User;
import com.example.socialnetworkgui2.exceptions.RepoException;
import com.example.socialnetworkgui2.exceptions.ServiceException;
import com.example.socialnetworkgui2.utils.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserService {
    /**
     * java.service pentru useri și relații de prietenii
     */
    UserRepoDb repo;
    FriendshipRepoDb friendshipRepo;

    private NetworksService networksService;

    public Set<String> getRequests(User user) throws RepoException {
        if (!isUser(user.getUserName())) {
            throw new RepoException(user + " is not a valid user");
        }
        return networksService.getRequests(user.getUserName());
    }


    /**
     * constructor
     *
     * @param repo           java.repo pentru utilizatori
     * @param friendshipRepo java.repo pentru relații de prietenie
     */
    public UserService(UserRepoDb repo, FriendshipRepoDb friendshipRepo) {
        this.friendshipRepo = friendshipRepo;
        this.repo = repo;
        networksService = new NetworksService(friendshipRepo, repo);
    }

    /**
     * adaugare un utilizator nou
     *
     * @param userName numele utilizatorului
     * @param password parola utilizatorului
     * @throws RepoException    dacă utilizatorul există deja
     * @throws ServiceException dacă data nu e ok sau emailul nu e ok
     */
    public void addUser(String userName, String email, String password, LocalDate birthDate) throws RepoException, ServiceException {
        if (!Validator.emailValidator(email)) {
            throw new ServiceException("Invalid Email");
        }
        if (!Validator.dateValidator(birthDate)) {
            throw new ServiceException("You need to be older than 15yo");
        }
        repo.addUser(new User(userName, email, password, birthDate.atStartOfDay()));
    }

    /**
     * adaugare un utilizator nou
     *
     * @param userName numele utilizatorului
     * @param password parola utilizatorului
     * @throws RepoException dacă utilizatorul există deja
     */
    public void addUser(String userName, String email, String password, String birthDate) throws RepoException, ServiceException {
        if (!Validator.emailValidator(email)) {
            throw new ServiceException("Invalid Email");
        }
        if (!Validator.dateValidator(birthDate)) {
            throw new ServiceException("Date format is yyyy-MM-dd");
        }
        repo.addUser(new User(userName, email, password,
                LocalDate.parse(birthDate, DateTimeFormatter.ofPattern(Strings.dateFormat))
                        .atStartOfDay()));
    }

    /**
     * șterge un utilizator
     *
     * @param userName numele utilizatorului care trebuie șters
     * @throws RepoException dacă utilizatorul nu există
     */
    public void removeUser(String userName) throws RepoException {
        repo.removeUser(new User(userName));
        removeFriendships(userName);
    }

    /**
     * verifică existența unui utilizator și parola dacă este corectă
     *
     * @param userName username dat
     * @param password parola data
     * @return false dacă utilizatorul nu există sau parola greșită
     * true dacă user-parolă este corect
     */
    public boolean checkLogin(String userName, String password) {
        User found = repo.find(new User(userName));
        if (found == null)
            return false;
        return Objects.equals(found.getPassword(), password);
    }

    /**
     * toți utilizatorii
     *
     * @return o mulțime cu toți utilizatorii din aplicație
     */
    public Set<User> getUsers() {
        return repo.getUsers();
    }

    /**
     * verifică dacă un nume de utilizator corespunde unui user
     *
     * @param userName numele de utilizator de verificat
     * @return true dacă există un user cu numele userName
     */
    private boolean isUser(String userName) {
        return repo.find(new User(userName)) != null;
    }

    /**
     * verifică dacă două nume de utilizatori corespund unor utilizatori valizi
     *
     * @param user1 nume de utilizator de verificat
     * @param user2 nume de utilizator de verificat
     * @return dacă user1 și user2 corespund unor utilozatori valizi
     */
    private boolean areUsers(String user1, String user2) {
        return isUser(user1) && isUser(user2) && !user1.equals(user2);
    }

    /**
     * adaugă o relație nouă de prietenie între user1 și user2
     *
     * @param user1 numele primului user
     * @param user2 numele celuilalt user
     * @throws RepoException dacă relația de prietenie există deja
     */
    public void addFriendship(String user1, String user2) throws RepoException {
        if (areUsers(user1, user2)) {
            friendshipRepo.addFriendship(new Friendship(user1, user2, LocalDate.now()));
        } else throw new RepoException("Can't find the user specified");
    }

    public void acceptFriendship(String user1, String user2) throws RepoException {
        if (areUsers(user1, user2)) {
            friendshipRepo.acceptFriendship(new Friendship(user1, user2, null));
        }
    }

    /**
     * șterge relația de prietenie dintre user1 și user2
     *
     * @param user1 primul utilizator
     * @param user2 celalalt utilizator
     * @throws RepoException dacă relatia de prietenie nu există
     */
    public void removeFriendship(String user1, String user2) throws RepoException {
        if (areUsers(user1, user2)) {
            friendshipRepo.removeFriendship(new Friendship(user1, user2, LocalDate.now()));
        }
    }

    /**
     * șterge toate relațiile de prietenie ale lui user
     *
     * @param user numele de utilizator ale cărui relații trebuie șterse (nu referă neapărat un user valid)
     */
    public void removeFriendships(String user) {
        List<Friendship> friendshipsToBeDeleted = new ArrayList<>();
        for (Friendship friendship : friendshipRepo.getFriendships()) {
            if (Objects.equals(friendship.getUser2(), user) || Objects.equals(friendship.getUser1(), user)) {
                friendshipsToBeDeleted.add(friendship);
            }
        }
        for (Friendship friendship : friendshipsToBeDeleted) {
            try {
                friendshipRepo.removeFriendship(friendship);
            } catch (RepoException ignored) {
                ///
            }
        }
    }

    /**
     * toate relațiile de prietenie
     *
     * @return o mulțime cu relații de prietenie
     */
    public Set<Friendship> getFriendships() {
        return friendshipRepo.getFriendships();
    }

    /**
     * lista de prieteni a unui utilizator
     *
     * @param user numele utilizatorului
     * @return lista cu prietenii utilizatorului user
     * @throws RepoException daca user nu referă către un utilizator valid
     */
    public Set<String> getFriends(User user) throws RepoException {
        if (!isUser(user.getUserName())) {
            throw new RepoException(user + " is not a valid user");
        }
        return networksService.getFriends(user.getUserName());
    }

    /**
     * Lista tuturor rețelelor din aplicație
     *
     * @return o listă de rețele; o rețea este o mulțime de utilizatori
     */
    public List<Set<User>> getNetworks() {
        return networksService.networks();
    }

    /**
     * obține cea mai lungă comunitate
     *
     * @return o mulțime de useri (un network)
     */
    public Set<User> getLongestNetwork() {
        return networksService.longestNetwork();
    }

    /**
     * dă userul cu un anumit username
     *
     * @param userName userName-ul căutat
     * @return User-ul căutat
     * null dacă nu a putut fi găsit
     */
    public User getUser(String userName) {
        for (User user1 : getUsers()) {
            if (Objects.equals(user1.getUserName(), userName))
                return user1;
        }
        return null;
    }
}
