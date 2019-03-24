package com.example.habittracker.habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.datamanagement.DateInfo;
import com.example.habittracker.datamanagement.FakeUserDatabase;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.datamanagement.NumericalHabitTracker;
import com.example.habittracker.login.R;

import java.util.Date;
import java.util.Set;

import static java.lang.Integer.parseInt;

//for the purposes of this class, assume NUMERICAL = INTEGER

public class AddData extends AppCompatActivity {

    EditText didEntry;
    EditText happEntry;

    HabitTracker thisHab;

    FakeUserDatabase f = FakeUserDatabase.getInstance();
    Set<HabitTracker> habits;

    String habit;

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
        habits = f.getUserInfo()
                .get(getIntent().getStringExtra("user")).getHabits();
        habit = getIntent().getStringExtra("habit");

        TextView happ = (TextView) findViewById(R.id.happiness_text_add);
        happ.setText("Happiness 1-10");
        TextView didHab = (TextView) findViewById(R.id.do_text_add);

        didEntry = (EditText) findViewById(R.id.do_text_add_entry);
        for (HabitTracker h : habits) {
            if (h.getHabitName().equals(habit)) {
                if (h.getHabitType() == HabitType.NUMERICAL) { //wanna add how many units done
                    thisHab = h;
                    didHab.setText("How much did you do it?");
                    didEntry.setHint("For example, if you ran 3 miles, enter 3");

                } else { //wanna say yes or no if did or not
                    thisHab = h;
                    didHab.setText("Did you do it?");
                    didEntry.setHint("Y or N");
                }
            }
        }

    }

    public void submitAddData(View v) {
        happEntry = findViewById(R.id.happiness_text_enter);
        String happData = happEntry.getText().toString();
        String didData = didEntry.getText().toString();


        if (happData.isEmpty() || didData.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_LONG).show();
        } else if (thisHab.getHabitType().equals(HabitType.NUMERICAL)) {
            try {
                parseInt(didData);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "You must enter a nonnegative interger for the first field.", Toast.LENGTH_LONG).show();
            }
        } else if (thisHab.getHabitType().equals((HabitType.BINARY))) {
            if (!didData.toLowerCase().equals("y") & !didData.toLowerCase().equals("n")) {
                Toast.makeText(getApplicationContext(), "You must enter Y or N for the first field.", Toast.LENGTH_LONG).show();
            }
        }

        if (happData.equals("1") || happData.equals("2") || happData.equals("3") || happData.equals("4")
                || happData.equals("5") || happData.equals("6") || happData.equals("7") || happData.equals("8")
                || happData.equals("9") || happData.equals("10")) {
            Date tDate = new Date();
            boolean done;
            float val;

            if (thisHab.getHabitType().equals(HabitType.NUMERICAL)) {
                done = (Integer.parseInt(didData) > 0);
                val = Integer.parseInt(didData);
            } else {
                done = didData.toLowerCase().equals("y");
                if (done) {
                    val = 1;
                } else {
                    val = 0;
                }
            }
            DateInfo d = new DateInfo(tDate, done, val, Integer.parseInt(happData));
            thisHab.addTracking(d);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "You must enter an integer between 1-10 for the second field", Toast.LENGTH_LONG).show();
            }

        }
    }
