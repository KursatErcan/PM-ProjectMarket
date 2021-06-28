package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.Model.User;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.helpers.GridSpacingItemDecoration;

import java.util.ArrayList;

public class FeedFragment extends Fragment implements PostRecyclerAdapter.OnMessageListener{

    private FirebaseFirestore db;
    PostRecyclerAdapter postRecyclerAdapter;
    ArrayList<Post> ppost;
    ImageView btn_filter;
    private int postScore = 0, minPrice = 0,maxPrice = 0, filter =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        btn_filter = view.findViewById(R.id.filter_btn);
        db = FirebaseFirestore.getInstance();
        ppost = new ArrayList<>();

        int filterNum = 0;
        Bundle bundle = this.getArguments();
        if(bundle != null){
            filterNum = bundle.getInt("filterNum",0);
            filter = bundle.getInt("filter",0);
            postScore = bundle.getInt("postScore",0);
            minPrice = bundle.getInt("minPrice",0);
            maxPrice = bundle.getInt("maxPrice",100000000);
        }

        getDataFromDB(filterNum);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_feedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerAdapter = new PostRecyclerAdapter(ppost,this);
        recyclerView.setAdapter(postRecyclerAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = new FilterFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.mainFragment, filterFragment);
                ft.commit();
            }
        });
        return view;
    }

    public void getDataFromDB(int filterNum){
        db.collection("Posts").whereArrayContains("postCategory",filterNum).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if(value != null){
                for(DocumentSnapshot doc : value.getDocuments()){
                    Post post = doc.toObject(Post.class);
                    assert post != null;
                    DocumentReference docRef = db.collection("Users").document(post.getUserId());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            if(filter==1){
                                if(Integer.parseInt(post.getScore())>= postScore
                                        && Integer.parseInt(post.getPrice()) < maxPrice
                                        && Integer.parseInt(post.getPrice()) >= minPrice){
                                    ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostContent(),post.getPostImageUrl(),post.getScore(),user.getProfileImageUrl(),doc.getId()));
                                    postRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                            else{
                                ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostContent(),post.getPostImageUrl(),post.getScore(),user.getProfileImageUrl(),doc.getId()));
                                postRecyclerAdapter.notifyDataSetChanged();
                            }


                        }
                    });
                }
            }
        });
    }

    public void onMessageClick(int position) {

        Bundle args = new Bundle();
        args.putString("token", ppost.get(position).getToken());
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        postDetailsFragment.setArguments(args);

        assert getFragmentManager() != null;
        postDetailsFragment.show(getFragmentManager(),"My Dialog");
    }
}