package com.kursat.pm_projectmarket.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kursat.pm_projectmarket.Model.MessageBox;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

public class MessageBoxAdapter extends RecyclerView.Adapter<MessageBoxAdapter.MessageBoxViewHolder> {
ArrayList<MessageBox> MessageBox;
OnMessageListener msgListener;
public MessageBoxAdapter(ArrayList<MessageBox> MessageBox ,OnMessageListener msgListener){
    this.msgListener=msgListener;
    this.MessageBox=MessageBox;
}

    @NonNull
    @Override
    public MessageBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.messagebox_item,parent,false);
        return new MessageBoxViewHolder(view,msgListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageBoxAdapter.MessageBoxViewHolder holder, int position) {
        MessageBox currentItem = MessageBox.get(position);
        holder.senderName.setText(currentItem.getSenderName());
        holder.messageContent.setText(currentItem.getContent());
        holder.messageDate.setText(currentItem.getDate());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(currentItem.getSenderId().equals(user.getUid()))
            holder.messageContent.setTextColor(Color.WHITE);
        else {
            if(currentItem.isRead.equals("0"))
                holder.messageContent.setTextColor(Color.RED);
            else
                holder.messageContent.setTextColor(Color.WHITE);
        }
        //notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return MessageBox.size();
    }



    public static class MessageBoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnMessageListener msgListener;
        public TextView senderName;
        public TextView messageDate;
        public TextView messageContent;



        public MessageBoxViewHolder(@NonNull View itemView , OnMessageListener msgListener) {
            super(itemView);
            this.msgListener=msgListener;
            senderName=itemView.findViewById(R.id.text_view_senderName);
            messageDate=itemView.findViewById(R.id.text_view_MessageDate);
            messageContent=itemView.findViewById(R.id.text_view_MessageContent);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            msgListener.onMessageClick(getAdapterPosition());

        }


    }



    public interface OnMessageListener{
        void onMessageClick(int position);
    }

}
