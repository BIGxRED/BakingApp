package com.palarz.mike.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;


    // DONE (1): First thing to do is to implement a RecyclerView w/ CardViews

    // DONE (2): After that, turn this into a fragment embedded within an activity so that
    // this is ready for a tablet layout as well

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(this);
//        recyclerViewManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(recyclerViewManager);
//
//        mAdapter = new RecipeAdapter(createList(30));
//        mRecyclerView.setAdapter(mAdapter);


    }

//    private List<Recipe> createList(int size) {
//
//        List<Recipe> result = new ArrayList<Recipe>();
//        for (int i=1; i <= size; i++) {
//            Recipe recipe = new Recipe();
//            recipe.name = Recipe.NAME_PREFIX + i;
//            recipe.surname = Recipe.SURNAME_PREFIX + i;
//            recipe.email = Recipe.EMAIL_PREFIX + i + "@test.com";
//
//            result.add(recipe);
//
//        }
//
//        return result;
//    }
}
