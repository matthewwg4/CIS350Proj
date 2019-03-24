package com.example.habittracker.datamanagement;

public class Resource {
    public String name;
    public String resInfo;

    Resource(String n, String info) {
        name = n;
        resInfo = info;
    }

    @Override
    public String toString() {
        return (name + ": " + resInfo);
    }
}
