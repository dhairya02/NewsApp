package com.example.abhis.newsapp;
public class News {
    // declare variables
    private String section;
    private String date;
    private String title;
    private String author;
    private String url;
    // default constructor for creating an news object
    public News(String section, String date, String title, String author, String url) {
        this.section = section;
        this.date = date;
        this.title = title;
        this.author = author;
        this.url = url;
    }
    // retrieves the section of a news article
    public String getSection() {
        return section;
    }
    // retrieves the publication date of a news article
    public String getDate() {
        return date;
    }
    // retrieves the title of a news article
    public String getTitle() {
        return title;
    }
    // retrieves the author(s) of a news article
    public String getAuthor() {
        return author;
    }
    // retrieves the URL or a news article
    public String getUrl() {
        return url;
    }
}
