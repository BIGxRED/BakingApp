package com.palarz.mike.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.palarz.mike.bakingapp.activities.RecipeSelection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;

/**
 * Created by mpala on 11/5/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeSelectionActivityTest {

    private static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeSelection> mActivityTestRule =
            new ActivityTestRule<>(RecipeSelection.class);


    @Test
    public void clickRecipeItem_CheckDetails(){
        onData(anything()).inAdapterView(withId(R.id.recycler_view)).atPosition(0).perform(click());
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME)));
    }

}
