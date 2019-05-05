package com.example.habittracker.datamanagement;

import java.util.Date;
import java.util.Set;

public class NumericalHabitTracker extends HabitTracker{
    private String unitName;

    public NumericalHabitTracker(String habitName, Set<String> addTags, boolean isPrivate, String unitName) {
        super(habitName, addTags, isPrivate);
        this.unitName = unitName;
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.NUMERICAL;
    }

    @Override
    public String getUnitName() {return unitName;}

    public void putDateInfo(Date date, float value, int happiness) {
        super.tracking.add(new DateInfo(date, false, value, happiness));
    }
}
