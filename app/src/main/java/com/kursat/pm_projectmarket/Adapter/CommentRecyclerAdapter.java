package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.Comment;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder>{

    public ArrayList<Comment> comment;
    OnMessageListener msgListener;

    public CommentRecyclerAdapter(ArrayList<Comment> comment, OnMessageListener msgListener){
        this.comment=comment;
        this.msgListener=msgListener;
    }


    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentHolder(view,msgListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentHolder holder, int position) {
        Comment currentItem = comment.get(position);
        holder.text_title.setText(currentItem.getPostTitle());
        holder.text_comment.setText(currentItem.getComment());
        holder.score_rb.setRating(Float.parseFloat(currentItem.getScore()));
    }

    @Override
    public int getItemCount() {
        return comment.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text_title;
        TextView text_comment;
        RatingBar score_rb;
        OnMessageListener msgListener;
        public CommentHolder(@NonNull View itemView , OnMessageListener msgListener) {

            super(itemView);
            this.msgListener=msgListener;
            score_rb=itemView.findViewById(R.id.ratingBar_score_commentItem);
            text_title = itemView.findViewById(R.id.textView_postTitle_commentItem);
            text_comment = itemView.findViewById(R.id.textView_comment_commentItem);

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
