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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    Context mContext;
    Step[] mSteps;
    StepLoader mCallback;

    public StepAdapter(Context context, Step[] steps, StepLoader stepLoader){
        this.mContext = context;
        this.mSteps = steps;
        mCallback = stepLoader;
    }

    /*
    * This interface allows us to have the hosting activity, which is StepDisplay in this case,
    * load a StepWatcher instance when a StepViewHolder is clicked on. It's important that
    * StepDisplay does this since it should be left to the hosting activity to perform all
    * FragmentTransactions and not the Fragments themselves.
    */
    public interface StepLoader{
        void loadNextStep(int stepIndex);
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

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.step_list_item_description) public TextView vStepDescriptionTV;

        public StepViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        // This method allows us to determine when an individual step is clicked on.
        @Override
        public void onClick(View view) {
            int stepIndex = getAdapterPosition();
            // When the step is clicked on, we want to pass the index of the clicked step within
            // mSteps to the StepLoader callback, which is implemented by the StepDisplay
            // hosting activity.
            mCallback.loadNextStep(stepIndex);
        }

    }
}
