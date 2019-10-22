package com.example.myboostproject.MoviePager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myboostproject.DataBase.AppHelper2;
import com.example.myboostproject.DataBase.ViewPagerData;
import com.example.myboostproject.MovieCommentandWriteFragment.AllCommentViewFragment;
import com.example.myboostproject.MovieCommentandWriteFragment.WriteFragment;
import com.example.myboostproject.MovieDetaill.MovieDetailFragment;
import com.example.myboostproject.MovieDetaill.MovieDetailItem;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.NetworkConnect.NetworkStatus;
import com.example.myboostproject.R;
import com.example.myboostproject.data.AppHelper;
import com.example.myboostproject.data.MovieDetaillInfo;
import com.example.myboostproject.data.MovieDetaillList;
import com.example.myboostproject.data.MovieInfo;
import com.example.myboostproject.data.MovieList;
import com.example.myboostproject.data.ResponseDetaillInfo;
import com.example.myboostproject.data.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MoviePager extends Fragment {
    ArrayList<MovieDetailItem> movieDetailItems;
    ArrayList<MovieSimpleItem> movieItems;
    MovieAdapter movieAdapter;
    ViewGroup rootView;

    Fragment getFragment2; //영화 상세보기 프래그먼트를 각각 담아줄 프래그먼트입니다
    Fragment getFragment3;
    Fragment getFragment4;
    Fragment getFragment5;
    Fragment getFragment6;

    Fragment allCommentFragment2; // 영화 한줄평모두보기 프래그먼트를 각각 담아줄 프래그먼트입니다
    Fragment allCommentFragment3;
    Fragment allCommentFragment4;
    Fragment allCommentFragment5;
    Fragment allCommentFragment6;

    Fragment getwriteFragment; //영화 한줄평쓰기 프래그먼트를 각각 담아줄 프래그먼트입니다
    Fragment getwriteFragment2;
    Fragment getwriteFragment3;
    Fragment getwriteFragment4;
    Fragment getwriteFragment5;

    ArrayList<Fragment> fragments;
    ViewPager viewpager;
    MoviePagerFragment moviepagerfragment;
    MovieDetailFragment movieDetailFragment;

    static int position = -1;

    onCommendmovie callback;  // 메인액티비티의 함수를 콜백으로가져오기위한 인터페이스 참조변수선언입니다

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof onCommendmovie) {
            callback = (onCommendmovie) context; //메인액티비티를 인터페이스 타입으로 변환합니다
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
        rootView = (ViewGroup) inflater.inflate(R.layout.moviepager, container, false);
        viewpager = rootView.findViewById(R.id.moviepager);
        int status = NetworkStatus.getConnectivityStatus(getContext());


        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        }

        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            Toast.makeText(getContext(), "인터넷에 연결되었습니다\n데이터베이스에 데이터를 저장합니다.", Toast.LENGTH_SHORT).show();
            AppHelper2.createTable("outline");
            AppHelper2.createTable("detaillmovie");
            AppHelper2.createTable("reviewermovie");

            requestMovieList2(); // 영화상세보기 데이터들을 서버와 통신하고 위에 선언되어있는 프래그먼트 각각에 대입해주는 역할을합니다
            requestMovieList3();
            requestMovieList4();
            requestMovieList5();
            requestMovieList6();
            requestMovieList(); // 뷰페이저의 영화목록 데이터갱신과 영화상세화면의 프래그먼트들을 초기화 해주는 역할을 합니다
        }

        viewpager.setClipToPadding(false);

        viewpager.setPadding(0, 0, 40, 0);
        viewpager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -15);

        viewpager.setOffscreenPageLimit(5);

        movieAdapter = new MovieAdapter(getChildFragmentManager());

        viewpager.setAdapter(movieAdapter);

        if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(getContext(), "인터넷 연결이 끊어졌습니다\n데이터베이스에서 데이터를 로딩합니다.", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 5; i++) {
                getPosition();

                MoviePagerFragment moviePagerFragment = new MoviePagerFragment();
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

                moviePagerFragment.MovieDetailFragment(movieDetailFragment);

                movieAdapter.addItem(moviePagerFragment);
            }
            viewpager.setAdapter(movieAdapter);
        }

        return rootView;
    }

    public static int getPosition(){
        position += 1;
        return position;
    }

    private void requestMovieList() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            processResponse(response);
                        }
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


    private void processResponse(String response) {
        Gson gson = new Gson();

        fragments = new ArrayList<>();
        fragments.add(getFragment2);
        fragments.add(getFragment3);
        fragments.add(getFragment4);
        fragments.add(getFragment5);
        fragments.add(getFragment6);

        ArrayList<Fragment> allCommentListFragment = new ArrayList<>();
        allCommentListFragment.add(allCommentFragment2);
        allCommentListFragment.add(allCommentFragment3);
        allCommentListFragment.add(allCommentFragment4);
        allCommentListFragment.add(allCommentFragment5);
        allCommentListFragment.add(allCommentFragment6);

        callback.getFragment(allCommentListFragment.get(0));
        callback.getFragment2(allCommentListFragment.get(1));
        callback.getFragment3(allCommentListFragment.get(2));
        callback.getFragment4(allCommentListFragment.get(3));
        callback.getFragment5(allCommentListFragment.get(4));

        ArrayList<Fragment> getwriteListFragment = new ArrayList<>();
        getwriteListFragment.add(getwriteFragment);
        getwriteListFragment.add(getwriteFragment2);
        getwriteListFragment.add(getwriteFragment3);
        getwriteListFragment.add(getwriteFragment4);
        getwriteListFragment.add(getwriteFragment5);

        callback.getWriteFragment(getwriteListFragment.get(0));
        callback.getWriteFragment2(getwriteListFragment.get(1));
        callback.getWriteFragment3(getwriteListFragment.get(2));
        callback.getWriteFragment4(getwriteListFragment.get(3));
        callback.getWriteFragment5(getwriteListFragment.get(4));


        ResponseInfo info = gson.fromJson(response, ResponseInfo.class);
        if (info.code == 200) {
            MovieList movieList = gson.fromJson(response, MovieList.class);
            for (int i = 0; i < movieList.result.size(); i++) {
                MovieInfo movieInfo = movieList.result.get(i);
                if (movieInfo != null) {
                    AppHelper2.insertData(movieInfo.id, movieInfo.title, movieInfo.reservation_rate, movieInfo.grade, movieInfo.image);

                    movieItems = new ArrayList<>();
                    movieItems.add(new MovieSimpleItem("\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + movieInfo.id + "." + movieInfo.title, movieInfo.image, movieInfo.reservation_rate, movieInfo.grade));
                    movieItems.add(new MovieSimpleItem(movieInfo.id + "." + movieInfo.title, movieInfo.image, movieInfo.reservation_rate, movieInfo.grade));
                    movieItems.add(new MovieSimpleItem(movieInfo.id + "." + movieInfo.title, movieInfo.image, movieInfo.reservation_rate, movieInfo.grade));
                    movieItems.add(new MovieSimpleItem(movieInfo.id + "." + movieInfo.title, movieInfo.image, movieInfo.reservation_rate, movieInfo.grade));
                    movieItems.add(new MovieSimpleItem("\t" + "\t" + movieInfo.id + "." + movieInfo.title, movieInfo.image, movieInfo.reservation_rate, movieInfo.grade));

                    moviepagerfragment = new MoviePagerFragment();

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("movieSimpleItem", movieItems.get(i));

                    moviepagerfragment.setArguments(bundle);

                    movieAdapter.addItem(moviepagerfragment);

                    moviepagerfragment.MovieDetailFragment(fragments.get(i));
                }
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    private void requestMovieList2() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFragment2 = processResponse2(response);
                        movieDetailFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1&limit=max");
                        movieDetailFragment.getInt("1"); //뷰페이저에 보여지는 각각의 영화상세버튼을 클릭할시 그에 맞게 영화상세화면을 띄워주는 역할을 하게됩니다
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


    private Fragment processResponse2(String response) {
        Gson gson = new Gson();

        ResponseDetaillInfo info = gson.fromJson(response, ResponseDetaillInfo.class);
        if (info.code == 200) {
            MovieDetaillList movieDetaillList = gson.fromJson(response, MovieDetaillList.class);
            if (movieDetaillList != null) {
                MovieDetaillInfo movieDetaillInfo = movieDetaillList.result.get(0);

                AppHelper2.detaillInsertData(movieDetaillInfo.title, movieDetaillInfo.date,movieDetaillInfo.genre,movieDetaillInfo.duration,movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike);

                movieDetailItems = new ArrayList<>();
                movieDetailItems.add(new MovieDetailItem(movieDetaillInfo.image,movieDetaillInfo.title, movieDetaillInfo.date + "개봉" + "\n" + movieDetaillInfo.genre + "/" + movieDetaillInfo.duration + "분", movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike, movieDetaillInfo.photos, movieDetaillInfo.videos));

                movieDetailFragment = new MovieDetailFragment();

                AllCommentViewFragment allCommentViewFragment = new AllCommentViewFragment();
                allCommentViewFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1&length=20");
                allCommentViewFragment.getStringdata("1");

                WriteFragment writeFragment = new WriteFragment();
                getwriteFragment = writeFragment;
                writeFragment.getStringData("1"); //각각의 영화 한줄평과 한줄평모두보기 를 영화에맞게 보여주기위한 역할을 합니다

                Bundle bundle = new Bundle();
                bundle.putSerializable("movieDetailItem", movieDetailItems.get(0));
                bundle.putString("title", movieDetaillInfo.title);
                bundle.putFloat("totalrating", movieDetaillInfo.audience_rating);
                bundle.putString("writetitle", movieDetaillInfo.title);

                writeFragment.setArguments(bundle);
                allCommentViewFragment.setArguments(bundle);
                movieDetailFragment.setArguments(bundle);
                allCommentFragment2 = allCommentViewFragment;
            }
        }
        return movieDetailFragment;
    }

    private void requestMovieList3() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=2";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFragment3 = processResponse3(response);
                        movieDetailFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=2&limit=max");
                        movieDetailFragment.getInt("2");

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


    private Fragment processResponse3(String response) {
        Gson gson = new Gson();


        ResponseDetaillInfo info = gson.fromJson(response, ResponseDetaillInfo.class);
        if (info.code == 200) {
            MovieDetaillList movieDetaillList = gson.fromJson(response, MovieDetaillList.class);
            if (movieDetaillList != null) {
                MovieDetaillInfo movieDetaillInfo = movieDetaillList.result.get(0);

                AppHelper2.detaillInsertData(movieDetaillInfo.title, movieDetaillInfo.date,movieDetaillInfo.genre,movieDetaillInfo.duration,movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike);

                movieDetailItems = new ArrayList<>();
                movieDetailItems.add(new MovieDetailItem(movieDetaillInfo.image, movieDetaillInfo.title, movieDetaillInfo.date + "개봉" + "\n" + movieDetaillInfo.genre + "/" + movieDetaillInfo.duration + "분", movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike,movieDetaillInfo.photos, movieDetaillInfo.videos));

                movieDetailFragment = new MovieDetailFragment();

                AllCommentViewFragment allCommentViewFragment = new AllCommentViewFragment();
                allCommentViewFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=2&length=20");
                allCommentViewFragment.getStringdata("2");

                WriteFragment writeFragment = new WriteFragment();
                getwriteFragment2 = writeFragment;
                writeFragment.getStringData("2");

                Bundle bundle = new Bundle();
                bundle.putSerializable("movieDetailItem", movieDetailItems.get(0));
                bundle.putString("title", movieDetaillInfo.title);
                bundle.putFloat("totalrating", movieDetaillInfo.audience_rating);
                bundle.putString("writetitle", movieDetaillInfo.title);

                writeFragment.setArguments(bundle);
                allCommentViewFragment.setArguments(bundle);
                movieDetailFragment.setArguments(bundle);

                allCommentFragment3 = allCommentViewFragment;
            }
        }
        return movieDetailFragment;
    }

    private void requestMovieList4() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=3";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFragment4 = processResponse4(response);
                        movieDetailFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=3&limit=max");
                        movieDetailFragment.getInt("3");
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


    private Fragment processResponse4(String response) {
        Gson gson = new Gson();


        ResponseDetaillInfo info = gson.fromJson(response, ResponseDetaillInfo.class);
        if (info.code == 200) {
            MovieDetaillList movieDetaillList = gson.fromJson(response, MovieDetaillList.class);
            if (movieDetaillList != null) {
                MovieDetaillInfo movieDetaillInfo = movieDetaillList.result.get(0);

                AppHelper2.detaillInsertData(movieDetaillInfo.title, movieDetaillInfo.date,movieDetaillInfo.genre,movieDetaillInfo.duration,movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike);

                movieDetailItems = new ArrayList<>();
                movieDetailItems.add(new MovieDetailItem(movieDetaillInfo.image, movieDetaillInfo.title, movieDetaillInfo.date + "개봉" + "\n" + movieDetaillInfo.genre + "/" + movieDetaillInfo.duration + "분", movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike,movieDetaillInfo.photos, movieDetaillInfo.videos));

                movieDetailFragment = new MovieDetailFragment();

                AllCommentViewFragment allCommentViewFragment = new AllCommentViewFragment();
                allCommentViewFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=3&length=20");
                allCommentViewFragment.getStringdata("3");

                WriteFragment writeFragment = new WriteFragment();
                getwriteFragment3 = writeFragment;
                writeFragment.getStringData("3");

                Bundle bundle = new Bundle();
                bundle.putSerializable("movieDetailItem", movieDetailItems.get(0));
                bundle.putString("title", movieDetaillInfo.title);
                bundle.putFloat("totalrating", movieDetaillInfo.audience_rating);
                bundle.putString("writetitle", movieDetaillInfo.title);

                writeFragment.setArguments(bundle);
                allCommentViewFragment.setArguments(bundle);
                movieDetailFragment.setArguments(bundle);

                allCommentFragment4 = allCommentViewFragment;
            }
        }
        return movieDetailFragment;
    }

    private void requestMovieList5() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=4";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFragment5 = processResponse5(response);
                        movieDetailFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=4&limit=max");
                        movieDetailFragment.getInt("4");
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


    private Fragment processResponse5(String response) {
        Gson gson = new Gson();


        ResponseDetaillInfo info = gson.fromJson(response, ResponseDetaillInfo.class);
        if (info.code == 200) {
            MovieDetaillList movieDetaillList = gson.fromJson(response, MovieDetaillList.class);
            if (movieDetaillList != null) {
                MovieDetaillInfo movieDetaillInfo = movieDetaillList.result.get(0);

                AppHelper2.detaillInsertData(movieDetaillInfo.title, movieDetaillInfo.date,movieDetaillInfo.genre,movieDetaillInfo.duration,movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike);

                movieDetailItems = new ArrayList<>();
                movieDetailItems.add(new MovieDetailItem(movieDetaillInfo.image, movieDetaillInfo.title, movieDetaillInfo.date + "개봉" + "\n" + movieDetaillInfo.genre + "/" + movieDetaillInfo.duration + "분", movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike,movieDetaillInfo.photos, movieDetaillInfo.videos));

                movieDetailFragment = new MovieDetailFragment();

                AllCommentViewFragment allCommentViewFragment = new AllCommentViewFragment();
                allCommentViewFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=4&length=20");
                allCommentViewFragment.getStringdata("4");

                WriteFragment writeFragment = new WriteFragment();
                getwriteFragment4 = writeFragment;
                writeFragment.getStringData("4");

                Bundle bundle = new Bundle();
                bundle.putSerializable("movieDetailItem", movieDetailItems.get(0));
                bundle.putString("title", movieDetaillInfo.title);
                bundle.putFloat("totalrating", movieDetaillInfo.audience_rating);
                bundle.putString("writetitle", movieDetaillInfo.title);

                writeFragment.setArguments(bundle);
                allCommentViewFragment.setArguments(bundle);
                movieDetailFragment.setArguments(bundle);

                allCommentFragment5 = allCommentViewFragment;
            }
        }
        return movieDetailFragment;
    }

    private void requestMovieList6() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=5";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFragment6 = processResponse6(response);
                        movieDetailFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=5&limit=max");
                        movieDetailFragment.getInt("5");
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


    private Fragment processResponse6(String response) {
        Gson gson = new Gson();


        ResponseDetaillInfo info = gson.fromJson(response, ResponseDetaillInfo.class);
        if (info.code == 200) {
            MovieDetaillList movieDetaillList = gson.fromJson(response, MovieDetaillList.class);
            if (movieDetaillList != null) {
                MovieDetaillInfo movieDetaillInfo = movieDetaillList.result.get(0);

                AppHelper2.detaillInsertData(movieDetaillInfo.title, movieDetaillInfo.date,movieDetaillInfo.genre,movieDetaillInfo.duration,movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike);

                movieDetailItems = new ArrayList<>();
                movieDetailItems.add(new MovieDetailItem(movieDetaillInfo.image, movieDetaillInfo.title, movieDetaillInfo.date + "개봉" + "\n" + movieDetaillInfo.genre + "/" + movieDetaillInfo.duration + "분", movieDetaillInfo.synopsis, movieDetaillInfo.director, movieDetaillInfo.actor, movieDetaillInfo.reservation_rate, movieDetaillInfo.reservation_grade, movieDetaillInfo.audience_rating, movieDetaillInfo.audience, movieDetaillInfo.like, movieDetaillInfo.dislike,movieDetaillInfo.photos, movieDetaillInfo.videos));

                movieDetailFragment = new MovieDetailFragment();

                AllCommentViewFragment allCommentViewFragment = new AllCommentViewFragment();
                allCommentViewFragment.requestCommentList("http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=5&length=20");
                allCommentViewFragment.getStringdata("5");

                WriteFragment writeFragment = new WriteFragment();
                getwriteFragment5 = writeFragment;
                writeFragment.getStringData("5");

                Bundle bundle = new Bundle();
                bundle.putSerializable("movieDetailItem", movieDetailItems.get(0));
                bundle.putString("title", movieDetaillInfo.title);
                bundle.putFloat("totalrating", movieDetaillInfo.audience_rating);
                bundle.putString("writetitle", movieDetaillInfo.title);


                writeFragment.setArguments(bundle);
                allCommentViewFragment.setArguments(bundle);
                movieDetailFragment.setArguments(bundle);

                allCommentFragment6 = allCommentViewFragment;
            }
        }
        return movieDetailFragment;
    }

    private class MovieAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MovieAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment fragment) {
            items.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public float getPageWidth(int position) {
            return (0.9f);
        }

        @Override
        public int getCount() {
            return items.size();
        }


    }

}
