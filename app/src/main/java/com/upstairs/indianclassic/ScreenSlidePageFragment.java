package com.upstairs.indianclassic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ScreenSlidePageFragment extends Fragment {


    PhotoViewAttacher mAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        int resID = getArguments().getInt("position");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_blank, container, false);
        ImageView herp = (ImageView) rootView.findViewById(R.id.fullscreenimage);
        Glide.with(getActivity()).load(resID).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(herp);
        mAttacher = new PhotoViewAttacher(herp);

        return rootView;
    }
}

