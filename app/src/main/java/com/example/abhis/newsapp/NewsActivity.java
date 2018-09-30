package com.example.abhis.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int NEWS_LOADER_ID = 0;
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?page-size=50&show-tags=contributor&q=" +
                    "software%20engineering&api-key=ba7a5ca7-2605-46a0-8578-3ebc1536ba01";
    private NewsAdapter adapter;
    private TextView emptyStateTextView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ListView newsListView = findViewById(R.id.news_list);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);
        try {
            emptyStateTextView = findViewById(R.id.empty_view);
            newsListView.setEmptyView(emptyStateTextView);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                android.app.LoaderManager loaderManager = getLoaderManager();
                if (loaderManager.getLoader(NEWS_LOADER_ID) != null) {
                    loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
                } else {
                    loaderManager.initLoader(NEWS_LOADER_ID, null, this);
                }
            } else {
                progressBar = findViewById(R.id.progress);
                progressBar.setVisibility(View.GONE);
                emptyStateTextView.setText(R.string.no_internet_connection);
            }
        } catch (NullPointerException npe) {
            Log.e("NewsActivity", "Error connecting", npe);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = adapter.getItem(position);
                try {
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(newsIntent);
                } catch (NullPointerException npe) {
                    Log.e("NewsActivity", "Error parsing URL", npe);
                }
            }
        });
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        emptyStateTextView.setText(R.string.no_news_articles);
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
