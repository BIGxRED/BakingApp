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
import com.palarz.mike.bakingapp.utilities.Bakery;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepSelection extends Fragment {

    // TODO: Maybe turn this into a ListFragment instead of a simple Fragment?

    private static final String ARGS_RECIPE_ID = "recipe_id";

    @BindView(R.id.step_selection_recycler_view) RecyclerView mRecyclerView;

    StepAdapter mAdapter;
    Step[] mSteps;

    public StepSelection(){
    }

    public static StepSelection newInstance(int recipeID){
        Bundle arguments = new Bundle();
        arguments.putInt(ARGS_RECIPE_ID, recipeID);

        StepSelection fragment = new StepSelection();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeID = getArguments().getInt(ARGS_RECIPE_ID);
        mSteps = Bakery.get().getRecipe(recipeID).getSteps();
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

        mAdapter = new StepAdapter(getContext(), mSteps);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
