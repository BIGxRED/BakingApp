package com.palarz.mike.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mpala on 10/3/2017.
 */

public class Step implements Parcelable {

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

    public Step(Parcel in){
        this.mID = in.readInt();
        this.mShortDescription = in.readString();
        this.mLongDescription = in.readString();
        this.mURL = in.readString();
        this.mThumbnail = in.readString();
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

    /* Methods required for the parcelable interface*/

    //A method which specifies if special Objects are being "flattened" into a Parcel, such as an
    //Object which contained a file descriptor. In the case of a Step, this is not needed so the
    //method simply returns 0.
    @Override
    public int describeContents(){
        return 0;
    }

    //This method "flattens" a Step object into a Parcel
    @Override
    public void writeToParcel(Parcel out,int flags){
        out.writeInt(mID);
        out.writeString(mShortDescription);
        out.writeString(mLongDescription);
        out.writeString(mURL);
        out.writeString(mThumbnail);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>(){

        //This method "unpacks" the Step; a new Step instance is created which is instantiated by
        //the parcel that had originally been created within writeToParcel()
        public Step createFromParcel(Parcel in){
            return new Step(in);
        }

        //This method creates a new array of the parcelable Movie class
        public Step[] newArray(int size){
            return new Step[size];
        }
    };

}