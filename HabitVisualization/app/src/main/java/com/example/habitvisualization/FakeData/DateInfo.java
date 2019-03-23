package com.example.habitvisualization.FakeData;

import java.util.Date;

public class DateInfo {
    //Represent input within a day
    private Date date;
    private boolean isDone;
    private float unitValue; // represents hours, calories, cups/bowls (engaged in this activity)
    private int happiness;

    public DateInfo(Date date, boolean isDone, float unitValue, int happiness) {
        this.date = date;
        this.isDone = isDone;
        this.unitValue = unitValue;
        this.happiness = happiness;
    }

    public boolean isDone() {
        return isDone;
    }

    public Date getDate() {
        return date;
    }

    public float getUnitValue() {
        return unitValue;
    }

    public int getHappiness() {
        return happiness;
    }
}
