package com.palarz.mike.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.utilities.Bakery;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mpala on 10/10/2017.
 */

public class RecipeDetails extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "com.palarz.mike.bakingapp.recipe_id";

    @BindView(R.id.recipe_details_name) TextView mNameTV;
    @BindView(R.id.recipe_details_image) ImageView mImageIV;
    @BindView(R.id.recipe_details_ingredients) TextView mIngredientsTV;
    @BindView(R.id.recipe_details_start_cooking_button) Button mStartCookingButton;

    Recipe mClickedRecipe;
    int mRecipeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null){
            if (receivedIntent.hasExtra(RecipeAdapter.EXTRA_RECIPE_ID)){
                mRecipeID = receivedIntent.getIntExtra(RecipeAdapter.EXTRA_RECIPE_ID, 0);
                mClickedRecipe = Bakery.get().getRecipe(mRecipeID);

                // TODO: Think of a better way to handle when the Recipe is null
                // If the received recipe doesn't exist in our Bakery, then we'll exit this activity,
                // notifying the user of the issue
                if (mClickedRecipe == null){
                    Toast.makeText(this, "Recipe is empty...", Toast.LENGTH_LONG).show();
                    finish();
                }

                mNameTV.setText(mClickedRecipe.getName());
                String recipeImage = mClickedRecipe.getImage();
                try{
                    mImageIV.setImageResource(Integer.valueOf(recipeImage));
                }
                catch (NumberFormatException nfe){
                    Picasso.with(this)
                            .load(recipeImage)
                            .placeholder(R.drawable.hourglass)
                            .into(mImageIV);
                }
                catch (Exception e){
                    Toast.makeText(this, "Could not load image recourse", Toast.LENGTH_SHORT).show();
                    finish();
                }

                mIngredientsTV.setText(mClickedRecipe.printIngredients());

                mStartCookingButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent stepDisplayIntent = new Intent(getApplicationContext(), StepDisplay.class);
                        stepDisplayIntent.putExtra(EXTRA_RECIPE_ID, mRecipeID);
                        startActivity(stepDisplayIntent);
                    }
                });
            }
        }

    }

}
