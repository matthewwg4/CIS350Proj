package com.example.habittracker.datamanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class HabitTracker {
    protected ArrayList<DateInfo> tracking;
    protected String habitName;
    protected HashSet<String> tags;

    public void setTracking(ArrayList<DateInfo> tracking) {
        this.tracking = tracking;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    protected boolean isPrivate;

    protected HabitTracker(String habitName, Set<String> addTags, boolean isPrivate) {
        this.habitName = habitName;
        this.tracking = new ArrayList<DateInfo>();
        tags = new HashSet<String>();
        tags.addAll(addTags);
        this.isPrivate = isPrivate;
    }

    public List<DateInfo> getTracking() {
        return tracking;
    }

    public abstract HabitType getHabitType();


    public DateInfo getDateInfo(Date date) {
        int bound = tracking.size();
        for (int i = 0; i < bound; i++) {
            if (tracking.get(i).getDate().compareTo(date) == 0) {
                return tracking.get(i);
            }
        }
        return null; // There is no data related to the input date
    }

    /*The below is only useful and override if HabitTracker is NumericalHabitTracker */
    public void setUnitName(String unitName) {}

    public String getUnitName() {return null;}

    public void addTracking(DateInfo d) {
        tracking.add(d);
    }
}
