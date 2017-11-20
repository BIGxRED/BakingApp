package com.palarz.mike.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.activities.RecipeSelection;
import com.palarz.mike.bakingapp.model.Ingredient;
import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.model.Step;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasKey;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.palarz.mike.bakingapp.Utils.atPosition;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class RecipeSelectionActivityTest {

    private static final String RECIPE_NAME_NUTELLA = "Nutella Pie";
    private static final String RECIPE_NAME_BROWNIES = "Brownies";
    private static final String RECIPE_NAME_YELLOW_CAKE = "Yellow Cake";
    private static final String RECIPE_NAME_CHEESECAKE = "Cheesecake";

    private static final String RECIPE_STEP_INTRO_TITLE = "Recipe Introduction";

    @Rule
    public IntentsTestRule<RecipeSelection> mActivityTestRule =
            new IntentsTestRule<>(RecipeSelection.class);

    // Clicks on a recipe and tests whether the correct recipe name is shown in RecipeDetails
    @Test
    public void clickRecipe_CheckDetails(){
        // Clicks on the first recipe
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Verifies that the correct name is shown
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_NUTELLA)));
    }

    // Clicks on a recipe, then clicks on the "Let's start cooking!" button in RecipeDetails,
    // then clicks on the first step in StepSelection, and finally checks if the SimpleExoPlayerView
    // is shown and if the short description is correct.
    @Test
    public void clickRecipe_GetToIntroVideo(){
        // Click on the first recipe in the list
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Within RecipeDetails, click on the 'Let's start cooking!' button
        onView(withId(R.id.recipe_details_start_cooking_button)).perform(click());
        // Click on the first step of the recipe
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Confirm that the SimpleExoPlayerView is shown
        onView(withId(R.id.step_watcher_player_view)).check(matches(isDisplayed()));
        // Verify that the short description of the step is correct
        onView(withId(R.id.step_watcher_short_description)).check(matches(withText(RECIPE_STEP_INTRO_TITLE)));
    }

    // A very thorough test which first checks if the correct title is shown for every recipe and
    // then clicks on every recipe and checks if the correct title is shown in RecipeDetails
    @Test
    public void clickEveryRecipe_CheckEachRecipeTitle(){
        // First we check to see if every single recipe has the right title. These tests were
        // partly done so that I can play around with asserting the contents of the RecyclerView's
        // ViewHolder.
        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(0,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_NUTELLA))))));
        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(1,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_BROWNIES))))));
        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(2,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_YELLOW_CAKE))))));
        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(3,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_CHEESECAKE))))));

        // Then, we click on every recipe and ensure that the correct recipe name is shown in
        // RecipeDetails
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_NUTELLA)));
        pressBack();
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_BROWNIES)));
        pressBack();
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(2, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_YELLOW_CAKE)));
        pressBack();
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(3, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_CHEESECAKE)));
    }

    // Purpose of this test is to ensure that the correct activity is launched when a recipe is
    // clicked on. I also wanted to play around with Intent verification.
    @Test
    public void clickFirstRecipe_CheckIntent(){
        // The first recipe in the RecyclerView is clicked on.
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // We then ensure that the correct class was launched
        intended(hasComponent(RecipeDetails.class.getName()));
        // We also ensure that the Intent had the right Extra key value
        intended(hasExtraWithKey(RecipeAdapter.EXTRA_RECIPE_ID));
    }

}
