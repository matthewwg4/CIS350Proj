package com.example.habittracker.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habittracker.datamanagement.FakeResourceDatabase;
import com.example.habittracker.datamanagement.Resource;
import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.TreeMap;

public class ResourcesActivity extends AppCompatActivity {

    private FakeResourceDatabase frd = FakeResourceDatabase.getInstance();
    private TreeMap<String, Resource> resources = frd.getResInfo();

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        Toolbar toolbar = findViewById(R.id.resources_name);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.resource_list);

        ArrayList<String> recList = new ArrayList(resources.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recList);

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
