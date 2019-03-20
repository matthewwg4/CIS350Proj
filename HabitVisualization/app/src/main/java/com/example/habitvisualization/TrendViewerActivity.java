package com.example.habitvisualization;

import android.graphics.Color;
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

import java.text.ParseException;
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

        //testing purpose below-----------------------------------
        dataStorage = new DataStorage();
        dataStorage.addNewHabitTracker("A", new NumericalHabitTracker());
        HabitTracker habitTrackerA = dataStorage.getHabitTracker("A");
        Date tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 10, 3);
//1

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 3, 5);
//2

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 300, 8);
//3

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 30, 4);
//4

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 30, 4);
//5

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 30, 4);
   //6

        calendar.add(Calendar.DATE, -1);
        tryDate = calendar.getTime();
        habitTrackerA.putDateInfo(tryDate, true, 10, 6);
//7

        calendar = Calendar.getInstance();

        dataStorage.addNewHabitTracker("B", new BinaryHabitTracker());
        //testing purpose above------------------------------------

        /* Initialize recycler view so the user can choose a habit to display
         * on the chart*/
        initRecyclerView();
        graph = findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);
//        Date newestDateOnAxis = defaultLatestDate;
//
//        String yAxisLeftTitle = getResources().getString(R.string.happiness_unit);
//        setYAxisLeft(yAxisLeftTitle);
//        setXAxis(newestDateOnAxis);
//        changeGraph("A"); //testing purpose
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
//        graph.getGridLabelRenderer().setVerticalAxisTitle(axisTitle);
        graph.getViewport().setMinY(0f);
        graph.getViewport().setMaxY(10f);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    private void setXAxis(Date newestDate) {
        calendar.add(Calendar.DATE, -numDateShow + 1);
        Date oldestDate = calendar.getTime();
        calendar = Calendar.getInstance(); //reset the date

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(numDateShow);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(2);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(oldestDate.getTime());
        graph.getViewport().setMaxX(newestDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    // Line graph corresponds to level of happiness
    private void setBarGraphHappiness(DataPoint[] dataPoints) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        graph.addSeries(series);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return ContextCompat.getColor(getBaseContext(), R.color.colorBarGraph);
            }
        });
        series.setSpacing(10);
        series.setDrawValuesOnTop(true);
    }

    // Bar graph corresponds to the data that is not the level of happiness
    private void setLineGraphUnitValue(DataPoint[] dataPoints) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.getSecondScale().addSeries(series);
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

    private DataPoint[] getDataPointsUnitValues(Date latestChosenDate, List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos == null) {
            Log.d(TAG, "getDataPointsHappiness: invalid input");
        } else {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints ++) {
                    calendar.add(Calendar.DATE, - indexDataPoints);
                    Log.d(TAG, "calendar.getTime()" + calendar.getTime().toString());
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
                    Log.d(TAG, "curDateToShow" + curDateToShow.toString());
                    float unitValue = 0;

                    // Check if there is data on the date of curDateToShow
                    int indexDateInfos = 0;
                    while (indexDateInfos < dateInfos.size()) {
                        Date curDateInDateInfos = dateInfos.get(indexDateInfos).getDate();
                        curDateInDateInfos = ymdFormat.parse(ymdFormat.format(curDateInDateInfos));
                        if (firstDateEqualsSecondDate(curDateToShow, curDateInDateInfos)) {
                            unitValue = dateInfos.get(indexDateInfos).getUnitValue();
                            break;
                        }
                        indexDateInfos++;
                    }

                    dataPoints[numDateShow - indexDataPoints - 1] =
                            new DataPoint(curDateToShow, unitValue);
                    // Reset calendar to the current time
                    calendar = Calendar.getInstance();
                }
            } catch (ParseException e) {
                Log.d(TAG, "getDataPointsUnitValue: got parse exception");
            }
        }
        return dataPoints;
    }

    //This is to be input into the line graph
    private DataPoint[] getDataPointsHappiness(Date latestChosenDate, List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos == null) {
            Log.d(TAG, "getDataPointsHappiness: invalid input");
        } else {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints ++) {
                    calendar.add(Calendar.DATE, - indexDataPoints);
                    Log.d(TAG, "calendar.getTime()" + calendar.getTime().toString());
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
                    Log.d(TAG, "curDateToShow" + curDateToShow.toString());
                    int happiness = 0;

                    // Check if there is data on the date of curDateToShow
                    int indexDateInfos = 0;
                    while (indexDateInfos < dateInfos.size()) {
                        Date curDateInDateInfos = dateInfos.get(indexDateInfos).getDate();
                        curDateInDateInfos = ymdFormat.parse(ymdFormat.format(curDateInDateInfos));
                       if (firstDateEqualsSecondDate(curDateToShow, curDateInDateInfos)) {
                            happiness = dateInfos.get(indexDateInfos).getHappiness();
                            break;
                        }
                        indexDateInfos++;
                    }

                    dataPoints[numDateShow - indexDataPoints - 1] =
                            new DataPoint(curDateToShow, happiness);
                    // Reset calendar to the current time
                    calendar = Calendar.getInstance();
                }
            } catch (ParseException e) {
                Log.d(TAG, "getDataPointsHappiness: got parse exception");
            }
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

    //Only compare the year, month and date within month
    private boolean firstDateEqualsSecondDate(Date date1, Date date2) {
        calendar.setTime(date1);
        int date1Year = calendar.get(Calendar.YEAR);
        int date1Month = calendar.get(Calendar.MONTH);
        int date1Date = calendar.get(Calendar.DATE);

        calendar.setTime(date2);
        int date2Year = calendar.get(Calendar.YEAR);
        int date2Month = calendar.get(Calendar.MONTH);
        int date2Date = calendar.get(Calendar.DATE);

        calendar = Calendar.getInstance(); // reset calendar

        if (date1Year == date2Year && date1Month == date2Month
                && date1Date == date2Date) {
            return true;
        } else {
            return false;
        }
    }

    private boolean firstDateBeforeSecondDate(Date date1, Date date2) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");
        try {
            Date date1NoTime = ymdFormat.parse(ymdFormat.format(date1));
            Date date2NoTime = ymdFormat.parse(ymdFormat.format(date2));
            if (date1NoTime.before(date2NoTime)) {
                return true;
            }
        } catch (ParseException e) {
            Log.d(TAG, "firstDateBeforeSecondDate: parse exception");
            e.printStackTrace();
        }
        return false;
    }

    private boolean firstDateAfterSecondDate(Date date1, Date date2) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");
        try {
            Date date1NoTime = ymdFormat.parse(ymdFormat.format(date1));
            Date date2NoTime = ymdFormat.parse(ymdFormat.format(date2));
            if (date1NoTime.after(date2NoTime)) {
                return true;
            }
        } catch (ParseException e) {
            Log.d(TAG, "firstDateAfterSecondDate: parse exception");
            e.printStackTrace();
        }
        return false;
    }


    // -------------------------------------------------------------------
    // PUBLIC FUNCTIONS --------------------------------------------------s
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
            setBarGraphHappiness(happyDataPoints);

            //Set numerical/binary data (bar graph)
            // Numerical data
            DataPoint[] unitValueDataPoints = getDataPointsUnitValues(latestChosenDate, dateInfos);
//            setBarGraphUnitValue(unitValueDataPoints);
//                        setYAxisRight("TEST RIGHT AXIS TITLE", 0, 100);

        } else {
            Log.d(TAG, "changeGraph: cannot find a habitTracker with the given habit name");
        }
    }

}