/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.utilities.Bakery;
import com.palarz.mike.bakingapp.utilities.BakingApplication;
import com.palarz.mike.bakingapp.utilities.ConnectivityReceiver;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;
import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.utilities.RecipesClient;
import com.palarz.mike.bakingapp.widget.GroceryListAppWidgetProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*
Primary purpose: This class allows the user to select a recipe from a grid of recipes. It is the
main activity of this app.
 */
public class RecipeSelection extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityListener {

    // View that is bound using Butterknife
    @BindView(R.id.step_selection_recycler_view) RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;

    /*
    In onResume, we register this activity as being a connectivity listener. The reason we are
    doing this is because we will be obtaining an HTTP response for the recipe data. Prior to
    polling for the data, we want to be sure that the user has a network connection. If they do
    not, then we should at least inform them.
     */
    @Override
    protected void onResume() {
        super.onResume();

        BakingApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);

        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager recyclerViewManager =
                new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(recyclerViewManager);

        mAdapter = new RecipeAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);

    }

    /*
    A callback method within ConnectivityReceiver which is called whenever the network connection
    changes.
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        /*
        If the device has a network connection, then we download the recipe data and notify the
        user that a network connection has been established.
         */
        if (isConnected){
            getRecipeData();
            Snackbar.make(
                    findViewById(R.id.step_selection_coordinator_layout),
                    getString(R.string.recipe_selection_internet_available),
                    Snackbar.LENGTH_SHORT)
                .show();
        }
        /*
        Otherwise, we notify the user that they do not have a network connection and let them know
        to check their settings.
         */
        else {
            // First we configure the Snackbar...
            Snackbar errorSnackbar = Snackbar.make(
                    findViewById(R.id.step_selection_coordinator_layout),
                    getString(R.string.recipe_selection_no_internet),
                    Snackbar.LENGTH_SHORT);

            // ...and then we obtain a reference to the root View within the Snackbar so that
            // we can adjust the color of the text within the Snackbar.
            View snackbarView = errorSnackbar.getView();
            TextView snackbarTextView =
                    snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            snackbarTextView.setTextColor(Color.RED);

            // Finally, the Snackbar is shown.
            errorSnackbar.show();
        }
    }

    /*
    A helper method which uses the Retrofit library to obtain the HTTP response. On success, the
    JSON data is parsed through and the Bakery is updated. The adapter of the RecyclerView is also
    notified of the updated data through a call to swapRecipes(). If we failed to obtain an HTTP
    response, then we notify the user through a Snackbar.
     */
    public void getRecipeData(){
        // We use Retrofit in order to obtain the HTTP response and parse the JSON data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipesClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // We then create an instance of our interface and create our Call object
        RecipesClient client = retrofit.create(RecipesClient.class);
        Call<List<Recipe>> call = client.getAllRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            // If we were successful in obtaining a response to our HTTP request...
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                // ...we extract the recipes from the response...
                List<Recipe> recipes = response.body();

                // ...and then add all of the recipes to our bakery as well as our adapter for this
                // activity.
                Bakery.get().addRecipes(recipes);
                mAdapter.swapRecipes(Bakery.get().getRecipes());

                // After all of this is complete, we ensure that our widget also contains the latest data
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeSelection.this);
                int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(RecipeSelection.this,
                        GroceryListAppWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.app_widget_list_view);
            }

            // If we failed to obtain an HTTP response, we at least notify the user.
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // First we configure the Snackbar...
                Snackbar errorSnackbar = Snackbar.make(
                        findViewById(R.id.step_selection_coordinator_layout),
                        getString(R.string.recipe_selection_recipe_data_error),
                        Snackbar.LENGTH_SHORT);

                // ...and then we obtain a reference to the root View within the Snackbar so that
                // we can adjust the color of the text within the Snackbar.
                View snackbarView = errorSnackbar.getView();
                TextView snackbarTextView =
                        snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                snackbarTextView.setTextColor(Color.RED);

                // Finally, the Snackbar is shown.
                errorSnackbar.show();
            }
        });
    }
}
