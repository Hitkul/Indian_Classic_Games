package com.upstairs.indianclassic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {

  //everytime text in the edit text box is changed then a query is sent ot DB to fetch all thee SubClassifications with those char sequence.
    List<String> tempList = new ArrayList<String>();
    List<String> tempSubSubClassifications = new ArrayList<String>();
    private void getSearchFromDb(String foo){
        tempList.clear();
        tempSubSubClassifications.clear();
        CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cdbhelper.getReadableDatabase();
        String[] projection={
                CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION,
                CardContract.CardTable.COLUMN_NAME_CARD_LIST
        };
        String selection = CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION+" LIKE ?";
        String[] selectionArgs = {"%"+foo+"%"};
        String sortOrder = CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION + " DESC";
        Cursor c = db.query(true, CardContract.CardTable.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder,null);

        if(c!=null){

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                String temp = c.getString(c.getColumnIndexOrThrow(CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION));
                    if(!tempSubSubClassifications.contains(temp)){  //Filtering out Famous Dog and Diet from search results
                        tempSubSubClassifications.add(temp);
                        temp = c.getString(c.getColumnIndexOrThrow(CardContract.CardTable.COLUMN_NAME_CARD_LIST));
                        tempList.add(temp);
                    }


            }
            db.close();
        }
    }


    ParallaxListView list;
    private search_list_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        final ImageButton clearButton = (ImageButton) findViewById(R.id.clearButton);
        final EditText searchBar = (EditText) findViewById(R.id.search_bar);
        clearButton.setVisibility(View.GONE);


        list = (ParallaxListView) findViewById(R.id.search_list);
        adapter = new search_list_adapter(this);




        assert searchBar != null;
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence string, int start, int before,
                                      int count) {
                if (string.toString().length() > 0) {   //Setting clear button visibility
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.GONE);
                }

                if(string.toString().replaceAll("\n","").trim().length() != 0){ //formatting text from search bar i.e replace return characters etc
                    getSearchFromDb(string.toString().replaceAll("\n","").trim());
                    adapter.clear();
                    adapter.addAll(tempSubSubClassifications);
                    list.setAdapter(adapter);
                }else{
                    adapter.clear();
                    tempSubSubClassifications.clear();
                    adapter.addAll(tempSubSubClassifications);
                    list.setAdapter(adapter);
                }

                 list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      //intent to card Screen when any search result is selected

                        Intent newintent = new Intent(search.this, ScreenSliderLayoutActivity.class);
                        newintent.putExtra("images", tempList.get(position).replaceAll("#",","));
                        startActivity(newintent);
                    }
                });

            }






            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });


        assert clearButton != null;
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                searchBar.setText("");
            }
        });

        assert backButton != null;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);//Slide out transition
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);//Slide out transition
    }
}
