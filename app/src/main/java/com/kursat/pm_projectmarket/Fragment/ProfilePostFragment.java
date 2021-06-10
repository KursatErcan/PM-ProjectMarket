package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ProfilePostFragment extends Fragment implements PostRecyclerAdapter.OnMessageListener{
    private FirebaseFirestore db;
    private String userId;
    PostRecyclerAdapter ProfilePostsAdapter;
    ArrayList<Post> ppost;

    public ProfilePostFragment() {

        // Required empty public constructor
        // it don't appear if i see the feeds !!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_post, container, false);

        db = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "none");

        ppost = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rcwProfilePosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProfilePostsAdapter = new PostRecyclerAdapter(ppost, this::onMessageClick);
        recyclerView.setAdapter(ProfilePostsAdapter);

        getDataFromDB();
        return view;
    }

    public void getDataFromDB(){


        db.collection("Posts")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Post post = doc.toObject(Post.class);
                            assert post != null;
                            System.out.println(post.getUserName());
                            //String userId,String userName, String price, String title, String postImageUrl
                            ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostImageUrl(),"nu",doc.getId()));

                        }
                        ProfilePostsAdapter.notifyDataSetChanged();
                    }
                });

    }


    public void onMessageClick(int position) {

        Bundle args = new Bundle();
        args.putString("token", ppost.get(position).getToken());

        PostDetailsFragment detailsFragment = new PostDetailsFragment();
        detailsFragment.setArguments(args);

        assert getFragmentManager() != null;
        detailsFragment.show(getFragmentManager(),"My Dialog");

    }
}