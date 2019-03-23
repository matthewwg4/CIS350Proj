package com.example.habittracker.datamanagement;

import java.util.Set;

public class UserEntry {
    public String username;
    public String password;
    Set<HabitTracker> habits;
    //when integrated, add storage for habits

    public UserEntry(String u, String p, Set<HabitTracker> habits) {
        username = u;
        password = p;
        this.habits = habits;
    }


}
