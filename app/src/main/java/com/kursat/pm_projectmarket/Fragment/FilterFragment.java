package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kursat.pm_projectmarket.FilteredPostsActivity;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.SettingsActivity;


public class FilterFragment extends Fragment {

    Button btn_cancel,btn_filter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        btn_cancel = view.findViewById(R.id.cancel);
        btn_filter = view.findViewById(R.id.filter);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilteredPostsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}