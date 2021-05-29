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
    private ArrayList<String> categoryId_db;
    //private ArrayList<CategoryCard> categoryCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        db = FirebaseFirestore.getInstance();
        categoryList_db = new ArrayList<>();
        categoryImageUrlList_db = new ArrayList<>();
        categoryId_db = new ArrayList<>();
        getDataFromDB();

        RecyclerView recyclerView = view.findViewById(R.id.rv_categories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryRecyclerAdapter(categoryList_db, categoryImageUrlList_db,categoryId_db);

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
                    categoryId_db.add(category.getCategoryId());
                }
                adapter.notifyDataSetChanged();
            } else {
                System.out.println("kategorilere ulaşmakta sıkıntı var");
            }
        });
    }
}
