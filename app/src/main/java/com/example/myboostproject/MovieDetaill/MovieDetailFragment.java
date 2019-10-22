package com.example.myboostproject.MovieDetaill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.example.myboostproject.DataBase.AppHelper2;
import com.example.myboostproject.DataBase.DetaillMovieData;
import com.example.myboostproject.DataBase.ReviewerData;
import com.example.myboostproject.MovieInterFace.MovieCommentList;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.MovieItem.MovieItem;
import com.example.myboostproject.NetworkConnect.NetworkStatus;
import com.example.myboostproject.PhotoView.PhotoFragment;
import com.example.myboostproject.R;
import com.example.myboostproject.data.AppHelper;
import com.example.myboostproject.data.CommentInfo;
import com.example.myboostproject.data.CommentList;
import com.example.myboostproject.data.CommentResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieDetailFragment extends Fragment implements MovieCommentList {
    ImageButton up_Button;
    ImageButton down_Button;

    TextView up_Increase_Text;
    TextView down_Decrease_Text;

    boolean up_State = false;
    boolean down_State = false;


    int up_Count = 0;
    int down_Count = 0;

    int getUp_Count = 0;
    int getDown_Count = 0;

    MovieAdapter adapter;
    ImageView movieImage;
    TextView movieTitle;
    TextView movieOpen;
    TextView movieContents;
    TextView movieDirector;
    TextView movieActor;
    TextView reservationRate;
    RatingBar ratingBar;
    TextView audience;
    TextView textRating;
    TextView reservationGrade;

    TextView like;
    TextView dislike;

    ListView listView;

    String i;

    ArrayList<MovieItem> items = new ArrayList<MovieItem>();

    RecyclerView recyclerView;
    MovieRecyclerAdapter movieRecyclerAdapter;

    onCommendmovie callback;

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

    @Override
    public void onPause() {
        if (up_State == true) {
            likeData();
            up_State = false;
        } else if (down_State == true) {
            dislikeData();
            down_State = false;
        }

        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int status = NetworkStatus.getConnectivityStatus(getContext());

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.moviedetailfragment, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        movieRecyclerAdapter = new MovieRecyclerAdapter(getContext());

        up_Button = rootView.findViewById(R.id.likebutton);
        down_Button = rootView.findViewById(R.id.likebutton2);
        up_Increase_Text = rootView.findViewById(R.id.textView);
        down_Decrease_Text = rootView.findViewById(R.id.textView2);
        listView = rootView.findViewById(R.id.listView);

        movieImage = rootView.findViewById(R.id.movieimage);
        movieTitle = rootView.findViewById(R.id.movietitle);
        movieOpen = rootView.findViewById(R.id.movieopen);
        movieContents = rootView.findViewById(R.id.moviecontents);
        movieDirector = rootView.findViewById(R.id.moviedirector);
        movieActor = rootView.findViewById(R.id.movieactor);
        reservationRate = rootView.findViewById(R.id.reservationrate);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        audience = rootView.findViewById(R.id.audience);
        textRating = rootView.findViewById(R.id.textrating);
        reservationGrade = rootView.findViewById(R.id.reservationgrade);

        like = rootView.findViewById(R.id.textView);
        dislike = rootView.findViewById(R.id.textView2);

        if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
            getPosition();

        }


        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

            if (getArguments() != null) {
                Bundle bundle = getArguments();
                MovieDetailItem movieDetailItem = (MovieDetailItem) bundle.getSerializable("movieDetailItem");

                Glide.with(this).asBitmap().load(movieDetailItem.movieDetailImage).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        movieImage.setImageBitmap(resource);
                    }
                });

                movieOpen.setText(movieDetailItem.movieDetailOpen);
                movieContents.setText(movieDetailItem.movieContents);
                movieDirector.setText(movieDetailItem.moviedirector);
                movieActor.setText(movieDetailItem.movieActor);
                movieTitle.setText(movieDetailItem.movieDetailTitle);
                reservationRate.setText(String.valueOf(movieDetailItem.reservationRate + "%"));
                audience.setText(String.valueOf(movieDetailItem.audience + "명"));
                ratingBar.setRating(movieDetailItem.ratingBar);
                textRating.setText(String.valueOf(movieDetailItem.ratingBar));
                like.setText(String.valueOf(movieDetailItem.like));
                dislike.setText(String.valueOf(movieDetailItem.dislike));
                reservationGrade.setText(String.valueOf(movieDetailItem.reservationGrade + "위"));

                up_Count = movieDetailItem.like;
                down_Count = movieDetailItem.dislike;


                if(movieDetailItem.photos != null) {
                    final ArrayList<String> videoUrl = new ArrayList<>();
                    final String[] movieUrl = movieDetailItem.photos.split(",");
                    final String[] videourl = movieDetailItem.videos.split(",");

                    for (int i = 0; i < movieUrl.length; i++) {
                        movieRecyclerAdapter.addItem(new MoviePictureItem(movieUrl[i]));
                    }
                    movieRecyclerAdapter.addItem(new MoviePictureItem("https://img.youtube.com/vi/VJAPZ9cIbs0/0.jpg"));
                    movieRecyclerAdapter.addItem(new MoviePictureItem("https://img.youtube.com/vi/y422jVFruic/0.jpg"));
                    movieRecyclerAdapter.addItem(new MoviePictureItem("https://img.youtube.com/vi/JNL44p5kzTk/0.jpg"));

                    for(int i =0; i < videourl.length; i++){
                        videoUrl.add(videourl[i]);
                    }

                    movieRecyclerAdapter.setOnItemClickListener(new MovieRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MovieRecyclerAdapter.ViewHolder holder, View view, int position) {
                            if(position < movieUrl.length) {
                                
                                MoviePictureItem moviePictureItem = movieRecyclerAdapter.getItem(position);

                                PhotoFragment photoFragment = new PhotoFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("photo", moviePictureItem.getMovieurl());

                                photoFragment.setArguments(bundle);

                                callback.photoViewFragment(photoFragment);
                            }else if(position >= movieUrl.length){

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl.get(position-5)));
                                startActivity(intent);
                            }
                        }
                    });
                }

            }

            listView.setAdapter(adapter);
            recyclerView.setAdapter(movieRecyclerAdapter);


            up_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (up_State) {
                        decrLikeCount();
                    } else {
                        incrLikeCount();
                    }
                    if (down_State == false) {
                        up_State = !up_State;

                    }
                }
            });

            down_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (down_State) {
                        decrLikeCount();
                    } else {
                        incrLikeCount2();
                    }
                    if (up_State == false) {
                        down_State = !down_State;
                    }
                }
            });

            Button wright = rootView.findViewById(R.id.wright);
            wright.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.WriteInfoReplace(i); //한줄평작성 데이터를 메인액티비티에서 처리하도록 메인액티비티에 설정해놓은 메소드를 호출합니다
                }
            });

            Button allcommant = rootView.findViewById(R.id.all_commantView);
            allcommant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                        callback.AllCommentInfoReplace(i);
                    } else if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
                        callback.AllCommentInfoReplace(i);
                    }
                }
            });
        }


        return rootView;
    }

    public void incrLikeCount() { //up_Count 1 증가 , down_Count 1감소 시키는 코드
        if (up_State == false) {
            up_Count += 1;
            getUp_Count = +1;

            up_Increase_Text.setText(String.valueOf(up_Count));
            up_Button.setBackgroundResource(R.drawable.ic_thumb_up_selected);

            if (down_State == true) {
                down_Count -= 1;
                getDown_Count -= 1;

                down_Button.setBackgroundResource(R.drawable.ic_thumb_down);
                down_Decrease_Text.setText(String.valueOf(down_Count));

                down_State = !down_State;
            }
        }
    }

    public void incrLikeCount2() { //down_Count 1 증가 , up_Count 1 감소 시키는 코드
        if (down_State == false) {
            down_Count += 1;
            getDown_Count += 1;

            down_Decrease_Text.setText(String.valueOf(down_Count));
            down_Button.setBackgroundResource(R.drawable.ic_thumb_down_selected);

            if (up_State == true) {
                up_Count -= 1;
                getUp_Count -= 1;

                up_Button.setBackgroundResource(R.drawable.ic_thumb_up);
                up_Increase_Text.setText(String.valueOf(up_Count));

                up_State = !up_State;
            }
        }
    }

    public void decrLikeCount() { //up_Count 1감소 시키는 코드
        if (up_State == true) {
            up_Count -= 1;
            getUp_Count -= 1;

            up_Increase_Text.setText(String.valueOf(up_Count));
            up_Button.setBackgroundResource(R.drawable.ic_thumb_up);
        } else if (down_State == true) { //down_Count 1감소 시키는 코드
            down_Count -= 1;
            getDown_Count -= 1;

            down_Decrease_Text.setText(String.valueOf(down_Count));
            down_Button.setBackgroundResource(R.drawable.ic_thumb_down);
        }
    }

    class MovieAdapter extends BaseAdapter {
        ArrayList<MovieItem> items;

        public MovieAdapter(ArrayList<MovieItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.movie_item, parent, false);
            }

            TextView id = convertView.findViewById(R.id.textView);
            TextView comment = convertView.findViewById(R.id.textView2);
            RatingBar ratingBar = convertView.findViewById(R.id.ratinBar);

            MovieItem item = items.get(position);

            id.setText(item.getName());
            comment.setText(item.getComment());
            ratingBar.setRating(item.getResId());

            return convertView;
        }
    }

    @Override
    public void requestCommentList(String data) {
        String url = data;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        commentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }


    private void commentResponse(String response) {
        Gson gson = new Gson();
        adapter = new MovieAdapter(items);

        CommentResponseInfo info = gson.fromJson(response, CommentResponseInfo.class);
        if (info.code == 200) {
            CommentList commentList = gson.fromJson(response, CommentList.class);
            if (commentList != null) {
                for (int i = 0; i < commentList.result.size(); i++) {
                    CommentInfo commentInfo = commentList.result.get(i);

                    AppHelper2.reviewerInsertData(commentInfo.writer, commentInfo.contents, commentInfo.rating);

                    items.add(new MovieItem(commentInfo.writer, commentInfo.contents, commentInfo.rating));

                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void getInt(String i) {
        this.i = i;
    }

    public void likeData() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseLikeDisLike";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", i);
                params.put("likeyn", "Y");

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void dislikeData() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseLikeDisLike";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", i);
                params.put("dislikeyn", "Y");

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void getPosition() {
        ArrayList<DetaillMovieData> list = AppHelper2.detaillSelectData();
        DetaillMovieData detaillMovieData = list.get(0);

        ArrayList<ReviewerData> list2 = AppHelper2.reviewerSelectData();
        ReviewerData reviewerData = list2.get(0);

        movieTitle.setText(detaillMovieData.getTitle());
        movieOpen.setText(detaillMovieData.getDataValue());
        movieContents.setText(detaillMovieData.getSynopsis());
        movieDirector.setText(detaillMovieData.getDirector());
        movieActor.setText(detaillMovieData.getActor());
        reservationRate.setText(String.valueOf(detaillMovieData.getReservation_rate()) + "%");
        ratingBar.setRating(detaillMovieData.getAudience_rating());
        textRating.setText(String.valueOf(detaillMovieData.getAudience_rating()));
        audience.setText(String.valueOf(detaillMovieData.getAudience()) + "명");
        reservationGrade.setText(String.valueOf(detaillMovieData.getReservation_grade()) + "위");
        like.setText(String.valueOf(detaillMovieData.getUpLike()));
        dislike.setText(String.valueOf(detaillMovieData.getDislike()));

        items.add(new MovieItem(reviewerData.getWriter(), reviewerData.getContents(), reviewerData.getRating()));

        listView.setAdapter(adapter);
    }
}
