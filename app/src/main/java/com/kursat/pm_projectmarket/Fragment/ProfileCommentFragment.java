package com.kursat.pm_projectmarket.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.CommentRecyclerAdapter;
import com.kursat.pm_projectmarket.Adapter.MessageBoxAdapter;
import com.kursat.pm_projectmarket.MessagesActivity;
import com.kursat.pm_projectmarket.Model.Comment;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCommentFragment extends Fragment implements MessageBoxAdapter.OnMessageListener{

    CommentRecyclerAdapter commentRecyclerAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Comment> comment;
    FirebaseFirestore db;
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
        commentRecyclerAdapter = new CommentRecyclerAdapter(comment,this::onMessageClick);
        recyclerView.setAdapter(commentRecyclerAdapter);
        db = FirebaseFirestore.getInstance();
        getUserComments();
        return view;
    }

    void getUserComments(){
        db.collection("Users/"+user.getUid()+"/Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            //String title, String comment,String point,String token
                            comment.add(new Comment(doc.get("comment_title").toString(),doc.get("comment_detail").toString(),doc.get("comment_point").toString(),doc.getId()));
                        }
                        commentRecyclerAdapter.notifyDataSetChanged();
                    }
                });

    }
    public void onMessageClick(int position) {
        Intent intent=new Intent(getActivity(), MessagesActivity.class);
        intent.putExtra("token",comment.get(position).getToken());
        startActivity(intent);

    }
}