package com.kursat.pm_projectmarket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kursat.pm_projectmarket.Fragment.CategoriesFragment;
import com.kursat.pm_projectmarket.Fragment.MessageFragment;
import com.kursat.pm_projectmarket.Fragment.ProfileFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new CategoriesFragment()).commit();

    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectListener =
            item -> {
                id=item.getItemId();
                switch(id){
                    case R.id.nav_home:
                        //selectedFragment = new FeedFragment();
                        selectedFragment = new CategoriesFragment();
                        break;
                    case R.id.nav_post:
                        selectedFragment = null;
                        Intent intent = new Intent(MainActivity.this,PostActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_message:
                        selectedFragment = new MessageFragment();
                        break;
                    case R.id.nav_profile:
                        /*SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                        editor.putString("profileId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        editor.apply();*/
                        selectedFragment = new ProfileFragment();
                        break;
                    default:
                        break;
                }
                if(selectedFragment != null){
                    //mainFragment içerisine seçili olan fragmenti koyar
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,selectedFragment).commit();
                }
                return true;
            };


}