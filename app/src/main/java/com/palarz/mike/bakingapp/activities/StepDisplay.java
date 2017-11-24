/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.fragments.StepSelection;
import com.palarz.mike.bakingapp.fragments.StepWatcher;
import com.palarz.mike.bakingapp.utilities.StepAdapter;
import com.palarz.mike.bakingapp.utilities.Utilities;

import timber.log.Timber;

/*
Primary purpose: This activity is responsible for displaying both a list of steps for baking the
recipe as well as the details of each step, all within the same activity. Both the list of steps
and the details of the step are fragments. Therefore, the same activity is used for both
handheld and tablet devices, where the handhelds are shown only a single fragment at a time
whereas the tablets are shown both fragments within a master-detail view.
 */
public class StepDisplay extends AppCompatActivity
        implements StepAdapter.StepLoader, StepWatcher.StepSwitcher {

    // A key that is used for the Bundle created within onSaveInstanceState()
    private static final String BUNDLE_SIS_KEY_RECIPE_ID = "step_display_recipe_id";

    // A tag which is used to refer to FragmentTransactions in the backstack of the FragmentManager
    private static final String FRAGMENT_TAG_STEP_WATCHER =
            "FRAGMENT_TAG_" + StepWatcher.class.getSimpleName();

    private boolean mTwoPane;
    private int mRecipeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_display);

        /* It is important for us to check if savedInstanceState is null. It is only null the first
        * time the activity is created. In that moment, we want the StepSelection (and StepWatcher)
        * fragment(s) to be the only fragment(s) that is/are displayed.
        *
        * If we didn't check this, then a StepSelection would be added to the display every time
        * this activity is recreated.
        */

        if (savedInstanceState == null) {

            // If the Intent contains the recipe ID, then we create a StepSelection fragment and
            // display it.
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
        else {
            // Otherwise, if savedInstanceState does exist, then we extract mRecipeID from the
            // Bundle so that we have it available.
            mRecipeID = savedInstanceState.getInt(BUNDLE_SIS_KEY_RECIPE_ID);

        }


        /* All of the previous setup applies to both a handheld and a tablet. Additional setup is
        required for the tablet since it is also going to display a StepWatcher fragment when
        this activity is first created.

        We set mTwoPane to be true if we find the view ID that will contain the StepWatcher
        fragment, which only exists in the tablet layout. And then we add the StepWatcher to the
        activity as well.
         */
        if (findViewById(R.id.step_display_tablet_contents) != null){
            mTwoPane = true;

            StepWatcher watcher = StepWatcher.newInstance(mRecipeID, 0);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.step_display_tablet_video, watcher)
                    .commit();
        }
    }


    /*
    We ensure that mRecipeID is saved so that it can retrieved after an orientation change. This is
    necessary since certain parts of StepDisplay depend on mRecipeID, such as in loadNextStep() for
    the StepAdapter.StepLoader interface.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_SIS_KEY_RECIPE_ID, mRecipeID);
    }

    /*
    We need to override this method since the fragments are handled differently between the
    handheld and tablet scenario. With the handheld, we are iteratively adding fragments to the
    FragmentManager's backstack. This is to ensure that we have the correct flow the user expects
    as they're switching between StepSelection and StepWatcher.

    Specifically, when the user selects a step, they are shown a StepWatcher. When the back button
    is pressed, the user expects to be brought back to StepSelection. The only way to achieve this
    is by adding FragmentTransactions to the backstack. If this is not done, then when the user is
    seeing a StepWatcher fragment and they press the back button, they will be brought to
    RecipeDetails, which is not at all what they expect.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // We only want to pop the backstack if we have any entries within it.
        if (fragmentManager.getBackStackEntryCount() > 0){

            /* We need to use this particular form of popBackStack(). This is to prevent a strange
            issue where fragments are overlapping each other.

            As things are being added to the backstack, the only transactions that are added use
            the same tag, FRAGMENT_TAG_STEP_WATCHER. By using the POP_BACK_STACK_INCLUSIVE flag,
            we are removing all of the entries from the backstack that have that tag, which allows
            us to be brought back to StepSelection every time.
              */
            fragmentManager.popBackStack(FRAGMENT_TAG_STEP_WATCHER, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            super.onBackPressed();
        }
    }

    /*
    This method is part of the StepLoader interface within the StepAdapter. This interface was
    created because we need to handle the click events differently for handhelds and tablets when
    the ViewHolders of the RecyclerView are clicked.
     */
    @Override
    public void loadNextStep(int stepIndex) {

        if (mTwoPane){
            /*
            If the device is a tablet, then we simply load the appropriate StepWatcher within
            the step_display_tablet_video view ID.
            */
            loadTabletVideo(stepIndex);
        }
        else {
            /*
            If the device is a handheld, then we replace the current fragment in the
            step_display_list_of_steps view ID with a new StepWatcher. Additionally, we add this
            FragmentTransaction to the backstack with the FRAGMENT_TAG_STEP_WATCHER tag. We only
            add entries to the backstack within switchStep(), which is only called for
            handheld devices.
            */
            switchStep(stepIndex);

        }
    }

    /*
    This method is part of the StepSwitcher interface within StepWatcher. This method is only
    called when the next or previous buttons are clicked on within StepWatcher. Each time one of
    these buttons are clicked, we replace the current fragment within the
    step_display_list_of_steps view ID with a new StepWatcher fragment for a handheld device. If
    the device is a tablet, then we create a StepWatcher within the step_display_tablet_video
    view ID.
     */
    @Override
    public void switchStep(int stepIndex) {
        if (mTwoPane){
            loadTabletVideo(stepIndex);
        }
        else {
            StepWatcher watcher = StepWatcher.newInstance(mRecipeID, stepIndex);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.step_display_list_of_steps, watcher)
                    .addToBackStack(FRAGMENT_TAG_STEP_WATCHER)
                    .commit();
        }
    }

    /*
    A helper method which creates a StepWatcher fragment in the view ID that is only present in
    the tablet layout (which is R.id.step_display_tablet_video).
     */
    private void loadTabletVideo(int stepIndex){
        StepWatcher watcher = StepWatcher.newInstance(mRecipeID, stepIndex);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.step_display_tablet_video, watcher)
                .commit();
    }
}
