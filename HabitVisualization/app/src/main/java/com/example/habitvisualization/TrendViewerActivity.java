package com.example.habitvisualization;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrendViewerActivity extends AppCompatActivity {

    private static final String TAG = "TrendViewerActivity";

    private DataStorage dataStorage;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GraphView graph;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private final int numDateShow = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormat = new SimpleDateFormat("E");

        // Set default values ------------------------------------
        calendar = Calendar.getInstance();
        Date defaultLatestDate = calendar.getTime();
        Log.d(TAG, "onCreate: defaultLatestDate is " +
                defaultLatestDate.toString());
        Log.d(TAG, "onCreate: formatted defaultLatestDate is " +
                dateFormat.format(defaultLatestDate));

        //testing purpose below-----------------------------------
        dataStorage = new DataStorage();

        dataStorage.addNewHabitTracker("A", new HabitTracker());
        dataStorage.addNewHabitTracker("B", new HabitTracker());
        dataStorage.addNewHabitTracker("C", new HabitTracker());
        //testing purpose above------------------------------------

        /* Initialize recycler view so the user can choose a habit to display
         * on the chart*/
        initRecyclerView();
        graph = findViewById(R.id.graph);

        //Testing purpose

        DataPoint[] dataPoints = getDataPoints(defaultLatestDate);

        Date newestDateOnAxis = defaultLatestDate;
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDateOnAxis = calendar.getTime();
        calendar = Calendar.getInstance();

        setYAxisLeft();
        setXAxis(oldestDateOnAxis, newestDateOnAxis);
        setBarGraph(dataPoints);
        setLineGraph(dataPoints);
        changeGraph("TEST TITLE");
    }

    // -------------------------------------------------------------------
    // PRIVATE FUNCTIONS -------------------------------------------
    // -------------------------------------------------------------------

    // The left axis corresponds to happiness scale from 1 to 10
    private void setYAxisLeft() {
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(11);
    graph.getViewport().setMinY(0f);
        graph.getViewport().setMaxY(10f);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    private void setXAxis(Date oldestDate, Date newestDate) {
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(numDateShow);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(oldestDate.getTime());
        graph.getViewport().setMaxX(newestDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    private void setLineGraph(DataPoint[] dataPoints) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);
    }

    private void setBarGraph(DataPoint[] dataPoints) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
               return ContextCompat.getColor(getBaseContext(), R.color.colorBarGraphNumerical);
            }
        });
        series.setSpacing(1);
        graph.addSeries(series);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        List<String> habitNamesList =
                new ArrayList<>(dataStorage.getAllHabitNames());

        recyclerView = findViewById(R.id.recyclerView_habitNames);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        recyclerViewAdapter = new HabitViewAdapter(this, habitNamesList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private DataPoint[] getDataPoints(Date latestChosenDate) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        calendar.setTime(latestChosenDate);
        Date curDate;
        for (int i = 0; i < dataPoints.length; i++) {
            curDate = calendar.getTime();
            //Testing: i in the below
            DataPoint newDataPoint = new DataPoint(curDate, i);
            dataPoints[dataPoints.length - i - 1] = newDataPoint;
            calendar.add(Calendar.DATE, -1);
        }
        // Reset calendar to the current time
        calendar = Calendar.getInstance();
        return dataPoints;
    }

    private void setGraphTitle(String habitNameChosen, Date newestDate) {
        calendar.setTime(newestDate);
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDate = calendar.getTime();

        SimpleDateFormat dateFormatForTitle = new SimpleDateFormat("MM/dd/yyyy");
        String title = habitNameChosen + " from " + dateFormatForTitle.format(oldestDate)
                + " to " + dateFormatForTitle.format(newestDate);
        graph.setTitle(title);
    }

    // -------------------------------------------------------------------
    // PUBLIC FUNCTIONS -------------------------------------------
    // -------------------------------------------------------------------

    public void changeGraph(String habitNameChosen) {
        Log.d(TAG, "changeGraph: chosen habit is " + habitNameChosen);
//Testing purpose
        Date newestDate = calendar.getTime();
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDate = calendar.getTime();
       calendar = Calendar.getInstance();

       // Default newest date is calendar.getTime() --> now
      setGraphTitle(habitNameChosen, calendar.getTime());
    }

}