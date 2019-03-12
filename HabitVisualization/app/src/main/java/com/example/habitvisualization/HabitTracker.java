package com.example.habitvisualization;

import android.support.v7.util.SortedList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HabitTracker {
    protected ArrayList<DateInfo> tracking;

    public List<DateInfo> getTracking() {
        return tracking;
    }

    public HabitType getHabitType() {
        return null;
    }

    public DateInfo getDateInfo(Date date) {
        int bound = tracking.size();
        for (int i = 0; i < bound; i++) {
            if (tracking.get(i).getDate().compareTo(date) == 0) {
                return tracking.get(i);
            }
        }
        return null; // There is no data related to the input date
    }

    public void putDateInfo(Date date, boolean isDone, float unitValue, int happiness) {
        tracking.add(new DateInfo(date, isDone, unitValue, happiness));
    }

    /*The below is only useful and override if HabitTracker is NumericalHabitTracker */
    public void setUnitName(String unitName) {}

    public String getUnitName() {return null;}
}
