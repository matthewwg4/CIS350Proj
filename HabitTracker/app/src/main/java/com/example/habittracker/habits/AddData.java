package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.datamanagement.FakeUserDatabase;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.datamanagement.NumericalHabitTracker;
import com.example.habittracker.login.R;

import java.util.Set;

public class AddData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        TextView tv0 = new TextView(this);
//        tv0.setText("");
//        tv0.setVisibility(View.VISIBLE);
//        tv0.setTextSize(50);
//        TextView tv1 = new TextView(this);
//        tv1.setText("Habit: " + check);
//        tv1.setVisibility(View.VISIBLE);
//        tv1.setTextSize(50);
//        TextView tv2 = new TextView(this);
//        tv2.setText("Type: " + (habit.getHabitType() == HabitType.BINARY ? "Binary" : "Numerical"));
//        tv2.setVisibility(View.VISIBLE);
//        tv2.setTextSize(50);
        //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin_layout);
//        linearLayout.addView(tv0);
//        linearLayout.addView(tv1);
//        linearLayout.addView(tv2);
        Set<HabitTracker> habits = FakeUserDatabase.getInstance().getUserInfo()
                .get(getIntent().getStringExtra("user")).getHabits();
        String habit = getIntent().getStringExtra("habit");

        TextView happ = (TextView) findViewById(R.id.happiness_text_add);
        happ.setText("Happiness 1-10");
        TextView didHab = (TextView) findViewById(R.id.do_text_add);

        EditText didEntry = (EditText) findViewById(R.id.do_text_add_entry);
        for (HabitTracker h : habits) {
            if (h.getHabitName().equals(habit)) {
                if (h.getHabitType() == HabitType.NUMERICAL) { //wanna add how many units done
                    didHab.setText("How much did you do it?");
                    didEntry.setHint("For example, if you ran 3 miles, enter 3");

                } else { //wanna say yes or no if did or not
                    didHab.setText("Did you do it?");
                    didEntry.setHint("Y or N");
                }
            }
        }

    }
}
