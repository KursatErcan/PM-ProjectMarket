package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kursat.pm_projectmarket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCommentFragment extends Fragment {

    public ProfileCommentFragment() {
        // Required empty public constructor
    }


    public static ProfileCommentFragment newInstance(String param1, String param2) {
        ProfileCommentFragment fragment = new ProfileCommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_comment, container, false);
    }
}