package com.example.myboostproject;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myboostproject.DataBase.AppHelper2;
import com.example.myboostproject.MovieCommentandWriteFragment.WriteFragment;
import com.example.myboostproject.MovieDetaill.MovieDetailFragment;
import com.example.myboostproject.MovieInterFace.onCommendmovie;
import com.example.myboostproject.MoviePager.MoviePager;
import com.example.myboostproject.MoviePager.MoviePagerFragment;
import com.example.myboostproject.NetworkConnect.NetworkStatus;
import com.example.myboostproject.data.AppHelper;

/**
 * 미천한 실력으로 열심히 코드를 작성하고 안드로이드를 열심히 공부중에 있는 직장인입니다!
 * <p>
 * 독학으로 공부하는 저에겐 값진 조언은 정말 저에게 큰힘이 됩니다!
 * <p>
 * 깔끔하지 않은 코드 리뷰해주시는 리뷰어님들 정말 감사드립니다!
 * <p>
 * 잘부탁드리겠습니다
 */

public class MainActivity extends AppCompatActivity
        implements onCommendmovie,NavigationView.OnNavigationItemSelectedListener {

    LinearLayout page;

    Animation translateUp;
    Animation translateDown;

    Button button2;
    ImageButton button3;
    ImageButton button4;
    ImageButton button5;
    int status;
    int i;
    boolean isPageOpen = false;

    MoviePager moviePager;
    MoviePagerFragment moviePagerFragment;
    Fragment writeFragment;
    Fragment writeFragment2;
    Fragment writeFragment3;
    Fragment writeFragment4;
    Fragment writeFragment5;

    Fragment getFragment;
    Fragment getFragment2;
    Fragment getFragment3;
    Fragment getFragment4;
    Fragment getFragment5;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page =findViewById(R.id.page);


        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        moviePager = new MoviePager();
        moviePagerFragment = new MoviePagerFragment();
        writeFragment = new WriteFragment();

        AppHelper2.openDatabase(getApplicationContext(),"movie");

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, moviePager).commit();//뷰페이저를 처음화면으로 띄우기위한 코드입니다

        translateUp = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        translateDown = AnimationUtils.loadAnimation(this,R.anim.translate_down);

        SlidingAnimationListener listener = new SlidingAnimationListener();
        translateUp.setAnimationListener(listener);
        translateDown.setAnimationListener(listener);

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    page.startAnimation(translateUp);
                }else{
                    page.startAnimation(translateDown);
                    page.setVisibility(View.VISIBLE);
                }

            }
        });

        button3 = findViewById(R.id.button3); //예매율
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    page.startAnimation(translateUp);
                    i = 3;
                }else{
                    page.startAnimation(translateDown);
                    page.setVisibility(View.VISIBLE);
                }

            }
        });

        button4 = findViewById(R.id.button4); //큐레이션
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    page.startAnimation(translateUp);
                    i = 4;
                }else{
                    page.startAnimation(translateDown);
                    page.setVisibility(View.VISIBLE);
                }

            }
        });

        button5 = findViewById(R.id.button5); //상영예정
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    page.startAnimation(translateUp);
                    i = 5;
                }else{
                    page.startAnimation(translateDown);
                    page.setVisibility(View.VISIBLE);
                }

            }
        });


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.movielist);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    class SlidingAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen){
                page.setVisibility(View.INVISIBLE);
                if(i%3 == 0){
                    button2.setText("예매율순");
                }else if(i%3 == 1){
                    button2.setText("큐레이션");
                }else if(i%3 == 2){
                    button2.setText("상영예정");
                }

                isPageOpen =false;
            }else{
                isPageOpen =true;
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public void replaceInfoFragment(Fragment moviedetailfragment) { //영화목록에서 상세화면을 클릭할경우 호출되는 메소드입니다.클릭하면 영화상세화면이뜹니다
        toolbar.setTitle("영화상세");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, moviedetailfragment).addToBackStack(null).commit();
    }

    public void photoViewFragment(Fragment photoFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,photoFragment).addToBackStack(null).commit();
    }

    public void WriteInfoReplace(String j){
        toolbar.setTitle("한줄평작성");
        switch (j) {
            case "1":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, writeFragment).addToBackStack(null).commit();
            break;

            case "2":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, writeFragment2).addToBackStack(null).commit();
            break;

            case "3":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, writeFragment3).addToBackStack(null).commit();
            break;

            case "4":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, writeFragment4).addToBackStack(null).commit();
            break;

            case "5":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, writeFragment5).addToBackStack(null).commit();
            break;
        }
    }

    public void AllCommentInfoReplace(String i){
        toolbar.setTitle("한줄평목록");
        switch (i) {
            case "1":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getFragment).addToBackStack(null).commit();
            break;

            case "2":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getFragment2).addToBackStack(null).commit();
            break;

            case "3":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getFragment3).addToBackStack(null).commit();
            break;

            case "4":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getFragment4).addToBackStack(null).commit();
            break;

            case "5":
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, getFragment5).addToBackStack(null).commit();
            break;
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toolbar.setTitle(R.string.movielist);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movie_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_0) {
            toolbar.setTitle(R.string.movielist);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, moviePager).commit();

        } else if (id == R.id.nav_1) {

        } else if (id == R.id.nav_2) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getFragment(Fragment getFragment){this.getFragment =getFragment;}

    public void getFragment2(Fragment getFragment){this.getFragment2 =getFragment;}

    public void getFragment3(Fragment getFragment){this.getFragment3 =getFragment;}

    public void getFragment4(Fragment getFragment){this.getFragment4 =getFragment;}

    public void getFragment5(Fragment getFragment){this.getFragment5 =getFragment;}

    public void getWriteFragment(Fragment writeFragment){this.writeFragment = writeFragment;}

    public void getWriteFragment2(Fragment writeFragment){this.writeFragment2 = writeFragment;}

    public void getWriteFragment3(Fragment writeFragment){this.writeFragment3 = writeFragment;}

    public void getWriteFragment4(Fragment writeFragment){this.writeFragment4 = writeFragment;}

    public void getWriteFragment5(Fragment writeFragment){this.writeFragment5 = writeFragment;}

}
