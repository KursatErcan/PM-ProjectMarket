package com.kursat.pm_projectmarket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kursat.pm_projectmarket.Model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class EditProfileActivity extends AppCompatActivity {
    ImageView imageView_profilePhoto;
    Uri imageData;
    Bitmap selectedImage;
    EditText editText_name, editText_mailAddress;
    String profilePhoto_Url;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView bioTw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        bioTw=findViewById(R.id.editText_bio);
        imageView_profilePhoto = findViewById(R.id.profileImage_editProfile);
        editText_name = findViewById(R.id.editText_userName);
        editText_mailAddress = findViewById(R.id.editText_mail);


        userInfo();




    }

    public void updateProfile(View view){
        UploadFile();
    }

    public void deleteProfile(View view){ }
/*
    public void deleteProfile(View view){
        new AlertDialog.Builder(this)
        .setMessage("Are you sure you want to deleting account?")
        .setCancelable(false)
                .setPositiveButton("Yes",(dialog, which) ->{
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("We are sorry you left us. We hope you will join us again..");
                    progressDialog.show();

                    db.collection("Posts").whereEqualTo("userId",user.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("Posts").document(document.getId()).delete();
                                }
                                Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                db.collection("Users").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        //Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(intent);
                                    }
                                });
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                        }
                                    }
                                });

                            }
                        }
                    });

                })
                .setNegativeButton("No", null)
                .show();


    }
*/
    private void userInfo(){

        DocumentReference reference = db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

        reference.addSnapshotListener((value, error) -> {
            if(error == null){
                if(getContext() == null){ return; }
                if(value != null){
                    User user = value.toObject(User.class);
                    assert user != null;
                    Picasso.get().load(user.getProfileImageUrl()).into(imageView_profilePhoto);
                    editText_name.setText(user.getUserName());
                    editText_mailAddress.setText(user.getEmail());
                    bioTw.setText(user.getBio());
                }

            }
        });
    }

    public void uploadImage(View view){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);//get image type files
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                imageData = data.getData();

                Picasso.get().load(imageData)
                        .resize(imageView_profilePhoto.getWidth(),imageView_profilePhoto.getHeight())
                        .into(imageView_profilePhoto);
                //postImage.setImageURI(imageData);

            }
        }
    }

    public String getFileExtension(Uri iUri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(iUri));
    }

    public void UploadFile(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref",MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");//this field is also be updated
        HashMap<String,String> postData = new HashMap<>();
        if(userName !=editText_name.getText().toString()){
            postData.put("userName",editText_name.getText().toString());
            userName = sharedPreferences.getString("userName", editText_name.getText().toString());//this field is also be updated
            db.collection("Posts").whereEqualTo("userId",user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                HashMap<String,String> user=new HashMap<>();
                                user.put("userName",editText_name.getText().toString());
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("Posts").document(document.getId())
                                    .set(user,SetOptions.merge());
                                }
                            } else {
                                Log.d("EditProfileActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        postData.put("bio",bioTw.getText().toString());
        postData.put("email",editText_mailAddress.getText().toString());
        if(imageData !=null){
            StorageReference fileReference= storageReference.child("profileImages/"+System.currentTimeMillis()+"."+getFileExtension(imageData));
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask2 = fileReference.putBytes(data);
                uploadTask2.addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        postData.put("profileImageUrl",downloadUrl);
                        db.collection("Users").document(user.getUid())
                                .set(postData,SetOptions.merge());
                        Toast.makeText(EditProfileActivity.this, "Your profile is successfully updated!", Toast.LENGTH_LONG).show();

                    });


                }).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show());


            } catch (IOException e) {
                e.printStackTrace();
            }}else{
            //image is not selected.

            db.collection("Users").document(user.getUid())
                    .set(postData, SetOptions.merge());
            Toast.makeText(EditProfileActivity.this, "Your profile is successfully updated!", Toast.LENGTH_LONG).show();

        }
    }

}