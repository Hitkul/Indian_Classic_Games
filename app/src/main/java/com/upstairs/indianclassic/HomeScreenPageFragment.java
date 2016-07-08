package com.upstairs.indianclassic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hitkul on 8/7/16.
 */
public class HomeScreenPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        int resID = getArguments().getInt("thumbnail");
        String name = getArguments().getString("game_name");
        final String image_list = getArguments().getString("image_list");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.card_layout, container, false);
        ImageView thumbnail = (ImageView) rootView.findViewById(R.id.img);
        Glide.with(getActivity()).load(resID).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(thumbnail);

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent = new Intent(getActivity(), ScreenSliderLayoutActivity.class);
                newintent.putExtra("images", image_list);
                startActivity(newintent);
            }
        });

        Typeface font_name = Typeface.createFromAsset(getActivity().getAssets(), "PermanentMarker.ttf");
        TextView game_name = (TextView) rootView.findViewById(R.id.name);
        game_name.setText(name);
        game_name.setTypeface(font_name);
        if (name.equalsIgnoreCase("raja mantri chor sipahi"))
            game_name.setTextSize(27);

        return rootView;
    }

}
