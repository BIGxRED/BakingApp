package com.palarz.mike.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.data.Step;
import com.palarz.mike.bakingapp.fragments.StepSelection;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepDisplay extends AppCompatActivity {

    public static final String BUNDLE_KEY_STEPS = "com.palarz.mike.bakingapp.activities.steps";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_display);

        StepSelection stepSelection = new StepSelection();

        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra(RecipeDetails.EXTRA_STEPS)){
            Parcelable[] parcelables = receivedIntent.getParcelableArrayExtra(RecipeDetails.EXTRA_STEPS);
            Step[] steps = new Step[parcelables.length];
            System.arraycopy(parcelables, 0, steps, 0, parcelables.length);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(BUNDLE_KEY_STEPS, steps);
            stepSelection.setArguments(bundle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_display_current_step, stepSelection)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
