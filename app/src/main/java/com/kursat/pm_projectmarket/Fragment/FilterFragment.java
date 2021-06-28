package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.kursat.pm_projectmarket.FilteredPostsActivity;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.SettingsActivity;


public class FilterFragment extends Fragment {

    Button btn_cancel,btn_filter;
    EditText editText_minPrice, editText_maxPrice;
    SeekBar seekBar_postScore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        btn_cancel = view.findViewById(R.id.cancel);
        btn_filter = view.findViewById(R.id.filter);
        editText_minPrice = view.findViewById(R.id.minPrice);
        editText_maxPrice = view.findViewById(R.id.maxPrice);
        seekBar_postScore = view.findViewById(R.id.seekBar_postScore);

        /*int filterNum = 0;
        Bundle bundle = this.getArguments();
        if(bundle != null){
            filterNum = bundle.getInt("filterNum",0);
            //System.out.println("gelen filterNum : "+filterNum);
        }
        */

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        //int finalFilterNum = filterNum;
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postScore = seekBar_postScore.getProgress();
                int minPrice = Integer.parseInt(String.valueOf(editText_minPrice.getText()));
                int maxPrice = Integer.parseInt(String.valueOf(editText_maxPrice.getText()));

                System.out.println("score : " + postScore);
                System.out.println("minPrice : " + minPrice);
                System.out.println("maxPrice : " + maxPrice);

                Bundle args = new Bundle();
                args.putInt("filter",1);
                args.putInt("postScore", postScore);

                FeedFragment feedFragment = new FeedFragment();
                feedFragment.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragment, feedFragment);
                ft.commit();


                /*Intent intent = new Intent(getContext(), FilteredPostsActivity.class);
                intent.putExtra("filterNum", finalFilterNum);
                intent.putExtra("postScore",postScore);
                startActivity(intent);*/
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