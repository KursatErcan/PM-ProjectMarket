package com.kursat.pm_projectmarket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kursat.pm_projectmarket.MessagesActivity;
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
    private String profileId;
    private String profileName;

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

        //SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE); //main activitede setlendi
        //profileId = prefs.getString("profileId", "none");

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
            System.out.println("ProfileFragment ++++++"+profileId);


            flbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), MessagesActivity.class);
                    intent.putExtra("userId",profileId);
                    intent.putExtra("userName",profileName);
                    startActivity(intent);

                    Toast.makeText(getContext(),"You can write your message..",Toast.LENGTH_LONG).show();
                    //Snackbar.make(getView(), "Mesajınızı yazabilirsiniz...", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
                }
            });
        }


        textView_UserName = view.findViewById(R.id.text_userName_profileFragment);
        imageView_profileImage = view.findViewById(R.id.imageView_profilePhoto);
        //imageView_postsButton = view.findViewById(R.id.posts_profileFragment);
        //imageView_commentsButton = view.findViewById(R.id.comments_profileFragment);


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