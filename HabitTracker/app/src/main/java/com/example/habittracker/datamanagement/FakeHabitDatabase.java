package com.example.habittracker.datamanagement;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FakeHabitDatabase {
    private TreeMap<String, HabitTracker> habits; //String represents the habit's ID, ID = name + numbers

    private FakeHabitDatabase() {
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

    private static final FakeHabitDatabase fhd = new FakeHabitDatabase();

    public static FakeHabitDatabase getInstance() {
        return fhd;
    }

    public Set<HabitTracker> getHabits(String habitName, boolean exact) {
        Set<HabitTracker> hs = new HashSet<>();
        if (exact) {
            for (String s: habits.keySet()) {
                if(s.equals(habitName)) {
                    hs.add(habits.get(s));
                }
            }
        } else {
            for (String s: habits.keySet()) {
                if(s.startsWith(habitName)) {
                    hs.add(habits.get(s));
                }
            }
        }
        return hs;
    }

    public Set<HabitTracker> getbyTag(String tag) {
        Set<HabitTracker> hs = new HashSet<>();
        for (HabitTracker h: habits.values()) {
            if(h.getTags().contains(tag)) {
                hs.add(h);
            }
        }
        return hs;
    }

    public void putHabitTracker(HabitTracker habit) {
        habits.put(habit.getHabitName(), habit);
    }
}
