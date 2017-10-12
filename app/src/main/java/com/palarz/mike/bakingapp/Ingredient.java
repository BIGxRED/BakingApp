package com.palarz.mike.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mpala on 10/3/2017.
 */

public class Ingredient implements Parcelable {

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

    public Ingredient(Parcel in){
        this.mDescription = in.readString();
        this.mMeasure = in.readString();
        this.mQuantity = in.readDouble();
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

    //Methods necessary to implement the Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(mDescription);
        out.writeString(mMeasure);
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
