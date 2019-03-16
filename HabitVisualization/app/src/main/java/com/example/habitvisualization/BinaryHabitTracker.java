package com.example.habitvisualization;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class BinaryHabitTracker extends HabitTracker {

    public BinaryHabitTracker() {
        this.tracking = new ArrayList<>();
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.BINARY;
    }

    public void putDateInfo(Date date, boolean isDone, int happiness) {
        super.tracking.add(new DateInfo(date, isDone, 0, happiness));
    }
}
