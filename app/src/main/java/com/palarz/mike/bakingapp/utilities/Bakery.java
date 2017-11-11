package com.palarz.mike.bakingapp.utilities;

import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

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

//        // Otherwise, we will iterate through all of the steps within the Recipe and hopefully
//        // return the correct step
//        for (Step step : currentRecipe.getSteps()){
//            if (step.getID() == stepID){
//                return step;
//            }
//        }

        // However, if we weren't able to locate the step, then we will return null
        return currentRecipe.getSteps()[stepIndex];
    }

}
