package com.kursat.pm_projectmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.MessageBoxAdapter;
import com.kursat.pm_projectmarket.Adapter.MessageSendAdapter;
import com.kursat.pm_projectmarket.Model.MessageBox;
import com.kursat.pm_projectmarket.Model.MessageSend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MessagesActivity extends AppCompatActivity {
    RecyclerView recView;
    ArrayList<MessageSend> MessageSend;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    MessageSendAdapter Adapter;
    String token;
    Button btnSend;
    EditText msgDetail;
    CollectionReference cfr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages2);
        token=getIntent().getStringExtra("token");
        recView=(RecyclerView) findViewById(R.id.MessageSend);
        recView.setLayoutManager(new LinearLayoutManager(this));
        MessageSend= new ArrayList<>();
        Adapter=new MessageSendAdapter(MessageSend);
        recView.setAdapter(Adapter);
        msgDetail=(EditText) findViewById(R.id.MessageDetail);
        btnSend=(Button) findViewById(R.id.SendMessage);


        if(user!=null) {
            db = FirebaseFirestore.getInstance();
            db.collection("Messages/"+token+"/Message_details")
                    .orderBy("message_date", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            MessageSend.clear();
                            for (QueryDocumentSnapshot doc1 : value) {
                                //Buradan String message_sended, String message_detail, String message_date, String message_sended_id
                                MessageSend.add(new MessageSend(doc1.get("message_detail").toString(),doc1.get("message_sended_id").toString()));


                            }
                            Adapter.notifyDataSetChanged();
                        }

                    });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> message = new HashMap<>();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    cfr=db.collection("Messages/"+token+"/Message_details");
                    DocumentReference docRef = db.collection("Users").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    message.put("message_date",formatter.format(date).toString());
                                    message.put("message_detail",msgDetail.getText().toString());
                                    message.put("message_sended",document.get("userName").toString());
                                    message.put("message_sended_id",user.getUid().toString());
                                    cfr.add(message);

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                }
            });

        }
        else{
            //Nobody is signed in

        }

    }
}