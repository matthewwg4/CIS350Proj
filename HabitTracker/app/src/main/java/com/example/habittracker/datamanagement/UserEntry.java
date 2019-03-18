package com.example.habittracker.datamanagement;

public class UserEntry {
    public String username;
    public String password;
    //when integrated, add storage for habits

    public UserEntry(String u, String p) {
        username = u;
        password = p;
    }
}
