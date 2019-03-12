package com.example.habitvisualization;

import java.util.TreeMap;

public class DataStorage { // Fake data
    private TreeMap<String, HabitTracker> habits; //String represents the habit's name

    public DataStorage() {
        habits = new TreeMap<>();
    }

    public HabitTracker getHabitTracker(String habitName) {
        return habits.get(habitName);
    }
}
