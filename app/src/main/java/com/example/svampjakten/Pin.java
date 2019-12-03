package com.example.svampjakten;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Pin {

    public String placePersonID;

    public String placeName;
    public double placeRating;

    public ArrayList<String> pinComments;
    public Bitmap pinPhoto;

    public PinLocation pinLocation = new PinLocation();

        Pin(){

        }

        Pin(PinLocation pinLocation, String placeName, String placePersonID, double placeRating){
            this.pinLocation = pinLocation;
            this.placeName = placeName;
            this.placePersonID = placePersonID;
            this.placeRating = placeRating;
        }
}

class PinLocation{
    public double latitude;
    public double longitude;

    PinLocation(){

    }

    PinLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
