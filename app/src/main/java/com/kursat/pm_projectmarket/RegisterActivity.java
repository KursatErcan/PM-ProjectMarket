package com.kursat.pm_projectmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    EditText userNameText,emailText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameText = findViewById(R.id.editText_userName);
        emailText = findViewById(R.id.editText_email);
        passwordText = findViewById(R.id.editText_password);

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
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_LONG).show();
                    createUser(userName, email);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
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
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}