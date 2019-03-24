package com.example.habittracker.datamanagement;

import java.util.Set;
import java.util.TreeSet;

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

    public UserEntry(String u, String p) {
        username = u;
        password = p;
        habits = new TreeSet<>();
    }

    public Set<HabitTracker> getHabits() {
        return habits;
    }
}
