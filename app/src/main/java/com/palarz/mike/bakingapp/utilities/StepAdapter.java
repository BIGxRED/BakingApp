package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.data.Step;

import java.util.List;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

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
            //TODO: Will need to implement this eventually
        }
    }
}
