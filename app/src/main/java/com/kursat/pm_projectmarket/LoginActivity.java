package com.kursat.pm_projectmarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextInputLayout emailText,passwordText;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.editText_email);
        passwordText = findViewById(R.id.editText_password);

    }

    //String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    public void  signInClicked(View view){

        emailText.setErrorEnabled(false);
        emailText.setError("");
        passwordText.setErrorEnabled(false);
        passwordText.setError("");


        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();


        if(TextUtils.isEmpty(email)) {
            emailText.setErrorEnabled(true);
            emailText.setError(getText(R.string.email_is_required));
        }else if(password.length()<6){
            passwordText.setErrorEnabled(true);
            passwordText.setError(getText(R.string.password_lenght_cannot_be_shorter_than_6));
        }else if(validate(email)){//email.matches(emailpattern)){
            emailText.setErrorEnabled(true);
            emailText.setError(getText(R.string.check_your_email_address));
        }else{
            loginUser(email,password);
        }

    }
    public void signUpClicked(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {

            /* Email Verification */
            /*if(firebaseAuth.getCurrentUser().isEmailVerified()){
                Toast.makeText(LoginActivity.this,"Welcome!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,"You haven't verified your email yet!",Toast.LENGTH_LONG).show();
            }*/

            //Where the userName get from



            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editPref = sharedPreferences.edit();
                            editPref.putString("userName", document.get("userName").toString());
                            editPref.putString("profileImageUrl",document.get("profileImageUrl").toString());
                            editPref.commit();
                        } else {
                            Log.d("LoginActivity", "No such document");
                        }
                    } else {
                        Log.d("LoginActivity", "get failed with ", task.getException());
                    }
                }
            });




            Toast.makeText(LoginActivity.this,R.string.welcome,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,R.string.opps,Toast.LENGTH_LONG).show());

    }
}