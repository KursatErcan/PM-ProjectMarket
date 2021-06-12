package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
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


public class ProfileCommentFragment extends Fragment implements MessageBoxAdapter.OnMessageListener{

    CommentRecyclerAdapter commentRecyclerAdapter;
    ArrayList<Comment> comment;
    FirebaseFirestore db;
    String userId;
    String token;

    public ProfileCommentFragment() {
        // Required empty public constructor
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
        Bundle args = this.getArguments();
        token=args.getString("token");
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "none");
        if(args==null)
            getUserComments();
        else
            getPostComments(token);
        return view;
    }

    void getUserComments(){

        db.collection("Posts").whereEqualTo("userId",userId).addSnapshotListener((value, error) -> {
            if(value != null){
                for(DocumentSnapshot doc : value.getDocuments()){
                    db.collection("Posts/"+doc.getId()+"/Comments")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w(TAG, "Listen failed.", e);
                                        return;
                                    }

                                    for (QueryDocumentSnapshot document : value) {
                                        //String title, String comment,String point,String token
                                        comment.add(new Comment(doc.get("title").toString(),document.get("commentText").toString(),document.getLong("score"),document.getId()));
                                    }

                                    //ratingBar.setRating(totalPoint);

                                    commentRecyclerAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }

        });
    }

    public void getPostComments(String token){
        db.collection("Posts/"+token+"/Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot document : value) {
                            //String title, String comment,String point,String token
                            comment.add(new Comment(document.get("commentText").toString(),document.get("date").toString(),document.getLong("score"),document.getId()));
                        }

                        //ratingBar.setRating(totalPoint);

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