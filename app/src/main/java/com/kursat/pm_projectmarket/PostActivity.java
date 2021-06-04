package com.kursat.pm_projectmarket;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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
    CheckBox chkDigitalData;
    CheckBox chkDigitalMarket;
    CheckBox chkGraphics;
    CheckBox chkVideo;
    CheckBox chkWriting;
    CheckBox chkProgramming;
    HashMap<String,String> array;


    public PostActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        chkDigitalData=findViewById(R.id.chkData);
        chkGraphics=findViewById(R.id.chkGraphics);
        chkVideo=findViewById(R.id.chkVideo);
        chkWriting=findViewById(R.id.chkWriting);
        chkProgramming=findViewById(R.id.chkProgramming);
        chkDigitalMarket=findViewById(R.id.chkDigitalMarket);
        array = new HashMap<>();
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

        if(requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                imageData = data.getData();

                postImage.setImageURI(imageData);

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
        String userName = sharedPreferences.getString("userName", "");
        if(imageData !=null){
            StorageReference fileReference= storageReference.child("postImages/"+System.currentTimeMillis()+"."+getFileExtension(imageData));
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask2 = fileReference.putBytes(data);
                uploadTask2.addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        HashMap<String,Object> postData = new HashMap<>();
                        postData.put("date",new Timestamp(new Date()));
                        postData.put("postImageUrl", downloadUrl);
                        postData.put("price",priceText.getText().toString());
                        postData.put("title",title.getText().toString());
                        postData.put("userId", user.getUid());
                        postData.put("userName",userName);
                        postData.put("postCategory",array);
                        //add the data into the Posts
                        cfr=db.collection("Posts");
                        cfr.add(postData);




                    });

                    Toast.makeText(PostActivity.this, "Your post is successfully posted!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();

                }).addOnFailureListener(e -> Toast.makeText(PostActivity.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show());


            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(PostActivity.this,"Olmadi !",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public HashMap<String,String> onCheckboxClicked(View view){
        //selected list of category
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.chkData:
                if (checked)
                    array.put("chkData",((CheckBox) view).getText().toString());
                else
                    array.remove("chkData");
                break;
            case R.id.chkDigitalMarket:
                if (checked)
                    array.put("chkDigitalMarket",((CheckBox) view).getText().toString());
                else
                    array.remove("chkDigitalMarket");
                break;
            case R.id.chkGraphics:
                if (checked)
                    array.put("chkGraphics",((CheckBox) view).getText().toString());
                else
                    array.remove("chkGraphics");
                    break;
            case R.id.chkVideo:
                if (checked)
                    array.put("chkVideo",((CheckBox) view).getText().toString());
                else
                    array.remove("chkVideo");
                    break;
            case R.id.chkWriting:
                if (checked)
                    array.put("chkWriting",((CheckBox) view).getText().toString());
                else
                    array.remove("chkWriting");
                    break;
            case R.id.chkProgramming:
                if (checked)
                    array.put("chkProgramming",((CheckBox) view).getText().toString());
                else
                    array.remove("chkProgramming");
                    break;
        }

        return array;
    }

}