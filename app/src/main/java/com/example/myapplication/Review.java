package com.example.myapplication;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Review { //TODO: Finish the review model

    private String rUserID;
    private String rUserName;
    private Date rDate;
    private String rContent;
    private float rReview;
    private String rRestaurantID;
    private String rPic;

    public Review() { } // Needed for Firebase

    public Review(String user, String userName, Date date, String content, float score, String restaurant, String pic) {
        rUserID = user;
        rUserName = userName;
        rDate = date;
        rContent = content;
        rReview = score;
        rRestaurantID = restaurant;
        rPic = pic;
    }

    public String getUser() { return rUserID; }

    public void setUser(String user) { rUserID = user; }

    public String getUserName() { return rUserName; }

    public void setUserName(String userName) { rUserName = userName; }

    public Date getDate() { return rDate; }

    public void setDate(Date date) { rDate = date; }

    public String getContent() { return rContent; }

    public void setContent(String content) { rContent = content; }

    public float getScore() { return rReview; }

    public void setScore(float score) { rReview = score; }

    public String getRestaurant() { return rRestaurantID; }

    public void setRestaurant(String restaurant) { rRestaurantID = restaurant; }

    public String getPic() { return rPic; }

    public void setPic(String pic) { rPic = pic; }
}
