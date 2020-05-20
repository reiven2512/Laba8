package com.example.laba_map;

import android.util.Log;

public class Place {
    String name;
    String lat;
    String lng;
    public Place(String name, String lat, String lng){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        Log.v("text_of_mine", lat);
        return Double.parseDouble(lat);
    }

    public double getLng() {
        Log.v("text_of_mine", lng);
        return Double.parseDouble(lng);
    }

    public String getName() {
        return name;
    }
}
