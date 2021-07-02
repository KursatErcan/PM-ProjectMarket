package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.kursat.pm_projectmarket.R;


public class FilterFragment extends Fragment {

    Button btn_cancel,btn_filter;
    EditText editText_minPrice, editText_maxPrice;
    SeekBar seekBar_postScore;
    SeekBar seekBar_userScore;
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
        seekBar_userScore=view.findViewById(R.id.seekBar_userScore);

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
                int postScore = seekBar_postScore.getProgress(),minPrice=0,maxPrice=1000000000,
                        userScore=seekBar_userScore.getProgress();
                if(!editText_minPrice.getText().toString().equals("") && !editText_maxPrice.getText().toString().equals("")){
                    minPrice = Integer.parseInt(String.valueOf(editText_minPrice.getText()));
                    maxPrice = Integer.parseInt(String.valueOf(editText_maxPrice.getText()));
                }
                System.out.println("score : " + postScore);
                System.out.println("minPrice : " + minPrice);
                System.out.println("maxPrice : " + maxPrice);

                Bundle args = new Bundle();
                args.putInt("filter",1);
                args.putInt("postScore", postScore);
                args.putInt("userScore", userScore);
                args.putInt("minPrice", minPrice);
                args.putInt("maxPrice", maxPrice);

                FeedFragment feedFragment = new FeedFragment();
                feedFragment.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragment, feedFragment);
                ft.commit();

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