package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kursat.pm_projectmarket.Model.MessageSend;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

public class MessageSendAdapter extends RecyclerView.Adapter<MessageSendAdapter.MessageSendViewHolder>{
    public ArrayList<MessageSend> MessageSend;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final int msg_type_left=0;
    private static final int msg_type_right=1;

    @NonNull
    @Override
    public MessageSendViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {


        if(viewType==msg_type_right) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right, parent, false);
            MessageSendViewHolder evh = new MessageSendViewHolder(v);
            return evh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left, parent, false);
            MessageSendViewHolder evh = new MessageSendViewHolder(v);
            return evh;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageSendAdapter.MessageSendViewHolder holder, int position) {
        MessageSend currentItem = MessageSend.get(position);
        holder.show_message.setText(currentItem.getMessage_detail());

    }

    @Override
    public int getItemCount() {
        return MessageSend.size();
    }

    public class MessageSendViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;

        public TextView message_sended_id;
        public MessageSendViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);


        }
    }

    public MessageSendAdapter(ArrayList<MessageSend> MessageSend) {
        this.MessageSend = MessageSend;
    }

    @Override
    public int getItemViewType(int position) {
        if(MessageSend.get(position).getMessage_sended_id().equals(user.getUid()))
        {
            return msg_type_right;
        }else
            return msg_type_left;

    }

}