package com.kursat.pm_projectmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    boolean isNightModeOn;
    TextView logout, editProfile,changePassword,language;
    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logout = findViewById(R.id.logout);
        editProfile = findViewById(R.id.editProfile);
        changePassword = findViewById(R.id.changePassword);
        language = findViewById(R.id.language);
        alertDialog = new AlertDialog.Builder(SettingsActivity.this);


        //function for enabling dark mode
        //mode = getActivity().getSharedPreferences("modePref",Context.MODE_PRIVATE);
        /*
        mode = getSharedPreferences(SettingsActivity.this.getPackageName(), MODE_PRIVATE);
        editor = mode.edit();
        isNightModeOn = mode.getBoolean("NightMode",false);

        if(isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch modeSwitch = findViewById(R.id.darkModeSwitch);
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
        */
        logout.setOnClickListener((v) -> new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",(dialog, which) ->{
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(SettingsActivity.this, IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show());

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle(R.string.change_password);
                final EditText oldPass = new EditText(SettingsActivity.this);
                final EditText newPass = new EditText(SettingsActivity.this);
                final EditText confirmPass = new EditText(SettingsActivity.this);

                oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                oldPass.setHint(R.string.old_password);
                newPass.setHint(R.string.new_password);
                confirmPass.setHint(R.string.confirm_password);
                LinearLayout ll=new LinearLayout(SettingsActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(oldPass);

                ll.addView(newPass);
                ll.addView(confirmPass);
                alertDialog.setView(ll);
                alertDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String newPassword= newPass.getText().toString();
                                String oldPassword= oldPass.getText().toString();
                                String confirmPassword= confirmPass.getText().toString();
                                if(oldPassword==null || oldPassword.isEmpty()){
                                    oldPassword = " ";
                                }
                                if(newPassword==null || newPassword.isEmpty()){
                                    oldPassword = "      ";
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String email = user.getEmail();
                                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            if(!newPassword.equals(confirmPassword)) {
                                                Toast.makeText(SettingsActivity.this, R.string.passwords_do_not_match, Toast.LENGTH_LONG).show();

                                            }else{
                                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(!task.isSuccessful()){
                                                            Toast.makeText(SettingsActivity.this,R.string.something_went_wrong,Toast.LENGTH_LONG).show();
                                                            dialog.cancel();
                                                        }else {
                                                                Toast.makeText(SettingsActivity.this, R.string.password_successfully_modified, Toast.LENGTH_LONG).show();
                                                                dialog.cancel();
                                                        }
                                                    }
                                                });
                                            }
                                        }else {
                                            Toast.makeText(SettingsActivity.this,R.string.make_sure_you_enter_your_password_correctly,Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String lang = sharedPreferences.getString("lang","");
                /*
                System.out.println("lang locale: "+ Locale.getDefault().getLanguage());
                System.out.println("lang sharedPref: "+ lang);
                */
                new AlertDialog.Builder(SettingsActivity.this)
                        .setMessage(R.string.choose_your_language)
                        .setCancelable(false)
                        .setPositiveButton("ENG",(dialog, which) ->{

                            if(!lang.equals("en")){

                                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor=saved_values.edit();
                                editor.putString("lang","en");
                                editor.commit();


                                Locale myLocale = new Locale("en");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.setLocale(myLocale);
                                //conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                                Intent refresh = new Intent(SettingsActivity.this, IntroActivity.class);
                                finish();
                                startActivity(refresh);

                            }
                        })
                        .setNegativeButton("TR",(dialog, which) ->{
                            if(!lang.equals("tr")){

                                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor=saved_values.edit();
                                editor.putString("lang","tr");
                                editor.commit();

                                Locale myLocale = new Locale("tr");
                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Configuration conf = res.getConfiguration();
                                conf.locale = myLocale;
                                res.updateConfiguration(conf, dm);
                                Intent refresh = new Intent(SettingsActivity.this, IntroActivity.class);
                                finish();
                                startActivity(refresh);

                            }

                        })
                        .show();
            }
        });
    }

}