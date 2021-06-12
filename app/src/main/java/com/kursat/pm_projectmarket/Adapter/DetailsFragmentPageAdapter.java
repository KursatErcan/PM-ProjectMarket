package com.kursat.pm_projectmarket.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kursat.pm_projectmarket.Fragment.PostDetailsFragment;
import com.kursat.pm_projectmarket.Fragment.ProfileCommentFragment;

public class DetailsFragmentPageAdapter extends FragmentPagerAdapter {
    private final String[] tabTitle = new String[] {"Post Detail", "Comments"};
    private final int ITEMS = tabTitle.length;
    Context context;
    public DetailsFragmentPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PostDetailsFragment();
        if(position == 0) {
            fragment = new PostDetailsFragment();
        }
        else if(position == 1) {
            fragment = new ProfileCommentFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() { return ITEMS; }

    public CharSequence getPageTitle(int position){ return tabTitle[position]; }
}

