package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostHolder>{

    private ArrayList<String> userNameList;
    private ArrayList<String> titleList;
    private ArrayList<String> profileImageList;
    private ArrayList<String> postImageList;
    private ArrayList<String> priceList;


    public PostRecyclerAdapter(ArrayList<String> userNameList,
                               ArrayList<String> titleList,
                               ArrayList<String> profileImageList,
                               ArrayList<String> postImageList,
                               ArrayList<String> priceList) {
        this.userNameList = userNameList;
        this.titleList = titleList;
        this.profileImageList = profileImageList;
        this.postImageList = postImageList;
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public PostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerAdapter.PostHolder holder, int position) {
        holder.text_userName.setText(userNameList.get(position));
        holder.text_title.setText(titleList.get(position));
        holder.text_price.setText(priceList.get(position));
        Picasso.get().load(profileImageList.get(position)).into(holder.imageView_profileImage);
        Picasso.get().load(postImageList.get(position)).into(holder.imageView_postImage);
        //System.out.println("PostRcyclerAdapter :: UserName => " + userNameList.get(position));
        //System.out.println("PostRcyclerAdapter :: Title => " + titleList.get(position));

    }

    @Override
    public int getItemCount() { return titleList.size(); }

    static class PostHolder extends RecyclerView.ViewHolder{

        TextView text_userName;
        TextView text_title;
        ImageView imageView_profileImage;
        ImageView imageView_postImage;
        TextView text_price;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            text_userName = itemView.findViewById(R.id.text_userName_postElement);
            text_title = itemView.findViewById(R.id.text_postTitle_postElement);
            imageView_profileImage = itemView.findViewById(R.id.imageView_profileImage_postElement);
            imageView_postImage = itemView.findViewById(R.id.imageView_postImage_postElement);
            text_price = itemView.findViewById(R.id.text_price_postElement);


        }
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