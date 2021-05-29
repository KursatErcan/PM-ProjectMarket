package com.kursat.pm_projectmarket;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
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
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

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

        spinner = findViewById(R.id.category_spinner);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                db.collection("Categories").whereEqualTo("categoryName",spinner.getSelectedItem().toString())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                            //Toast.makeText(getApplicationContext(), doc.get("categoryId")+" "+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                            //doc.get("categoryId");
                            categoryId[0] = Objects.requireNonNull(doc.get("categoryId")).toString();
                            //System.out.println("1 - categoryId : " + categoryId[0]);
                            //System.out.println("2 - categoryId : " + doc.get("categoryId").toString());
                        }
                    } else {
                        System.out.println("kategorilere ulaşmakta sıkıntı var");
                    }
                });
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                }
                postImage.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    public void onCloseClick(View view){
        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }

    public void onShareClick(View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        //final String[] categoryId = new String[1];
        if(imageData != null) {

            UUID postId = UUID.randomUUID();
            String imageName = "postImages/" + postId + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> { //image'i firebase'e yükleme basarılı
                StorageReference newReference = FirebaseStorage.getInstance().getReference("Images/"+imageName);
                newReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();

                    String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    String titleText = title.getText().toString();
                    String price = priceText.getText().toString();
                    HashMap<String,Object> postData = new HashMap<>();
                    postData.put("postId",postId.toString());
                    postData.put("userId",userId);
                    postData.put("postImageUrl",downloadUrl);
                    postData.put("title",titleText);
                    postData.put("price",price);
                    postData.put("date", FieldValue.serverTimestamp());
                    postData.put("categoryId", categoryId[0]);


                    db.collection("Posts").add(postData).addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }).addOnFailureListener(e -> Toast.makeText(PostActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show());

                });
            }).addOnFailureListener(e -> Toast.makeText(PostActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show());

        }
    }


}