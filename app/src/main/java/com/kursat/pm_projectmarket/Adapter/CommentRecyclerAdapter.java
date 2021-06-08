package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.Comment;
import com.kursat.pm_projectmarket.R;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder>{

    public ArrayList<Comment> comment;

    public CommentRecyclerAdapter(ArrayList<Comment> comment){
        this.comment=comment;
    }


    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentHolder holder, int position) {
        Comment currentItem = comment.get(position);
        holder.text_title.setText(currentItem.getTitle());
        holder.text_comment.setText(currentItem.getComment());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    static class CommentHolder extends RecyclerView.ViewHolder {

        TextView text_title;
        TextView text_comment;

        public CommentHolder(@NonNull View itemView ) {
            super(itemView);

            text_title = itemView.findViewById(R.id.textView_postTitle_commentItem);
            text_comment = itemView.findViewById(R.id.textView_comment_commnetItem);

        }

    }
}
