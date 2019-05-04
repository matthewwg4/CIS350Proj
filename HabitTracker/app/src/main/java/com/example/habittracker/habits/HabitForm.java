package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.habittracker.datamanagement.DataSource;
import com.example.habittracker.datamanagement.DateInfo;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.Set;

public class HabitForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Set<HabitTracker> habits = DataSource.getInstance()
                .getUser(getIntent().getStringExtra("user"))
                .getHabits();
        HabitTracker h1 = null;
        final String check = getIntent().getStringExtra("habit");
        for (HabitTracker h : habits) {
            if (h.getHabitName().equals(check)) {
                h1 = h;
                break;
            }
        }
        final HabitTracker habit = h1;
        if (habit == null) {
            return;
        }


        ListView ph = findViewById(R.id.prev_habits);
        ArrayList<String> entriesList = new ArrayList();

        if(!h1.getTracking().isEmpty()) {
            for (DateInfo d : h1.getTracking()) {
                entriesList.add(d.getDate().toString());
                if (h1.getHabitType().equals(HabitType.NUMERICAL)) {
                    entriesList.add(Float.toString(d.getUnitValue()) + " done");
                } else {
                    entriesList.add("Habit done?: " + Boolean.toString(d.isDone()));
                }


            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entriesList);
            ph.setAdapter(adapter);
        }

    }

    public void gotoAddData(View v) {
        final String check = (String)getIntent().getStringExtra("habit");
        String msg = getIntent().getStringExtra("user");
        Intent i = new Intent(getApplicationContext(), AddData.class);
        i.putExtra("user", msg);
        i.putExtra("habit", check);

        startActivity(i);
        finish();
    }
}
