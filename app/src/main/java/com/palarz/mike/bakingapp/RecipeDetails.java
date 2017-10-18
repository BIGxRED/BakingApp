package com.palarz.mike.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by mpala on 10/10/2017.
 */

public class RecipeDetails extends AppCompatActivity {

    TextView mRecipeName;
    ImageView mRecipeImage;
    TextView mIngredientsTV;
    TextView mIngredientsHeadingTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mRecipeName = (TextView) findViewById(R.id.recipe_details_name);
        mRecipeImage = (ImageView) findViewById(R.id.recipe_details_image);
        mIngredientsTV = (TextView) findViewById(R.id.recipe_details_ingredients);
        mIngredientsHeadingTV = (TextView) findViewById(R.id.recipe_details_ingredients_heading);

        mIngredientsHeadingTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mIngredientsTV.setVisibility(mIngredientsTV.isShown() ? View.GONE : View.VISIBLE);
            }
        });

        Intent receivedIntent = getIntent();
        Recipe clickedRecipe = null;
        if (receivedIntent != null){
            if (receivedIntent.hasExtra(RecipeAdapter.EXTRA_RECIPE)){
                clickedRecipe = receivedIntent.getParcelableExtra(RecipeAdapter.EXTRA_RECIPE);
                mRecipeName.setText(clickedRecipe.getName());
                String recipeImage = clickedRecipe.getImage();
                try{
                    mRecipeImage.setImageResource(Integer.valueOf(recipeImage));
                }
                catch (NumberFormatException nfe){
                    Picasso.with(this)
                            .load(recipeImage)
                            .placeholder(R.drawable.hourglass)
                            .into(mRecipeImage);
                }
                catch (Exception e){
                    Toast.makeText(this, "Could not load image resourse", Toast.LENGTH_SHORT).show();
                    finish();
                }

                mIngredientsTV.setText(clickedRecipe.printIngredients());

            }
        }
    }

}
