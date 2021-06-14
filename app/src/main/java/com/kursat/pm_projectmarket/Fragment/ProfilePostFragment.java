package com.kursat.pm_projectmarket.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.Adapter.ProfilePostsAdapter;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.helpers.GridSpacingItemDecoration;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ProfilePostFragment extends Fragment implements PostRecyclerAdapter.OnMessageListener, ProfilePostsAdapter.OnLongClickListener{
    private FirebaseFirestore db;
    private String userId;
    ProfilePostsAdapter ProfilePostsAdapter;
    ArrayList<Post> ppost;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        ProfilePostsAdapter = new ProfilePostsAdapter(ppost, this::onMessageClick,this::onLongClick);
        recyclerView.setAdapter(ProfilePostsAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

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
                            ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostContent(),post.getPostImageUrl(),post.getScore(),"nu",doc.getId()));
                            ProfilePostsAdapter.notifyDataSetChanged();
                        }

                    }
                });

    }


    public void onMessageClick(int position) {

        Bundle args = new Bundle();
        args.putString("token", ppost.get(position).getToken());

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(args);

        assert getFragmentManager() != null;
        detailsFragment.show(getFragmentManager(),"My Dialog");

    }
    public void onLongClick(int position) {
        if(!ppost.get(position).getUserId().equals(user.getUid()))
            return;
        new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes",(dialog, which) ->{
                    db.collection("Posts").document(ppost.get(position).getToken())
                            .delete();
                    Toast toast = Toast.makeText(getContext(), R.string.the_post_is_successfully_deleted, Toast.LENGTH_SHORT);
                    toast.show();

                })
                .setNegativeButton("No", null)
                .show();



    }
}