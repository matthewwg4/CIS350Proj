package com.example.habitvisualization;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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

        public HabitViewHolder(@NonNull View view) {
            super(view);
            this.habitName = view.findViewById(R.id.habitName);
        }
    }

    public HabitViewAdapter(Context context, List<String> habitsNames) {
        this.habitsNames = habitsNames;
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
    public void onBindViewHolder(@NonNull HabitViewHolder habitViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String habitName = habitsNames.get(position);
        habitViewHolder.habitName.setText(habitName);

        if (position % 2 == 0) {
            habitViewHolder.itemView.setBackgroundColor(
                    Color.parseColor(String.format("#%06X", (0xFFFFFF & colorRowEven))));
        } else {
            habitViewHolder.itemView.setBackgroundColor(
                    Color.parseColor(String.format("#%06X", (0xFFFFFF & colorRowOdd))));
        }

    }

    // Return the size of the list
    @Override
    public int getItemCount() {
        return habitsNames.size();
    }
}
