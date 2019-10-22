package com.example.myboostproject.MovieDetaill;

import java.io.Serializable;

public class MovieDetailItem implements Serializable {

    String movieDetailImage;
    String movieDetailTitle;
    String movieDetailOpen;
    String movieContents;
    String moviedirector;
    String movieActor;
    String photos;
    String videos;
    float reservationRate;
    int reservationGrade;
    float ratingBar;
    int audience;
    int like;
    int dislike;


    public MovieDetailItem(String movieDetailImage, String movieDetailTitle,String movieDetailOpen, String movieContents, String moviedirector, String movieActor,float reservationRate,int reservationGrade,float ratingBar,int audience, int like , int dislike,String photos,String videos){
        this.movieDetailImage = movieDetailImage;
        this.movieDetailTitle = movieDetailTitle;
        this.movieDetailOpen = movieDetailOpen;
        this.movieContents = movieContents;
        this.moviedirector = moviedirector;
        this.movieActor = movieActor;
        this.reservationRate =reservationRate;
        this.reservationGrade =reservationGrade;
        this.ratingBar =ratingBar;
        this.audience =audience;
        this.like = like;
        this.dislike = dislike;
        this.photos = photos;
        this.videos = videos;
    }
}
