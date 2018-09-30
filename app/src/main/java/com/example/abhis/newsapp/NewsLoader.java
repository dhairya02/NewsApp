package com.example.abhis.newsapp;
import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;
public class NewsLoader extends AsyncTaskLoader<List<News>> {
    // declare variables
    private String url;
    // default constructor for loader object
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }
    // force load on start
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    // conducted on background thread
    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        // perform the network request, parse the JSON response, and extract a list of news articles
        List<News> news = QueryUtils.fetchNewsData(url);
        return news;
    }
}