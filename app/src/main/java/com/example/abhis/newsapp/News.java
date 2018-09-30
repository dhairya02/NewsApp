package com.example.abhis.newsapp;
public class News {
    private String section;
    private String date;
    private String title;
    private String author;
    private String url;
    public News(String section, String date, String title, String author, String url) {
        this.section = section;
        this.date = date;
        this.title = title;
        this.author = author;
        this.url = url;
    }
    public String getSection() {
        return section;
    }
    public String getDate() {
        return date;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getUrl() {
        return url;
    }
}
