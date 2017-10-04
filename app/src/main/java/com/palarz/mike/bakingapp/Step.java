package com.palarz.mike.bakingapp;

/**
 * Created by mpala on 10/3/2017.
 */

public class Step {

    int mID;
    String mShortDescription;
    String mLongDescription;
    String mURL;
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

}
