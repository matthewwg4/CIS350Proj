package com.example.habitvisualization;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.*;

//DONE
public class HabitViewAdapter extends RecyclerView.Adapter<HabitViewAdapter.HabitViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context context;
    private List<String> habitsNames;
    private int colorRowEven;
    private int colorRowOdd;

    //ViewHolder creates the view ----------------------------------
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        //Each data item is just a string in this case
        private TextView habitName;
        RelativeLayout parentLayout;

        public HabitViewHolder(@NonNull View view) {
            super(view);
            parentLayout = view.findViewById(R.id.parent_layout);
            this.habitName = view.findViewById(R.id.habitName);
        }
    }

    public HabitViewAdapter(Context context, List<String> habitsNames) {
        this.habitsNames = habitsNames;
        this.context = context;
        colorRowEven = ContextCompat.getColor(context, R.color.colorRecyclerView);
        colorRowOdd = ContextCompat.getColor(context, R.color.colorRecyclerView1);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public HabitViewAdapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder: called");

        // Create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_listitem, parent, false);
        HabitViewHolder habitViewHolder = new HabitViewHolder(view);
        return habitViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder habitViewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String habitName = habitsNames.get(position);
        habitViewHolder.habitName.setText(habitName);

        if (position % 2 == 0) {
            habitViewHolder.itemView.setBackgroundColor(
                    Color.parseColor(String.format("#%06X", (0xFFFFFF & colorRowEven))));
        } else {
            habitViewHolder.itemView.setBackgroundColor(
                    Color.parseColor(String.format("#%06X", (0xFFFFFF & colorRowOdd))));
        }

        habitViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chosenHabitName = habitsNames.get(position);
                Log.d(TAG, "onClick: clicked on: " + chosenHabitName);
                ((TrendViewerActivity) context).changeGraph(chosenHabitName);
            }
        });
    }

    // Return the size of the list
    @Override
    public int getItemCount() {
        return habitsNames.size();
    }
}
