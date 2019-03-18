package com.example.habittracker.resources;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.habittracker.datamanagement.FakeResourceDatabase;
import com.example.habittracker.datamanagement.Resource;
import com.example.habittracker.login.R;

import java.util.TreeMap;

public class CustomResourceActivity extends AppCompatActivity {

    FakeResourceDatabase f = FakeResourceDatabase.getInstance();
    private TreeMap<String, Resource> resources = f.getResInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_resource);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        String recName = getIntent().getStringExtra("name");

        String recInfo = resources.get(recName).resInfo;

        Toolbar title = (Toolbar) findViewById(R.id.resource_name);
        title.setTitle(recName);

        TextView info = (TextView) findViewById(R.id.resource_info);
        info.setText(recInfo);
    }

}
