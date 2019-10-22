package com.example.myboostproject.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;

import com.example.myboostproject.MoviePager.MoviePagerFragment;

import java.util.ArrayList;

public class AppHelper2 {
    private static final String TAG = "AppHelper";

    public static SQLiteDatabase database;

    private static String createTableOutlineSql = "create table if not exists outline" +
            "(" +
            "   _id integer PRIMARY KEY autoincrement, "+
            "   id integer,"+
            "   title text,"+
            "   title_eng text,"+
            "   dataValue text,"+
            "   user_rating float,"+
            "   audience_rating float,"+
            "   reviewer_rating float,"+
            "   reservation_rate float,"+
            "   reservation_grade integer,"+
            "   grade integer,"+
            "   thumb text, "+
            "   image text"+
            ")";

    private static String createTableDetailMovieSql = "create table if not exists detaillmovie" +
            "(" +
            "  _id integer PRIMARY KEY autoincrement, "+
            "   title text,"+
            "   id integer,"+
            "   dataValue text,"+
            "   user_rating float,"+
            "   audience_rating float,"+
            "   reviewer_rating float,"+
            "   reservation_rate float,"+
            "   reservation_grade integer,"+
            "   grade integer,"+
            "   thumb text,"+
            "   image text,"+
            "   photos text,"+
            "   videos text,"+
            "   outlinks text,"+
            "   genre text,"+
            "   duration integer,"+
            "   audience integer,"+
            "   synopsis text,"+
            "   director text,"+
            "   actor text,"+
            "   upLike integer,"+
            "   dislike integer"+
            ")";

    private static String createTableReviewerMovieSql = "create table if not exists reviewermovie"+
            "(" +
            "  _id integer PRIMARY KEY autoincrement, "+
            "   id integer,"+
            "   writer text,"+
            "   movieId integer,"+
            "   dataValue text,"+
            "   timestamp integer,"+
            "   rating float,"+
            "   contents text,"+
            "   recommend integer"+
            ")";

    public static void openDatabase(Context context, String databaseName){
        println("openDatabase 호출됨.");

        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
                println("데이터베이스" + databaseName + "오픈됨");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName){
        println("createTable 호출됨 :" + tableName);

        if(database != null){
            if(tableName.equals("outline")){
                database.execSQL(createTableOutlineSql);
                println("outline 테이블생성요청됨.");

            }else if(tableName.equals("detaillmovie")){
                database.execSQL(createTableDetailMovieSql);
                println("detailmovie 테이블생성요청됨.");

            }else if(tableName.equals("reviewermovie")){
                database.execSQL(createTableReviewerMovieSql);
                println("reviewermovie 테이블생성요청됨.");

            }
            else {
                println("데이터베이스를 먼저 오픈하세요.");
            }
        }
    }

    public static void insertData(int id,String title,float reservation_rate,int grade,String image){
        println("insertData() 호출됨.");

        if(database != null){
            String sql = "insert or ignore into  outline(id,title,reservation_rate,grade,image) Values(?, ?, ?, ?, ?)";

            Object[] params = {id,title,reservation_rate,grade,image};
            database.execSQL(sql,params);

            println("insertData ViewPager Data저장됨.");
        }
    }

    public static void detaillInsertData(String title,String dataValue,String genre,int duration,
                                         String synopsis,String director,String actor,Float reservation_rate,
                                         int reservation_grade,Float audience_rating,int audience,
                                         int upLike,int dislike) {
        println("detaillInsertData() 호출됨");

        if(database != null){
            String sql = "insert or ignore into detaillmovie2(title,dataValue,genre,duration,synopsis," +
                    "director,actor,reservation_rate,reservation_grade," +
                    "audience_rating,audience,upLike,dislike) Values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Object[] params = {title,dataValue,genre,duration,synopsis,director,actor,reservation_rate,reservation_grade,audience_rating,audience,upLike,dislike};
            database.execSQL(sql,params);

            println("detaillInsertData() detaill Data저장됨.");
        }
    }

    public static void reviewerInsertData(String writer,String contents,Float rating){
        println("reviewerInsertData() 호출됨.");

        if(database != null){
            String sql = "insert or ignore into reviewermovie(writer,contents,rating) Values(?,?,?)";

            Object[] params = {writer,contents,rating};
            database.execSQL(sql,params);

            println("reviewerInsertData() 저장됨.");
        }
    }

    public static ArrayList<ViewPagerData> selectData(){
        println("selectData 호출됨.");
        ArrayList<ViewPagerData> list = new ArrayList<>();
        if(database != null){
            String sql ="select id,title,reservation_rate,grade,image from "+"outline";
            Cursor cursor = database.rawQuery(sql,null);

            for(int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();
                ViewPagerData viewPagerData = new ViewPagerData();
                viewPagerData.title = cursor.getInt(0) +"."+ cursor.getString(1);
                viewPagerData.rating = cursor.getFloat(2);
                viewPagerData.grade = cursor.getInt(3);
                viewPagerData.image = cursor.getString(4);
                list.add(viewPagerData);
            }
            cursor.close();
        }
        return list;
    }

    public static ArrayList<DetaillMovieData> detaillSelectData(){
        println("detaillSelectData 호출됨.");
        ArrayList<DetaillMovieData> list = new ArrayList<>();
        if(database != null){
            String sql = "select DISTINCT title,dataValue,genre,duration,synopsis,director,actor,reservation_rate,reservation_grade,audience_rating,audience,upLike,dislike from "+"detaillmovie";
            Cursor cursor = database.rawQuery(sql,null);

            for(int i = 0; i < cursor.getCount(); i++){
                cursor.moveToNext();

                DetaillMovieData detaillMovieData = new DetaillMovieData();
                detaillMovieData.title = cursor.getString(0);
                detaillMovieData.dataValue = cursor.getString(1)+"개봉" + "\n" +cursor.getString(2)+ "/" + cursor.getInt(3) + "분";
                detaillMovieData.synopsis = cursor.getString(4);
                detaillMovieData.director = cursor.getString(5);
                detaillMovieData.actor = cursor.getString(6);
                detaillMovieData.reservation_rate = cursor.getFloat(7);
                detaillMovieData.reservation_grade = cursor.getInt(8);
                detaillMovieData.audience_rating = cursor.getFloat(9);
                detaillMovieData.audience = cursor.getInt(10);
                detaillMovieData.upLike = cursor.getInt(11);
                detaillMovieData.dislike = cursor.getInt(12);

                list.add(detaillMovieData);
            }
            cursor.close();
        }
       return list;
    }

    public static ArrayList<ReviewerData> reviewerSelectData(){
        println("reviewerSelectData() 호출됨.");
        ArrayList<ReviewerData>list = new ArrayList<>();

        if(database != null){
            String sql = "select writer,contents,rating from "+"reviewermovie";
            Cursor cursor = database.rawQuery(sql,null);

            for(int i = 0; i<cursor.getCount(); i++){
                cursor.moveToNext();
                ReviewerData reviewerData = new ReviewerData();

                reviewerData.writer = cursor.getString(0);
                reviewerData.contents = cursor.getString(1);
                reviewerData.rating = cursor.getFloat(2);

                list.add(reviewerData);
            }
            cursor.close();
        }
        return list;
    }

    public static void println(String data){
        Log.d(TAG,data);
    }
}
