package com.upstairs.indianclassic;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenSliderLayoutActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    List<String> durr;
    private List<ImageView> dots;
    List<Integer> Resources = new ArrayList<Integer>();



    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    LinearLayout dotsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent foo = getIntent();

        durr = new ArrayList<String>(Arrays.asList(foo.getStringExtra("images").split(",")));
        for(int i=0;i<durr.size();i++) {
            String t = durr.get(i).toLowerCase().replaceAll(" ", "_");
            int ResId = getResources().getIdentifier(t, "drawable", getPackageName());
            Resources.add(ResId);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpagerlayout);
        dotsLayout = (LinearLayout)findViewById(R.id.dots);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (HackyViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);

        addDots();

        dotsLayout.bringToFront();

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle newbundle = new Bundle();
            newbundle.putInt("position",Resources.get(position));
            ScreenSlidePageFragment foo= new ScreenSlidePageFragment();
            foo.setArguments(newbundle);
            return foo;
        }
        @Override
        public int getCount() {
            return durr.size();
        }

    }


    public void addDots() {
        dots = new ArrayList<>();


        for(int i = 0; i < mPagerAdapter.getCount(); i++) {
            ImageView dot = new ImageView(this);
            if(i==0)
                dot.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            else
                dot.setImageDrawable(getResources().getDrawable(R.drawable.no_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5,0,5,0);
            params.gravity = Gravity.CENTER_VERTICAL;
            dotsLayout.addView(dot, params);

            dotsLayout.bringChildToFront(dot);

            dots.add(dot);
        }
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < mPagerAdapter.getCount(); i++) {
            int drawableId = (i==idx)?(R.drawable.selected):(R.drawable.no_selected);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }



}


