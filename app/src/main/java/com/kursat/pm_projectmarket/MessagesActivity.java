package com.kursat.pm_projectmarket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.kursat.pm_projectmarket.Adapter.MessageSendAdapter;
import com.kursat.pm_projectmarket.Model.MessageSend;

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
    String receiverName;
    String receiverId;
    String messageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages2);
        receiverName=getIntent().getStringExtra("userName");
        receiverId=getIntent().getStringExtra("userId");
        messageUser=getIntent().getStringExtra("userN");
        token=getIntent().getStringExtra("token");
        recView=(RecyclerView) findViewById(R.id.MessageSend);
        recView.setLayoutManager(new LinearLayoutManager(this));
        MessageSend= new ArrayList<>();
        Adapter=new MessageSendAdapter(MessageSend);
        recView.setAdapter(Adapter);
        msgDetail=(EditText) findViewById(R.id.MessageDetail);
        btnSend=(Button) findViewById(R.id.SendMessage);

        TextView textUser= findViewById(R.id.textViewUser);
        textUser.setText(messageUser);



        if(user!=null && token != null) {

            db = FirebaseFirestore.getInstance();
            if(token!=null){
                db.collection("Messages/"+token+"/Message_details")
                        .orderBy("date", Query.Direction.ASCENDING)
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
                                    MessageSend.add(new MessageSend(doc1.get("content").toString(),doc1.get("senderId").toString()));
                                    HashMap<String,String> hp=new HashMap<>();

                                    if(!doc1.get("senderId").toString().equals(user.getUid())) {
                                        hp.put("isRead","1");
                                        db.collection("Messages/" + token + "/Message_details").document(doc1.getId())
                                                .set(hp, SetOptions.merge());
                                    }else{
                                        hp.put("isReadMe","1");
                                        db.collection("Messages/" + token + "/Message_details").document(doc1.getId())
                                                .set(hp, SetOptions.merge());
                                    }
                                }
                                Adapter.notifyDataSetChanged();
                            }
                        });
            }
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPref",MODE_PRIVATE);
                    String userName = sharedPreferences.getString("userName", "");
                    Map<String, Object> message = new HashMap<>();
                    cfr=db.collection("Messages/"+token+"/Message_details");

                    /*message.put("message_date",new Timestamp(new Date()));
                    message.put("message_detail",msgDetail.getText().toString());
                    message.put("message_sended",userName);
                    message.put("message_sended_id",user.getUid().toString());
                    message.put("message_viewed","0");
                    message.put("message_viewed_received","0");
                    */

                    message.put("date",new Timestamp(new Date()));
                    message.put("content",msgDetail.getText().toString());
                    message.put("senderName",userName);
                    message.put("senderId",user.getUid().toString());
                    message.put("isRead","0");
                    message.put("isReadMe","0");


                    if(msgDetail.getText().toString().isEmpty())
                        Toast.makeText(MessagesActivity.this,R.string.you_can_write_your_message,Toast.LENGTH_LONG).show();
                    else{
                        cfr.add(message);
                        msgDetail.setText("");
                    }


                }
            });

        }
        else{
            //Nobody is signed in
        }
    }

}