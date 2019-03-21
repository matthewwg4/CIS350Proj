package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.habittracker.login.R;

public class AddExistingHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_habit);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

    }
    //add onclick methods for habit and viz buttons during integration

//    public void gotoName(View v) {
//        String msg = getIntent().getStringExtra("user");
//        Intent i = new Intent(getApplicationContext(), ResourcesActivity.class);
//        i.putExtra("user", msg);
//
//        startActivity(i);
//    }
//
//    public void gotoSurveys(View v) {
//        boolean surveysToDo = false;
//        String msg = getIntent().getStringExtra("user");
//
//        for(Survey s: surveys.values()) {
//            if(!s.responses.containsKey(msg)) {
//                surveysToDo = true;
//            }
//        }
//
//        if(!surveysToDo) {
//            Toast.makeText(getApplicationContext(), "There are currently no unfinished surveys for you. Please check back later.", Toast.LENGTH_LONG).show();
//        } else {
//            Intent i = new Intent(getApplicationContext(), SurveyActivity.class);
//            i.putExtra("user", msg);
//
//            startActivity(i);
//        }
//
//
//    }
}
