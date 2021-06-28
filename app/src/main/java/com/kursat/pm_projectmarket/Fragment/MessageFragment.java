package com.kursat.pm_projectmarket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.MessageBoxAdapter;
import com.kursat.pm_projectmarket.MessagesActivity;
import com.kursat.pm_projectmarket.Model.MessageBox;
import com.kursat.pm_projectmarket.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MessageFragment extends Fragment implements MessageBoxAdapter.OnMessageListener{

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    RecyclerView recView;
    ArrayList<MessageBox> MessageBox;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    MessageBoxAdapter Adapter;
    TextView MsgTw;
    String[] messageUser;
    TextView cardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Buradan devam
        View view = inflater.inflate(R.layout.fragment_messagebox, container, false);
        recView=(RecyclerView) view.findViewById(R.id.messagebox_recycleview);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageBox= new ArrayList<>();
        Adapter=new MessageBoxAdapter(MessageBox,this);
        recView.setAdapter(Adapter);
        MsgTw = view.findViewById(R.id.MessageBoxTw);
        cardView=view.findViewById(R.id.text_view_MessageContent);
        messageUser=new String[100];
        if(user!=null) {
            db = FirebaseFirestore.getInstance();
            db.collection("Messages")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            int counter=0;
                            for (QueryDocumentSnapshot doc1 : value) {
                                if(value.isEmpty())
                                    MsgTw.setText("Mesaj kutunuz boş.");
                                //System.out.println(user.getUid());
                                if(doc1.get("message_received").toString().equals(user.getUid()) ||
                                        doc1.get("message_posted").toString().equals(user.getUid())){

                                    if(doc1.get("message_received").toString().equals(user.getUid()))
                                        messageUser[counter]=doc1.get("message_posted_name").toString();
                                    else
                                        messageUser[counter]=doc1.get("message_received_name").toString();
                                    counter++;

                                    db.collection("Messages/"+doc1.getId()+"/Message_details")
                                            .orderBy("date", Query.Direction.DESCENDING)
                                            .limit(1)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value,
                                                                    @Nullable FirebaseFirestoreException e) {
                                                    if (e != null) {
                                                        return;
                                                    }
                                                    if(value.isEmpty())
                                                        MsgTw.setText("Mesaj kutunuz boş.");

                                                    for (QueryDocumentSnapshot doc : value) {
                                                        String date=getDate((Timestamp) doc.get("date"));
                                                        MessageBox.add(new MessageBox(date,
                                                                doc.get("content").toString(),
                                                                doc.get("senderId").toString(),
                                                                doc.get("senderName").toString(),
                                                                doc.get("isRead").toString(),
                                                                doc1.getId()));

                                                        //if(doc.get("message_viewed").toString().equals("0")){
                                                        //System.out.println(cardView.getCurrentTextColor()+"-----------<");

                                                        //}

                                                    }
                                                    Adapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                            }
                            Adapter.notifyDataSetChanged();
                        }
                    });
        }
        else{
            //No one is signed in

        }



        return view;
    }
    public void onMessageClick(int position) {
        Intent intent=new Intent(getActivity(), MessagesActivity.class);
        intent.putExtra("token",MessageBox.get(position).getToken());
        intent.putExtra("userN",messageUser[position]);
        startActivity(intent);

    }
    private String getDate(Timestamp milliSeconds) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( milliSeconds.getSeconds()*1000);
        return formatter.format(calendar.getTime());
    }
}