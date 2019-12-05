package com.example.svampjakten;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Pin {

    public String placePersonID;

    public String placeName;
    public double placeRating;
    public String comment;

    public PinLocation pinLocation;

        Pin(){

        }

        Pin(PinLocation pinLocation, String placeName, String comment, String placePersonID, double placeRating){
            this.pinLocation = pinLocation;
            this.placeName = placeName;
            this.comment = comment;
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
