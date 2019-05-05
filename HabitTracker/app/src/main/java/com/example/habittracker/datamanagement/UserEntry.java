package com.example.habittracker.datamanagement;

import java.util.HashSet;
import java.util.Set;

public class UserEntry {
    public String username;
    public String password;
    Set<HabitTracker> habits;
    //when integrated, add storage for habits

    public UserEntry() {}

    public UserEntry(String u, String p) {
        username = u;
        password = p;
        habits = new HashSet<>();
    }

    public void addHabit(HabitTracker habit) {
        habits.add(habit);
    }

    public void addNewHabit(HabitTracker habit) {
        if (!habits.contains(habit)) {
            DataSource ds = DataSource.getInstance();
            if (!ds.addHabit(this, habit)) {
                return;
            }
            habits.add(habit);
        }
    }

    public Set<HabitTracker> getHabits() {
        return habits;
    }

}
