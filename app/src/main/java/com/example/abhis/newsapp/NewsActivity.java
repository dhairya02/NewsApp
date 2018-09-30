package com.example.abhis.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    // constant value for news loader ID
    private static final int NEWS_LOADER_ID = 0;
    // constant URL for retrieving data from the Guardian API
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?page-size=50&show-tags=contributor&q=" +
                    "software%20engineering&api-key=ba7a5ca7-2605-46a0-8578-3ebc1536ba01";
    // declare variables
    private NewsAdapter adapter;
    private TextView emptyStateTextView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        // find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.news_list);
        // create a new adapter that takes an empty list of news objects as input
        adapter = new NewsAdapter(this, new ArrayList<News>());
        // set the adapter on the {@link ListView} so that the list can be populated
        newsListView.setAdapter(adapter);
        // try to check for an internet connection and return a response if it isn't
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
                // set empty state to display "No internet connection."
                emptyStateTextView.setText(R.string.no_internet_connection);
            }
        } catch (NullPointerException npe) {
            Log.e("NewsActivity", "Error connecting", npe);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news article that was clicked on
                News currentNews = adapter.getItem(position);
                try {
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    // Create a new intent to view the news article URI
                    Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    // Send the intent to launch a new activity
                    startActivity(newsIntent);
                } catch (NullPointerException npe) {
                    Log.e("NewsActivity", "Error parsing URL", npe);
                }
            }
        });
    }
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // create a new loader for the given URL
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        // clear the adapter of previous news data
        adapter.clear();
        // set empty state to display "No news articles found."
        emptyStateTextView.setText(R.string.no_news_articles);
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // reset loader, so existing data can be cleared out
        adapter.clear();
    }
}
