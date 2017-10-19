package com.palarz.mike.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.StepDisplay;
import com.palarz.mike.bakingapp.data.Step;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepSelection extends Fragment {

    StepAdapter mAdapter;
    RecyclerView mRecyclerView;
    Step[] mSteps;

    public StepSelection(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_selection, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.step_selection_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerViewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewManager);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){
            mSteps = (Step[]) receivedBundle.getParcelableArray(StepDisplay.BUNDLE_KEY_STEPS);
        }

        mAdapter = new StepAdapter(getContext(), null);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
