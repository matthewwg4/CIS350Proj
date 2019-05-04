package com.example.habittracker.habits;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.habittracker.datamanagement.BinaryHabitTracker;
import com.example.habittracker.datamanagement.DataSource;
import com.example.habittracker.datamanagement.FakeTemplateDatabase;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.datamanagement.NumericalHabitTracker;
import com.example.habittracker.login.R;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AddExistingHabitActivity extends AppCompatActivity {

    private String spinnerChoice = "----";
    private EditText editText;

    DataSource ds = DataSource.getInstance();

    FakeTemplateDatabase ft = FakeTemplateDatabase.getInstance();

    TreeMap<String, HabitType> t = ft.getTemInfo();

    Set<HabitTracker> habits;

    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_habit);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        e = findViewById(R.id.new_habit_name_entry);

        RadioGroup rg = findViewById(R.id.habit_type_options);
        for(String s : t.keySet()) {
            RadioButton r = new RadioButton(this);
            r.setText(s);
            rg.addView(r);
        }
        String u = getIntent().getStringExtra("user");
        habits = ds.getUser(u).getHabits();
    }

    public void createNewHabit(View v) {
        int response = ((RadioGroup) findViewById(R.id.habit_type_options)).getCheckedRadioButtonId();
        EditText p = findViewById(R.id.privacy_entry);
        EditText type = findViewById(R.id.habit_thing_tracking_entry);
        String typeName = type.getText().toString();

        if(e.getText().toString().isEmpty() || response == -1 || p.getText().toString().isEmpty() || typeName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_LONG).show();
        } else if(!(p.getText().toString().toLowerCase()).equals("y") & !(p.getText().toString().toLowerCase()).equals("n")) {
            Toast.makeText(getApplicationContext(), "The privacy field can only accept Y or N", Toast.LENGTH_LONG).show();

        } else {
                boolean nameExists = false;
                for(HabitTracker h : habits) {
                    if(h.getHabitName().equals(e.getText())) {
                        nameExists = true;
                    }
                }

                if(nameExists) {
                    Toast.makeText(getApplicationContext(), "Another habit already has this name.", Toast.LENGTH_LONG).show();
                } else {
                    HabitTracker ht;


                    if (((RadioButton) findViewById(response)).getText().toString().equals("Numerical")) {
                        ht = new NumericalHabitTracker(e.getText().toString(), new TreeSet<String>(),
                                p.getText().toString().toLowerCase().equals("y"),
                                typeName);
                    } else {
                        ht = new BinaryHabitTracker(e.getText().toString(), new TreeSet<String>(),
                                p.getText().toString().toLowerCase().equals("y"));
                    }
                    String u = getIntent().getStringExtra("user");
                    ds.getUser(u).addNewHabit(ht);
                    finish();

                }
            }
        }
    }

