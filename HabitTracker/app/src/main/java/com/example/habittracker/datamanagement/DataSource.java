package com.example.habittracker.datamanagement;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataSource {

    private String TAG = "DataSource";

    public DataSource() {
    }

    private static final DataSource ds = new DataSource();

    public static DataSource getInstance() {
        return ds;
    }

    public UserEntry getUser(String userName) {
        // TODO
        // The main purpose of this function is to test
        // connection to mongo
        try {
            URL url = new URL("http://10.0.2.2:3000/api?name=" + userName);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            UserEntry user = task.get();
            Log.d(TAG, "getUser: userName: " + user.username);
            Log.d(TAG, "getUser: " + user.password);
            return user;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }
}

class AccessWebTask extends AsyncTask<URL, String, UserEntry> {

    private String TAG = "AccessWebTask";

    @Override
    protected UserEntry doInBackground(URL... urls) {
        try {
            Log.d(TAG, "doInBackground : called"); // debug purpose
            // get the first URL from the array
            URL url = urls[0];
            // create connection and send HTTP request
            HttpURLConnection conn =
                    (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            // read first line of data that is returned
            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();
            // use Android JSON library to parse JSON
            JSONObject jo = new JSONObject(msg);
            // assumes that JSON object contains a "userName" field
            String userName = jo.getString("userName");
            String password = jo.getString("password");

            JSONArray habitsArray = jo.getJSONArray("habits");


            Log.d(TAG, "doInBackground: userName: " + userName);
            Log.d(TAG, "doInBackground: password: " + password);


            //ArrayList<>
            UserEntry user = new UserEntry(userName, password);

            for (int i = 0; i < habitsArray.length(); i++) {
                JSONObject habitObj = habitsArray.getJSONObject(i);
                String habitName = habitObj.getString("habitName");
                Log.d(TAG, "doInBackground: habit:" + habitName);
                String type = habitObj.getString("type");

                if (type.equals("binary")) {
                    JSONArray jsonTags = habitObj.getJSONArray("tags");
                    JSONArray jsonEntries = habitObj.getJSONArray("dailyEntries");
                    Set<String> tags = new TreeSet<>();
                    for (int j = 0; j < jsonTags.length(); j++) {
                        String tagName = jsonTags.getString(i);
                        tags.add(tagName);
                    }
                    BinaryHabitTracker binHabit = new BinaryHabitTracker(habitName, tags, false);
                    user.addHabit(binHabit);
                    for (int k = 0; k < jsonEntries.length(); k++) {
                        JSONObject dailyEntryObj = jsonEntries.getJSONObject(k);
                        String timestamp = dailyEntryObj.getString("timestamp");
                        boolean isDone = dailyEntryObj.getBoolean("isDone");
                        int happiness = dailyEntryObj.getInt("happiness");
                        @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("MM/dd/yyyy").parse(timestamp);
                        binHabit.putDateInfo(date, isDone, happiness);
                    }


                } else { //type equals numeric
                    String unit = habitObj.getString("unit");
                    JSONArray jsonTags = habitObj.getJSONArray("tags");
                    JSONArray jsonEntries = habitObj.getJSONArray("dailyEntries");
                    Set<String> tags = new TreeSet<>();
                    for (int j = 0; j < jsonTags.length(); j++) {
                        String tagName = jsonTags.getString(i);
                        tags.add(tagName);
                    }
                    NumericalHabitTracker numHabit = new NumericalHabitTracker(habitName, tags, false, unit);
                    user.addHabit(numHabit);
                    for (int k = 0; k < jsonEntries.length(); k++) {
                        JSONObject dailyEntryObj = jsonEntries.getJSONObject(k);
                        String timestamp = dailyEntryObj.getString("timestamp");
                        //float value = (float)dailyEntryObj.getDouble("isDone");
                        float value = BigDecimal.valueOf(dailyEntryObj.getDouble("amount")).floatValue();
                        int happiness = dailyEntryObj.getInt("happiness");
                        @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("MM/dd/yyyy").parse(timestamp);
                        Log.d(TAG, "doInBackground: date: " + date);
                        Log.d(TAG, "doInBackground: happiness: " + happiness);
                        Log.d(TAG, "doInBackground: value: " + value);
                        numHabit.putDateInfo(date, value, happiness);
                    }
                }

            }

            // this will be passed to onPostExecute method
            return user;
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }
}

