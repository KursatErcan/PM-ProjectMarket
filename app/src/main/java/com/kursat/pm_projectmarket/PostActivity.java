package com.kursat.pm_projectmarket;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
    private ImageView postImage;
    private EditText title,priceText;
    private TextView shareText;
    private Bitmap selectedImage;
    private Uri imageData;
    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference;
    CollectionReference cfr;
    Picasso picasso;
    private Spinner spinner;
    final String[] categoryId = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postImage = findViewById(R.id.post_image);
        title = findViewById(R.id.text_title);
        priceText = findViewById(R.id.text_price);
        shareText = findViewById(R.id.text_share);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");



    }


    public void onShareClick(View view){
        //when click share button, this function is triggered
        UploadFile();

    }


    public void uploadImage(View v){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);//get image type files
    }


    public void onCloseClick(View view){
        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data.getData() != null){
            imageData=data.getData();

            postImage.setImageURI(imageData);

        }
    }

    public String getFileExtension(Uri iUri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(iUri));
    }

    public void UploadFile(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref",MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        if(imageData !=null){
            StorageReference fileReference= storageReference.child("postImages/"+System.currentTimeMillis()+"."+getFileExtension(imageData));
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask2 = fileReference.putBytes(data);
                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            HashMap<String,Object> postData = new HashMap<>();
                            postData.put("date",new Timestamp(new Date()));
                            postData.put("postImageUrl",downloadUrl.toString());
                            postData.put("price",priceText.getText().toString());
                            postData.put("title",title.getText().toString());
                            postData.put("userId",user.getUid().toString());
                            postData.put("userName",userName);
                            //add the data into the Posts
                            cfr=db.collection("Posts");
                            cfr.add(postData);


                        });

                        Toast.makeText(PostActivity.this, "Your post is successfully posted!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(PostActivity.this,"Olmadi !",Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> onCheckboxClicked(View view){
        ArrayList<String> array = new ArrayList<>();
        //selected list of category

        return array;
    }

}