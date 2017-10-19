package com.palarz.mike.bakingapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;
import com.palarz.mike.bakingapp.data.RecipeFetcher;
import com.palarz.mike.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpala on 10/1/2017.
 */

public class RecipeSelection extends Fragment {

    RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;

    public RecipeSelection() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_selection, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerViewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewManager);

        mAdapter = new RecipeAdapter(getContext(), null);
        mRecyclerView.setAdapter(mAdapter);

        new FetchRecipesTask().execute();

        return rootView;
    }

    private List<Recipe> createList(int size) {

        List<Recipe> result = new ArrayList<Recipe>();
        for (int i=1; i <= size; i++) {
            Recipe recipe = new Recipe();
            recipe.setName(getString(R.string.default_recipe_name));
            recipe.setServings(0);

            result.add(recipe);

        }

        return result;
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
