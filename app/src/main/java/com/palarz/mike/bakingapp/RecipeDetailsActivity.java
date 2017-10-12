package com.palarz.mike.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by mpala on 10/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    TextView mRecipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mRecipeName = (TextView) findViewById(R.id.recipe_details_name);
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
