package com.example.habittracker.datamanagement;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
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
        Calendar calendar = Calendar.getInstance();
        bht.putDateInfo(calendar.getTime(), true, 10);
        calendar.add(Calendar.DATE , -1);
        bht.putDateInfo(calendar.getTime(), false, 3);
        calendar.add(Calendar.DATE , -1);
        bht.putDateInfo(calendar.getTime(), false, 4);
        calendar.add(Calendar.DATE , -1);
        bht.putDateInfo(calendar.getTime(), true, 8);
        calendar.add(Calendar.DATE , -1);
        bht.putDateInfo(calendar.getTime(), true, 9);
calendar = Calendar.getInstance();

        Set<String> tags2 = new TreeSet<>();
        tags.add("sleep");
        NumericalHabitTracker nht = new NumericalHabitTracker("Hours Slept", tags2, true, "hours");
        habits.put("Sleep1", nht);
nht.putDateInfo(calendar.getTime(), 10, 10);
        calendar.add(Calendar.DATE , -1);
nht.putDateInfo(calendar.getTime(), 20, 3);
        calendar.add(Calendar.DATE , -1);
        nht.putDateInfo(calendar.getTime(), 38, 10);
        calendar.add(Calendar.DATE , -1);
        nht.putDateInfo(calendar.getTime(), 3, 1);
    }

    public HabitTracker getHabitTracker(String habitName) {
        return habits.get(habitName);
    }

    public Set<HabitTracker> getAllHabitTrackers() {
        Set<HabitTracker> habitsSet = new HashSet<>();
        for (Map.Entry<String, HabitTracker> entry : habits.entrySet()) {
            HabitTracker habitTracker = entry.getValue();
            habitsSet.add(habitTracker);
        }
        return habitsSet;
    }

    public void putHabitTracker(HabitTracker habit) {
        habits.put(habit.getHabitName(), habit);
    }
}
