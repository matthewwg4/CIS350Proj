package com.example.habittracker.datamanagement;

import java.util.ArrayList;
import java.util.TreeMap;

public class Survey {
    public String name;
    public String question;
    public ArrayList<String> options;
    public TreeMap<String, String> responses;

    Survey(String n, String q, ArrayList<String> o) {
        name = n;
        question = q;
        options = o;
        responses = new TreeMap<>();
    }

    public void enterResponse(String response, String user) {
        responses.put(user, response);
    }

}
