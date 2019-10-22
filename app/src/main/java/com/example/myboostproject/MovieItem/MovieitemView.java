package com.example.myboostproject.MovieItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myboostproject.R;

public class MovieitemView extends LinearLayout {
    TextView id_TextView;
    TextView comment_TextView;
    RatingBar ratingBar;

    public MovieitemView(Context context) {
        super(context);
        init(context);
    }

    public MovieitemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_item, this, true);

        id_TextView = (TextView) findViewById(R.id.textView);
        comment_TextView = (TextView) findViewById(R.id.textView2);
        ratingBar = (RatingBar) findViewById(R.id.ratinBar);
    }

    public void setName(String name) {
        id_TextView.setText(name);
    }

    public void setComment(String comment) {
        comment_TextView.setText(comment);
    }

    public void setRatingBar(float ratingbar){
        ratingBar.setRating(ratingbar);
    }
}
