package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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

        int filterNum = 0;
        Bundle bundle = this.getArguments();
        if(bundle != null){
            filterNum = bundle.getInt("filterNum",0);
            //System.out.println("gelen filterNum : "+filterNum);
        }

        getDataFromDB(filterNum);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_feedFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerAdapter = new PostRecyclerAdapter(ppost,this);
        recyclerView.setAdapter(postRecyclerAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

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
                            ppost.add(new Post(post.getUserId(),post.getUserName(),post.getPrice(),post.getTitle(),post.getPostContent(),post.getPostImageUrl(),post.getScore(),user.getProfileImageUrl(),doc.getId()));
                            postRecyclerAdapter.notifyDataSetChanged();
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