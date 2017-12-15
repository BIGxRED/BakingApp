/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("id")
    int mID;

    @SerializedName("shortDescription")
    String mShortDescription;

    @SerializedName("description")
    String mLongDescription;

    @SerializedName("videoURL")
    String mURL;

    @SerializedName("thumbnailURL")
    String mThumbnail;

    public Step(){
        this.mID = 0;
        this.mShortDescription = "";
        this.mLongDescription = "";
        this.mURL = "";
        this.mThumbnail = "";
    }

    public Step(int ID, String shortDescription, String longDescription,
                String URL, String thumbnail){
        this.mID = ID;
        this.mShortDescription = shortDescription;
        this.mLongDescription = longDescription;
        this.mURL = URL;
        this.mThumbnail = thumbnail;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        this.mID = ID;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.mShortDescription = shortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public void setLongDescription(String longDescription) {
        this.mLongDescription = longDescription;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        this.mURL = URL;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String toString(){
        return "ID: " + mID + "\nShort description: " + mShortDescription + "\nLong description: "
                + mLongDescription + "\nURL: " + mURL + "\nThumbnail address: " + mThumbnail;
    }

    // A helper method which checks if two Step objects are exactly the same
    public static boolean sameStep(Step step1, Step step2){
        return (step1.getID() == step2.getID())
                && step1.getShortDescription().equals(step2.getShortDescription())
                && step1.getLongDescription().equals(step2.getLongDescription())
                && step1.getURL().equals(step2.getURL())
                && step1.getThumbnail().equals(step2.getThumbnail());
    }

}
