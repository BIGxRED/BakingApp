package com.palarz.mike.bakingapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;
import com.palarz.mike.bakingapp.data.RecipeFetcher;
import com.palarz.mike.bakingapp.data.Recipe;

import java.util.List;

/**
 * Created by mpala on 10/1/2017.
 */

public class RecipeSelection extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(this);
        recyclerViewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewManager);

        mAdapter = new RecipeAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);

        new FetchRecipesTask().execute();

    }

    private class FetchRecipesTask extends AsyncTask<Void, Void, List<Recipe>>{

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return new RecipeFetcher().fetchRecipes();
        }

        @Override
        protected void onPostExecute(List<Recipe> fetchedRecipes) {
            if (fetchedRecipes.size() > 0){
                mAdapter.swapRecipes(fetchedRecipes);
            }
        }
    }

}
