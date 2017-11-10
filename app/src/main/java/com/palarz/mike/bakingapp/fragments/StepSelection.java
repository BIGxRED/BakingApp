package com.palarz.mike.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.StepDisplay;
import com.palarz.mike.bakingapp.model.Step;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepSelection extends Fragment {

    // TODO: Maybe turn this into a ListFragment instead of a simple Fragment?

    StepAdapter mAdapter;
    @BindView(R.id.step_selection_recycler_view) RecyclerView mRecyclerView;
    Step[] mSteps;

    public StepSelection(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_selection, container, false);

        ButterKnife.bind(this, rootView);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerViewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewManager);

        Bundle receivedBundle = this.getArguments();
        if (receivedBundle != null){
            Parcelable[] parcelables = receivedBundle.getParcelableArray(StepDisplay.BUNDLE_KEY_STEPS);
            mSteps = new Step[parcelables.length];
            System.arraycopy(parcelables, 0, mSteps, 0, parcelables.length);
        }

        mAdapter = new StepAdapter(getContext(), mSteps);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
