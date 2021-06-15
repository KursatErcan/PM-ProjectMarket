package com.kursat.pm_projectmarket.Adapter;

import android.annotation.SuppressLint;
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
    private OnLongClickListener LongMsgListener;
    private String currency;
    public ProfilePostsAdapter(ArrayList<Post> post, OnMessageListener msgListener, OnLongClickListener LongMsgListener) {
        this.post=post;
        this.msgListener=msgListener;
        this.LongMsgListener=LongMsgListener;

    }

    @NonNull
    @Override
    public ProfilePostsAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item1,parent,false);
        currency = " "+view.getContext().getResources().getString(R.string.tl);
        return new PostHolder(view,msgListener,LongMsgListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProfilePostsAdapter.PostHolder holder, int position) {
        Post currentItem = post.get(position);
        //holder.text_userName.setText(currentItem.getUserName());
        holder.text_title.setText(currentItem.getTitle());
        holder.text_price.setText(currentItem.getPrice()+currency);
        Picasso.get().load(currentItem.getPostImageUrl())
                .resize(240,150)
                .into(holder.imageView_postImage);
    } 

    @Override
    public int getItemCount() { return post.size(); }

    static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        //TextView text_userName;
        TextView text_title;
        ImageView imageView_postImage;
        TextView text_price;
        OnMessageListener msgListener;
        OnLongClickListener LongMsgListener;

        public PostHolder(@NonNull View itemView , OnMessageListener msgListener,OnLongClickListener LongMsgListener) {
            super(itemView);
            this.LongMsgListener=LongMsgListener;
            this.msgListener=msgListener;
            //text_userName = itemView.findViewById(R.id.text_userName_postElement);
            text_title = itemView.findViewById(R.id.text_postTitle_postElement);
            imageView_postImage = itemView.findViewById(R.id.imageView_postImage_postElement);
            text_price = itemView.findViewById(R.id.text_price_postElement);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            msgListener.onMessageClick(getAdapterPosition());
        }
        public boolean onLongClick(View v) {
            LongMsgListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnMessageListener{
        void onMessageClick(int position);
    }
    public interface OnLongClickListener{
        void onLongClick(int position);
    }

}