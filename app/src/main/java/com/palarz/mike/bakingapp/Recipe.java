package com.palarz.mike.bakingapp;

/**
 * Created by mpala on 9/30/2017.
 */

public class Recipe {

    int mID;
    String mName;
    Ingredient[] mIngredients;
    Step[] mSteps;
    int mServings;
    String mImage;

    public Recipe(){
        this.mID = 0;
        this.mName = "";
        this.mIngredients = new Ingredient[0];
        this.mSteps = new Step[0];
        this.mServings = 0;
        this.mImage = "";
    }

    public Recipe(int ID, String name, Ingredient[] ingredients, Step[] steps, int servings,
                  String image){
        this.mID = ID;
        this.mName = name;

        this.mIngredients = new Ingredient[ingredients.length];
        for (int i = 0; i < ingredients.length; i++){
            mIngredients[i] = ingredients[i];
        }

        this.mSteps = new Step[steps.length];
        for (int j = 0; j < steps.length; j++){
            mSteps[j] = steps[j];
        }

        this.mServings = servings;
        this.mImage = image;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        this.mID = ID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Ingredient[] getIngredients() {
        return mIngredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.mIngredients = new Ingredient[ingredients.length];
        for (int i = 0; i < ingredients.length; i++){
            mIngredients[i] = ingredients[i];
        }
    }

    public Step[] getSteps() {
        return mSteps;
    }

    public void setSteps(Step[] steps) {
        this.mSteps = new Step[steps.length];
        for (int j = 0; j < steps.length; j++){
            mSteps[j] = steps[j];
        }
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String toString(){
        String recipeToString =  "Recipe ID: " + mID + "\nName: " + mName + "\nIngredients:\n";
        for (int i = 0; i < this.getIngredients().length; i++){
            recipeToString += this.getIngredients()[i].toString() + "\n";
        }
        recipeToString += "\nSteps:\n";
        for (int j = 0; j < this.getSteps().length; j++){
            recipeToString += this.getSteps()[j].toString() + "\n";
        }
        recipeToString += "\nServings: " + mServings + "\nImage: " + mImage + "\n";
        return recipeToString;
    }

}
