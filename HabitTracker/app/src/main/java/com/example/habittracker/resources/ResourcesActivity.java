package com.example.habittracker.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.datamanagement.FakeResourceDatabase;
import com.example.habittracker.datamanagement.Resource;
import com.example.habittracker.login.R;

import java.util.ArrayList;
import java.util.TreeMap;

public class ResourcesActivity extends AppCompatActivity {

    private FakeResourceDatabase frd = FakeResourceDatabase.getInstance();
    private TreeMap<String, Resource> resources = frd.getResInfo();

    ListView list;

    private EditText searchVal;

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

        searchVal = (EditText) findViewById(R.id.resources_search_entry);
    }

    public void searchResources(View v) {
        String searchText = searchVal.getText().toString();
        if(searchText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please type in the name or tag to search for", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(getApplicationContext(), ResourcesSearchResultActivity.class);
            i.putExtra("search", searchText);

            startActivity(i);
        }

    }

}
