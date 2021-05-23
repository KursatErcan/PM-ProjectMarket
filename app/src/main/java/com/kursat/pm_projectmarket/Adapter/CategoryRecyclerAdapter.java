package com.kursat.pm_projectmarket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kursat.pm_projectmarket.Fragment.CategoriesFragment;
import com.kursat.pm_projectmarket.Fragment.FeedFragment;
import com.kursat.pm_projectmarket.Model.CategoryCard;
import com.kursat.pm_projectmarket.R;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryHolder>{

    private ArrayList<String> categoryList;
    private ArrayList<String> categoryImageUrlList;

    public CategoryRecyclerAdapter(ArrayList<String> categoryList,
                                   ArrayList<String> categoryImageUrlList){
        this.categoryList = categoryList;
        this.categoryImageUrlList = categoryImageUrlList;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_item,parent,false);
        CategoryHolder cHolder = new CategoryHolder(view);

        cHolder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "Test Click"+String.valueOf(cHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

                ((FragmentActivity) view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragment, new FeedFragment()).commit();

            }

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

        private TextView text_categoryName;
        private ImageView imageView_categoryImage;
        private LinearLayout category_item;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            category_item=(LinearLayout) itemView.findViewById(R.id.category_item);

            text_categoryName = itemView.findViewById(R.id.category_name);
            imageView_categoryImage = itemView.findViewById(R.id.card_view_image);


        }
    }
}

/*
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kursat.pm_projectmarket.Model.CategoryCard;
import com.kursat.pm_projectmarket.databinding.CategoryItemBinding;
import com.kursat.pm_projectmarket.listeners.CategoryItemClickListener;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter._ViewHolder> {

    Context mContext;
    private List<CategoryCard> mData;
    private CategoryItemClickListener categoryItemClickListener;

    public CategoryRecyclerAdapter(Context mContext, List<CategoryCard> mData, CategoryItemClickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.categoryItemClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter._ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card,viewGroup,false);
//        return new _ViewHolder(view);

        LayoutInflater layoutInflater= LayoutInflater.from(viewGroup.getContext());
        CategoryItemBinding itemCardBinding = CategoryItemBinding.inflate(layoutInflater,viewGroup,false);
        return new _ViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryRecyclerAdapter._ViewHolder viewHolder, final int i) {
//        viewHolder.mItem = mData.get(i);
        final int pos = viewHolder.getAdapterPosition();
        //Set ViewTag
        viewHolder.itemView.setTag(pos);

        viewHolder.setPostImage(mData.get(i));



//        viewHolder.itemCardBinding.stagItemCourse.setText(mData.get(i).getCourseTitle());
//        viewHolder.itemCardBinding.stagItemQuantityCourse.setText(mData.get(i).getQuantityCourses());

        //1st intent 2 methods
//        if (i%2==1){
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.card_item.getLayoutParams();
//            cardViewMarginParams.setMargins(dpToPx(8), dpToPx(16), 0, 0);
//            viewHolder.card_item.requestLayout();
//        }
//        if (i==1){
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.card_item.getLayoutParams();
//            cardViewMarginParams.setMargins(dpToPx(8), dpToPx(32), 0, 0);
//            viewHolder.card_item.requestLayout();
//        }

        //2nd intent card only bottom margin in xml  and only top margin in adapter- it works

//        if (i%2==1){
//
//            int dimenTopPixeles = getDimensionValuePixels(R.dimen.staggedmarginbottom);
//            int dimenleftPixeles = getDimensionValuePixels(R.dimen.horizontal_card);
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) viewHolder.itemCardBinding.cardItem.getLayoutParams();
//            cardViewMarginParams.setMargins(dimenleftPixeles, dimenTopPixeles, 0, 0);
//            viewHolder.itemCardBinding.cardItem.requestLayout();
//        }

//      viewHolder.card_item.setBackgroundColor(mContext.getResources().getColor(R.color.color1));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryItemClickListener.onDashboardCategoryClick(mData.get(i), viewHolder.itemCardBinding.cardViewImage);
            }
        });
    }

    public int getDimensionValuePixels(int dimension)
    {
        return (int) mContext.getResources().getDimension(dimension);
    }


    public int dpToPx(int dp)
    {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public long getItemId(int position) {
        CategoryCard courseCard = mData.get(position);
        return courseCard.getId();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class _ViewHolder extends RecyclerView.ViewHolder{
//        ImageView imageView;
//        TextView course;
//        TextView quantity_courses;
//        CardView card_item;
//        public CourseCard mItem;
//        public _ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            card_item = itemView.findViewById(R.id.card_item);
//            imageView = itemView.findViewById(R.id.card_view_image);
//            course = itemView.findViewById(R.id.stag_item_course);
//            quantity_courses = itemView.findViewById(R.id.stag_item_quantity_course);
//        }

        CategoryItemBinding itemCardBinding;
        public _ViewHolder(@NonNull CategoryItemBinding cardBinding) {
            super(cardBinding.getRoot());
            this.itemCardBinding = cardBinding;

            //this.itemRecyclerMealBinding.
        }

        void setPostImage(CategoryCard courseCard){
            //Picasso.get().load(courseCard.getImageUrlCategory()).into(this.itemCardBinding.cardViewImage.setImageResource(courseCard.getImageUrlCategory()));
//            this.itemCardBinding.cardViewImage.setImageResource(courseCard.getImageUrlCategory());
            //this.itemCardBinding.cardViewImage.setImageResource(Picasso.get().load(courseCard.getImageUrlCategory()).into(viewHolder.imageView_categoryImage));
            //this.itemCardBinding.cardViewImage.setImageResource(courseCard.getImageUrlCategory());

//            this.itemCardBinding.cardViewImage.setImageURI(Uri.parse(courseCard.getImageUrlCategory()));

            this.itemCardBinding.cardViewImage.setImageURI(Uri.parse(courseCard.getImageUrl()));

            this.itemCardBinding.categoryName.setText(courseCard.getCategoryName());
            this.itemCardBinding.itemQuantityCategory.setText(courseCard.getQuantityCategory());
        }

    }
}


*/