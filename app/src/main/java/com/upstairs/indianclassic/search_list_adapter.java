package com.upstairs.indianclassic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by hitkul on 7/7/16.
 */
public class search_list_adapter extends ArrayAdapter<String> {
    private Activity context;

    search_list_adapter(Activity context){
        super(context,R.layout.search_list_item_layout);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.search_list_item_layout, null, true);

        TextView name = (TextView) view.findViewById(R.id.game_name);
        name.setText(toTitleCase(getItem(position)));

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
