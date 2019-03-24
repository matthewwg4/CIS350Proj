package com.example.habittracker.datamanagement;

import java.util.TreeMap;

public class FakeTemplateDatabase {
    private static TreeMap<String, HabitType> temps = new TreeMap<>();

    private FakeTemplateDatabase() {
        temps.put("Numerical", HabitType.NUMERICAL);
        temps.put("Binary", HabitType.BINARY);
    }

    private static final FakeTemplateDatabase ftd = new FakeTemplateDatabase();

    public static FakeTemplateDatabase getInstance() {
        return ftd;
    }

    public TreeMap<String, HabitType>  getTemInfo() {
        return ftd.temps;
    }
}
