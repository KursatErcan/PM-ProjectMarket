package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.messageBox_item,parent,false);
        return new MessageBoxViewHolder(view,msgListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageBoxAdapter.MessageBoxViewHolder holder, int position) {
        MessageBox currentItem = MessageBox.get(position);
        holder.Received.setText(currentItem.getDetail());
        holder.MessageContent.setText(currentItem.getMessageContent());
        holder.MessageDate.setText(currentItem.getMessageDate());
    }

    @Override
    public int getItemCount() {
        return MessageBox.size();
    }

    public static class MessageBoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnMessageListener msgListener;
        public TextView Received;
        public TextView MessageContent;
        public TextView MessageDate;

        public MessageBoxViewHolder(@NonNull View itemView , OnMessageListener msgListener) {
            super(itemView);
            this.msgListener=msgListener;
            Received=itemView.findViewById(R.id.text_view_Received);
            MessageDate=itemView.findViewById(R.id.text_view_MessageDate);
            MessageContent=itemView.findViewById(R.id.text_view_MessageContent);
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
