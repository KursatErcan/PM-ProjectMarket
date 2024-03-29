package com.kursat.pm_projectmarket;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private ImageView postImage;
    private EditText title,postContent,priceText;
    private TextView shareText;
    private Uri imageData;
    private FirebaseFirestore db;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference;
    private CollectionReference cfr;

    ListView lv_Categories;
    private Integer categoryEntryControl = 0;
    private Integer[] categoryArr;
    static final int SELECTED_IMAGE=1;

    public PostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        lv_Categories=findViewById(R.id.lv_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories_array,android.R.layout.simple_list_item_multiple_choice);
        lv_Categories.setAdapter(adapter);

        postImage = findViewById(R.id.post_image);
        title = findViewById(R.id.text_title);
        postContent = findViewById(R.id.text_postContent);
        priceText = findViewById(R.id.text_price);
        shareText = findViewById(R.id.text_share);
        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Images");

    }
    public void getCategoryInfo(){
        SparseBooleanArray checked = lv_Categories.getCheckedItemPositions();
        categoryArr=new Integer[checked.size()+1];
        categoryArr[0]=0;
        categoryEntryControl=0;
        for(int i=0;i<lv_Categories.getCount();i++){
            if(lv_Categories.isItemChecked(i)){
                categoryEntryControl++;
                categoryArr[categoryEntryControl] = i+1;
            }
        }
    }

    public void onShareClick(View view){
        //when click share button, this function is triggered
        getCategoryInfo();
        UploadFile();
    }

    public void uploadImage(View v){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,SELECTED_IMAGE);//get image type files
    }

    public void onCloseClick(View view){
        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECTED_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                imageData = data.getData();

                Picasso.get().load(imageData)
                        .resize(postImage.getWidth(),postImage.getHeight())
                        .into(postImage);
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
                        postData.put("postContent",postContent.getText().toString());
                        postData.put("userId", user.getUid());
                        postData.put("userName",userName);
                        postData.put("postCategory",Arrays.asList(categoryArr));
                        postData.put("score",0.0);
                        System.out.println(postData+"--------------<");
                        if(postData.containsValue(null) || postData.containsValue("") ||
                                priceText.getText().toString().isEmpty() ||
                                postContent.getText().toString().isEmpty() ||
                                title.getText().toString().isEmpty() ||
                                categoryEntryControl==0){
                            Toast.makeText(PostActivity.this,R.string.you_must_fill_in_the_required_fields,Toast.LENGTH_SHORT).show();
                        }else {
                            //add the data into the Posts
                            Toast.makeText(PostActivity.this, R.string.your_post_is_successfully_posted, Toast.LENGTH_LONG).show();
                            cfr = db.collection("Posts");
                            cfr.add(postData);
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }).addOnFailureListener(e -> Toast.makeText(PostActivity.this, R.string.your_post_is_successfully_posted + "" + e, Toast.LENGTH_LONG).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(PostActivity.this,R.string.you_must_fill_in_the_required_fields,Toast.LENGTH_SHORT).show();
        }
    }
}