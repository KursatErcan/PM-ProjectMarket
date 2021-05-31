package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.MessageSend;
import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostHolder>{

    public ArrayList<Post> post;
    private OnMessageListener msgListener;

    public PostRecyclerAdapter(ArrayList<Post> post, OnMessageListener msgListener) {
        this.post=post;
        this.msgListener=msgListener;

    }

    @NonNull
    @Override
    public PostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item,parent,false);

        return new PostHolder(view,msgListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerAdapter.PostHolder holder, int position) {
        Post currentItem = post.get(position);
        holder.text_userName.setText(currentItem.getUserName());
        holder.text_title.setText(currentItem.getTitle());
        holder.text_price.setText(currentItem.getPrice());
        Picasso.get().load(currentItem.getPostImageUrl()).into(holder.imageView_profileImage);
        Picasso.get().load(currentItem.getPostImageUrl()).into(holder.imageView_postImage);
        //System.out.println("PostRcyclerAdapter :: UserName => " + userNameList.get(position));
        //System.out.println("PostRcyclerAdapter :: Title => " + titleList.get(position));

    }

    @Override
    public int getItemCount() { return post.size(); }

    static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text_userName;
        TextView text_title;
        ImageView imageView_profileImage;
        ImageView imageView_postImage;
        TextView text_price;
        OnMessageListener msgListener;

        public PostHolder(@NonNull View itemView , OnMessageListener msgListener) {
            super(itemView);

            this.msgListener=msgListener;
            text_userName = itemView.findViewById(R.id.text_userName_postElement);
            text_title = itemView.findViewById(R.id.text_postTitle_postElement);
            imageView_profileImage = itemView.findViewById(R.id.imageView_profileImage_postElement);
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





















/*
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>{

    public Context context;
    public List<Post> postList;

    private FirebaseAuth firebaseAuth;

    public PostRecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_element,parent,false),

        return new PostRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImage, postImage;
        public TextView userName, postTitle, price;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.imageView_profileImage);
            postImage = itemView.findViewById(R.id.post_image);
            userName = itemView.findViewById(R.id.text_userName);
            postTitle = itemView.findViewById(R.id.text_title);
            price = itemView.findViewById(R.id.text_price);
        }
    }

    private void userInfo(ImageView profileImage, TextView userName, String userId){
        DatabaseReference databaseReference = FirebaseDa
    }
}
*/