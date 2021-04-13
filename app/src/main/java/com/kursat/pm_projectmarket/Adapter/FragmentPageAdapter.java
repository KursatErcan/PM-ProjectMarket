package com.kursat.pm_projectmarket.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.kursat.pm_projectmarket.Fragment.ProfileCommentFragment;
import com.kursat.pm_projectmarket.Fragment.ProfilePostFragment;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private String[] tabTitle = new String[] {"Posts", "Commets"};
    private final int ITEMS = tabTitle.length;
    Context context;
    public FragmentPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ProfilePostFragment();
        if(position == 0) {
            fragment = new ProfilePostFragment();
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
