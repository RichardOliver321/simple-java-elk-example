package com.elkdemo.demo.model;

import java.util.Date;

public class MovieRating {

    private String title;
    private String directorName;
    private double rating;
    private Date dateOfReview;


    public MovieRating() {
    }

    public MovieRating(String title, String directorName, double rating) {
        this.title = title;
        this.directorName = directorName;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getDateOfReview() {
        return dateOfReview;
    }

    public void setDateOfReview(Date dateOfReview) {
        this.dateOfReview = dateOfReview;
    }
}
