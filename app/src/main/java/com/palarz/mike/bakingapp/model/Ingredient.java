package com.palarz.mike.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.InflateException;

/**
 * Created by mpala on 10/3/2017.
 */

public class Ingredient implements Parcelable {

    double mQuantity;
    String mUnit;
    String mDescription;

    public Ingredient(){
        this.mQuantity = 0.0;
        this.mDescription = "";
        this.mUnit = "";
    }

    public Ingredient (String description, String unit, double quantity){
        this.mDescription = description;
        this.mQuantity = quantity;
        this.mUnit = formatUnit(unit);
    }

    public Ingredient(Parcel in){
        this.mDescription = in.readString();
        this.mUnit = in.readString();
        this.mQuantity = in.readDouble();
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getMeasure() {
        return mUnit;
    }

    public void setMeasure(String measure) {
        this.mUnit = measure;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String toString(){
        /*
        This if() is meant to format the data when we display the ingredients list in
        RecipeDetails. I felt that it looked weird that without this formatting, the ingredients
        list would say the user needs '8.0 eggs'. It should just say 8, not 8.0. If there isn't a
        fractional part in the double (double % 1 returns 0 in that case), then we cast the double
        into an int.
         */
        if ((mQuantity % 1) == 0){
            int quantityForToString = (int) mQuantity;
            return quantityForToString + " " + mUnit + " " + mDescription;
        }

        /*
        Otherwise, we keep the double as is if it has a fractional part and display that in the
        ingredients list through toString().
         */
        return mQuantity + " " + mUnit + " " + mDescription;
    }

    /*
    This is a helper function to convert the units that are provided in the JSON response into
    something more readable within the ingredients ScrollView inside of RecipeDetails.
     */
    private String formatUnit(String originalUnit){
        switch (originalUnit){
            case "G":
                if (mQuantity > 1.0) {
                    mUnit = "grams of";
                }
                else {
                    mUnit = "gram of";
                }
                return mUnit;
            case "TSP":
                if (mQuantity > 1.0) {
                    mUnit = "teaspoons of";
                }
                else {
                    mUnit = "teaspoon of";
                }
                return mUnit;
            case "TBLSP":
                if (mQuantity > 1.0) {
                    mUnit = "tablespoons of";
                }
                else {
                    mUnit = "tablespoon of";
                }
                return mUnit;
            case "UNIT":
                mUnit = "";
                return mUnit;
            case "K":
                if (mQuantity > 1.0) {
                    mUnit = "kilograms of";
                }
                else {
                    mUnit = "kilogram of";
                }
                return mUnit;
            case "CUP":
                if (mQuantity > 1.0) {
                    mUnit = "cups of";
                }
                else {
                    mUnit = "cup of";
                }
                return mUnit;
            case "OZ":
                if (mQuantity > 1.0) {
                    mUnit = "ounces of";
                }
                else {
                    mUnit = "ounce of";
                }
                return mUnit;
            default:
                mUnit = originalUnit + " of";
                return mUnit;
        }
    }

    // A helper method which determine if two Ingredients are exactly the same
    public static boolean sameIngredients(Ingredient ingredient1, Ingredient ingredient2){
        return ingredient1.getQuantity() == ingredient2.getQuantity()
                && ingredient1.getDescription().equals(ingredient2.getDescription())
                && ingredient1.getMeasure().equals(ingredient2.getMeasure());
    }

    //Methods necessary to implement the Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(mDescription);
        out.writeString(mUnit);
        out.writeDouble(mQuantity);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}
