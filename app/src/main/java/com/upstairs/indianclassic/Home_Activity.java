package com.upstairs.indianclassic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    VerticalViewPager verticalViewPager;

    private List<Card> cardList;
    TextView title;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        title = (TextView) findViewById(R.id.title);
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        final FrameLayout tutorial = (FrameLayout) findViewById(R.id.tutorial);
        if (!SharedPreferencesHelper.checkFlag(SharedPreferencesHelper.FIRST_TIME,getApplicationContext())){
            //ON FIRST TIME USE FLAG SHOULD BE FALSE. SET IT TO TRUE IMMEDIATELY.
            tutorial.setVisibility(View.VISIBLE);
            SharedPreferencesHelper.setFlag(SharedPreferencesHelper.FIRST_TIME,getApplicationContext(),true);

        }
        assert tutorial!=null;
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorial.setVisibility(View.GONE);
            }
        });

        cardList = getCardsFromDb();

        verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        setCards();

        title.setText("All Games");


        assert searchButton!=null;
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearch = new Intent(Home_Activity.this, search.class);
                startActivity(toSearch);
                overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("86a5cea36f538f181fed6bb096a637094253b883c03c7aa4571178bf649676af")
                .build();
        mAdView.loadAd(adRequest);

        //loading Ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });


    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("86a5cea36f538f181fed6bb096a637094253b883c03c7aa4571178bf649676af")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    private class HomeScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public HomeScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle newbundle = new Bundle();

            Resources res = getResources();
            String mDrawableName = cardList.get(position).getImage().replaceAll(" ", "_").toLowerCase();
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());

            newbundle.putInt("thumbnail",resID);
            newbundle.putString("game_name",toTitleCase(cardList.get(position).getTitle()));
            newbundle.putString("image_list",cardList.get(position).getList_string());

            HomeScreenPageFragment foo= new HomeScreenPageFragment();
            foo.setArguments(newbundle);
            return foo;
        }
        @Override
        public int getCount() {
            return cardList.size();
        }
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

        HomeScreenSlidePagerAdapter mPagerAdapter = new HomeScreenSlidePagerAdapter(getSupportFragmentManager());
        verticalViewPager.setOffscreenPageLimit(1);
        verticalViewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        } else{
            finish();
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
            title.setText("Outdoor Games");
            setCards();
        } else if (id == R.id.indoor) {
            cardList.clear();
            cardList = getCardsOfCategoryFromDb("Indoor");
            title.setText("Indoor Games");
            setCards();

        } else if (id == R.id.all) {
            cardList.clear();
            cardList = getCardsFromDb();
            title.setText("All Games");
            setCards();

        } else if (id == R.id.card) {
            cardList.clear();
            cardList = getCardsOfCategoryFromDb("Card Games");
            title.setText("Card Games");
            setCards();

        } else if (id == R.id.nav_share) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Classic Indian Games");
            String sAux = "https://play.google.com/store/apps/details?id=com.upstairs.indianclassic ";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose One"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
