package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.Timestamp;
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
import com.kursat.pm_projectmarket.helpers.GridSpacingItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class CommentFragment extends Fragment implements MessageBoxAdapter.OnMessageListener{

    CommentRecyclerAdapter commentRecyclerAdapter;
    ArrayList<Comment> comment;
    FirebaseFirestore db;
    String userId;
    String token;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comment, container, false);

        comment = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_commentFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecyclerAdapter = new CommentRecyclerAdapter(comment,this::onMessageClick);
        recyclerView.setAdapter(commentRecyclerAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        db = FirebaseFirestore.getInstance();
        Bundle args = this.getArguments();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "none");
        if(args==null)
            getUserComments();
        else{
            token=args.getString("token");
            getPostComments(token);
        }
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
                            String date=getDate((Timestamp)document.get("date"));
                            comment.add(new Comment(document.get("commentText").toString(),date,document.getLong("score"),document.getId()));
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

    private String getDate(Timestamp milliSeconds) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( milliSeconds.getSeconds()*1000);
        return formatter.format(calendar.getTime());
    }
}