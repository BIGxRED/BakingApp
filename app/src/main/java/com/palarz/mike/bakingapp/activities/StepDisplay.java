package com.palarz.mike.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.fragments.StepSelection;
import com.palarz.mike.bakingapp.fragments.StepWatcher;
import com.palarz.mike.bakingapp.utilities.StepAdapter;

import timber.log.Timber;

import static com.palarz.mike.bakingapp.utilities.StepAdapter.BUNDLE_KEY_ALL_STEPS;
import static com.palarz.mike.bakingapp.utilities.StepAdapter.BUNDLE_KEY_STEP_ARRAY_INDEX;

/**
 * Created by mpala on 10/19/2017.
 */

public class StepDisplay extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener, StepAdapter.StepLoader, StepWatcher.StepSwitcher {

    private boolean mTwoPane;
    private int mRecipeID;

    // TODO: Perhaps look into reusing that SingleFragmentActivity class from Big Nerd Ranch?

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Timber.d("Contents of backstack in onBackStackChanged():\n");
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
            Timber.d("Index: " + i + "; fragment transaction name: "
                    + fragmentManager.getBackStackEntryAt(i).getName());
        }
        Timber.d("\n");
    }

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

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {

            Intent receivedIntent = getIntent();
            if (receivedIntent.hasExtra(RecipeDetails.EXTRA_RECIPE_ID)) {

                mRecipeID = receivedIntent.getIntExtra(RecipeDetails.EXTRA_RECIPE_ID, 0);
                StepSelection stepSelection = StepSelection.newInstance(mRecipeID);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.step_display_list_of_steps, stepSelection)
                        .commit();
            }

        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
            Timber.d("onBackPressed() has been called\n");
//            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
//                Timber.d("Index: " + i + "; backstack entry ID: " + fragmentManager.getBackStackEntryAt(i));
//            }
//            Timber.d("\n");
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void loadNextStep(int stepIndex) {
        Timber.d("loadNextStep() has been called");

        if (mTwoPane){
            // We're gonna do something here, lots of things
        }
        else {
            StepWatcher watcher = StepWatcher.newInstance(mRecipeID, stepIndex);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.step_display_list_of_steps, watcher)
                    .addToBackStack("Remove StepSelection, add StepWatcher")
                    .commit();

        }
    }

    @Override
    public void switchStep(int stepIndex) {
        StepWatcher watcher = StepWatcher.newInstance(mRecipeID, stepIndex);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.step_display_list_of_steps, watcher)
                .commit();
    }
}
