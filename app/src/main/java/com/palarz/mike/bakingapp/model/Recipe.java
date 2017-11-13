package com.palarz.mike.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mpala on 9/30/2017.
 */

public class Recipe implements Parcelable {

    // Strings of recipe names that are primarily used for the RecipeAdapter when it tries to match
    // a recipe to a drawable image
    public static final String RECIPE_NUTELLA_PIE = "Nutella Pie";
    public static final String RECIPE_BROWNIES = "Brownies";
    public static final String RECIPE_YELLOW_CAKE = "Yellow Cake";
    public static final String RECIPE_CHEESECAKE = "Cheesecake";

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

    // An overloaded constructor needed to implement Parcelable
    public Recipe(Parcel in, ClassLoader loader){
        this.mID = in.readInt();
        this.mName = in.readString();
        this.mIngredients = in.createTypedArray(Ingredient.CREATOR);
        this.mSteps = in.createTypedArray(Step.CREATOR);
        this.mServings = in.readInt();
        this.mImage = in.readString();
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

    public String printIngredients(){
        String recipeIngredients = "";
        for (int j = 0; j < this.getIngredients().length; j++){
            recipeIngredients += this.getIngredients()[j].toString() + "\n\n";
        }

        return recipeIngredients;
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

    /*
    A helper method which iterates through all of the contents of 2 Step arrays and checks if
     the contents of the array are the same. The way the check is performed is independent of the
     actual index of the array. Therefore, if a step with ID 4 was at index 0, we still check if
     the other array has the exact same step at a different index.
     */
    public static boolean sameSteps(Step[] steps1, Step[] steps2){
        boolean sameSteps = false;

        // For each step within the first Step array, we will try to find a perfect match within
        // the Steps contained in the second Step array
        for (Step step1 : steps1){
            for (Step step2 : steps2){
                // If we do have a perfect match, then we will set our boolean to true
                if (Step.sameStep(step1, step2)){
                    sameSteps = true;
                    break;
                }
                sameSteps = false;
            }
        }

        return sameSteps;
    }

    /*
    A helper method which iterates through all of the contents of 2 Ingredient arrays similarly to how
    sameSteps() was designed.
    */
    public static boolean sameIngredients(Ingredient[] ingredients1, Ingredient[] ingredients2){
        boolean sameIngredients = false;

        for (Ingredient ingredient1 : ingredients1){
            for (Ingredient ingredient2 : ingredients2){

                if (Ingredient.sameIngredients(ingredient1, ingredient2)){
                    sameIngredients = true;
                    break;
                }
                sameIngredients = false;
            }
        }

        return sameIngredients;
    }

    /*
    A helper method which ensures that two Recipes are exactly the same.
     */
    public static boolean sameRecipe(Recipe recipe1, Recipe recipe2){
        boolean sameSteps = sameSteps(recipe1.getSteps(), recipe2.getSteps());
        boolean sameIngredients =
                sameIngredients(recipe1.getIngredients(), recipe2.getIngredients());

        /*
        Notice that getImage() isn't compared between the 2 recipes? That's because I made my life
        complicated by using a locally-stored drawable for the Recipe's image if it didn't exist.
        However, it is possible that we'll compare Recipes before the image is set to a drawable for
        at least one of the Recipes.

        This is a minor issue and far too complex for me to overcome. All of the other checks still
        compose a thorough comparison between the 2 Recipes.
         */

        return recipe1.getID() == recipe2.getID()
                && recipe1.getServings() == recipe2.getServings()
                && recipe1.getName().equals(recipe2.getName())
                && sameSteps
                && sameIngredients;

    }

    //Methods necessary to implement the Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mID);
        out.writeString(mName);
        out.writeTypedArray(mIngredients,flags);
        out.writeTypedArray(mSteps, flags);
        out.writeInt(mServings);
        out.writeString(mImage);
    }

    public static final Parcelable.ClassLoaderCreator<Recipe> CREATOR =
            new Parcelable.ClassLoaderCreator<Recipe>(){
        @Override
        public Recipe createFromParcel(Parcel source, ClassLoader loader) {
            return new Recipe(source, loader);
        }

        @Override
        public Recipe createFromParcel(Parcel source) {
            return createFromParcel(source, null);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
