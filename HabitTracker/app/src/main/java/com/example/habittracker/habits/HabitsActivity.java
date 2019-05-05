package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.habittracker.login.R;

public class HabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

    }
    //add onclick methods for habit and viz buttons during integration

    /*
    goes to the page to add a habit
    when the user clicks on the "add" button
    */
    public void gotoAdd(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), AddExistingHabitActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    /*
    goes to a page with a list of all the user's habits
    when the user clicks on the "current" button
    */
    public void gotoCurrent(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), CurrentHabitsActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }
}
