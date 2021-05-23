package com.kursat.pm_projectmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    ImageView imageView_profilePhoto;
    Uri imageData;
    Bitmap selectedImage;
    EditText editText_name, editText_mailAddress;
    String profilePhoto_Url;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("Images");


        imageView_profilePhoto = findViewById(R.id.profileImage_editProfile);
        editText_name = findViewById(R.id.editText_userName);
        editText_mailAddress = findViewById(R.id.editText_mail);

        Picasso.get().load(user.getPhotoUrl()).into(imageView_profilePhoto);
        editText_name.setText(user.getDisplayName());
        editText_mailAddress.setText(user.getEmail());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }else{
                String msg = getString(R.string.gallery_requestPermission);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                }
                if (selectedImage == null) {
                    System.out.println("selected Image NULL");
                }
                else {
                    imageView_profilePhoto.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadImage(View view){
        if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditProfileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //intentToGallery.setType("image/*");
            //startActivityForResult(intentToGallery,2);
            startActivityForResult(intentToGallery,2);

        }
    }

    public void updateProfile(View view){
        ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String name = editText_name.getText().toString();
        String mailAddress = editText_mailAddress.getText().toString();

        String userId = user.getUid();
        String imageName = "profileImages/" + userId + ".jpg";


        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> { //image'i firebase'e yükleme basarılı
            StorageReference newReference = FirebaseStorage.getInstance().getReference("Images/"+imageName);
            newReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                db.collection("Users").document(userId).update(
                        "userName", name,
                        "profileImageUrl",downloadUrl);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(downloadUrl))
                        .build();

                user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("güncelleme tamam");
                    }
                });
                        /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(this,"Güncelleme Başarılı",Toast.LENGTH_SHORT).show();
                                    String msg = "Güncelleme Başarılı";
                                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                */

            progressDialog.dismiss();
            Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            });
        }).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show());


       /* UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        */


    }

}