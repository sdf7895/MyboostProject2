package com.example.myboostproject.data;

import java.io.Serializable;

/**
 id: 5726,
 writer: "lili78",
 movieId: 1,
 writer_image: null,
 time: "2019-09-24 20:28:13",
 timestamp: 1569324492,
 rating: 4,
 contents: "고생이 많으십니다 여러분, 조금 더 힘내주세오!",
 recommend: 0
 */

public class CommentInfo implements Serializable {
    public int id;
    public String writer;
    public int movieId;
    public String writer_image;
    public String time;
    public int timestamp;
    public float rating;
    public String contents;
    public int recommend;


}
