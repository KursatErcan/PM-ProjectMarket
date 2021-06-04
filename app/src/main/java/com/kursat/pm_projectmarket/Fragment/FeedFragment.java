package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements PostRecyclerAdapter.OnMessageListener{

    private FirebaseFirestore db;
    PostRecyclerAdapter postRecyclerAdapter;
    ArrayList<Post> ppost;

    public FeedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        db = FirebaseFirestore.getInstance();

        ppost = new ArrayList<>();

        String filterNum;

        Bundle bundle = this.getArguments();
        if(bundle != null){
            filterNum = bundle.getString("filterNum","0");
            System.out.println("gelen filterNum : "+filterNum);
        }

        getDataFromDB();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_feedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerAdapter = new PostRecyclerAdapter(ppost,this);
        recyclerView.setAdapter(postRecyclerAdapter);


        return view;
    }

    public void getDataFromDB(){

            db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                if(value != null){
                    for(DocumentSnapshot doc : value.getDocuments()){
                        Post post = doc.toObject(Post.class);
                        assert post != null;
                        System.out.println(post.getUserName());
                        //String userId,String userName, String price, String title, String postImageUrl
                        ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostImageUrl(),doc.getId()));
                        postRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            });

        /*else{
            db.collection("Posts").whereEqualTo("categoryId",filterNum).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
                if(value != null){
                    for(DocumentSnapshot doc : value.getDocuments()){
                        Post post = doc.toObject(Post.class);

                        assert post != null;
                        db.collection("Users").document(post.getUserId()).get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        User user = Objects.requireNonNull(task.getResult()).toObject(User.class);

                                        assert user != null;
                                        userNameList_db.add(user.getUserName());
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
        }*/
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