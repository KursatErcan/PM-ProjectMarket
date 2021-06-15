package com.kursat.pm_projectmarket.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements MessageBoxAdapter.OnMessageListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    RecyclerView recView;
    ArrayList<MessageBox> MessageBox;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    MessageBoxAdapter Adapter;
    TextView MsgTw;
    String messageUser;
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


                            for (QueryDocumentSnapshot doc1 : value) {

                                System.out.println(user.getUid());
                                if(doc1.get("message_received").equals(user.getUid()) || doc1.get("message_posted").equals(user.getUid())){
                                    if(doc1.get("message_received").equals(user.getUid()))
                                        messageUser=doc1.get("message_posted_name").toString();
                                    else
                                        messageUser=doc1.get("message_received_name").toString();

                                    db.collection("Messages/"+doc1.getId()+"/Message_details")
                                            .orderBy("message_date", Query.Direction.DESCENDING)
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
                                                        String date=getDate((Timestamp) doc.get("message_date"));

                                                        MessageBox.add(new MessageBox(date, doc.get("message_sended").toString(), doc.get("message_detail").toString(), doc1.getId()));

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
        intent.putExtra("userN",messageUser);
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