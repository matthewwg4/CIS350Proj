package com.example.habittracker.datamanagement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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

            Log.d(TAG, "doInBackground: userName: " + userName);
            Log.d(TAG, "doInBackground: password: " + password);

            //ArrayList<>
            UserEntry user = new UserEntry(userName, password);

            // this will be passed to onPostExecute method
            return user;
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }
}

