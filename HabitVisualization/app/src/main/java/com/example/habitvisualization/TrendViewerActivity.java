package com.example.habitvisualization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrendViewerActivity extends AppCompatActivity {

    private static final String TAG = "TrendViewerActivity";

    private DataStorage dataStorage;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started.");

        //testing purpose below-----------------------------------
        dataStorage = new DataStorage();

        dataStorage.addNewHabitTracker("A", new HabitTracker());
        dataStorage.addNewHabitTracker("B", new HabitTracker());
        dataStorage.addNewHabitTracker("C", new HabitTracker());
        //testing purpose above------------------------------------

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        List<String> habitNamesList =
                new ArrayList<String>(dataStorage.getAllHabitNames());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_habitNames);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        recyclerViewAdapter = new HabitViewAdapter(this, habitNamesList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
