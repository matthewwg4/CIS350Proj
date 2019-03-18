package com.example.habittracker.surveys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeSurveyDatabase;
import com.example.habittracker.datamanagement.Survey;
import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.TreeMap;

public class SurveyActivity extends AppCompatActivity {

    FakeSurveyDatabase f = FakeSurveyDatabase.getInstance();
    private TreeMap<String, Survey> surveys = f.getSurInfo();

    ListView list;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        user = getIntent().getStringExtra("user");

        list = (ListView) findViewById(R.id.survey_list);

        ArrayList<String> surList = new ArrayList();

        for(Survey s: surveys.values()) {
            if(!s.responses.containsKey(user)) {
                surList.add(s.name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, surList);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = ((TextView) view).getText().toString();
                Intent i = new Intent(getApplicationContext(), CustomSurveyActivity.class);
                i.putExtra("name", msg);
                i.putExtra("user", user);

                startActivity(i);
                finish();
            }
        });

    }
}
