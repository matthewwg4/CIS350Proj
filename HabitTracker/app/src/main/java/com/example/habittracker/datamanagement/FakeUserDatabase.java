package com.example.habittracker.datamanagement;

import java.util.TreeMap;

public class FakeUserDatabase extends DataSource {
    private static TreeMap<String, UserEntry> users = new TreeMap<>();

    private FakeUserDatabase() {
        UserEntry u1 = new UserEntry("user1@email.com", "password1");
        UserEntry u2 = new UserEntry("user2@email.com", "password2");

        users.put(u1.username, u1);
        users.put(u2.username, u2);
    }

    private static final FakeUserDatabase fud = new FakeUserDatabase();

    public static FakeUserDatabase getInstance() {
        return fud;
    }

    public TreeMap<String, UserEntry>  getUserInfo() {
        return users;
    }

    public static void registerNewUser(UserEntry u) {
        users.put(u.username, u);
    }

}
