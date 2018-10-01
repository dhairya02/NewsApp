package com.example.abhis.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
    public View getView(int position, View convertView, @Nullable ViewGroup parent) {

        final ViewHolder holder;

        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position < getCount()) {
                News currentNews = getItem(position);

                holder.sectionTextView.setText(currentNews.getSection());
                holder.titleTextView.setText(currentNews.getTitle());
                holder.dateTextView.setText(formatDate(currentNews.getDate()));
                holder.authorTextView.setText(currentNews.getAuthor());
            }
        } catch (NullPointerException npe) {
            Log.e("NewsAdapter", "getSection() throws npe", npe);
        }
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

    // Create ViewHolder to increase loading efficiency
    static class ViewHolder {
        private TextView sectionTextView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView authorTextView;

        ViewHolder(View view) {
            sectionTextView = view.findViewById(R.id.news_section);
            titleTextView = view.findViewById(R.id.news_title);
            dateTextView = view.findViewById(R.id.news_pub_date);
            authorTextView = view.findViewById(R.id.news_author);
        }
    }
}
