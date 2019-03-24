package com.example.habittracker.habits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.habittracker.login.R;

public class CurrentHabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_habits);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);


        Button myButton = new Button(this);
        myButton.setText("Push Me");
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText
                        (getApplicationContext(), "You just took directions from a button", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
        layout.addView(myButton);

    }
    //add onclick methods for habit and viz buttons during integration

//    public void gotoResources(View v) {
//        String msg = getIntent().getStringExtra("user");
//        Intent i = new Intent(getApplicationContext(), ResourcesActivity.class);
//        i.putExtra("user", msg);
//
//        startActivity(i);
//    }
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