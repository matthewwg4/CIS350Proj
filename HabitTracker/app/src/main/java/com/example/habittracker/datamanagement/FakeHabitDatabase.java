package com.example.habittracker.datamanagement;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FakeHabitDatabase {
    private TreeMap<String, HabitTracker> habits; //String represents the habit's ID, ID = name + numbers

    public FakeHabitDatabase() {
        habits = new TreeMap<>();
        Set<String> tags = new TreeSet<>();
        tags.add("relaxation");
        BinaryHabitTracker bht = new BinaryHabitTracker("Meditation", tags, true);
        habits.put("Meditation1", bht);

        Set<String> tags2 = new TreeSet<>();
        tags.add("sleep");
        NumericalHabitTracker nht = new NumericalHabitTracker("Hours Slept", tags2, true, "hours");
        habits.put("Sleep1", nht);
    }

    public HabitTracker getHabitTracker(String habitName) {
        return habits.get(habitName);
    }

    public void putHabitTracker(HabitTracker habit) {
        habits.put(habit.getHabitName(), habit);
    }
}
