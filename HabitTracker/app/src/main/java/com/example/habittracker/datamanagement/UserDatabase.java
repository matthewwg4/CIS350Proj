package com.example.habittracker.datamanagement;

import com.example.habittracker.async.AccessWebTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class UserDatabase extends DataSource {
    private static TreeMap<String, UserEntry> users = new TreeMap<>();

    private UserDatabase() {
        try {
            URL url = new URL("http://10.0.0.2:3000/api/users");
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String msg = task.get();

            JSONObject jo = new JSONObject(msg);
            JSONArray users = (JSONArray) jo.get("users");
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                UserEntry ue = new UserEntry((String) user.get("username"), (String) user.get("password"));
                JSONArray habits = (JSONArray) user.get("habits");
                for (int j = 0; j < habits.length(); j++) {
                    JSONObject habit = (JSONObject) habits.get(j);
                    JSONArray tags = (JSONArray) habit.get("tags");
                    Set<String> tagSet = new TreeSet<>();
                    for (int k = 0; k < tags.length(); k++) {
                        tagSet.add((String) tags.get(k));
                    }
                    String type = (String) habit.get("type");
                    if (type.equals("numerical")) {
                        NumericalHabitTracker nhabit = new NumericalHabitTracker((String) habit.get("habitName"),
                                tagSet, true, (String) habit.get("unit"));
                        JSONArray info = (JSONArray) habit.get("dailyEntries");
                        for (int l = 0; l < info.length(); l++) {
                            JSONObject infoObj = (JSONObject) info.get(l);
                            nhabit.putDateInfo(new Date(Long.parseLong((String) infoObj.get("time"))),
                                    (float) infoObj.get("amount"), (int) infoObj.get("happiness"));
                        }
                        ue.addHabit(nhabit);
                    } else if (type.equals("binary")) {
                        BinaryHabitTracker bhabit = new BinaryHabitTracker((String) habit.get("habitName"), tagSet,
                                true);
                        JSONArray info = (JSONArray) habit.get("dailyEntries");
                        for (int l = 0; l < info.length(); l++) {
                            JSONObject infoObj = (JSONObject) info.get(l);
                            bhabit.putDateInfo(new Date(Long.parseLong((String) infoObj.get("time"))),
                                    (boolean) infoObj.get("isDone"), (int) infoObj.get("happiness"));
                        }
                        ue.addHabit(bhabit);
                    } else {
                        throw new RuntimeException("Nonconforming habit type");
                    }
                }
                this.users.put(ue.username, ue);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final UserDatabase ud = new UserDatabase();

    public static UserDatabase getInstance() {
        return ud;
    }

    public TreeMap<String, UserEntry> getUserInfo() {
       return users;
    }

    public static void registerNewUser(UserEntry u) {
        users.put(u.username, u);
    }

    public UserEntry getTheUserEntry(String userName) {
        return users.get(userName);
    }
}
