package com.example.habittracker.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeSurveyDatabase;
import com.example.habittracker.datamanagement.Survey;
import com.example.habittracker.habits.HabitsActivity;
import com.example.habittracker.login.R;
import com.example.habittracker.resources.ResourcesActivity;
import com.example.habittracker.surveys.SurveyActivity;
import com.example.habittracker.visualization.TrendViewerActivity;

import java.util.TreeMap;

public class MenuActivity extends AppCompatActivity {

    FakeSurveyDatabase f = FakeSurveyDatabase.getInstance();
    private TreeMap<String, Survey> surveys = f.getSurInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

    }

    protected void onResume() {
        super.onResume();
        f.populateSurveys();
    }

    //add onclick methods for habit and viz buttons during integration

    /*
    goes to the page to chose to add a habit or view current habits
    when the user clicks on the "habits" button
    */
    public void gotoHabits(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), HabitsActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    /*
    goes to the page to chose with a list of resources
    when the user clicks on the "resources" button
    */
    public void gotoResources(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), ResourcesActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    /*
    goes to the page to with habit visualizations
    when the user clicks on the "visualizations" button
    */
    public void goToVisualization(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), TrendViewerActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    /*
    goes to the page to chose to view current surveys
    when the user clicks on the "surveys" button
    */
    public void gotoSurveys(View v) {
        boolean surveysToDo = false;
        String msg = getIntent().getStringExtra("user");

        Intent i = new Intent(getApplicationContext(), SurveyActivity.class);
        i.putExtra("user", msg);
        startActivity(i);

    }
}
