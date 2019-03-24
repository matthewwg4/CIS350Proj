package com.example.habittracker.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeResourceDatabase;
import com.example.habittracker.datamanagement.Resource;
import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.TreeMap;

public class ResourcesSearchResultActivity extends AppCompatActivity {

    ListView list;
    private FakeResourceDatabase frd = FakeResourceDatabase.getInstance();
    private TreeMap<String, Resource> resources = frd.getResInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_search_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String msg = getIntent().getStringExtra("search");

        list = (ListView) findViewById(R.id.resources_searched);
        ArrayList<String> recList = new ArrayList(resources.keySet());

        ArrayList<String> searchedList = new ArrayList<>();

        for(String s : recList) {
            if(s.toLowerCase().contains(msg.toLowerCase())) {
                searchedList.add(s);
            }
        }

        for(Resource r: resources.values()) {
            if(!searchedList.contains(r.name) & r.tags.contains(msg.toLowerCase())) {
                searchedList.add(r.name);
            }
        }

        if(searchedList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No resources found", Toast.LENGTH_LONG).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchedList);

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String msg = ((TextView) view).getText().toString();
                    Intent i = new Intent(getApplicationContext(), CustomResourceActivity.class);
                    i.putExtra("name", msg);

                    startActivity(i);
                }
            });
        }

    }

}
