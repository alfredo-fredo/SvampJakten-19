package com.example.svampjakten;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Pin {

    public String placeName;
    public double placeRating;

    public ArrayList<String> pinComments;
    public Bitmap pinPhoto;

    public PinLocation pinLocation;


        Pin(String placeName, double placeRating, ArrayList<String> pinComments, Bitmap pinPhoto, PinLocation pinLocation){

        this.placeName = placeName;

        this.placeRating = placeRating;

        if(pinComments != null){
            this.pinComments = new ArrayList<>(pinComments);
        }else{
            this.pinComments = new ArrayList<>();
        }

        if(pinPhoto != null){
            this.pinPhoto = pinPhoto;
        }

        this.pinLocation = pinLocation;

    }

    public String getPlaceName() {
        return placeName;
    }

    public double getPlaceRating() {
        return placeRating;
    }

    public ArrayList<String> getPinComments() {
        return pinComments;
    }

    public Bitmap getPinPhoto() {
        return pinPhoto;
    }

    public PinLocation getPinLocation() {
        return pinLocation;
    }
}

class PinLocation{
    public double latitude;
    public double longitude;

    PinLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
