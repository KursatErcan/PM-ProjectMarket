package com.kursat.pm_projectmarket.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kursat.pm_projectmarket.MainActivity;
import com.kursat.pm_projectmarket.PostActivity;
import com.kursat.pm_projectmarket.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment {

    ImageView imageClose,postImage;
    EditText title,priceText;
    TextView shareText;
    Bitmap selectedImage;
    Uri imageData;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        imageClose = (ImageView) view.findViewById(R.id.close);
        postImage = view.findViewById(R.id.post_image);
        title = view.findViewById(R.id.text_title);
        priceText = view.findViewById(R.id.text_price);
        shareText = view.findViewById(R.id.text_share);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");



        // Inflate the layout for this fragment
        return view;
    }
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageData);
                }
                postImage.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void uploadImage(View view){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentToGallery.setType("image/*");
            //startActivityForResult(intentToGallery,2);
            startActivityForResult(Intent.createChooser(intentToGallery,"select image"),2);

        }
    }

    public void onShareClick(View view){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading..");
        progressDialog.show();

        if(imageData != null) {

            UUID postId = UUID.randomUUID();
            String imageName = "postImages/" + postId + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(taskSnapshot -> { //image'i firebase'e yükleme basarılı
                StorageReference newReference = FirebaseStorage.getInstance().getReference("Images/"+imageName);
                newReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();

                    String userId = firebaseAuth.getCurrentUser().getUid();
                    String titleText = title.getText().toString();
                    String price = priceText.getText().toString();
                    HashMap<String,Object> postData = new HashMap<>();
                    postData.put("postId",postId.toString());
                    postData.put("userId",userId);
                    postData.put("postImageUrl",downloadUrl);
                    postData.put("title",titleText);
                    postData.put("price",price);
                    postData.put("date", FieldValue.serverTimestamp());

                    db.collection("Posts").add(postData).addOnSuccessListener(documentReference -> {
                        /*progressDialog.dismiss();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();*/

                    }).addOnFailureListener(e -> Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show());

                });
            }).addOnFailureListener(e -> Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show());

        }
    }
}