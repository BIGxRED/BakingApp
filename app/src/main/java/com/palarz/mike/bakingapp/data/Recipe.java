package com.palarz.mike.bakingapp.data;

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
    boolean mImageOverride;

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

    public Step[] getSteps() {
        return mSteps;
    }

    public void setSteps(Step[] steps) {
        this.mSteps = new Step[steps.length];
        for (int j = 0; j < steps.length; j++){
            mSteps[j] = steps[j];
        }
    }

    public String printIngredients(){
        String recipeIngredients = "";
        for (int j = 0; j < this.getIngredients().length; j++){
            recipeIngredients += this.getIngredients()[j].toString() + "\n\n";
        }

        return recipeIngredients;
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
