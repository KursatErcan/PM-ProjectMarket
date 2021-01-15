package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kursat.pm_projectmarket.R;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    ImageView imageView_profileImage;
    ImageButton imageView_postsButton,imageView_commentsButton;
    TextView textView_UserName;

    FirebaseUser user;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);




        return view;
    }
}