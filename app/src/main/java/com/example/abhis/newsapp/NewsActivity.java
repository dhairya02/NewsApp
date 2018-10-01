package com.example.abhis.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    // constant value for news loader ID
    private final static int NEWS_LOADER_ID = 0;

    // constant values for complete URL and appropriate keys
    private final static String REQUEST_URL = BuildConfig.GUARDIAN_REQUEST_URL;
    private final static String PAGE_SIZE = BuildConfig.PAGE_SIZE;
    private final static String TAGS = BuildConfig.TAGS;
    private final static String AUTHOR = BuildConfig.AUTHOR;
    private final static String SECTION = BuildConfig.SECTION;
    private final static String QUERY = BuildConfig.QUERY;
    private final static String ORDER_BY = BuildConfig.ORDER_BY;
    private final static String API_KEY = BuildConfig.API_KEY;
    private final static String MY_API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;

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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minArticles = sharedPrefs.getString(
                getString(R.string.settings_min_articles_key),
                getString(R.string.settings_min_articles_default));

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String searchContent = sharedPrefs.getString(
                getString(R.string.settings_search_content_key),
                getString(R.string.settings_search_content_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `page-size=10`
        uriBuilder.appendQueryParameter(PAGE_SIZE, minArticles);
        uriBuilder.appendQueryParameter(TAGS, AUTHOR);
        uriBuilder.appendQueryParameter(SECTION, section);
        uriBuilder.appendQueryParameter(QUERY, searchContent);
        uriBuilder.appendQueryParameter(ORDER_BY, orderBy);
        uriBuilder.appendQueryParameter(API_KEY, MY_API_KEY);

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
