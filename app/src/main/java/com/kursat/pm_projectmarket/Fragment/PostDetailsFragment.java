package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kursat.pm_projectmarket.Adapter.DetailsFragmentPageAdapter;
import com.kursat.pm_projectmarket.R;


public class PostDetailsFragment extends DialogFragment {

    private TabLayout tabs;
    private ViewPager viewPager;
    String token;
    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.82);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.30);
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);

        Bundle args = this.getArguments();
        token=args.getString("token");
        tabs = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new DetailsFragmentPageAdapter(getChildFragmentManager(),getContext(),args));
        tabs.setupWithViewPager(viewPager);

        return view;
    }

}