package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.GeoPoint;

public class Restaurant {

    private String rName;
    private String rLogo;
    private String rBackPic;
    private float rReview;
    private int rNumReview;
    private GeoPoint rLocation;
    private String rUid;
    private float rDistance;

    public Restaurant() { } // Needed for Firebase

    public Restaurant(String name, String logo, String backPic, float review, int numReview, GeoPoint location, String uid) {
        rName = name;
        rLogo = logo;
        rBackPic = backPic;
        rReview = review;
        rNumReview = numReview;
        rLocation = location;
        rUid = uid;
    }

    public String getName() { return rName; }

    public void setName(String name) { rName = name; }

    public String getLogo() { return rLogo; }

    public void setLogo(String logo) { rLogo = logo; }

    public String getUid() { return rUid; }

    public void setUid(String uid) { rUid = uid; }

    public String getBackPic() { return rBackPic; }

    public void setBackPic(String backPic) { rBackPic = backPic; }

    public float getReview() { return rReview; }

    public void setReview(float review) { rReview = review; }

    public int getNumReview() { return rNumReview; }

    public void setNumReview(int numReview) { rNumReview = numReview; }

    public GeoPoint getLocation() { return rLocation; }

    public void setLocation(GeoPoint location) { rLocation = location; }

    public float getDistance() { return rDistance; }

    public void calculateDistance(float latitude, float longitude){
        double restLat = rLocation.getLatitude();
        double restLong = rLocation.getLongitude();

        double theta = longitude - restLong;
        double dist = Math.sin((latitude * Math.PI / 180.0)) * Math.sin((restLat * Math.PI / 180.0))
                + Math.cos((latitude * Math.PI / 180.0))
                * Math.cos((restLat * Math.PI / 180.0))
                * Math.cos((theta * Math.PI / 180.0));
        dist = Math.acos(dist);
        dist = (dist * 180.0 / Math.PI);
        dist = dist * 60 * 1.1515;
        rDistance = (float) dist;
    }

}
