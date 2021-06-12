package com.kursat.pm_projectmarket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class IntroActivity extends AppCompatActivity {

    ImageView pm_logo;
    TextView projectMarket;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = sharedPreferences.getString("lang","en");

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        if(isOnline()){
            load();
        }else{
            try {
                new AlertDialog.Builder(IntroActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available, Cross check your internet connectivity")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                load();
                            }
                        }).show();
            } catch (Exception e) {
                //Log.d(Constants.TAG, "Show Dialog: " + e.getMessage());
            }
        }
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }
    public void load(){
        pm_logo = (ImageView)findViewById(R.id.pm_logo);
        projectMarket = (TextView)findViewById(R.id.projectMarket);


        pm_logo.animate().alpha(0f).setDuration(0);
        projectMarket.animate().alpha(0f).setDuration(0);

        pm_logo.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                projectMarket.animate().alpha(1f).setDuration(800);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                firebaseAuth = FirebaseAuth.getInstance();

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent n = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(n);
                    finish();
                } else {
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        },3000);
    }
}