package com.kursat.pm_projectmarket.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.Post;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostHolder> implements Filterable {

    public ArrayList<Post> post;
    public ArrayList<Post> postF;
    private OnMessageListener msgListener;
    private String currency;

    public PostRecyclerAdapter(ArrayList<Post> post, OnMessageListener msgListener) {
        this.post=post;
        postF=new ArrayList<>(post);
        System.out.println(post+"-----------------------<");
        this.msgListener=msgListener;


    }

    @NonNull
    @Override
    public PostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item,parent,false);
        currency = " "+view.getContext().getResources().getString(R.string.tl);
        return new PostHolder(view,msgListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostRecyclerAdapter.PostHolder holder, int position) {
        Post currentItem = post.get(position);
        holder.text_userName.setText(currentItem.getUserName());
        holder.text_title.setText(currentItem.getTitle());
        String priceText = currentItem.getPrice()+  R.string.tl;
        holder.text_price.setText(currentItem.getPrice()+currency);
        Picasso.get().load(currentItem.getProfileImage()).into(holder.imageView_profileImage);
        Picasso.get().load(currentItem.getPostImageUrl())
                .resize(240,150)
                .into(holder.imageView_postImage);
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

    @Override
    public Filter getFilter() {
        return postFiltered;
    }

    private Filter postFiltered=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Post> filteredPost= new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                filteredPost.addAll(postF);

            }else {
                String filterPattern =constraint.toString().toLowerCase().trim();
                for(Post item: postF){
                    if(item.getTitle().toLowerCase().contains(filterPattern))
                        filteredPost.add(item);
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredPost;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                post.clear();
                post.addAll((List)results.values);
                notifyDataSetChanged();
        }
    };
}