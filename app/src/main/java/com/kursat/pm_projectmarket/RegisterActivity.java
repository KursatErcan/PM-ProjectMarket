package com.kursat.pm_projectmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    EditText userNameText,emailText,passwordText;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameText = findViewById(R.id.editText_userName);
        emailText = findViewById(R.id.editText_email);
        passwordText = findViewById(R.id.editText_password);

        progressDialog = new ProgressDialog(this);
    }
    public void backToSignIn(View view){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void signUpClicked(View view){
        String userName = userNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please, fill in all fields!",Toast.LENGTH_LONG).show();
        }else if(password.length()<6){
            Toast.makeText(RegisterActivity.this,"Password must be longer than 5 characters!",Toast.LENGTH_LONG).show();
        }else {
            registerUser(userName, email, password);
        }
    }
    private void registerUser(String userName, String email, String password){
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            createUser(userName, email);
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
    }
    private void createUser(final String userName, final String email){
        String userId = (String) firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        //user.put("userId",userId);
        user.put("userName", userName);
        user.put("email", email);
        user.put("bio", "");
        user.put("profileImageUrl","https://firebasestorage.googleapis.com/v0/b/pm-projectmarket.appspot.com/o/placeHolder.jpg?alt=media&token=d5510906-ad33-49e0-9be4-ad6446bd21e6");
        db.collection("Users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    /* Email Verification */
                    /*firebaseAuth.getCurrentUser().sendEmailVerification()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this,"Verify your email address! (Don't forget to check your spam folder!)",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(e-> Toast.makeText(RegisterActivity.this,"Verify your email address! (Don't forget to check your spam folder!)",Toast.LENGTH_LONG).show());
                    */

                    progressDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                });

    }
}