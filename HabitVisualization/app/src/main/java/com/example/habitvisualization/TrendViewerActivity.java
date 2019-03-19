package com.example.habitvisualization;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

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
    Random random; //testing purpose

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Declaring variables as data for bar chart below
    private BarChart barChart;
    ArrayList<BarEntry> barEntries;
    // End variables above are data for bar chart


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

        /* Initialize recycler view so the user can choose a habit to display
         * on the chart*/
        initRecyclerView();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        /* Initialize bar graph so that the user can view their happiness
         * in correlation to their other unit input across days*/
        barChart = (BarChart) findViewById(R.id.barChartView);
        createBarGraph("2016.05.05", "2016.05.11", "");
    }

    // Input data in the x-axis
    private void createBarGraph(String dateOldestStr, String dateNewestStr,
                                String graphName) {
        Log.d(TAG, "createBarGraph: is called");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        ArrayList<String> dates = new ArrayList<>();

        try {
            Date dateNewest = simpleDateFormat.parse(dateNewestStr);
            Date dateOldest = simpleDateFormat.parse(dateOldestStr);

            Calendar calenDateNewest = Calendar.getInstance();
            Calendar calenDateOldest = Calendar.getInstance();
            calenDateNewest.clear();
            calenDateOldest.clear();

            calenDateNewest.setTime(dateNewest);
            calenDateOldest.setTime(dateOldest);

            String endDateStr = calenDateNewest.getTime().toString();
            String startDateStr = calenDateOldest.getTime().toString();

            Log.d(TAG, "createBarGraph: startDate is " + startDateStr);
            Log.d(TAG, "createBarGraph: endDate is " + endDateStr);


            dates = getList(calenDateOldest, calenDateNewest);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;

            Log.d(TAG, "createBarGraph: dates.size() is " + Integer.toString(dates.size()));
            for(int j = 0; j< dates.size();j++){
                max = 10f;
                barEntries.add(new BarEntry(j, j));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Happiness level");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add((IBarDataSet) barDataSet);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f); // set custom bar width
        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh

        //Title of the bar chart
        Description description = new Description();
        description.setText(graphName);
        barChart.setDescription(description);

    }

    private ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        String startDateStr = startDate.getTime().toString();
        String endDateStr = endDate.getTime().toString();

        Log.d(TAG, "getList: startDate is " + startDateStr);
        Log.d(TAG, "getList: endDate is " + endDateStr);

        ArrayList<String>  list = new ArrayList();
        while(startDate.compareTo(endDate) <= 0) {
            String dateStr = startDate.get(Calendar.YEAR) + "." +
                    (startDate.get(Calendar.MONTH) + 1) + "." +
                    startDate.get(Calendar.DATE);
            list.add(dateStr);
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "getList: number of element in list is " + Integer.toString(list.size()));
        return list;
    }

//    private String getDate(Calendar calendar) {
//        Log.d(TAG, "getDate: calendar.getTime() is " + calendar.getTime().toString());
//        String curDate = calendar.get(Calendar.YEAR) + "." +
//                (calendar.get(Calendar.MONTH) + 1) + "." +
//                calendar.get(Calendar.DATE);
//        Log.d(TAG, "getDate: curDate is " + curDate);
//
//        try {
//            Date date = new SimpleDateFormat("yyyy.MM.dd").parse(curDate);
//            curDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return curDate;
//    }

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

    /*Input: expects to have 7 elements only to view data a 7 days, where the most recent in
    that*/
    private void setUpXAxis(String[] xAxisStrs) {
        String[] xAxisStrs = new String[] {"99", "100", "101", "102", "103", "104", "105"};
        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(xAxisStrs);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(xAxisValueFormatter);
    }

    private void getDatesForXAxis() {

    }

    public void changeGraph(String habitNameChosen) {
        Log.d(TAG, "changeGraph: chosen habit is " + habitNameChosen);
    }

}

class XAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public XAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//        return mValues[(int) value];
        if (value >= 0) {
            if (mValues.length > (int) value) {
                return mValues[(int) value];
            } else return "";
        } else {
            return "";
        }
    }
}
