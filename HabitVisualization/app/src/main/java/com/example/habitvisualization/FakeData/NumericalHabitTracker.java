package com.example.habitvisualization.FakeData;

import java.util.ArrayList;

public class NumericalHabitTracker extends HabitTracker {
    private String unitName;

    public NumericalHabitTracker() {
        unitName = "Undefined";
        this.tracking = new ArrayList<>();
    }

    @Override
    public HabitType getHabitType() {
        return HabitType.NUMERICAL;
    }

    @Override
    public String getUnitName() {return unitName;}
}

