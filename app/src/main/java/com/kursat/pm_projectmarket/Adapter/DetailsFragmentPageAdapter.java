package com.kursat.pm_projectmarket.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kursat.pm_projectmarket.Fragment.DetailsFragment;
import com.kursat.pm_projectmarket.Fragment.CommentFragment;

public class DetailsFragmentPageAdapter extends FragmentPagerAdapter {
    private final String[] tabTitle = new String[] {"Post Detail", "Comments"};
    private final int ITEMS = tabTitle.length;
    Bundle data;
    Context context;
    public DetailsFragmentPageAdapter(@NonNull FragmentManager fm, Context context,Bundle data) {
        super(fm);
        this.context=context;
        this.data=data;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new DetailsFragment();
        if(position == 0) {
            fragment = new DetailsFragment();
        }
        else if(position == 1) {
            fragment = new CommentFragment();
        }
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getCount() { return ITEMS; }

    public CharSequence getPageTitle(int position){ return tabTitle[position]; }
}

