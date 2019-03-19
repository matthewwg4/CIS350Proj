package com.example.habitvisualization;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

        dateFormat = new SimpleDateFormat("MM/dd");

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
        Date newestDate = defaultLatestDate;
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDate = new Date((long) dataPoints[0].getX());
        calendar.add(Calendar.DATE, +numDateShow + 1);

        setXAxis(oldestDate, newestDate);
        setLineGraph(dataPoints);

    }

    // -------------------------------------------------------------------
    // PRIVATE FUNCTIONS -------------------------------------------
    // -------------------------------------------------------------------

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
calendar.add(Calendar.DATE, numDateShow);
        return dataPoints;
    }

    // -------------------------------------------------------------------
    // PUBLIC FUNCTIONS -------------------------------------------
    // -------------------------------------------------------------------

    public void changeGraph(String habitNameChosen) {
        Log.d(TAG, "changeGraph: chosen habit is " + habitNameChosen);
    }

}