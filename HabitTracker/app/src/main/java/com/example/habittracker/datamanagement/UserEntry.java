package com.example.habittracker.datamanagement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class UserEntry {
    public String username;
    public String password;
    Set<HabitTracker> habits;
    //when integrated, add storage for habits


    public void addHabit(HabitTracker habit) {
        habits.add(habit);

    public UserEntry(String u, String p) {
        username = u;
        password = p;
        habits = new TreeSet<>();
    }

    public Set<HabitTracker> getHabits() {
        return habits;
    }
  
    public void putAllHabits(Set<HabitTracker> allHabits) {
        habits = allHabits;
    }

}
