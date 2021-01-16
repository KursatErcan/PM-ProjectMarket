package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.Model.User;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    String postId;
    PostRecyclerAdapter postRecyclerAdapter;

    ArrayList<String> userNameList_db;
    ArrayList<String> titleList_db;
    ArrayList<String> profileImageList_db;
    ArrayList<String> postImageList_db;
    ArrayList<String> priceList_db;

    public FeedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameList_db = new ArrayList<>();
        titleList_db = new ArrayList<>();
        profileImageList_db = new ArrayList<>();
        postImageList_db = new ArrayList<>();
        priceList_db = new ArrayList<>();

        getDataFromDB();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_feedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerAdapter = new PostRecyclerAdapter(userNameList_db, titleList_db,
                profileImageList_db, postImageList_db, priceList_db);
        recyclerView.setAdapter(postRecyclerAdapter);


        return view;
    }

    public void getDataFromDB(){

        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener((EventListener<QuerySnapshot>) (value, error) -> {
            if(value != null){
                for(DocumentSnapshot doc : value.getDocuments()){
                    Post post = doc.toObject(Post.class);

                    db.collection("Users").document(post.getUserId()).get()
                            .addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
                                if (task.isSuccessful()){
                                    User user = task.getResult().toObject(User.class);

                                    userNameList_db.add((String) user.getUserName());
                                    profileImageList_db.add(user.getProfileImageUrl());
                                    titleList_db.add(post.getTitle());
                                    postImageList_db.add(post.getPostImageUrl());
                                    priceList_db.add(post.getPrice());

                                    postRecyclerAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });
    }
}