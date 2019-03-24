package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.datamanagement.FakeHabitDatabase;
import com.example.habittracker.datamanagement.FakeUserDatabase;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.datamanagement.NumericalHabitTracker;
import com.example.habittracker.login.R;

import java.util.Set;

public class HabitForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Set<HabitTracker> habits = FakeUserDatabase.getInstance().getUserInfo()
                .get((String)getIntent().getStringExtra("user"))
                .getHabits();
        HabitTracker h1 = null;
        final String check = (String)getIntent().getStringExtra("habit");
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
        TextView tv0 = new TextView(this);
        tv0.setText("");
        tv0.setVisibility(View.VISIBLE);
        tv0.setTextSize(50);
        TextView tv1 = new TextView(this);
        tv1.setText("Habit: " + check);
        tv1.setVisibility(View.VISIBLE);
        tv1.setTextSize(50);
        TextView tv2 = new TextView(this);
        tv2.setText("Type: " + (habit.getHabitType() == HabitType.BINARY ? "Binary" : "Numerical"));
        tv2.setVisibility(View.VISIBLE);
        tv2.setTextSize(50);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.lin_layout);
        linearLayout.addView(tv0);
        linearLayout.addView(tv1);
        linearLayout.addView(tv2);
        if (habit.getHabitType() == HabitType.NUMERICAL) {
            TextView tv3 = new TextView(this);
            tv3.setText("Units: " + ((NumericalHabitTracker)habit).getUnitName());
            tv3.setVisibility(View.VISIBLE);
            tv3.setTextSize(50);
            linearLayout.addView(tv3);
        }
        if (((String)getIntent().getStringExtra("time")).equals("current")) {
            Button myButton = new Button(this);
            myButton.setText("Add Data");
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = getIntent().getStringExtra("user");
                    Intent i = new Intent(getApplicationContext(), AddData.class);
                    i.putExtra("user", msg);
                    i.putExtra("habit", check);

                    startActivity(i);
                }
            });
            linearLayout.addView(myButton);
        }

        if (((String)getIntent().getStringExtra("time")).equals("future")) {
            Button myButton = new Button(this);
            myButton.setText("Add Habit");
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FakeUserDatabase.getInstance()
                            .getUserInfo().get((String)getIntent().getStringExtra("user"))
                            .addHabit(habit);
                    finishActivity(0);
                }
            });
            linearLayout.addView(myButton);
        }
    }

}
