/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/*
Primary purpose: This class is a singleton of a List<Recipe>. This approach was taken because I was
initially using Parcelable throughout my app, which made for some very messy looking code when
passing a Recipe or Step reference from one activity/fragment onto another.
 */
public class Bakery {

    // We purposely make sBakery a private static variable so that this is the only class that
    // is able to access it.
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

    /*
    A helper method which returns a recipe if the ID is provided.
     */
    public Recipe getRecipe(int ID){
        for (Recipe recipe : mRecipes){
            if (recipe.getID() == ID){
                return recipe;
            }
        }
        return null;
    }

    /*
    A helper method which returns a step if the step and recipe ID are provided.
     */
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
     This method is useful because every time we obtain an HTTP response within RecipeSelection, we
     are adding Recipes to the Bakery. However, we want to avoid adding redundant Recipes. This
     method allows us to check if the same recipes have been obtained from the HTTP response.
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

    /*
    This is a helper method which is useful for adding in the new Recipes once we have finished
    processing the HTTP response of the JSON data.
     */
    public void addRecipes(List<Recipe> newRecipes){
        // If the size of the list is 0 or it is null, we won't do anything
        if (newRecipes.size() == 0 || newRecipes == null){
            return;
        }

        // Otherwise, we only add the Recipes to the Bakery if they're new
        for (Recipe currentRecipe : newRecipes){
            if (isNewRecipe(currentRecipe)){
                mRecipes.add(currentRecipe);
            }
        }
    }

}
