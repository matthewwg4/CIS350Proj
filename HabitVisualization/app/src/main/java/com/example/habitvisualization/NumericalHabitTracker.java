package com.example.habitvisualization;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class NumericalHabitTracker extends HabitTracker{
    private String unitName;

    public NumericalHabitTracker() {
        unitName = "Undefined";
        this.tracking = new ArrayList<>();
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.BINARY;
    }

    @Override
    public String getUnitName() {return unitName;}
}

