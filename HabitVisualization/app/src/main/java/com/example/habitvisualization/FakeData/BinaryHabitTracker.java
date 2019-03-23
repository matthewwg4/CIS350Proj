package com.example.habitvisualization.FakeData;

import java.util.ArrayList;
import java.util.Date;

public class BinaryHabitTracker extends HabitTracker {

    public BinaryHabitTracker() {
        this.tracking = new ArrayList<>();
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.BINARY;
    }

    @Override
    public void putDateInfo(Date date, boolean isDone, float unitValue, int happiness) {
        super.putDateInfo(date, isDone, 0, happiness);
    }
}
