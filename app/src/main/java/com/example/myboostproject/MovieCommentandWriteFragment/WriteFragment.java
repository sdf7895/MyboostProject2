package com.example.myboostproject.MovieCommentandWriteFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myboostproject.MovieDetaill.MovieDetailFragment;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.R;
import com.example.myboostproject.data.AppHelper;

import java.util.HashMap;
import java.util.Map;

public class WriteFragment extends Fragment {
    EditText comment;
    RatingBar ratingBar;
    String data;
    TextView writetitle;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.writefragment, container, false);


        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());
        }

        comment = rootView.findViewById(R.id.comment);
        ratingBar = rootView.findViewById(R.id.starsmovie);
        writetitle = rootView.findViewById(R.id.writetitle);

        if(getArguments() != null){
            Bundle bundle = getArguments();
            writetitle.setText(bundle.getString("writetitle"));
        }

        Button save_Button = (Button) rootView.findViewById(R.id.savebutton);
        save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("comment",comment.getText().toString());
                bundle.putFloat("rating",ratingBar.getRating());
                movieDetailFragment.setArguments(bundle);

                String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/createComment";
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
                        params.put("id", data);
                        params.put("writer", "jun15");
                        params.put("rating", Float.toString(ratingBar.getRating()));
                        params.put("contents", comment.getText().toString());

                        return params;
                    }
                };
                request.setShouldCache(false);
                AppHelper.requestQueue.add(request);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(WriteFragment.this).commit();
                fragmentManager.popBackStack();
            }
        });

        Button cancel_Button = (Button) rootView.findViewById(R.id.falsebutton);
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(WriteFragment.this).commit();
                fragmentManager.popBackStack();

            }
        });

        return rootView;
    }

    public void getStringData(String data) {
        this.data = data;
    }


}
