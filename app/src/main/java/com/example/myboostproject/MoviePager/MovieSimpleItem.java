package com.example.myboostproject.MoviePager;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MovieSimpleItem implements Serializable {
    String movieSimpletitle;
    String movieSimpleImage;
    float movieSimplereservation;
    int movieSimplegrade;


    public MovieSimpleItem(String movieSimpletitle,String movieSimpleImage,float movieSimplereservation,int movieSimplegrade){
        this.movieSimpletitle = movieSimpletitle;
        this.movieSimpleImage = movieSimpleImage;
        this.movieSimplereservation = movieSimplereservation;
        this.movieSimplegrade = movieSimplegrade;
    }

}
