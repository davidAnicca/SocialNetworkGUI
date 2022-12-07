package com.example.socialnetworkgui2.service;


import com.example.socialnetworkgui2.db.FriendshipRepoDb;
import com.example.socialnetworkgui2.db.UserRepoDb;
import com.example.socialnetworkgui2.domain.Friendship;
import com.example.socialnetworkgui2.domain.FriendshipStatus;
import com.example.socialnetworkgui2.domain.User;

import java.util.*;

public class NetworksService {
    private FriendshipRepoDb friendshipRepo;
    private UserRepoDb repo;

    /**
     * constructor
     *
     * @param friendshipRepo java.repo pentru prietenii
     * @param repo           java.repo pentru utilizatori
     */
    public NetworksService(FriendshipRepoDb friendshipRepo, UserRepoDb repo) {
        this.friendshipRepo = friendshipRepo;
        this.repo = repo;
    }

    /**
     * determină lista de cereri de prietenie
     *
     * @param user userul pt care s-a dat cererea de prietenie (primul user o cere și celălalt o acceptă)
     * @return o mulțime de prietenii în starea de pending
     */
    public Set<String> getRequests(String user) {
        Set<String> requests = new HashSet<>();
        for (Friendship friendship : friendshipRepo.getFriendships()) {
            if (friendship.getStatus() != FriendshipStatus.PENDING) continue;
            if (Objects.equals(friendship.getUser2(), user)) {
                requests.add(friendship.getUser1());
            }
        }
        return requests;
    }

    /**
     * determină lista de prieteni a unui user
     *
     * @param user userName-ul userului
     * @return o mulțime de useri care sunt prieteni cu user
     */
    public Set<String> getFriends(String user) {
        Set<String> friends = new HashSet<>();
        for (Friendship friendship : friendshipRepo.getFriendships()) {
            if (friendship.getStatus() != FriendshipStatus.ACCEPTED) continue;
            if (Objects.equals(friendship.getUser1(), user)) {
                friends.add(friendship.getUser2());
            }
            if (Objects.equals(friendship.getUser2(), user)) {
                friends.add(friendship.getUser1());
            }
        }
        return friends;
    }

    /**
     * verifică dacă doi useri sunt prieteni
     *
     * @param user1 userName-ul primului user
     * @param user2 userName-ul celui de-al doilea
     * @return
     */
    public boolean areFriends(String user1, String user2) {
        for (Friendship friendship : friendshipRepo.getFriendships()) {
            if (friendship.getStatus() != FriendshipStatus.ACCEPTED) continue;
            if (Objects.equals(friendship.getUser1(), user1) && Objects.equals(friendship.getUser2(), user2) ||
                    Objects.equals(friendship.getUser2(), user1) && Objects.equals(friendship.getUser1(), user2))
                return true;
        }
        return false;
    }

    /**
     * dfs pe mulțimea de useri și prietenii
     *
     * @param user    utilizatorul de început al vizitei
     * @param visitet o mulțime în care apar toți utilizatorii vizitați
     * @param network rețeaua în care se introduce utilizatorul (componenta conexă)
     * @param lentgh  lungimea celui mai lung drum
     * @return
     */
    private int visit(User user, Set<User> visitet, Set<User> network, int lentgh) {
        int len = lentgh;
        network.add(user);
        visitet.add(user);
        for (String friend : getFriends(user.getUserName())) {
            User friendU = repo.find(new User(friend));
            if (!visitet.contains(friendU)) {
                len++;
                len += visit(friendU, visitet, network, len);
            }
        }
        return len;
    }

    /**
     * determină toate rețelele din aplicație (componente conexe)
     *
     * @return o listă de mulțimi de useri
     */
    public List<Set<User>> networks() {
        Set<User> visited = new HashSet<>();
        List<Set<User>> networksSet = new ArrayList<>();
        for (User user : repo.getUsers()) {
            if (!visited.contains(user)) {
                Set<User> newSet = new HashSet<>();
                networksSet.add(newSet);
                int length = visit(user, visited, newSet, 0);
            }
        }
        return networksSet;
    }

    /**
     * identifică cea mai lungă rețea din aplicație ( componenta cu cel mai lung drum)
     *
     * @return o mulțime de utilizatori (network)
     */
    public Set<User> longestNetwork() {
        int len = 0;
        Set<User> longestNetworkSet = null;
        Set<User> visited = new HashSet<>();
        List<Set<User>> networksSet = new ArrayList<>();
        for (User user : repo.getUsers()) {
            if (!visited.contains(user)) {
                Set<User> newSet = new HashSet<>();
                networksSet.add(newSet);
                int length = visit(user, visited, newSet, 0);
                if (length > len) {
                    longestNetworkSet = newSet;
                    len = length;
                }
            }
        }
        return longestNetworkSet;
    }
}
