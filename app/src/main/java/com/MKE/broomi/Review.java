package com.MKE.broomi;

public class Review {
    public String myuid;
    public String destuid;
    public float Rating;
    public String review;
    public String usernick;


    public String getMyuid() {
        return myuid;
    }

    public void setMyuid(String myuid) {
        this.myuid = myuid;
    }

    public String getDestuid() {
        return destuid;
    }

    public void setDestuid(String destuid) {
        this.destuid = destuid;
    }

    public float getRating1() {
        return Rating;
    }

    public void setRating1(float rating) {
        Rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Review(String myuid, String destuid, float rating , String review, String usernick) {
        this.myuid = myuid;
        this.destuid = destuid;
        this.Rating = rating;
        this.review = review;
        this.usernick = usernick;
    }
    public Review() {}

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }
}
