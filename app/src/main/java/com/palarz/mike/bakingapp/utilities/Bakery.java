package com.palarz.mike.bakingapp.utilities;

import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by mpala on 11/11/2017.
 */

public class Bakery {

    private static Bakery sBakery;
    private List<Recipe> mRecipes;

    public static Bakery get(){
        if (sBakery == null){
            sBakery = new Bakery();
        }
        return sBakery;
    }

    private Bakery(){
        mRecipes = new ArrayList<>();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(int ID){
        for (Recipe recipe : mRecipes){
            if (recipe.getID() == ID){
                return recipe;
            }
        }
        return null;
    }

    public Step getStep(int recipeID, int stepIndex){
        Recipe currentRecipe = getRecipe(recipeID);

        // If we have a faulty Recipe ID, then don't even try to find the step and return nothing
        if (currentRecipe == null){
            return null;
        }


        /*
        Otherwise, we will attempt to locate the step. If the step could not be located because
        the index was erroneous, then we will flag the error and return null.
         */
        try{
            return currentRecipe.getSteps()[stepIndex];
        }
        catch (IndexOutOfBoundsException exception){
            Timber.e(exception, "Index used was out of bounds of the Recipe array. Are you sure " +
                    "you used a valid index value?");
            return null;
        }
    }

    /*
     This is a helper method which ensures that every Recipe that is added to the Bakery is unique.
     This method is useful because every time we use fetchRecipes() within RecipeFetcher, we are
     adding Recipes to the Bakery. However, we want to avoid adding redundant Recipes to the Bakery.

     ***** Additional Explanation *****
     We are using fetchRecipes() within an ASyncTask in RecipeSelection, where the ASyncTask is
     re-launched every time onCreate() is called (such as when the screen is rotated). If we don't
     check if we have unique Recipes, then redundant Recipes would be added to the Bakery each
     time onCreate() is called.
      */
    public boolean isNewRecipe(Recipe newRecipe){
        for (Recipe currentRecipe : mRecipes){
            // If the new Recipe is the same as any of the Recipes that are already in mRecipes,
            // we will return false.
            if (Recipe.sameRecipe(currentRecipe, newRecipe)){
                return false;
            }
        }

        return true;
    }

}