package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.FragmentPageAdapter;
import com.kursat.pm_projectmarket.MessagesActivity;
import com.kursat.pm_projectmarket.Model.User;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.SettingsActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class ProfileFragment extends Fragment {

    private ImageView imageView_profileImage;
    private TextView textView_UserName;
    private ImageView settings;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FirebaseFirestore db;
    private String profileId;
    String token;
    CollectionReference cfr;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String profileName;
    TextView msgDetail;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile1, container, false);

        db = FirebaseFirestore.getInstance();
        FloatingActionButton flbtn= view.findViewById(R.id.fab);
        settings=view.findViewById(R.id.settings);

        textView_UserName = view.findViewById(R.id.text_userName_profileFragment);
        imageView_profileImage = view.findViewById(R.id.imageView_profilePhoto);


        tabs = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new FragmentPageAdapter(getChildFragmentManager(),getContext()));
        tabs.setupWithViewPager(viewPager);
        //setupWithViewPager(viewPager);

        //SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE); //main activitede setlendi
        //profileId = prefs.getString("profileId", "none");
        msgDetail=(TextView)view.findViewById(R.id.MessageDetail);
        profileId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle= this.getArguments();

        if(bundle==null){
            flbtn.hide();
        }
        if(bundle!=null) {
            settings.setVisibility(View.GONE);
            if(profileId.equals(bundle.getString("userId"))){
                flbtn.hide();
                settings.setVisibility(View.VISIBLE);
            }

            profileId = bundle.getString("userId");
            profileName = bundle.getString("userName");
            flbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db.collection("Messages")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc1 : task.getResult()) {

                                            if((doc1.get("message_received").equals(profileId) && doc1.get("message_posted").equals(user.getUid())) ||
                                                    doc1.get("message_received").equals(user.getUid()) && doc1.get("message_posted").equals(profileId)){
                                                Intent intent=new Intent(getActivity(), MessagesActivity.class);
                                                intent.putExtra("userId",profileId);
                                                intent.putExtra("userName",profileName);
                                                intent.putExtra("token",doc1.getId());
                                                System.out.println("Buraya girdim !!!!!!!!!!!!!!!!!!!!");
                                                startActivity(intent);

                                                return;
                                            }

                                        }

                                            //String token=documentReference.getId();
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                                            String userName = sharedPreferences.getString("userName", "");
                                            Map<String, Object> messageSend = new HashMap<>();
                                            messageSend.put("message_posted",user.getUid());
                                            messageSend.put("message_posted_name",userName);
                                            messageSend.put("message_received",profileId);
                                            messageSend.put("message_received_name",profileName);
                                            db.collection("Messages")
                                                    .add(messageSend)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            token=documentReference.getId();
                                                            Intent intent=new Intent(getActivity(), MessagesActivity.class);
                                                            intent.putExtra("userId",profileId);
                                                            intent.putExtra("userName",profileName);
                                                            intent.putExtra("token",token);
                                                            System.out.println("Buraya daaaaaaaaaaaa girdim !!!!!!!!!!!!!!!!!!!!");
                                                            startActivity(intent);
                                                            return;
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });



                    Toast.makeText(getContext(),"You can write your message..",Toast.LENGTH_LONG).show();
                }
            });
        }




        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        userInfo();
        return view;
    }

    private void userInfo(){
        DocumentReference reference = db.collection("Users").document(profileId);

        reference.addSnapshotListener((value, error) -> {
            if(error == null){
                if(getContext() == null){ return; }
                if(value != null){
                    User user = value.toObject(User.class);
                    //assert user != null;

                    try {
                        assert user != null;
                        Picasso.get().load(user.getProfileImageUrl()).into(imageView_profileImage);
                        textView_UserName.setText(user.getUserName());
                    }catch (Exception e){
                        System.out.println("Exception : " + e);
                    }
                }
            }
        });
    }
}