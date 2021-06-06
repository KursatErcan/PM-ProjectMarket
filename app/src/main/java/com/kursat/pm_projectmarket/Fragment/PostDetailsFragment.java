package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

public class PostDetailsFragment extends DialogFragment {
    String token;
    ImageView postImage,profileClick;
    TextView title,userName,price;
    EditText commentText;
    Button addCommentButton;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_post_details,null);

        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.85);
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);

        postImage = view.findViewById(R.id.post_detail_img);
        profileClick= view.findViewById(R.id.post_detail_goToProfile);
        title = view.findViewById(R.id.post_detail_title);
        userName = view.findViewById(R.id.post_detail_userName);
        price = view.findViewById(R.id.post_detail_price);
        commentText = view.findViewById(R.id.post_detail_comment);
        addCommentButton = view.findViewById(R.id.post_detail_add_comment_btn);

        Bundle args = getArguments();
        assert args != null;
        token = args.getString("token");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Posts").document(token);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Post post = document.toObject(Post.class);
                    assert post != null;
                    title.setText(post.getTitle());
                    userName.setText(post.getUserName());
                    price.setText(post.getPrice()+price.getText());
                    Picasso.get().load(post.getPostImageUrl()).into(postImage);

                    profileClick.setOnClickListener(v -> {
                        dismiss();
                        Bundle args1 = new Bundle();
                        args1.putString("userId", post.getUserId());
                        args1.putString("userName", post.getUserName());
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(args1);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.mainFragment, profileFragment, "tag");
                        ft.addToBackStack(null).commit();
                    });


                }
            }
        });



        return  view;
    }

}
