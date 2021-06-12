package com.kursat.pm_projectmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    EditText userNameText,emailText,passwordText;
    ProgressDialog progressDialog;
    //Uri placeHolderUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        /*
        StorageReference newReference = FirebaseStorage.getInstance().getReference().child("Images/placeHolder.jpg");
        newReference.getDownloadUrl().addOnCompleteListener(this, new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    placeHolderUrl = task.getResult();
                }
            }
        });

         */

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
            Toast.makeText(RegisterActivity.this,R.string.you_must_fill_in_the_required_fields,Toast.LENGTH_LONG).show();
        }else if(password.length()<6){
            Toast.makeText(RegisterActivity.this,R.string.password_must_be_longer_than_6_characters,Toast.LENGTH_LONG).show();
        }else {
            registerUser(userName, email, password);
        }
    }
    private void registerUser(String userName, String email, String password){
        progressDialog.setMessage(getText(R.string.please_wait));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            createUser(userName, email);
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });


    }
    private void createUser(final String userName, final String email){
        String placeHolder = "https://firebasestorage.googleapis.com/v0/b/pm-projectmarket.appspot.com/o/placeHolder.jpg?alt=media&token=d5510906-ad33-49e0-9be4-ad6446bd21e6";
        String userId = (String) firebaseAuth.getCurrentUser().getUid();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(Uri.parse(placeHolder))
                .build();

        firebaseAuth.getCurrentUser().updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("kayıt tamam");
            }
        });

        Map<String, Object> user = new HashMap<>();
        //user.put("userId",userId);
        user.put("userName", userName);
        user.put("email", email);
        user.put("bio", "");
        user.put("profileImageUrl",placeHolder);
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