package com.example.habittracker.surveys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeResourceDatabase;
import com.example.habittracker.datamanagement.FakeSurveyDatabase;
import com.example.habittracker.datamanagement.Resource;
import com.example.habittracker.datamanagement.Survey;
import com.example.habittracker.login.R;

import java.util.TreeMap;

public class CustomSurveyActivity extends AppCompatActivity {

    FakeSurveyDatabase f = FakeSurveyDatabase.getInstance();
    private TreeMap<String, Survey> surveys = f.getSurInfo();
    String surName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_survey);
        Toolbar toolbar = findViewById(R.id.survey_name);
        setSupportActionBar(toolbar);

        surName = getIntent().getStringExtra("name");
        Survey thisSurvey = surveys.get(surName);

        String surveyQ = thisSurvey.question;

        this.setTitle(surName);

        TextView question = findViewById(R.id.survey_question);
        question.setText(surveyQ);

        RadioGroup rg = findViewById(R.id.survey_choices);

        for(String s : thisSurvey.options) {
            RadioButton r = new RadioButton(this);
            r.setText(s);
            r.setTextSize(30);
            rg.addView(r);
        }


    }

    /*
    submit's the response to a survey
    when the user clicks on the "submit" button
    */
    public void submitSurvey(View v) {
        int response = ((RadioGroup) findViewById(R.id.survey_choices)).getCheckedRadioButtonId();
        if(response == -1) {
            Toast.makeText(getApplicationContext(), "Please select an option.", Toast.LENGTH_LONG).show();
        } else {
            String msg = getIntent().getStringExtra("user");
            f.UpdateSurvey(surName, ((RadioButton) findViewById(response)).getText().toString(), msg);
            finish();
        }
    }


}
