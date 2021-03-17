package com.kursat.pm_projectmarket.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kursat.pm_projectmarket.IntroActivity;
import com.kursat.pm_projectmarket.LoginActivity;
import com.kursat.pm_projectmarket.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {
    SharedPreferences mode;
    SharedPreferences.Editor editor;
    boolean isNightModeOn;
    TextView logout;
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_settings, container, false);

        logout = (TextView) RootView.findViewById(R.id.logout);

        //function for enabling dark mode
        //mode = getActivity().getSharedPreferences("modePref",Context.MODE_PRIVATE);
        mode = getActivity().getSharedPreferences(getActivity().getPackageName(), MODE_PRIVATE);
        editor = mode.edit();
        isNightModeOn = mode.getBoolean("NightMode",false);

        if(isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch modeSwitch = RootView.findViewById(R.id.darkModeSwitch);
        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if(isNightModeOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // BUYUK İHTİMAL HATA BURADA
                    editor.putBoolean("NightMode",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("NightMode",true);
                }
                editor.apply();
            }
        });

        logout.setOnClickListener((v) -> {
            new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getContext(), IntroActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return RootView;
    }

    private void editProfileClicked(View view){
        getFragmentManager().beginTransaction().replace(R.id.mainFragment,new FeedFragment()).commit();

    }

}