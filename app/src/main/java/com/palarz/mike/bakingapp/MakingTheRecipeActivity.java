package com.palarz.mike.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by mpala on 10/10/2017.
 */

public class MakingTheRecipeActivity extends AppCompatActivity {

    TextView mRecipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_the_recipe);

        mRecipeName = (TextView) findViewById(R.id.recipe_name_test);
        Intent receivedIntent = getIntent();
        Recipe clickedRecipe = null;
        if (receivedIntent != null){
            if (receivedIntent.hasExtra(RecipeAdapter.EXTRA_RECIPE)){
                clickedRecipe = receivedIntent.getParcelableExtra(RecipeAdapter.EXTRA_RECIPE);
                mRecipeName.setText(clickedRecipe.getName());
            }
        }
    }

}
