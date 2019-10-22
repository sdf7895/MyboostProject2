package com.example.myboostproject.MoviePager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myboostproject.DataBase.AppHelper2;
import com.example.myboostproject.DataBase.ReviewerData;
import com.example.myboostproject.DataBase.ViewPageFragment;
import com.example.myboostproject.DataBase.ViewPagerData;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.NetworkConnect.NetworkStatus;
import com.example.myboostproject.R;

import java.util.ArrayList;

public class MoviePagerFragment extends Fragment {
    Fragment movieFragment;

    ImageView movieImage;
    TextView movieText;
    TextView movieText2;
    TextView movieText3;
    onCommendmovie callback;

    int position;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof onCommendmovie) {
            callback = (onCommendmovie) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.moviepagerfragment, container, false);
        movieImage = rootView.findViewById(R.id.movieimage);
        movieText = rootView.findViewById(R.id.movietext);
        movieText2 = rootView.findViewById(R.id.movietext2);
        movieText3 = rootView.findViewById(R.id.movietext3);
        final int status = NetworkStatus.getConnectivityStatus(getContext());

        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                MovieSimpleItem movieSimpleItem = (MovieSimpleItem) bundle.getSerializable("movieSimpleItem");
                movieText.setText(movieSimpleItem.movieSimpletitle);
                movieText2.setText(String.valueOf(movieSimpleItem.movieSimplereservation));
                movieText3.setText(String.valueOf(movieSimpleItem.movieSimplegrade));
                Glide.with(this).asBitmap().load(movieSimpleItem.movieSimpleImage).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        movieImage.setImageBitmap(resource);
                    }
                });
            }
        }

        if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
            position = MoviePager.getPosition();
            ArrayList<ViewPagerData> list = AppHelper2.selectData();
            ViewPagerData viewPagerData = list.get(position);

            movieText.setText(viewPagerData.getTitle());
            movieText2.setText(String.valueOf(viewPagerData.getRating()));
            movieText3.setText(String.valueOf(viewPagerData.getGrade()));
        }


        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    callback.replaceInfoFragment(movieFragment);//영화목록에서 상세보기클릭을하면 영화상세정보가 화면에 띄워지게됩니다
                } else if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
                    callback.replaceInfoFragment(movieFragment);
                }
            }
        });

        return rootView;
    }

    public void MovieDetailFragment(Fragment fragment) {
        this.movieFragment = fragment;
    }

}
