package com.example.android.news1;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsFragmentPageAdapter extends FragmentPagerAdapter {

    private String[] title = {"Education","Tech","Sports","Market"};

    public NewsFragmentPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
            return new EducationFragment();
        else if(i==1)
            return new TechnologyFragment();
        else if(i==2)
            return new SportsFragment();
        else
            return new MarketFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
