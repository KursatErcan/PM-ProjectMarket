package com.kursat.pm_projectmarket;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    TextInputLayout emailText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.editText_email);
        passwordText = findViewById(R.id.editText_password);

        /*FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            //Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", Pattern.CASE_INSENSITIVE);
        //Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    public void  signInClicked(View view){

        emailText.setErrorEnabled(false);
        emailText.setError("");
        passwordText.setErrorEnabled(false);
        passwordText.setError("");

        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // regex
        //String emailpattern = "^(.+)@(.+)$";
        boolean isValidEmail = false, isValidPass = false;
        String email = emailText.getEditText().getText().toString().trim();
        String password = passwordText.getEditText().getText().toString().trim();

        /*if(TextUtils.isEmpty(email)){
            //isValid = false;
            //Toast.makeText(LoginActivity.this,"Please, fill in all fields!",Toast.LENGTH_LONG).show();
            emailText.setErrorEnabled(true);
            emailText.setError("Email is required!");
        }else{
            //emailpattern.matches(email)
            //validate(email)
            if(emailpattern.matches(email)){
                isValidEmail = true;
            }else{
                //isValid = false;
                emailText.setErrorEnabled(true);
                emailText.setError("Check your email address!");
            }
        }
        if(TextUtils.isEmpty(password)){
            isValidPass = false;
            //Toast.makeText(LoginActivity.this,"Password must be longer than 5 characters!",Toast.LENGTH_LONG).show();
            passwordText.setErrorEnabled(true);
            passwordText.setError("Password is required!");
        }else{
            isValidPass = true;
        }

        if (isValidEmail && isValidPass){
            loginUser(email,password);
        }*/

        //if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
        if(TextUtils.isEmpty(email)) {
            //Toast.makeText(LoginActivity.this,"Please, fill in all fields!",Toast.LENGTH_LONG).show();
            emailText.setErrorEnabled(true);
            emailText.setError("Email is required!");
            //passwordText.setErrorEnabled(true);
            //passwordText.setError("Password is required!");
        }else if(password.length()<6){
            //Toast.makeText(LoginActivity.this,"Password must be longer than 5 characters!",Toast.LENGTH_LONG).show();
            passwordText.setErrorEnabled(true);
            passwordText.setError("Password lenght cannot be shorter than 6.");
        }else if(validate(email)){//email.matches(emailpattern)){
            emailText.setErrorEnabled(true);
            emailText.setError("Check your email address!");
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

            Toast.makeText(LoginActivity.this,"Welcome!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,"Opps! Something went wrong.",Toast.LENGTH_LONG).show());

    }
}