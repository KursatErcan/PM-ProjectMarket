package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.kursat.pm_projectmarket.Model.User;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    ImageView imageView_profileImage;
    ImageButton imageView_postsButton,imageView_commentsButton;
    TextView textView_UserName;

    private FirebaseFirestore db;
    FirebaseUser currentUser;
    String profileId;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE); //main activitede setlendi
        profileId = prefs.getString("profileId","none");

        imageView_profileImage = view.findViewById(R.id.profileImage_profileFragment);
        imageView_postsButton = view.findViewById(R.id.posts_profileFragment);
        imageView_commentsButton = view.findViewById(R.id.comments_profileFragment);
        textView_UserName = view.findViewById(R.id.text_userName_profileFragment);


        userInfo();
        return view;
    }

    private void userInfo(){
        DocumentReference reference = db.collection("Users").document(profileId);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    if(getContext() == null){ return; }
                    if(value != null){
                        User user = value.toObject(User.class);
                        Picasso.get().load(user.getProfileImageUrl()).into(imageView_profileImage);
                        textView_UserName.setText(user.getUserName());

                        System.out.println("userName => " + user.getUserName());
                        System.out.println("profilImageUrl => " + user.getProfileImageUrl());
                    }

                }
            }
        });
    }
}