package com.example.habitvisualization.FakeData;

import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class HabitTracker {
    private static final String TAG = "HabitTracker";
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
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd"); //Get rid of time
        try {
            date = ymdFormat.parse(ymdFormat.format(date));
            tracking.add(new DateInfo(date, isDone, unitValue, happiness));
        } catch (ParseException e) {
            Log.d(TAG, "putDateInfo: got ParseException. No data is added");
        }
    }

    /*The below is only useful and override if HabitTracker is NumericalHabitTracker */
    public void setUnitName(String unitName) {}

    public String getUnitName() {return null;}
}
