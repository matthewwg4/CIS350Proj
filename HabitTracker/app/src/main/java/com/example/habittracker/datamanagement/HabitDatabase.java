package com.example.habittracker.datamanagement;

import java.util.TreeMap;

public class HabitDatabase {
    private TreeMap<String, HabitTracker> habits; //String represents the habit's ID, ID = name + numbers

    public HabitDatabase() {
        habits = new TreeMap<>();
    }

    public HabitTracker getHabitTracker(String habitName) {
        return habits.get(habitName);
    }

    public void putHabitTracker(HabitTracker habit) {
        habits.put(habit.getHabitName(), habit);
    }
}
