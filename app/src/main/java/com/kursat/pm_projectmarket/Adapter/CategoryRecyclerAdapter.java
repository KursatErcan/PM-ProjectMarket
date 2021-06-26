package com.kursat.pm_projectmarket.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Fragment.FeedFragment;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryHolder>{

    private final ArrayList<String> categoryList;
    private final ArrayList<String> categoryImageUrlList;
    private final ArrayList<String> categoryIdList;

    public CategoryRecyclerAdapter(ArrayList<String> categoryList,
                                   ArrayList<String> categoryImageUrlList, ArrayList<String> categoryIdList){
        this.categoryList = categoryList;
        this.categoryImageUrlList = categoryImageUrlList;
        this.categoryIdList = categoryIdList;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_item,parent,false);
        CategoryHolder cHolder = new CategoryHolder(view);

        cHolder.category_item.setOnClickListener(v -> {

            Bundle filter = new Bundle();
            filter.putInt("filterNum",Integer.parseInt(categoryIdList.get(cHolder.getAdapterPosition())));

            FeedFragment feedFragment = new FeedFragment();
            feedFragment.setArguments(filter);

            ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, feedFragment).commit();
        });
        return cHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.CategoryHolder holder, int position) {
        holder.text_categoryName.setText(categoryList.get(position));

        Picasso.get().load(categoryImageUrlList.get(position)).into(holder.imageView_categoryImage);

    }

    @Override
    public int getItemCount() { return categoryList.size(); }

    static class CategoryHolder extends RecyclerView.ViewHolder{

        private final TextView text_categoryName;
        private final ImageView imageView_categoryImage;
        private final LinearLayout category_item;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            category_item=(LinearLayout) itemView.findViewById(R.id.category_item);

            text_categoryName = itemView.findViewById(R.id.category_name);
            imageView_categoryImage = itemView.findViewById(R.id.card_view_image);

        }
    }
}
