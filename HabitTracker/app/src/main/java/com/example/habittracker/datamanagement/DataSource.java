package com.example.habittracker.datamanagement;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import java.math.BigDecimal;
import java.util.Date;

public class DataSource {

    private TreeMap<String, UserEntry> cache = new TreeMap<>();
    private String TAG = "DataSource";

    private static final DataSource ds = new DataSource();

    public static DataSource getInstance() {
        return ds;
    }

    /*
    attempts to get the user with username as userName,
    first by checking the cache and then by using an api call
    to access the mongo database through the web app
    */
    public UserEntry getUser(String userName) {
        if (cache.containsKey(userName)) {
            return cache.get(userName);
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/api/user?name=" + userName);
            AccessWebTask task = new AccessWebTask();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            UserEntry user = task.get();
            if (user == null) {
                return user;
            }
            cache.put(userName, user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /*
    registers a new user if the user is valid through
    an api call to the web app
    */
    public boolean registerNewUser(UserEntry user) {
        try {
            URL url = new URL("http://10.0.2.2:3000/api/newuser?name=" + user.username +
                    "&password=" + user.password);
            AccessWebTaskNewData task = new AccessWebTaskNewData();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            Boolean success = task.get();
            if (!success) {
                return false;
            }
            cache.put(user.username, user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    adds a habit to the user by using an api call
    to access the mongo database through the web app
    */
    public boolean addHabit(UserEntry user, HabitTracker habit) {
        try {
            String urlAddress = "http://10.0.2.2:3000/api/addHabit?name=" + user.username +
                    "&habitName=" + habit.habitName + "&type=";
            if (habit.getHabitType() == HabitType.BINARY) {
                urlAddress = urlAddress + "binary&unit=";
            } else {
                urlAddress = urlAddress + "numerical&unit=" +
                        ((NumericalHabitTracker)habit).getUnitName();
            }
            URL url = new URL(urlAddress);
            AccessWebTaskNewData task = new AccessWebTaskNewData();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            Boolean success = task.get();
            if (!success) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    adds an info point to the habit for the user by using an api call
    to access the mongo database through the web app
    */
    public boolean addInfoPoint(UserEntry user, HabitTracker habit, DateInfo info) {
        try {
            String urlAddress = "http://10.0.2.2:3000/api/addInfoPoint?name=" + user.username +
                    "&habit=" + habit.habitName + "&timestamp=" + info.getDate().getTime() +
                    "&amount=" + info.getUnitValue() + "&isDone=" + info.isDone() +
                    "&happiness=" + info.getHappiness();
            URL url = new URL(urlAddress);
            AccessWebTaskNewData task = new AccessWebTaskNewData();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            Boolean success = task.get();
            if (!success) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

/*
the AsyncTask responsible for obtaining
all the data for a user
*/
class AccessWebTask extends AsyncTask<URL, String, UserEntry> {

    private int count = 0;
    private String TAG = "AccessWebTask";

    @Override
    protected UserEntry doInBackground(URL... urls) {
        try {
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

            UserEntry user = new UserEntry(userName, password);

            for (int i = 0; i < habitsArray.length(); i++) {
                JSONObject habitObj = habitsArray.getJSONObject(i);
                String habitName = habitObj.getString("habitName");
                String type = habitObj.getString("type");

                if (type.equals("binary")) {
                    JSONArray jsonTags = habitObj.getJSONArray("tags");
                    JSONArray jsonEntries = habitObj.getJSONArray("dailyEntries");
                    Set<String> tags = new TreeSet<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");

                    for (int j = 0; j < jsonTags.length(); j++) {
                        String tagName = jsonTags.getString(i);
                        tags.add(tagName);
                    }
                    BinaryHabitTracker binHabit = new BinaryHabitTracker(habitName, tags, false);
                    user.addHabit(binHabit);
                    for (int k = 0; k < jsonEntries.length(); k++) {
                        JSONObject dailyEntryObj = jsonEntries.getJSONObject(k);
                        String timestamp = dailyEntryObj.getString("time");
                        boolean isDone = dailyEntryObj.getBoolean("isDone");
                        int happiness = dailyEntryObj.getInt("happiness");
                        Date date = dateFormat.parse(timestamp);
                        binHabit.putDateInfo(date, isDone, happiness);
                    }


                } else { //type equals numeric
                    String unit = habitObj.getString("unit");
                    JSONArray jsonTags = habitObj.getJSONArray("tags");
                    JSONArray jsonEntries = habitObj.getJSONArray("dailyEntries");
                    Set<String> tags = new TreeSet<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");

                    for (int j = 0; j < jsonTags.length(); j++) {
                        String tagName = jsonTags.getString(i);
                        tags.add(tagName);
                    }
                    NumericalHabitTracker numHabit = new NumericalHabitTracker(habitName, tags, false, unit);
                    user.addHabit(numHabit);
                    for (int k = 0; k < jsonEntries.length(); k++) {
                        JSONObject dailyEntryObj = jsonEntries.getJSONObject(k);
                        String timestamp = dailyEntryObj.getString("time");
                        float value = BigDecimal.valueOf(dailyEntryObj.getDouble("amount")).floatValue();
                        int happiness = dailyEntryObj.getInt("happiness");
                        Date date = dateFormat.parse(timestamp);
                        numHabit.putDateInfo(date, value, happiness);
                    }
                }

            }

            // this will be passed to onPostExecute method
            return user;
        }
        catch (Exception e) {
            return null;
        }
    }
}

/*
the AsyncTask responsible for adding new data
(such as habits and info points) to a user
*/
class AccessWebTaskNewData extends AsyncTask<URL, String, Boolean> {

    private String TAG = "AccessWebTaskNewData";

    @Override
    protected Boolean doInBackground(URL... urls) {
        try {
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
            return msg.contains("success");
        }
        catch (Exception e) {
            return false;
        }
    }
}