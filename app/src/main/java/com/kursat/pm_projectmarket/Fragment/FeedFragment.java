package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.PostRecyclerAdapter;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;
import java.util.Map;

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
    //String userName;
    //String profileImageDownloadUrl;
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
        //ProgressDialog progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Uploading..");
        //progressDialog.show();
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    for(DocumentSnapshot doc : value.getDocuments()){

                        Map<String,Object> data = doc.getData();
                        //String id = (String) data.get("userId");

                        db.collection("Users").document(data.get("userId").toString()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){

                                            DocumentSnapshot document = task.getResult();

                                            String name = (String) document.get("userName");
                                            String profileUrl = (String) document.get("profileImageUrl");
                                            userNameList_db.add(name);
                                            profileImageList_db.add(profileUrl);
                                            //System.out.println("Feed fragment (in onComplete):: userName[0] => " + userName[0]);
                                            //System.out.println("Feed fragment (in onComplete):: profileImageDownloadUrl[0] => " + profileImageDownloadUrl[0]);
                                            String title = (String) data.get("title");
                                            String postImageDownloadUrl = (String) data.get("postImageUrl");
                                            String price = (String) data.get("price") + " TL";

                                            //userNameList_db.add(name);
                                            //profileImageList_db.add(Url);
                                            titleList_db.add(title);
                                            postImageList_db.add(postImageDownloadUrl);
                                            priceList_db.add(price);


                                            postRecyclerAdapter.notifyDataSetChanged();

                                        }
                                    }
                                });

                    }
                }
            }
        });

    }
    /*public void buttonClick(View view) {
        Intent intent = new Intent(getActivity(), postDetail.class );
        intent.putExtra("postId",postId);
        startActivity(intent);
    }*/

}