package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeSurveyDatabase;
import com.example.habittracker.datamanagement.Survey;
import com.example.habittracker.login.R;
import com.example.habittracker.resources.ResourcesActivity;
import com.example.habittracker.surveys.SurveyActivity;

import java.util.TreeMap;

public class HabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

    }
    //add onclick methods for habit and viz buttons during integration

    public void gotoAdd(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), AddHabitsActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    public void gotoCurrent(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), CurrentHabitsActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    public void gotoPast(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), PastHabitsActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }
}
