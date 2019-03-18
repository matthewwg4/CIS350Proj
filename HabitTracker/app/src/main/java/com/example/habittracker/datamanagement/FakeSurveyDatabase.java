package com.example.habittracker.datamanagement;

import android.icu.text.StringSearch;

import java.util.ArrayList;
import java.util.TreeMap;

public class FakeSurveyDatabase extends DataSource {
    private static TreeMap<String, Survey> surveys = new TreeMap<>();

    private FakeSurveyDatabase() {
        ArrayList<String> op1 = new ArrayList<>();
        op1.add("Yes");
        op1.add("No");

        ArrayList<String> op2 = new ArrayList<>();
        op2.add("Less than once a week");
        op2.add("About once a week");
        op2.add("Several times a week");
        op2.add("Every day");
        op2.add("Multiple times a day");

        Survey s1 = new Survey("User Experience", "Do you like using this app?", op1);
        Survey s2 = new Survey("Frequency of Use", "How often do you use this app?", op2);

        surveys.put(s1.name, s1);
        surveys.put(s2.name, s2);
    }

    private static final FakeSurveyDatabase fsd = new FakeSurveyDatabase();

    public static FakeSurveyDatabase getInstance() {
        return fsd;
    }

    public TreeMap<String, Survey> getSurInfo() {
        return fsd.surveys;
    }

    public void UpdateSurvey(String name, String response, String user) {
        Survey s = surveys.get(name);
        s.enterResponse(response, user);
        surveys.put(name, s);
    }
}