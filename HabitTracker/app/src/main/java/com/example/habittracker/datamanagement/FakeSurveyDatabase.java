package com.example.habittracker.datamanagement;
import android.icu.text.StringSearch;
import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class FakeSurveyDatabase {
    private static TreeMap<String, Survey> surveys = new TreeMap<>();

    private FakeSurveyDatabase() {
    }

    private static final FakeSurveyDatabase fsd = new FakeSurveyDatabase();


    public static FakeSurveyDatabase getInstance() {
        return fsd;
    }

    public TreeMap<String, Survey> getSurInfo() {
        return fsd.surveys;
    }

    public void UpdateSurvey(String name, String response, String user) {
        try {
            String[] s = {name, response, user};
            UpdateSurveyTask task = new UpdateSurveyTask();
            task.execute(s);
        } catch (Exception e){
        }
    }

    public class UpdateSurveyTask extends AsyncTask<String, Void, String>{
        /*This method is called in background when this object's "execute" method is invoked.The arguments passed to "execute" are passed to this method.*/
        protected String doInBackground(String... params) {
            try {
                MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.2.2:27017"));
                DB database = mongoClient.getDB("myDatabase");
                DBCollection collection = database.getCollection("surveys");
                DBCursor surList = collection.find(new BasicDBObject("surveyName", params[0]));
                DBObject sur = surList.next();

                DBObject query = new BasicDBObject("_id", sur.get("_id"));
                DBObject resp = new BasicDBObject("userResponses", new BasicDBObject("username", params[2]).append("response",params[1]));
                DBObject update = new BasicDBObject("$push", resp);
                collection.update(query, update);

                mongoClient.close();
                return "fine";
            } catch (Exception e) {
                return e.toString();
            }
        }

        /*This method is called in foreground after doInBackground finishes.It can access and update Views in user interface.*/
        protected void onPostExecute(String msg) {
        }
    }


    public class PopulateSurveysTask extends AsyncTask<Void, Void, String>{
        /*This method is called in background when this object's "execute" method is invoked.The arguments passed to "execute" are passed to this method.*/
        protected String doInBackground(Void... params) {
            try {
                MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.2.2:27017"));
                DB database = mongoClient.getDB("myDatabase");
                DBCollection collection = database.getCollection("surveys");

                DBCursor cursor = collection.find();

                while (cursor.hasNext()) {

                    DBObject survey = cursor.next();
                    ArrayList<String> options = new ArrayList<>();
                    BasicDBList ops = (BasicDBList) survey.get("options");
                    for(Object o: ops) {
                        options.add((String) o);
                    }
                    TreeMap<String, String> resp = new TreeMap<>();

                    BasicDBList ur = (BasicDBList) survey.get("userResponses");
                    for(Object o : ur) {
                        resp.put((String) ((DBObject) o).get("username"), (String) ((DBObject) o).get("response"));
                    }

                    Survey s = new Survey((String) survey.get("surveyName"), (String) survey.get("question"), options);
                    s.responses = resp;

                    surveys.put(s.name, s);

                }
                mongoClient.close();
                return "fine";
            } catch (Exception e) {
                return e.toString();
            }
        }/*This method is called in foreground after doInBackground finishes.It can access and update Views in user interface.*/
        protected void onPostExecute(String msg) {
        }
        // not implemented but you can use this if you’d like}}
    }

    public String populateSurveys() {
        try {
            surveys = new TreeMap<>();
            PopulateSurveysTask task = new PopulateSurveysTask();
            task.execute();
            return("fine");
        } catch (Exception e){
            return e.toString();
        }

    }
}