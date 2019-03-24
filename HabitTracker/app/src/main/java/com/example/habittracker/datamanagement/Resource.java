package com.example.habittracker.datamanagement;

import java.util.Set;
import java.util.TreeSet;

public class Resource {
    public String name;
    public String resInfo;

    //tags should be unique and all lowercase
    public Set<String> tags = new TreeSet<>();

    Resource(String n, String info) {
        name = n;
        resInfo = info;
    }

    @Override
    public String toString() {
        return (name + ": " + resInfo);
    }
}
