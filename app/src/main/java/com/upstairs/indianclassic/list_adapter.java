package com.upstairs.indianclassic;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by hitkul on 2/7/16.
 */
public class list_adapter extends ArrayAdapter<Card> {

    private Activity context;

    list_adapter(Activity context){
        super(context,R.layout.card_layout);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.card_layout, null, true);

        ImageView img = (ImageView) view.findViewById(R.id.img);
        TextView name = (TextView) view.findViewById(R.id.name);

        Resources res = context.getResources();
        String mDrawableName = getItem(position).getImage().replaceAll(" ", "_").toLowerCase();
        int resID = res.getIdentifier(mDrawableName, "drawable", context.getPackageName());

        Glide.with(context).load(resID).into(img);

        name.setText(toTitleCase(getItem(position).getTitle()));

        return view;
    }

    private String toTitleCase(String foo) {
        String [] arr = foo.split(" ");
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<arr.length;i++){
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
