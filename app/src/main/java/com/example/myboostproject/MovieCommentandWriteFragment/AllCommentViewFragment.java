package com.example.myboostproject.MovieCommentandWriteFragment;

import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myboostproject.MovieDetaill.MovieDetailFragment;
import com.example.myboostproject.MovieInterFace.MovieCommentList;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.MovieItem.MovieItem;
import com.example.myboostproject.R;
import com.example.myboostproject.data.AppHelper;
import com.example.myboostproject.data.CommentInfo;
import com.example.myboostproject.data.CommentList;
import com.example.myboostproject.data.CommentResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;


public class AllCommentViewFragment extends Fragment implements MovieCommentList {
    TextView totalParticipation;
    TextView commenTtitle;
    TextView totalRating;
    RatingBar ratingBar;
    Button wright_Comment;
    ListView all_Listview;
    MovieAdapter adapter;
    ArrayList<MovieItem> items = new ArrayList<>();
    onCommendmovie callback;
    String data;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.allcommentviewfragment, container, false);
        all_Listview = rootView.findViewById(R.id.alllistView);
        totalParticipation = rootView.findViewById(R.id.total);
        commenTtitle = rootView.findViewById(R.id.commenttitle);
        totalRating = rootView.findViewById(R.id.totalrating);
        ratingBar = rootView.findViewById(R.id.ratingBar);


        if(getArguments() != null){
            Bundle bundle = getArguments();
            commenTtitle.setText(bundle.getString("title"));
            totalRating.setText(String.valueOf(bundle.getFloat("totalrating")));
            ratingBar.setRating(bundle.getFloat("totalrating"));


        }

        all_Listview.setAdapter(adapter);

        wright_Comment = rootView.findViewById(R.id.wrightcommant);
        wright_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.WriteInfoReplace(data);
            }
        });

        return rootView;
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

                    items.add(new MovieItem(commentInfo.writer, commentInfo.contents, commentInfo.rating));

                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void getStringdata(String data){
        this.data = data;
    }
}
