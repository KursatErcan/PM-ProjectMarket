package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class PostDetailsFragment extends DialogFragment {
    String token;
    ImageView postImage,profileClick;
    TextView title,userName,price;
    EditText commentText;
    Button addCommentButton;
    RatingBar ratingBar_comment;
    RatingBar ratingBar_post;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_post_details,null);

        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.85);
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);
        final String[] userId = new String[1];

        postImage = view.findViewById(R.id.post_detail_img);
        profileClick= view.findViewById(R.id.post_detail_goToProfile);
        title = view.findViewById(R.id.post_detail_title);
        userName = view.findViewById(R.id.post_detail_userName);
        price = view.findViewById(R.id.post_detail_price);
        commentText = view.findViewById(R.id.post_detail_comment);
        addCommentButton = view.findViewById(R.id.post_detail_add_comment_btn);
        ratingBar_comment=view.findViewById(R.id.post_detail_comment_ratingBar);
        ratingBar_post=view.findViewById(R.id.post_detail_post_ratingBar);

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
                    userId[0] = post.getUserId();

                    title.setText(post.getTitle());
                    userName.setText(post.getUserName());
                    price.setText(post.getPrice()+" "+price.getText());
                    ratingBar_post.setRating(Float.parseFloat(post.getScore()));

                    Picasso.get().load(post.getPostImageUrl())
                            .resize(postImage.getWidth(),postImage.getHeight())
                            .into(postImage);

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


        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId[0] == FirebaseAuth.getInstance().getCurrentUser().getUid()){
                    Toast.makeText(getContext(),"You cannot comment on your own post!",Toast.LENGTH_LONG).show();
                    ratingBar_comment.setRating(0);
                    commentText.setText(null);
                }

                float score = ratingBar_comment.getRating();
                //System.out.println("score : " +score);
                if(score!=0 && commentText.getText()!=null){
                    HashMap<String, Object> commentData = new HashMap<>();
                    commentData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    commentData.put("date",new Timestamp(new Date()));
                    commentData.put("commentText",commentText.getText());
                    commentData.put("score",score);

                    //CollectionReference collection = db.collection("Posts/"+token+"/Comments");
                    //collection.document()
                    db.collection("Posts").document(token).collection("Comments")
                            .add(commentData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(),"Posting a comment is successful..",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Failed to post comment..",Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });


        return  view;
    }


}
