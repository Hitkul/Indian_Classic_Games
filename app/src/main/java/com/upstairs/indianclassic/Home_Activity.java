package com.upstairs.indianclassic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;
import java.util.List;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ParallaxListView game_list;
    list_adapter adapter;
    private List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String imageList = "first,second,third,fourth,fifth";

        cardList = getCardsFromDb();
        game_list = (ParallaxListView) findViewById(R.id.Home_cards_list);
        adapter = new list_adapter(Home_Activity.this);
        setCards();

        game_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newintent = new Intent(Home_Activity.this, ScreenSliderLayoutActivity.class);
                newintent.putExtra("images", adapter.getItem(position).getList_string());
                startActivity(newintent);
            }
        });






    }

    private List<Card> getCardsFromDb() {
        List<Card> temp = new ArrayList<Card>();
        CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cdbhelper.getReadableDatabase();
        String[] projection={
                CardContract.CardTable.COLUMN_NAME_CLASSIFICATION,
                CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION,
                CardContract.CardTable.COLUMN_NAME_CARD_IMAGE,
                CardContract.CardTable.COLUMN_NAME_CARD_LIST,
        };
        String sortOrder = CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION + " ASC";
        Cursor c = db.query(CardContract.CardTable.TABLE_NAME,projection,null,null,null,null,sortOrder);
        if(c!=null){
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                Card Temp = new Card(c);
                temp.add(Temp);
            }
        }
        db.close();
        return temp;
    }


    private List<Card> getCardsOfCategoryFromDb(String Category) {
        List<Card> temp = new ArrayList<Card>();
        CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cdbhelper.getReadableDatabase();
        String[] projection={
                CardContract.CardTable.COLUMN_NAME_CLASSIFICATION,
                CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION,
                CardContract.CardTable.COLUMN_NAME_CARD_IMAGE,
                CardContract.CardTable.COLUMN_NAME_CARD_LIST,
        };
        String sortOrder = CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION + " ASC";
        String Selection = CardContract.CardTable.COLUMN_NAME_CLASSIFICATION + " = ? ";
        String[] SelectionArgs = { Category
        };
        Cursor c = db.query(CardContract.CardTable.TABLE_NAME,projection,Selection,SelectionArgs,null,null,sortOrder);
        if(c!=null){
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                Card Temp = new Card(c);
                temp.add(Temp);
            }
        }
        db.close();
        return temp;
    }

    private void setCards(){
        adapter.clear();
        adapter.addAll(cardList);
        game_list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.outdoor) {
            cardList.clear();
            cardList = getCardsOfCategoryFromDb("Outdoor");
            setCards();
        } else if (id == R.id.indoor) {
            cardList.clear();
            cardList = getCardsOfCategoryFromDb("Indoor");
            setCards();

        } else if (id == R.id.all) {
            cardList.clear();
            cardList = getCardsFromDb();
            setCards();

        } else if (id == R.id.card) {
            cardList.clear();
            cardList = getCardsOfCategoryFromDb("Card Games");
            setCards();

        } else if (id == R.id.nav_share) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Dog Care");
            String sAux = "https://play.google.com/store/apps/details?id=com.upstairs.indianclassic ";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose One"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
