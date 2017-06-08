package com.example.lucie.materielsmobiles;

/**
 * Created by lucie on 08/06/2017.
 */

public class Post {

    private String Title, Image, Date, gps;

    public Post(){

    }

    public Post(String title, String image, String date, String gps) {
        Title = title;
        Image = image;
        Date = date;
        this.gps = gps;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}
