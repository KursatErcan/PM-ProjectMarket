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

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class DetailsFragment extends DialogFragment {
    String token;
    ImageView postImage,profileClick;
    TextView title,postContent,userName,price;
    EditText commentText;
    Button addCommentButton;
    RatingBar ratingBar_comment;
    RatingBar ratingBar_post;
    float totalScore=0.0f;
    int counter=0;
    int score=0;
    int counterComment=0;
    Number currentScore=0;
    String commentO;
    String docNumComment;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_details,null);

        Bundle args = this.getArguments();
        postImage = view.findViewById(R.id.post_detail_img);
        profileClick= view.findViewById(R.id.post_detail_goToProfile);
        title = view.findViewById(R.id.post_detail_title);
        postContent = view.findViewById(R.id.post_content);
        userName = view.findViewById(R.id.post_detail_userName);
        price = view.findViewById(R.id.post_detail_price);
        commentText = view.findViewById(R.id.post_detail_comment);
        addCommentButton = view.findViewById(R.id.post_detail_add_comment_btn);
        ratingBar_comment=view.findViewById(R.id.post_detail_comment_ratingBar);
        ratingBar_post=view.findViewById(R.id.post_detail_post_ratingBar);

        assert args != null;
        token=args.getString("token");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Posts").document(token);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                assert document != null;
                if (document.exists()) {
                    //Post post = document.toObject(Post.class);
                    //assert post != null;
                    title.setText(document.get("title").toString());
                    postContent.setText(document.get("postContent").toString());
                    userName.setText(document.get("userName").toString());
                    price.setText(document.get("price").toString()+" "+price.getText());

                    //getPostScrore
                    db.collection("Posts/"+document.getId()+"/Comments")
                            .addSnapshotListener((value, error) -> {
                                if(value != null) {
                                    for (DocumentSnapshot doc : value.getDocuments()) {
                                        //all comments is here doc.get("detail")
                                        score += doc.getDouble("score");
                                        counter++;
                                        if (doc.get("userId").toString().equals(user.getUid())) {
                                            counterComment++;
                                            docNumComment = doc.getId();
                                            commentO = doc.get("commentText").toString();
                                            currentScore=doc.getDouble("score").floatValue();
                                        }
                                    }
                                    totalScore = 0.0f;

                                    if (counter == 0) {
                                        totalScore = 0;
                                    } else {
                                        totalScore = score / counter;

                                    }
                                    ratingBar_post.setRating(totalScore);
                                    //post score
                                    HashMap<String, Object> hp = new HashMap<>();
                                    hp.clear();
                                    hp.put("score", totalScore);
                                    db.collection("Posts").document(document.getId())
                                            .set(hp, SetOptions.merge());
                                    if(counterComment>=1){
                                        commentText.setText(commentO);
                                        ratingBar_comment.setRating(currentScore.floatValue());
                                    addCommentButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            HashMap<String, Object> commentData = new HashMap<>();
                                            float score = ratingBar_comment.getRating();
                                            //System.out.println("score : " +score);


                                            commentData.put("commentText", commentText.getText().toString());
                                            commentData.put("score", score);
                                            db.collection("Posts/" + document.getId() + "/Comments").document(docNumComment)//from hereee.
                                                    .set(commentData, SetOptions.merge());
                                            Toast toast = Toast.makeText(getContext(), R.string.your_comment_has_been_sent, Toast.LENGTH_SHORT);
                                            toast.show();
                                            //docNumComment;

                                        }
                                    });
                                }
                                    else{
                                        addCommentButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                HashMap<String, Object> commentData = new HashMap<>();
                                                float score = ratingBar_comment.getRating();
                                                //System.out.println("score : " +score);
                                                if (score != 0 && commentText.getText() != null) {
                                                    commentData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    commentData.put("date", new Timestamp(new Date()));
                                                    commentData.put("commentText", commentText.getText().toString());
                                                    commentData.put("score", score);
                                                    db.collection("Posts/" + token + "/Comments")
                                                            .add(commentData);
                                                    Toast toast = Toast.makeText(getContext(), R.string.your_comment_has_been_sent, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }


                                            }
                                        });
                                    }
                                }

                            });
                    //end of getPostScore
                    Picasso.get().load(document.get("postImageUrl").toString())
                            .resize(postImage.getWidth(),postImage.getHeight())
                            .into(postImage);

                    if(document.get("userId").toString().equals(user.getUid())){
                        commentText.setVisibility(View.GONE);
                        ratingBar_comment.setVisibility(View.GONE);
                        addCommentButton.setVisibility(View.GONE);
                    }
                    profileClick.setOnClickListener(v -> {
                        dismiss();
                        PostDetailsFragment parentFrag = ((PostDetailsFragment) DetailsFragment.this.getParentFragment());
                        Bundle args1 = new Bundle();
                        args1.putString("userId", document.get("userId").toString());
                        args1.putString("userName", document.get("userName").toString());
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(args1);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        assert parentFrag != null;
                        ft.remove(parentFrag);
                        ft.replace(R.id.mainFragment, profileFragment, "tag");//from here.
                        ft.addToBackStack(null).commit();
                    });


                }
            }
        });
        return  view;
    }
}
