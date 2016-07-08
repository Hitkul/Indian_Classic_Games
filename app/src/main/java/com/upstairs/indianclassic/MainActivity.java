package com.upstairs.indianclassic;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    XMLAssetHandler ParserTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("SKIPPING PARSE", String.valueOf(SharedPreferencesHelper.checkFlag(SharedPreferencesHelper.DB_READY_KEY,getApplicationContext())));
        if(SharedPreferencesHelper.checkFlag(SharedPreferencesHelper.DB_READY_KEY,getApplicationContext())) {//Data has been parsed

            Intent foo = new Intent(getApplicationContext(),Home_Activity.class);
            startActivity(foo);
            finish();

        }else {
            ParserTask = new XMLAssetHandler(getApplicationContext(),MainActivity.this);
            ParserTask.execute();
        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_parser);
    }

    /*@Override
    protected void onStop(){
        super.onStop();
        if(ParserTask!=null){
            ParserTask.cancel(true);
        }
        SharedPreferencesHelper.setFlag(SharedPreferencesHelper.DB_READY_KEY,getApplicationContext(),false);
    }*/
    @Override
    protected void onRestart(){
        super.onRestart();
        CardDBHelper cardDBHelper = new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cardDBHelper.getWritableDatabase();
        cardDBHelper.onUpgrade(db,1,1);

        if(SharedPreferencesHelper.checkFlag(SharedPreferencesHelper.DB_READY_KEY,getApplicationContext())) {
            Intent foo = new Intent(getApplicationContext(),Home_Activity.class);
            startActivity(foo);
            finish();

        }else {
            ParserTask = new XMLAssetHandler(getApplicationContext(),MainActivity.this);
            ParserTask.execute();
        }

    }
}
