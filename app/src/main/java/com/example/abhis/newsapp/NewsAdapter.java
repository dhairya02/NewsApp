package com.example.abhis.newsapp;

import android.widget.TextView;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }
        News currentNews = getItem(position);
        TextView sectionTextView = convertView.findViewById(R.id.news_section);
        sectionTextView.setText(currentNews.getSection());
        TextView titleTextView = convertView.findViewById(R.id.news_title);
        titleTextView.setText(currentNews.getTitle());
        TextView dateTextView = convertView.findViewById(R.id.news_pub_date);
        dateTextView.setText(formatDate(currentNews.getDate()));
        TextView authorTextView = convertView.findViewById(R.id.news_author);
        authorTextView.setText(currentNews.getAuthor());
        return convertView;
    }
    /* returns the formatted date string from a Date object in
       the following example format: Jan 09, 2018 10:05:08 AM */
    private String formatDate(String date) {
        Date dateObject = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.US);
            dateObject = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
        String dateFormatted = newDateFormat.format(dateObject);
        return dateFormatted;
    }
}