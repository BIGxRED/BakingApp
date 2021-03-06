/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.fragments;

import android.content.Context;
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

/*
Primary purpose: This fragment is responsible for showing the user a list of steps for baking a
particular recipe. It is always displayed within the StepDisplay activity for both handheld and
tablet devices.
 */
public class StepSelection extends Fragment {

    private static final String ARGS_RECIPE_ID = "recipe_id";

    @BindView(R.id.step_selection_recycler_view) RecyclerView mRecyclerView;

    StepAdapter mAdapter;
    Step[] mSteps;

    // This callback is used in order for us to be able to handle the click events of the
    // ViewHolders within the RecyclerView.
    StepAdapter.StepLoader mAdapterCallback;

    public StepSelection(){
    }

    /*
    A helper method for properly creating a StepSelection fragment. The bundle that is attached
    to the StepSelection is used later on in onCreate().
     */
    public static StepSelection newInstance(int recipeID){
        Bundle arguments = new Bundle();
        arguments.putInt(ARGS_RECIPE_ID, recipeID);

        StepSelection fragment = new StepSelection();
        fragment.setArguments(arguments);
        return fragment;
    }

    /*
    We override this method to ensure that the hosting activity has implemented the
    StepAdapter.StepLoader interface.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // In this case, we first obtain a reference to the hosting activity, StepDisplay,
            // through the context. Then, we ensure that StepDisplay has implemented the interface.
            // Otherwise, we throw an exception.
            mAdapterCallback = (StepAdapter.StepLoader) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepLoader");
        }

    }

    /*
    We obtain the Bundle that was set as the arguments to the fragment within
    StepSelection.newInstance() and obtain a reference to the steps.
     */
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

        mAdapter = new StepAdapter(getContext(), mSteps, mAdapterCallback);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
