package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class PostDetailsFragment extends DialogFragment {
    String token;
    String tokenDocument;
    ImageView postImage,profileClick;
    TextView title,userName,price;
    EditText commentText;
    Button addCommentButton;
    RatingBar ratingBar_comment;
    RatingBar ratingBar_post;
    Button getComments;
    float totalScore=0.0f;
    int counter=0;
    int score=0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_post_details,null);

        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.85);
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);
        Bundle args = this.getArguments();
        postImage = view.findViewById(R.id.post_detail_img);
        profileClick= view.findViewById(R.id.post_detail_goToProfile);
        title = view.findViewById(R.id.post_detail_title);
        userName = view.findViewById(R.id.post_detail_userName);
        price = view.findViewById(R.id.post_detail_price);
        commentText = view.findViewById(R.id.post_detail_comment);
        addCommentButton = view.findViewById(R.id.post_detail_add_comment_btn);
        ratingBar_comment=view.findViewById(R.id.post_detail_comment_ratingBar);
        ratingBar_post=view.findViewById(R.id.post_detail_post_ratingBar);
        getComments=view.findViewById(R.id.post_detail_get_comments_btn);
//        Bundle args = getArguments();
//        assert args != null;
//        token = args.getString("token");
//        String userPost=args.getString("userId");
        token=args.getString("token");
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
                    price.setText(post.getPrice()+" "+price.getText());

                    //getPostScrore
                    db.collection("Posts/"+document.getId()+"/Comments")
                            .addSnapshotListener((value, error) -> {
                                if(value != null){
                                    for(DocumentSnapshot doc : value.getDocuments()){
                                        //all comments is here doc.get("detail")
                                        score+=doc.getLong("score");
                                        counter++;
                                    }
                                    totalScore=0.0f;

                                    if(counter==0){
                                        totalScore = 0;
                                    }
                                    else{
                                        totalScore=score/counter;

                                    }
                                    ratingBar_post.setRating(totalScore);
                                }

                            });
                    //end of getPostScore
                    Picasso.get().load(post.getPostImageUrl())
                            .resize(postImage.getWidth(),postImage.getHeight())
                            .into(postImage);

                    if(post.getUserId().equals(user.getUid())){
                        commentText.setVisibility(View.GONE);
                        ratingBar_comment.setVisibility(View.GONE);
                        addCommentButton.setVisibility(View.GONE);
                    }
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

                    addCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            float score = ratingBar_comment.getRating();
                            //System.out.println("score : " +score);
                            if(score!=0 && commentText.getText()!=null){
                                HashMap<String, Object> commentData = new HashMap<>();
                                commentData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                commentData.put("date",new Timestamp(new Date()));
                                commentData.put("commentText",commentText.getText().toString());
                                commentData.put("score",score);
                                db.collection("Posts/"+token+"/Comments")
                                        .add(commentData);
                                Toast toast = Toast.makeText(getContext(), "Mesajınız gönderildi.", Toast.LENGTH_SHORT);
                                toast.show();



                            }

                        }
                    });

                    getComments.setOnClickListener(v -> {

                    });


                }
            }
        });




        return  view;
    }




}
