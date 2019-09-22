package com.example.android.topbooks;

/**
 * Created by omkarpathak on 4/5/18.
 */

public class Book {

    String mTitle;
    String mAuthor;
    String mURL;

    public Book(String title, String author, String url){
        mTitle = title;
        mAuthor = author;
        mURL = url;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mURL;
    }
}
