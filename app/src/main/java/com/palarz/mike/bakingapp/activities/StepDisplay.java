package com.palarz.mike.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.fragments.StepWatcher;
import com.palarz.mike.bakingapp.model.Step;
import com.palarz.mike.bakingapp.fragments.StepSelection;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepDisplay extends AppCompatActivity implements StepWatcher.StepSwitcher {

    public static final String BUNDLE_KEY_STEPS = "com.palarz.mike.bakingapp.activities.steps";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_display);

        /* It is important for us to check if savedInstanceState is null. It is only null the first
        * time the activity is created. In that moment, we want the StepSelection fragment to
        * be the only fragment that is being displayed.
        *
        * If we didn't check this, then a StepSelection would be added to the display every time
        * the screen would be rotated.
        */
        if (savedInstanceState == null){
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

    @Override
    public void switchStep(Step[] steps, int nextStepIndex) {
        StepWatcher watcher = new StepWatcher();

        Bundle bundle = new Bundle();
        bundle.putParcelableArray(StepAdapter.BUNDLE_KEY_ALL_STEPS, steps);
        bundle.putInt(StepAdapter.BUNDLE_KEY_STEP_ARRAY_INDEX, nextStepIndex);
        watcher.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.step_display_current_step, watcher)
                .commit();
    }
}
