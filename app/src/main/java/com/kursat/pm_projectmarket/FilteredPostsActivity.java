package com.kursat.pm_projectmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class FilteredPostsActivity extends AppCompatActivity {
    RelativeLayout filtersBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);

        filtersBar = findViewById(R.id.filtersBar);
        filtersBar.setVisibility(View.GONE);

    }
}