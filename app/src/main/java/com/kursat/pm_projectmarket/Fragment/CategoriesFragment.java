package com.kursat.pm_projectmarket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kursat.pm_projectmarket.Adapter.CategoryRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.CategoryCard;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.helpers.GridSpacingItemDecoration;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private FirebaseFirestore db;
    private CategoryRecyclerAdapter adapter;

    private ArrayList<String> categoryList_db;
    private ArrayList<String> categoryImageUrlList_db;

    //private ArrayList<CategoryCard> categoryCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        db = FirebaseFirestore.getInstance();
        categoryList_db = new ArrayList<>();
        categoryImageUrlList_db = new ArrayList<>();

        getDataFromDB();

        RecyclerView recyclerView = view.findViewById(R.id.rv_categories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryRecyclerAdapter(categoryList_db, categoryImageUrlList_db);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        recyclerView.setAdapter(adapter);
        return view;
    }

    public void getDataFromDB() {
        //categoryCards = new ArrayList<>();

        db.collection("Categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    CategoryCard category = doc.toObject(CategoryCard.class);
                    categoryList_db.add(category.getCategoryName());
                    categoryImageUrlList_db.add(category.getImageUrl());
                }
                adapter.notifyDataSetChanged();
            } else {
                System.out.println("kategorilere ulaşmakta sıkıntı var");
            }
        });
    }
}

/*

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kursat.pm_projectmarket.Adapter.CategoryRecyclerAdapter;
import com.kursat.pm_projectmarket.Model.CategoryCard;
import com.kursat.pm_projectmarket.R;
import com.kursat.pm_projectmarket.databinding.FragmentCategoriesBinding;
import com.kursat.pm_projectmarket.helpers.GridSpacingItemDecoration;
import com.kursat.pm_projectmarket.listeners.CategoryItemClickListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment
        implements CategoryItemClickListener {

    private FragmentCategoriesBinding binding;
    private Context mcontext;
    private ArrayList<CategoryCard> categoryCards;
    private CategoryRecyclerAdapter adapter;

    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_courses_stagged, container, false);

        binding = FragmentCategoriesBinding.inflate(getLayoutInflater());
        mcontext = this.getContext();
        View view = binding.getRoot();

        db= FirebaseFirestore.getInstance();

        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //For this example only use seach option
                    //U can use a other view with activityresult
                    performSearch();
                    Toast.makeText(mcontext,
                            "Edt Searching Click: " + binding.edtSearch.getText().toString().trim(),
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL);

        binding.rvCategories.setLayoutManager(
                layoutManager
        );
        binding.rvCategories.setClipToPadding(false);
        binding.rvCategories.setHasFixedSize(true);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_card);
        binding.rvCategories.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        categoryCards = new ArrayList<>();

        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        CategoryCard category = doc.toObject(CategoryCard.class);
                        categoryCards.add(new CategoryCard(category.getImageUrl(),category.getCategoryName(),"17 courses"));

                        //categoryCards.add(category.getCategoryName());
                        //categoryImageUrlList_db.add(category.getCategoryImageUrl());
                    }
                    adapter = new CategoryRecyclerAdapter(mcontext, categoryCards,new CategoryItemClickListener);
                    //adapter.notifyDataSetChanged();
                } else {
                    System.out.println("kategorilere ulaşmakta sıkıntı var");
                }
            }
        });


        String url ="https://firebasestorage.googleapis.com/v0/b/pm-projectmarket.appspot.com/o/Images%2FcategoryImages%2FProgrammingAndTech.jpg?alt=media&token=05844f6a-2d61-49d2-ba53-e05c81350f56";
        categoryCards.add(new CategoryCard(1, url, "Desing Thinking", "19 courses"));
        categoryCards.add(new CategoryCard(2, url, "Software Development", "14 courses"));
        categoryCards.add(new CategoryCard(3, url, "Marketing", "24 courses"));
        categoryCards.add(new CategoryCard(4, url, "Security Expert", "18 courses"));
        categoryCards.add(new CategoryCard(5, url, "Locations", "21 courses"));
        categoryCards.add(new CategoryCard(6, url, "Big Data", "10 courses"));

        adapter = new CategoryRecyclerAdapter(mcontext, categoryCards, this);


//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_margin);
//        binding.rvCourses.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        binding.rvCategories.setAdapter(adapter);
        return view;
    }

    private void performSearch() {
        binding.edtSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
        //...perform search
    }

    @Override
    public void onDashboardCategoryClick(CategoryCard categoryCard, ImageView imageView) {
        Toast.makeText(mcontext, categoryCard.getCategoryName(), Toast.LENGTH_LONG).show();
    }
}


*/

