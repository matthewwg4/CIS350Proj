package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.habittracker.login.R;

public class AddHabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habits);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

    }
    //add onclick methods for habit and viz buttons during integration

    public void gotoAddExisting(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), AddExistingHabitActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }

    public void gotoCreateNew(View v) {
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), CreateNewHabitActivity.class);
        i.putExtra("user", msg);

        startActivity(i);
    }
}
