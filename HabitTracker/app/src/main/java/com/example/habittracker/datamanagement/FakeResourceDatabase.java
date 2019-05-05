package com.example.habittracker.datamanagement;

import java.util.TreeMap;

public class FakeResourceDatabase {
    private static TreeMap<String, Resource> res = new TreeMap<>();

    private FakeResourceDatabase() {
        Resource r1 = new Resource("CAPS", "CAPS is the Counseling and Psychological Services at Penn. They offer different types of services, such as individual and group therapy. Their website is https://www.vpul.upenn.edu/caps/");
        Resource r2 = new Resource("Relaxing White Noise Generator", "This white noise generator can help users relax and reduce stress by creating a custom white noise to listen to." +
                "\n https://mynoise.net/NoiseMachines/rainNoiseGenerator.php");

        r2.tags.add("sound");
        res.put(r1.name, r1);
        res.put(r2.name, r2);

    }

    private static final FakeResourceDatabase frd = new FakeResourceDatabase();

    public static FakeResourceDatabase getInstance() {
        return frd;
    }

    public TreeMap<String, Resource>  getResInfo() {
        return frd.res;
    }


}
