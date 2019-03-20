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

        dateFormat = new SimpleDateFormat("EE");

        // Set default values ------------------------------------
        calendar = Calendar.getInstance();
        Date defaultLatestDate = calendar.getTime();
        Log.d(TAG, "onCreate: defaultLatestDate is " +
                defaultLatestDate.toString());
        Log.d(TAG, "onCreate: formatted defaultLatestDate is " +
                dateFormat.format(defaultLatestDate));

        //testing purpose below-----------------------------------
        dataStorage = new DataStorage();

        dataStorage.addNewHabitTracker("A", new NumericalHabitTracker());
        HabitTracker habitTrackerA = dataStorage.getHabitTracker("A");
        habitTrackerA.putDateInfo(calendar.getTime(), true, 10, 3);
        calendar.add(Calendar.DATE, -3);
        Date oldestDateA = calendar.getTime();
        habitTrackerA.putDateInfo(oldestDateA, true, 300, 8);
        calendar = Calendar.getInstance();

        dataStorage.addNewHabitTracker("B", new BinaryHabitTracker());
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

        String yAxisLeftTitle = getResources().getString(R.string.happiness_unit);
        setYAxisLeft(yAxisLeftTitle);
        setYAxisRight("TEST RIGHT AXIS TITLE", 0, 100);
        setXAxis(oldestDateOnAxis, newestDateOnAxis);
        setBarGraph(dataPoints);
        setLineGraph(dataPoints);
        changeGraph("A"); //testing purpose
    }

    // -------------------------------------------------------------------
    // PRIVATE FUNCTIONS -------------------------------------------------
    // -------------------------------------------------------------------

    //The right axis corresponds to the non-happiness data input by the user
    private void setYAxisRight(String axisTitle, float minY, float maxY) {
        graph.getSecondScale().setMinY(minY);
        graph.getSecondScale().setMaxY(maxY);
        graph.getSecondScale().setVerticalAxisTitle(axisTitle);
    }

    // The left axis corresponds to happiness scale from 1 to 10
    private void setYAxisLeft(String axisTitle) {
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(11);
        graph.getGridLabelRenderer().setVerticalAxisTitle(axisTitle);
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

    // Line graph corresponds to level of happiness
    private void setLineGraph(DataPoint[] dataPoints) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);
    }

    // Bar graph corresponds to the data that is not the level of happiness
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

    //TESTING
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

    //This is to be input into the line graph
    private DataPoint[] getDataPointsHappiness(Date latestChosenDate, List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos == null) {
            Log.d(TAG, "getDataPointsHappiness: invalid input");
        } else {
            calendar.setTime(latestChosenDate);
            calendar.add(Calendar.DATE, -numDateShow + 1);
            Date oldestDateShown = calendar.getTime();
            calendar = Calendar.getInstance(); //reset the date

            // Find the range of indices for newest and oldest input dates between
            // latestChosenDate and oldestDateShown
            int indexDataInfos = dateInfos.size() - 1;
            boolean existInputWithinRange = false;
            int indexLatestInputDateWithinRange = 0;
            int indexOldestInputDateWithinRange = 0;
            while (indexDataInfos >= 0) {
                Date curDate = dateInfos.get(indexDataInfos).getDate();
                if (curDate.before(latestChosenDate) && curDate.after(oldestDateShown)) {
                    if (!existInputWithinRange) {
                        indexLatestInputDateWithinRange = indexDataInfos;
                        existInputWithinRange = true;
                    } else {
                        indexOldestInputDateWithinRange = indexDataInfos;
                    }
                }
                indexDataInfos--;
            }

            Date latestInputDateWithinRange = dateInfos.get(indexLatestInputDateWithinRange).getDate();
            Date oldestInputDateWithinRange = dateInfos.get(indexOldestInputDateWithinRange).getDate();

            // Between indexLatestInputDateWithinRage and indexOldestInputDateWithinRange;
            int indexBetweenFoundIndices = indexLatestInputDateWithinRange;
            Date curDate;
            for (int i = 0; i < dataPoints.length; i++) {
                curDate = calendar.getTime();
                float happiness = 0f;

                if (curDate.after(oldestInputDateWithinRange) &&
                        curDate.before(latestInputDateWithinRange)) {
                    for (int j = indexBetweenFoundIndices; j >= indexOldestInputDateWithinRange; j++) {
                        Date inputDate = dateInfos.get(j).getDate();
                        if (curDate.equals(inputDate)) {
                            happiness = dateInfos.get(indexBetweenFoundIndices).getHappiness();
                            indexBetweenFoundIndices--;
                        }
                    }
                }

                DataPoint newDataPoint = new DataPoint(curDate, happiness);
                dataPoints[dataPoints.length - i - 1] = newDataPoint;
                calendar.add(Calendar.DATE, -1);
            }

            // Reset calendar to the current time
            calendar = Calendar.getInstance();
        }
        return dataPoints;
    }

    private void setGraphTitle(String habitNameChosen, Date newestDate) {
        calendar.setTime(newestDate);
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDate = calendar.getTime();

        SimpleDateFormat dateFormatForTitle = new SimpleDateFormat("EEE MM/dd/yyyy");
        String title = habitNameChosen + " from " + dateFormatForTitle.format(oldestDate)
                + " to " + dateFormatForTitle.format(newestDate);
        graph.setTitle(title);
    }

    // -------------------------------------------------------------------
    // PUBLIC FUNCTIONS --------------------------------------------------
    // -------------------------------------------------------------------

    public void changeGraph(String habitNameChosen) {
        Log.d(TAG, "changeGraph: chosen habit is " + habitNameChosen);

        HabitTracker habitTracker = dataStorage.getHabitTracker(habitNameChosen);

        if (habitTracker != null) {
            List<DateInfo> dateInfos = habitTracker.getTracking();

            //Set general graph data
            Date latestChosenDate = calendar.getTime();
            setGraphTitle(habitNameChosen, latestChosenDate);

            //Set happiness data (line graph)
            DataPoint[] happyDataPoints = getDataPointsHappiness(latestChosenDate, dateInfos);

            //Set numerical/binary data (bar graph)
        } else {
            Log.d(TAG, "changeGraph: cannot find a habitTracker with the given habit name");
        }
    }

}