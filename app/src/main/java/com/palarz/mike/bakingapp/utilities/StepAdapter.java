package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.model.Step;
import com.palarz.mike.bakingapp.fragments.StepWatcher;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    public static final String BUNDLE_KEY_CURRENT_STEP = "com.palarz.mike.bakingapp.utilities.current_step";

    Context mContext;
    Step[] mSteps;

    public StepAdapter(Context context, Step[] steps){
        this.mContext = context;
        this.mSteps = steps;
    }

    @Override
    public int getItemCount() {
        if (mSteps == null){
            return 0;
        }
        return mSteps.length;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);


        return new StepViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(StepViewHolder stepViewHolder, int position) {
        Step currentStep = mSteps[position];
        stepViewHolder.vStepDescriptionTV.setText(currentStep.getShortDescription());

    }

    public void swapSteps(Step[] newSteps){
        mSteps = newSteps;
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView vStepDescriptionTV;

        public StepViewHolder(View view){
            super(view);
            vStepDescriptionTV = view.findViewById(R.id.step_list_item_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            StepWatcher watcher = new StepWatcher();
            Step currentStep = mSteps[this.getAdapterPosition()];
            Bundle b = new Bundle();
            b.putParcelable(BUNDLE_KEY_CURRENT_STEP, currentStep);
            watcher.setArguments(b);

            // TODO: You can add an transition animation to the FragmentTransaction by using
            // setTransition(). Maybe look into playing around with this?
            FragmentActivity activity = (FragmentActivity) mContext;
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.step_display_current_step, watcher)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
