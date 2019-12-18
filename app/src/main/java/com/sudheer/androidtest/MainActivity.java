package com.sudheer.androidtest;

import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sudheer.androidtest.adapter.RecyclerAdapter;
import com.sudheer.androidtest.model.DataListModel;
import com.sudheer.androidtest.model.DataModel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView txtCount;
    ProgressBar progressBar;
    ArrayList<DataListModel> list = new ArrayList<DataListModel>();
    RecyclerAdapter adapter;
    boolean isLoadingData = false;
    int nPages = 20;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        initViews();

        //set Adapter
        adapter = new RecyclerAdapter(this, list);
        recyclerView.setAdapter(adapter);

        //add ScrollListener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get next page data
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (!isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    getData((list.size() / nPages) + 1);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //call api for Data
        getData(1);
    }

    //set Toolbar items
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtCount = (TextView) toolbar.findViewById(R.id.txtCount);
    }

    //initViews
    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    //Get Data from server
    private void getData(final int pageNumber) {
        isLoadingData = true;
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hn.algolia.com/api/v1/search_by_date?tags=story&page=" + pageNumber;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoadingData = false;
                progressBar.setVisibility(View.GONE);
                try {
                    DataModel dataModel = new Gson().fromJson(response, DataModel.class);
                    list.addAll(dataModel.hits);
                    txtCount.setText("" + list.size());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoadingData = false;
                progressBar.setVisibility(View.GONE);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
