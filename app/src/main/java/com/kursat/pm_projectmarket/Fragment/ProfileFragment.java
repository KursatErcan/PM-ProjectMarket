package com.kursat.pm_projectmarket.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kursat.pm_projectmarket.Model.User;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.SettingsActivity;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private ImageView imageView_profileImage;
    //ImageButton imageView_postsButton,imageView_commentsButton;
    private TextView textView_UserName;
    private ImageView settings;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FirebaseFirestore db;
    //FirebaseUser currentUser;
    private String profileId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile1, container, false);

        db = FirebaseFirestore.getInstance();

        //currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle= this.getArguments();
        if(bundle==null) {
            SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE); //main activitede setlendi
            profileId = prefs.getString("profileId", "none");
        }else{
            profileId = bundle.getString("userId");
        }

        //System.out.println("profileId: " + profileId);

        textView_UserName = view.findViewById(R.id.text_userName_profileFragment);
        imageView_profileImage = view.findViewById(R.id.imageView_profilePhoto);
        //imageView_postsButton = view.findViewById(R.id.posts_profileFragment);
        //imageView_commentsButton = view.findViewById(R.id.comments_profileFragment);
        settings=view.findViewById(R.id.settings);

        //tabs = view.findViewById(R.id.tabs);
        //viewPager = view.findViewById(R.id.viewPager);

        //tabs.setupWithViewPager(viewPager);
        //setupWithViewPager(viewPager);

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
                    assert user != null;
                    Picasso.get().load(user.getProfileImageUrl()).into(imageView_profileImage);
                    textView_UserName.setText(user.getUserName());
                }
            }
        });
    }


/*
    private void setupWithViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new ProfilePostFragment(), "Posts");
        adapter.addFrag(new ProfileCommentFragment(), "Comments");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment name, String title) {
            fragmentList.add(name);
            titleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

 */
}