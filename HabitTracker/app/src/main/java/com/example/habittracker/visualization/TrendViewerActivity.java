package com.example.habittracker.visualization;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.habittracker.datamanagement.DataSource;
import com.example.habittracker.datamanagement.DateInfo;
import com.example.habittracker.datamanagement.HabitTracker;
import com.example.habittracker.datamanagement.HabitType;
import com.example.habittracker.datamanagement.UserEntry;
import com.example.habittracker.login.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
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
import java.util.Set;

public class TrendViewerActivity extends AppCompatActivity {

    private static final String TAG = "TrendViewerActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GraphView graph;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("E");
    private Calendar calendar;
    private final int numDateShow = 7;

    private Set<HabitTracker> habitTrackerSet;

    private DataSource database = DataSource.getInstance();

    private String habitNameDisplay;
    private Date latestDateDisplay;

    private float oldX = 0;
    private float oldY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);


        // Set default values ------------------------------------
        calendar = Calendar.getInstance();
        latestDateDisplay = calendar.getTime();

        Bundle extras = getIntent().getExtras();
        String userName;
        if (extras != null) {
            userName = extras.getString("user");

            //testing purpose below------------------------------------
            UserEntry userEntry = database.getUser(userName);
            if (userEntry != null) {
                habitTrackerSet = userEntry.getHabits();
                //testing purpose above------------------------------------

                /* Initialize recycler view so the user can choose a habit to display
                 * on the chart*/
                initRecyclerView();
                graph = findViewById(R.id.graph);

                if (habitTrackerSet.size() == 0) {
                    setGraphTitle("No habit to show", calendar.getTime());
                    habitNameDisplay = null;
                    setXAxis(calendar.getTime());
                    setYAxisLeft();
                    setLegend();
                } else {
                    HabitTracker habitTrackerDisplay = null;
                    for (HabitTracker h : habitTrackerSet) {
                        habitTrackerDisplay = h;
                        habitNameDisplay = h.getHabitName();
                        break;
                    }
                    changeGraph(habitTrackerDisplay.getHabitName());
                }
            }
        }
    }

    // -------------------------------------------------------------------
    // PRIVATE FUNCTIONS -------------------------------------------------
    // -------------------------------------------------------------------

    //The right axis corresponds to the non-happiness data input by the user
    private void setYAxisRight(List<DateInfo> dateInfos) {
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < dateInfos.size(); i++) {
            double curUnitVal = dateInfos.get(i).getUnitValue();
            if (dateInfos.get(i).getUnitValue() < minY) minY = curUnitVal;
            if (dateInfos.get(i).getUnitValue() > maxY) maxY = curUnitVal;
        }

        if (minY > 0) minY = 0;
        maxY += maxY / 5f;
        graph.getSecondScale().setMinY(minY);
        graph.getSecondScale().setMaxY(maxY);
    }

    // The left axis corresponds to happiness scale from 1 to 10
    private void setYAxisLeft() {
        graph.getGridLabelRenderer().setNumVerticalLabels(11);
        graph.getViewport().setMinY(0f);
        graph.getViewport().setMaxY(10f);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    private void setXAxis(Date newestDate) {
        calendar.setTime(newestDate);
        graph.getViewport().setMaxX((double)calendar.getTime().getTime());
        calendar.add(Calendar.DATE, -numDateShow + 1);
        graph.getViewport().setMinX((double)calendar.getTime().getTime());

        calendar = Calendar.getInstance(); //reset the date

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter
                (this, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(numDateShow);
//        graph.getViewport().setXAxisBoundsManual(true); // set manual x bounds to have nice steps

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    // Bar graph corresponds to level of happiness for binary tracker on days that
    // an activity is not done
    private void setBarGraphHappinessBinaryNegative(String habitName, DataPoint[] dataPoints) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        graph.addSeries(series);
        int barGraphColor = ContextCompat.getColor(getBaseContext(), R.color.colorBarGraphNegative);

        series.setSpacing(50);
        series.setColor(barGraphColor);

        String seriesTitle = getResources().getString(R.string.happiness_unit_negative) + " "
                + habitName;
        series.setTitle(seriesTitle);
    }

    // Bar graph corresponds to level of happiness for binary tracker on days that
    // an activity is done
    private void setBarGraphHappinessBinaryPositive(String habitName, DataPoint[] dataPoints) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        graph.addSeries(series);
        int barGraphColor = ContextCompat.getColor(getBaseContext(), R.color.colorBarGraphPositive);

        series.setSpacing(50);
        series.setColor(barGraphColor);

        String seriesTitle = getResources().getString(R.string.happiness_unit_positive) + " "
                + habitName;
        series.setTitle(seriesTitle);
    }

    // Bar graph corresponds to level of happiness for numerical tracker
    private void setBarGraphHappinessNumerical(DataPoint[] dataPoints) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        graph.addSeries(series);
        int barGraphColor = ContextCompat.getColor(getBaseContext(), R.color.colorBarGraph);

        series.setSpacing(50);
        series.setColor(barGraphColor);

        String seriesTitle = getResources().getString(R.string.happiness_unit);
        series.setTitle(seriesTitle);
    }

    // Bar graph corresponds to the data that is not the level of happiness
    private void setLineGraphUnitValue(DataPoint[] dataPoints, String seriesTitle) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.getSecondScale().addSeries(series);
        series.setTitle(seriesTitle);
    }

    private void initRecyclerView() {
        List<String> habitNamesList = new ArrayList<>();
        for (HabitTracker h : habitTrackerSet) {
            habitNamesList.add(h.getHabitName());
        }

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
        if (dateInfos != null) {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints++) {
                    calendar.add(Calendar.DATE, -indexDataPoints);
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
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
            }
        }
        return dataPoints;
    }

    private DataPoint[] getDataPointsHappinessBinaryPositive(Date latestChosenDate,
                                                             List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos != null) {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints++) {
                    calendar.add(Calendar.DATE, -indexDataPoints);
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
                    int happiness = 0;

                    // Check if there is data on the date of curDateToShow
                    int indexDateInfos = 0;
                    while (indexDateInfos < dateInfos.size()) {
                        DateInfo curDateInfo = dateInfos.get(indexDateInfos);
                        Date curDateInDateInfos = curDateInfo.getDate();
                        curDateInDateInfos = ymdFormat.parse(ymdFormat.format(curDateInDateInfos));
                        if (firstDateEqualsSecondDate(curDateToShow, curDateInDateInfos) &&
                                curDateInfo.isDone()) {
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
            }
        }

        return dataPoints;
    }

    private DataPoint[] getDataPointsHappinessBinaryNegative(Date latestChosenDate,
                                                             List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos != null) {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints++) {
                    calendar.add(Calendar.DATE, -indexDataPoints);
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
                    int happiness = 0;

                    // Check if there is data on the date of curDateToShow
                    int indexDateInfos = 0;
                    while (indexDateInfos < dateInfos.size()) {
                        DateInfo curDateInfo = dateInfos.get(indexDateInfos);
                        Date curDateInDateInfos = curDateInfo.getDate();
                        curDateInDateInfos = ymdFormat.parse(ymdFormat.format(curDateInDateInfos));
                        if (firstDateEqualsSecondDate(curDateToShow, curDateInDateInfos) &&
                                !curDateInfo.isDone()) {
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
            }
        }
        return dataPoints;
    }

    //This is to be input into the line graph
    private DataPoint[] getDataPointsHappinessNumerical(Date latestChosenDate,
                                                        List<DateInfo> dateInfos) {
        DataPoint[] dataPoints = new DataPoint[numDateShow];
        if (dateInfos != null) {
            try {
                SimpleDateFormat ymdFormat = new SimpleDateFormat("yy/MM/dd");

                // Find the latest date to be shown and the earliest date to be shown
                // and eliminate the time (only keep year, month and date)
                Date latestDateNoTime = ymdFormat.parse(ymdFormat.format(latestChosenDate).toString());
                calendar = Calendar.getInstance(); // reset calendar

                // Iterate through all of the dates from the latestChosenDate to
                // seven days prior to this day
                for (int indexDataPoints = 0; indexDataPoints < numDateShow; indexDataPoints++) {
                    calendar.add(Calendar.DATE, -indexDataPoints);
                    Date curDateToShow = ymdFormat.parse(ymdFormat.format(calendar.getTime()).toString());
                    int happiness = 0;

                    // Check if there is data on the date of curDateToShow
                    int indexDateInfos = 0;
                    while (indexDateInfos < dateInfos.size()) {
                        DateInfo curDateInfo = dateInfos.get(indexDateInfos);
                        Date curDateInDateInfos = curDateInfo.getDate();
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

        return date1Year == date2Year && date1Month == date2Month && date1Date == date2Date;
    }

    private void setLegend() {
        int backgroundColor = ContextCompat.getColor(getBaseContext(), R.color.colorLegendBackground);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(30);
        graph.getLegendRenderer().setSpacing(20);
        graph.getLegendRenderer().setBackgroundColor(backgroundColor);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    private void setAxisDescription(String unitName, HabitType habitType) {
        String happiness_unit = getResources().getString(R.string.happiness_unit);
        TextView textView = findViewById(R.id.axisDescription);
        String description = "The unit of the left y-axis is " + happiness_unit + ".";
        if (habitType == habitType.NUMERICAL) {
            description = description + "\nThe unit of the right y-axis is " + unitName + ".";
        }
        textView.setText(description);
    }
    // -------------------------------------------------------------------
    // PUBLIC FUNCTIONS --------------------------------------------------
    // -------------------------------------------------------------------

    public void changeGraph(String habitNameChosen) {
        HabitTracker habitTracker = null;
        for (HabitTracker h : habitTrackerSet) {
            if (habitNameChosen.equals(h.getHabitName())) {
                habitTracker = h;
                break;
            }
        }

        if (habitNameChosen != habitNameDisplay) {
            habitNameDisplay = habitNameChosen;
            latestDateDisplay = calendar.getTime();
        }

        if (habitTracker != null) {
            List<DateInfo> dateInfos = habitTracker.getTracking();
            HabitType habitType = habitTracker.getHabitType();

            graph.removeAllSeries();
            graph.clearSecondScale();

            if (habitType == HabitType.NUMERICAL) {
                // Set happiness data (bar graph)
                DataPoint[] happyDataPoints =
                        getDataPointsHappinessNumerical(latestDateDisplay, dateInfos);
                setBarGraphHappinessNumerical(happyDataPoints);

                // Set unit value data (line graph)
                DataPoint[] unitValueDataPoints = getDataPointsUnitValues(latestDateDisplay, dateInfos);
                setLineGraphUnitValue(unitValueDataPoints, habitTracker.getUnitName());
                setYAxisRight(dateInfos);
            } else {
                //Get happiness on days that a certain activity is done
                DataPoint[] happyDataPointsPos =
                        getDataPointsHappinessBinaryPositive(latestDateDisplay, dateInfos);
                setBarGraphHappinessBinaryPositive(habitNameChosen, happyDataPointsPos);

                //Get happiness on days that a certain activity is not done
                DataPoint[] happyDataPointsNeg =
                        getDataPointsHappinessBinaryNegative(latestDateDisplay, dateInfos);
                setBarGraphHappinessBinaryNegative(habitNameChosen, happyDataPointsNeg);
            }

            setGraphTitle(habitNameChosen, latestDateDisplay);
            setXAxis(latestDateDisplay);
            setYAxisLeft();
            setLegend();
            setAxisDescription(habitTracker.getUnitName(), habitType);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int motionEvent = e.getAction();
        if (motionEvent == MotionEvent.ACTION_DOWN) {
            oldX = e.getX();
            oldY = e.getY();
        } else if (motionEvent == MotionEvent.ACTION_UP) {
            float newX = e.getX();
            float newY = e.getY();

            if (Math.abs(newX - oldX) > Math.abs(newY - oldY)) {
                if (newX > oldX) {
                    // Swiped left -> user can see data about 7 days previous to the oldest
                    // currently display date
                    calendar.setTime(latestDateDisplay);
                    calendar.add(Calendar.DATE, -numDateShow);
                    latestDateDisplay = calendar.getTime();
                    changeGraph(habitNameDisplay);
                    calendar = Calendar.getInstance();
                } else {
                    // Swiped right -> user can see data about 7 days after to the newest
                    // currently display date
                    calendar.setTime(latestDateDisplay);
                    calendar.add(Calendar.DATE, numDateShow);
                    latestDateDisplay = calendar.getTime();
                    calendar = Calendar.getInstance();
                    changeGraph(habitNameDisplay);
                }
            }
        }
        return true;
    }
}