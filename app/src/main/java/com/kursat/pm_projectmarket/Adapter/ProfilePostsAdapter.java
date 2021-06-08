package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.PostHolder>{

    public ArrayList<Post> post;
    private OnMessageListener msgListener;

    public ProfilePostsAdapter(ArrayList<Post> post, OnMessageListener msgListener) {
        this.post=post;
        this.msgListener=msgListener;

    }

    @NonNull
    @Override
    public ProfilePostsAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item,parent,false);

        return new PostHolder(view,msgListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostsAdapter.PostHolder holder, int position) {
        Post currentItem = post.get(position);
        holder.text_userName.setText(currentItem.getUserName());
        holder.text_title.setText(currentItem.getTitle());
        holder.text_price.setText(currentItem.getPrice());
        Picasso.get().load(currentItem.getPostImageUrl())
                .resize(240,150)
                .into(holder.imageView_postImage);
    } 

    @Override
    public int getItemCount() { return post.size(); }

    static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text_userName;
        TextView text_title;
        ImageView imageView_postImage;
        TextView text_price;
        OnMessageListener msgListener;

        public PostHolder(@NonNull View itemView , OnMessageListener msgListener) {
            super(itemView);

            this.msgListener=msgListener;
            text_userName = itemView.findViewById(R.id.text_userName_postElement);
            text_title = itemView.findViewById(R.id.text_postTitle_postElement);
            imageView_postImage = itemView.findViewById(R.id.imageView_postImage_postElement);
            text_price = itemView.findViewById(R.id.text_price_postElement);
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