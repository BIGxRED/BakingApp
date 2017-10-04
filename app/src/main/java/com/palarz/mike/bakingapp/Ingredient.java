package com.palarz.mike.bakingapp;

/**
 * Created by mpala on 10/3/2017.
 */

public class Ingredient {

    double mQuantity;
    String mMeasure;
    String mDescription;

    public Ingredient(){
        this.mQuantity = 0.0;
        this.mDescription = "";
        this.mMeasure = "";
    }

    public Ingredient (String description, String measure, double quantity){
        this.mDescription = description;
        this.mMeasure = measure;
        this.mQuantity = quantity;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        this.mMeasure = measure;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String toString(){
        return "Ingredient name: " + mDescription + "\nQuantity: " + mQuantity + "\nUnit: " + mMeasure;
    }

}
