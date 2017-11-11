package com.palarz.mike.bakingapp.utilities;

import android.content.Context;

import com.palarz.mike.bakingapp.model.Recipe;

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

}
