package com.example.androidu.glcamera.landmark;

import android.location.Location;

public class Landmark {
    public String title;
    public String description;
    public float latitude;
    public float longitude;
    public float altitude;

    public Landmark(String title, String description, float latitude, float longitude, float altitude){
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public float distance(Landmark other){
        l1.setLatitude(latitude);
        l1.setLongitude(longitude);
        l2.setLatitude(other.latitude);
        l2.setLongitude(other.longitude);
        return l1.distanceTo(l2);
    }

    public float compassDirection(Landmark other){
        l1.setLatitude(latitude);
        l1.setLongitude(longitude);
        l2.setLatitude(other.latitude);
        l2.setLongitude(other.longitude);
        return l1.bearingTo(l2);
    }



    private static Location l1 = new Location("");
    private static Location l2 = new Location("");
}
