package com.example.myboostproject.MovieDetaill;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.example.myboostproject.R;


import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {
    Context context;

    ArrayList<MoviePictureItem> items = new ArrayList<>();

    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view,int position);
    }

    public MovieRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.movierecycler_item,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieRecyclerAdapter.ViewHolder viewHolder, int i) {
        MoviePictureItem item = items.get(i);

        viewHolder.setItem(item);
        viewHolder.imageView2.setImageResource(R.drawable.play_button);

        if(i < items.size()-3){
            viewHolder.imageView2.setVisibility(View.GONE);
        }

        Glide.with(context).asBitmap().load(item.getMovieurl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                viewHolder.imageView.setImageBitmap(resource);
            }
        });

        viewHolder.setOnItemClickListener(listener);

    }

    public void addItem(MoviePictureItem item){
        items.add(item);
    }



    public MoviePictureItem getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView imageView2;
        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView2 = itemView.findViewById(R.id.imageView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posotion = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,v,posotion);
                    }
                }
            });

        }

        public void setItem(MoviePictureItem item){

        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


    }

}
