package com.example.myapplication;

import com.google.firebase.firestore.GeoPoint;

public class Restaurant {

    private String rName;
    private String rLogo;
    private String rBackPic;
    private int rReview;
    private int rNumReview;
    private GeoPoint rLocation;
    private String rUid;

    public Restaurant() { } // Needed for Firebase

    public Restaurant(String name, String logo, String back_pic, int review, int numReview, GeoPoint location, String uid) {
        rName = name;
        rLogo = logo;
        rBackPic = back_pic;
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

    public int getReview() { return rReview; }

    public void setReview(int review) { rReview = review; }

    public int getNumReview() { return rNumReview; }

    public void setNumReview(int numReview) { rNumReview = numReview; }

    public GeoPoint getLocation() { return rLocation; }

    public void setLocation(GeoPoint location) { rLocation = location; }

}
