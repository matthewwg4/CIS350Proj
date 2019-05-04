package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.datamanagement.DataSource;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.login.R;

public class CurrentHabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_habits);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        for (HabitTracker h : DataSource.getInstance().getUser(getIntent().getStringExtra("user")).getHabits()) {
            final String habit = h.getHabitName();
            Button myButton = new Button(this);
            myButton.setText(habit);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     String msg = getIntent().getStringExtra("user");
                     Intent i = new Intent(getApplicationContext(), HabitForm.class);
                     i.putExtra("user", msg);
                     i.putExtra("habit", habit);
                     i.putExtra("time", "current");

                     startActivity(i);
                }
            });
            LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
            layout.addView(myButton);
        }

    }
}
