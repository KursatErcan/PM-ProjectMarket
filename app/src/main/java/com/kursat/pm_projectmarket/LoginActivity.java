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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    EditText emailText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.editText_email);
        passwordText = findViewById(R.id.editText_password);
/*
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
  */
    }
    public void  signInClicked(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this,"Please, fill in all fields!",Toast.LENGTH_LONG).show();
        }else if(password.length()<6){
            Toast.makeText(LoginActivity.this,"Password must be longer than 5 characters!",Toast.LENGTH_LONG).show();
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

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Opps! Something went wrong.",Toast.LENGTH_LONG).show();
            }
        });

    }
}