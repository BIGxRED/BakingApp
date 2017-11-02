package com.palarz.mike.bakingapp.utilities;

import android.content.Context;

import com.palarz.mike.bakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * Created by mpala on 11/2/2017.
 */

public class RecipeLab {

    private static RecipeLab sRecipeLab;
    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    private RecipeLab(Context context){
        this.mContext = context;
    }

    public static RecipeLab get(Context context){
        if (sRecipeLab == null){
            sRecipeLab = new RecipeLab(context.getApplicationContext());
        }

        return sRecipeLab;
    }

    public ArrayList<Recipe> getRecipes(){
        return mRecipes;
    }

    public Recipe getRecipe(int ID){
        for (Recipe recipe : mRecipes){
            if (ID == recipe.getID()){
                return recipe;
            }
        }

        return null;
    }

}
