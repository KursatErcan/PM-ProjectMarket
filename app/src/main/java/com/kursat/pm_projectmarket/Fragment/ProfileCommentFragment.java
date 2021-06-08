package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kursat.pm_projectmarket.Adapter.CommentRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.Comment;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCommentFragment extends Fragment {

    CommentRecyclerAdapter commentRecyclerAdapter;
    ArrayList<Comment> comment;
    public ProfileCommentFragment() {
        // Required empty public constructor
    }

    public static ProfileCommentFragment newInstance(String param1, String param2) {
        ProfileCommentFragment fragment = new ProfileCommentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_comment, container, false);

        comment = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_profileCommentFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecyclerAdapter = new CommentRecyclerAdapter(comment);
        recyclerView.setAdapter(commentRecyclerAdapter);



        return view;
    }
}