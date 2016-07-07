package com.upstairs.indianclassic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nirhart.parallaxscroll.views.ParallaxListView;

public class search extends AppCompatActivity {

 /*   //everytime text in the edit text box is changed then a query is sent ot DB to fetch all thee SubClassifications with those char sequence.
    List<String> tempClassifications = new ArrayList<String>();
    List<String> tempSubClassifications = new ArrayList<String>();
    private void getSearchFromDb(String foo){
        tempClassifications.clear();
        tempSubClassifications.clear();
        CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cdbhelper.getReadableDatabase();
        String[] projection={
                CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION,
                CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION
        };
        String selection = CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION+" LIKE ?";
        String[] selectionArgs = {"%"+foo+"%"};
        String sortOrder = CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION + " DESC";
        Cursor c = db.query(true, CardDBContract.CardTable.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder,null);

        if(c!=null){

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                String temp = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION));
                    if(!tempSubClassifications.contains(temp) && !temp.equals("Famous Dogs") && !temp.equals("Diet")){  //Filtering out Famous Dog and Diet from search results
                        tempSubClassifications.add(temp);
                        temp = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION));
                        tempClassifications.add(temp);
                    }


            }
            db.close();
        }
    }


    ParallaxListView breeds_list;
    private breed_list_view_adapter breeds_adapter;
*/
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


//        breeds_list = (ParallaxListView) findViewById(com.upstairs.dogcare.R.id.breed_list);
 //       breeds_adapter = new breed_list_view_adapter(this);



/*
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


                breeds_adapter.clear();
                breeds_adapter.addAll(tempSubClassifications);
                breeds_list.setAdapter(breeds_adapter);
                }else{
                    breeds_adapter.clear();
                    tempSubClassifications.clear();
                    breeds_adapter.addAll(tempSubClassifications);
                    breeds_list.setAdapter(breeds_adapter);
                }

                breeds_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      //intent to card Screen when any search result is selected

                        Intent intent = new Intent(search.this, CardsActivity.class);
                        intent.putExtra("Category",tempClassifications.get(position));
                        intent.putExtra("Breed",breeds_adapter.getItem(position));

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                // the context of the activity
                                search.this,


                                new Pair<View, String>(view.findViewById(com.upstairs.dogcare.R.id.img),
                                        getString(com.upstairs.dogcare.R.string.transition_name_image))

                        );
                        ActivityCompat.startActivity(search.this, intent, options.toBundle());

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

*/
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
