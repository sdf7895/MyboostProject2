package com.example.myboostproject.MovieItem;

import java.io.Serializable;

public class MovieItem implements Serializable {

    String name;
    String comment;
    float ratingBar;

    public MovieItem(String name, String comment, float ratingBar) {
        this.name = name;
        this.comment = comment;
        this.ratingBar = ratingBar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getResId() {
        return ratingBar;
    }

    public void setResId(float ratingBar) {
        this.ratingBar = ratingBar;
    }

    @Override
    public String toString() {
        return "MovieItem{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", resId=" + ratingBar +
                '}';
    }
}
