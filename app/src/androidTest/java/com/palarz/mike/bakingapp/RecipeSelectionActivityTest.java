/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.activities.RecipeSelection;
import com.palarz.mike.bakingapp.utilities.OkHttpClientProvider;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.palarz.mike.bakingapp.Utils.atPosition;
import static org.hamcrest.core.AllOf.allOf;

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
        // First, we ensure that Espresso waits until Retrofit has finished obtaining the HTTP
        // response by creating an IdlingResource and registering it
        IdlingResource idlingResource =
                OkHttp3IdlingResource.create("okhttp", OkHttpClientProvider.getOkHttpClient());
        IdlingRegistry.getInstance().register(idlingResource);

        // Clicks on the first recipe
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Verifies that the correct name is shown
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_NUTELLA)));

        // Finally, we unregister our IdlingResource
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    /*
    Clicks on a recipe, then clicks on the "Let's start cooking!" button in RecipeDetails,
    then clicks on the first step in StepSelection, and finally checks if the SimpleExoPlayerView
    is shown and if the short description is correct.
    */
    @Test
    public void clickRecipe_GetToIntroVideo(){
        // First, we ensure that Espresso waits until Retrofit has finished obtaining the HTTP
        // response by creating an IdlingResource and registering it
        IdlingResource idlingResource =
                OkHttp3IdlingResource.create("okhttp", OkHttpClientProvider.getOkHttpClient());
        IdlingRegistry.getInstance().register(idlingResource);

        // Click on the first recipe in the list
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Within RecipeDetails, click on the 'Let's start cooking!' button
        onView(withId(R.id.recipe_details_start_cooking_button)).perform(click());
        // Click on the first step of the recipe
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // Confirm that the SimpleExoPlayerView is shown
        onView(withId(R.id.step_watcher_player_view)).check(matches(isDisplayed()));
        // Verify that the short description of the step is correct
        onView(withId(R.id.step_watcher_short_description)).check(matches(withText(RECIPE_STEP_INTRO_TITLE)));

        // Finally, we unregister our IdlingResource
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    /*
    A very thorough test which first checks if the correct title is shown for every recipe and
    then clicks on every recipe and checks if the correct title is shown in RecipeDetails.
     */
    @Test
    public void clickEveryRecipe_CheckEachRecipeTitle(){
        // First, we ensure that Espresso waits until Retrofit has finished obtaining the HTTP
        // response by creating an IdlingResource and registering it
        IdlingResource idlingResource =
                OkHttp3IdlingResource.create("okhttp", OkHttpClientProvider.getOkHttpClient());
        IdlingRegistry.getInstance().register(idlingResource);


        // We check to see if every single recipe has the right title. These tests were
        // partly done so that I can play around with asserting the contents of the RecyclerView's
        // ViewHolder.
        onView(withId(R.id.step_selection_recycler_view))
                .check(matches(atPosition(0,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_NUTELLA))))));
        onView(withId(R.id.step_selection_recycler_view))
                .check(matches(atPosition(1,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_BROWNIES))))));
        onView(withId(R.id.step_selection_recycler_view))
                .check(matches(atPosition(2,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_YELLOW_CAKE))))));
        onView(withId(R.id.step_selection_recycler_view))
                .check(matches(atPosition(3,
                        hasDescendant(allOf(withId(R.id.recipe_list_item_name),withText(RECIPE_NAME_CHEESECAKE))))));

        // Then, we click on every recipe and ensure that the correct recipe name is shown in
        // RecipeDetails
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_NUTELLA)));
        pressBack();
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_BROWNIES)));
        pressBack();
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(2, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_YELLOW_CAKE)));
        pressBack();
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(3, click()));
        onView(withId(R.id.recipe_details_name)).check(matches(withText(RECIPE_NAME_CHEESECAKE)));

        // Finally, we unregister our IdlingResource
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    /*
    Purpose of this test is to ensure that the correct activity is launched when a recipe is
    clicked on. I also wanted to play around with Intent verification.
     */
    @Test
    public void clickFirstRecipe_CheckIntent(){
        // First, we ensure that Espresso waits until Retrofit has finished obtaining the HTTP
        // response by creating an IdlingResource and registering it
        IdlingResource idlingResource =
                OkHttp3IdlingResource.create("okhttp", OkHttpClientProvider.getOkHttpClient());
        IdlingRegistry.getInstance().register(idlingResource);

        // The first recipe in the RecyclerView is clicked on.
        onView(withId(R.id.step_selection_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        // We then ensure that the correct class was launched
        intended(hasComponent(RecipeDetails.class.getName()));
        // We also ensure that the Intent had the right Extra key value
        intended(hasExtraWithKey(RecipeAdapter.EXTRA_RECIPE_ID));

        // Finally, we unregister our IdlingResource
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

}
