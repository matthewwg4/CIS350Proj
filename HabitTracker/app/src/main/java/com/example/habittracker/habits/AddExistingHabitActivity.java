package com.example.habittracker.habits;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddExistingHabitActivity extends AppCompatActivity {

    private String spinnerChoice = "----";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing_habit);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerChoice = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editText = (EditText) findViewById(R.id.editText);
    }
    //add onclick methods for habit and viz buttons during integration

    public void gotoExisitngHabits(View v) {
        String searchWith = editText.getText().toString();
        if (spinnerChoice.equals("----") || searchWith == null || searchWith.equals("")) {
            Toast.makeText
                    (getApplicationContext(), "Invalid Search", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String msg = getIntent().getStringExtra("user");
        //change this line
        Intent i = null; //new Intent(getApplicationContext(), ResourcesActivity.class);
        i.putExtra("user", msg);
        i.putExtra("search by", spinnerChoice);
        i.putExtra("search with", searchWith);

        startActivity(i);
    }
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
