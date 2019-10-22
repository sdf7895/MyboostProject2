package com.example.myboostproject.PhotoView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myboostproject.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoFragment extends Fragment {
    ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.photofragment, container, false);

        final PhotoView photoView = rootView.findViewById(R.id.photoView);

        if(getArguments() != null) {
            Bundle bundle = getArguments();

            Glide.with(this).asBitmap().load(bundle.getString("photo")).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    photoView.setImageBitmap(resource);
                }
            });


        }


        return rootView;
    }
}
