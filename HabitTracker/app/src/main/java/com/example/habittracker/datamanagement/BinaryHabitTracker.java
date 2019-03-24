package com.example.habittracker.datamanagement;

import java.util.Date;
import java.util.Set;

public class BinaryHabitTracker extends HabitTracker {

    public BinaryHabitTracker(String habitName, Set<String> addTags, boolean isPrivate) {
        super(habitName, addTags, isPrivate);
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.BINARY;
    }

    public void putDateInfo(Date date, boolean isDone, int happiness) {
        super.tracking.add(new DateInfo(date, isDone, 0, happiness));
    }
}